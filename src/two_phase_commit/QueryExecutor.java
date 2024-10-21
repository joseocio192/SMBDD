package two_phase_commit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import errors.ErrorHandler;

public class QueryExecutor {

    private static final long TIMEOUT = 10000;

    private ConnectionManager connectionManager;
    private QueryParser parser;

    private Map<Zona, Connection> conexiones;
    private TwoPhaseCommitManager twoPhaseCommitManager;

    public QueryExecutor(Connection conexionFragmentos) {
        conexiones = new HashMap<>();
        connectionManager = new ConnectionManager(conexionFragmentos, conexiones);
        twoPhaseCommitManager = new TwoPhaseCommitManager(conexiones);
        parser = new QueryParser(connectionManager);

    }

    public List<Map<String, Object>> ejecutarSelect(String sentencia) throws SQLException {
        List<String> targetFragments = parser.parseQuery(sentencia, true);
        if (!connectionManager.crearConexiones(targetFragments))
            return new ArrayList<>();

        List<Map<String, Object>> resultados = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(targetFragments.size());
        List<Future<List<Map<String, Object>>>> futures = new ArrayList<>();

        for (String fragmento : targetFragments) {
            Zona zona = twoPhaseCommitManager.obtenerZonaPorNombre(fragmento);
            if (zona == null) {
                System.out.println("No se encontró la zona para el fragmento " + fragmento);
                continue;
            }
            Connection conexion = conexiones.get(zona);
            if (conexion == null) {
                ErrorHandler.showMessage("No se encontró la conexión para el fragmento " + fragmento,
                        "Error de conexión",
                        ErrorHandler.ERROR_MESSAGE);
                continue;
            }

            futures.add(executor.submit(() -> {
                List<Map<String, Object>> fragmentResult = prepararSentencia(sentencia, conexion);
                if (fragmentResult.isEmpty()) {
                    System.out.println("No se encontraron resultados para el fragmento " + fragmento);
                }
                return fragmentResult;
            }));
        }

        for (int i = 0; i < futures.size(); i++) {
            Future<List<Map<String, Object>>> future = futures.get(i);
            String fragmento = targetFragments.get(i);
            try {
                resultados.addAll(future.get(5000, TimeUnit.MILLISECONDS));
            } catch (TimeoutException e) {
                ErrorHandler.manejarTimeout(fragmento);
            } catch (ExecutionException | InterruptedException e) {
                ErrorHandler.manejarErrorEjecucion(fragmento, e);
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        return resultados;
    }

    public void ejecutarTransaccion(String sentencia) throws SQLException {
        List<String> targetFragments = parser.parseQuery(sentencia, false);
        twoPhaseCommitManager.ejecutarTransaccion(sentencia, targetFragments, connectionManager);
    }

    private List<Map<String, Object>> prepararSentencia(String sentencia, Connection conexion) throws SQLException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try (Statement statement = conexion.createStatement();
                ResultSet resultSet = statement.executeQuery(sentencia)) {

            while (resultSet.next()) {
                Map<String, Object> fila = new LinkedHashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    fila.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
                }
                resultados.add(fila);
                System.out.println(fila.toString());
            }
        }
        return resultados;
    }
}