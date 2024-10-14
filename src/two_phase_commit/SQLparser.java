package two_phase_commit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import base_de_datos.DatabaseModelMysql;
import base_de_datos.DatabaseModelPostgres;
import base_de_datos.DatabaseModelSQLServer;
import errors.ErrorHandler;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class SQLparser {

    private static final long TIMEOUT = 60000; // 60 segundos de timeout

    private Semaforo semaforo;
    private Map<String, AtomicBoolean> fragmentStatus;
    private AtomicBoolean allPrepared;

    public enum Zona {
        NORTE(new String[] {
                "Baja California", "Baja California Sur", "Sonora", "Chihuahua", "Coahuila",
                "Nuevo León", "Tamaulipas", "Durango", "Sinaloa"
        }),
        CENTRO(new String[] {
                "Aguascalientes", "Zacatecas", "San Luis Potosí", "Guanajuato", "Querétaro",
                "Jalisco", "Michoacán", "Estado de México", "Hidalgo", "Morelos",
                "Tlaxcala", "Ciudad de México", "Colima", "Nayarit"
        }),
        SUR(new String[] {
                "Chiapas", "Guerrero", "Oaxaca", "Puebla", "Tabasco", "Veracruz",
                "Campeche", "Yucatán", "Quintana Roo"
        });

        private final String[] estados;

        Zona(String[] estados) {
            this.estados = estados;
        }

        public String[] getEstados() {
            return estados;
        }

        public boolean contieneEstado(String nombreEstado) {
            for (String estado : estados) {
                if (estado.equalsIgnoreCase(nombreEstado)) {
                    return true;
                }
            }
            return false;
        }
    }

    private Connection conexionFragmentos;
    private Map<Zona, Connection> conexiones;

    public SQLparser(Connection conexionFragmentos) {
        this.conexionFragmentos = conexionFragmentos;
        this.conexiones = new HashMap<>();
        this.fragmentStatus = new ConcurrentHashMap<>();
        this.allPrepared = new AtomicBoolean(false);
    }

    public List<String> parseQuery(String query) {
        List<String> targetFragments = new ArrayList<>();

        if (contieneInsert(query)) {
            agregarFragmentosPorZona(query, targetFragments);
        } else {
            agregarFragmentosPorCondiciones(query, targetFragments);
        }

        if (targetFragments.isEmpty()) {
            agregarTodasLasZonas(targetFragments);
        }

        return targetFragments;
    }

    private boolean contieneInsert(String query) {
        return query.toLowerCase().contains("insert");
    }

    private void agregarFragmentosPorZona(String query, List<String> targetFragments) {
        for (Zona zona : Zona.values()) {
            for (String estado : zona.getEstados()) {
                if (query.toLowerCase().contains(estado.toLowerCase())) {
                    targetFragments.add(zona.name());
                    System.out.println("Agregando " + zona.name());
                    break;
                }
            }
        }
    }

    private void agregarFragmentosPorCondiciones(String query, List<String> targetFragments) {
        Pattern wherePattern = Pattern.compile("(?i)\\bWHERE\\b.*\\bEstado\\b");
        Matcher whereMatcher = wherePattern.matcher(query);

        if (whereMatcher.find()) {
            agregarFragmentosPorZona(query, targetFragments);
        }
    }

    private void agregarTodasLasZonas(List<String> targetFragments) {
        for (Zona zona : Zona.values()) {
            targetFragments.add(zona.name());
        }
    }

    public List<Map<String, Object>> ejecutarSelect(String sentencia) throws SQLException {
        List<String> targetFragments = parseQuery(sentencia);
        if (!crearConexiones(targetFragments))
            return new ArrayList<>();

        List<Map<String, Object>> resultados = new ArrayList<>();
        for (String fragmento : targetFragments) {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null) {
                Connection conexion = conexiones.get(zona);
                if (conexion != null) {
                    System.out.println("Ejecutando en " + fragmento);
                    resultados.addAll(prepararSentencia(sentencia, conexion));
                    if (resultados.isEmpty()) {
                        System.out.println("No se encontraron resultados para el fragmento " + fragmento);
                    }
                }
            } else
                ErrorHandler.showMessage("No se encontró la zona para el fragmento " + fragmento, "Error de fragmento",
                        ErrorHandler.ERROR_MESSAGE);
        }
        return resultados;
    }

    private boolean crearConexiones(List<String> targetFragments) throws SQLException {
        Statement statement = conexionFragmentos.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM fragmentos");

        while (resultSet.next()) {
            String fragmento = resultSet.getString("Fragmento");
            Zona zona = obtenerZonaPorEstado(fragmento);
            if (zona != null && targetFragments.contains(zona.name())) {
                String servidor = resultSet.getString("IP");
                System.out.println("Servidor: " + servidor);
                String gestor = resultSet.getString("gestor");
                String basededatos = resultSet.getString("basededatos");
                String usuario = resultSet.getString("usuario");
                String password = resultSet.getString("Contraseña");
                Connection conexion = asignarConexion(servidor, gestor, basededatos, usuario, password);
                if (conexion == null) {
                    ErrorHandler.showMessage("Error al crear la conexión para el fragmento " + fragmento,
                            "Error de fragmento",
                            ErrorHandler.ERROR_MESSAGE);
                    return false;
                }
                conexiones.put(zona, conexion);
            }
        }
        if (conexiones.isEmpty()) {
            ErrorHandler.showMessage("No se encontraron fragmentos para las zonas seleccionadas", "Error de fragmento",
                    ErrorHandler.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private Zona obtenerZonaPorEstado(String nombre) {
        for (Zona zona : Zona.values()) {
            if (zona.contieneEstado(nombre)) {
                System.out.println(nombre + " pertenece a la zona " + zona.name());
                return zona;
            }
        }
        return null;
    }

    private Zona obtenerZonaPorNombre(String nombre) {
        for (Zona zona : Zona.values()) {
            if (zona.name().equalsIgnoreCase(nombre)) {
                return zona;
            }
        }
        return null;
    }

    private Connection asignarConexion(String servidor, String gestor, String basededatos, String usuario,
            String password) {
        switch (gestor.toLowerCase()) {
            case "sqlserver":
                return new DatabaseModelSQLServer(servidor, basededatos, usuario, password).getConexion();
            case "mysql":
                return new DatabaseModelMysql(servidor, basededatos, usuario, password).getConexion();
            case "postgres":
                return new DatabaseModelPostgres(servidor, basededatos, usuario, password).getConexion();
            default:
                System.err.println("Error al crear la conexión para el gestor: " + gestor);
                return null;
        }
    }

    public void ejecutarTransaccion(String sentencia) throws SQLException {
        List<String> targetFragments = parseQuery(sentencia);
        if (!crearConexiones(targetFragments)) {
            return;
        }

        semaforo = new Semaforo(targetFragments.size());
        fragmentStatus.clear();
        for (String fragmento : targetFragments) {
            fragmentStatus.put(fragmento, new AtomicBoolean(false));
        }

        ExecutorService executor = Executors.newFixedThreadPool(targetFragments.size() + 1);
        List<Future<?>> futures = new ArrayList<>();

        futures.add(executor.submit(this::supervisorThread));

        for (String fragmento : targetFragments) {
            futures.add(executor.submit(() -> prepararFragmento(sentencia, fragmento)));
        }

        for (Future<?> future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                ErrorHandler.showMessage("Error en la ejecución de la transacción: " + e.getMessage(),
                        "Error de transacción",
                        ErrorHandler.ERROR_MESSAGE);
            }
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(TIMEOUT, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        if (allPrepared.get()) {
            System.out.println("Transacción completada con éxito");
            ErrorHandler.showMessage("Transacción completada con éxito", "Transacción completada",
                    ErrorHandler.INFORMATION_MESSAGE);
        } else {
            System.err.println("Transacción abortada. Los cambios han sido revertidos.");
            ErrorHandler.showMessage("Transacción abortada. Los cambios han sido revertidos.", "Transacción abortada",
                    ErrorHandler.ERROR_MESSAGE);
        }
    }

    private void supervisorThread() {
        long startTime = System.currentTimeMillis();
        boolean timeout = false;
        while (!timeout && !allPrepared.get()) {
            boolean allDone = true;
            for (AtomicBoolean status : fragmentStatus.values()) {
                if (!status.get()) {
                    allDone = false;
                    break;
                }
            }
            if (allDone) {
                allPrepared.set(true);
                commitAll();
                break;
            }
            if (System.currentTimeMillis() - startTime > TIMEOUT) {
                timeout = true;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (timeout || !allPrepared.get()) {
            rollbackAll();
        }

        for (int i = 0; i < fragmentStatus.size(); i++) {
            semaforo.libera();
        }
    }

    private void prepararFragmento(String sentencia, String fragmento) {
        try {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null) {
                Connection conexion = conexiones.get(zona);
                if (conexion != null) {
                    conexion.setAutoCommit(false);
                    if (prepararSentenciaUpdate(sentencia, conexion)) {
                        fragmentStatus.get(fragmento).set(true);
                    }
                }
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al preparar el fragmento " + fragmento + ": " + e.getMessage(),
                    "Error de preparación", ErrorHandler.ERROR_MESSAGE);
        } finally {
            semaforo.espera();
        }
    }

    private void commitAll() {
        for (Connection conexion : conexiones.values()) {
            if (conexion != null) {
                try {
                    conexion.commit();
                } catch (SQLException e) {
                    ErrorHandler.showMessage("Error al hacer commit: " + e.getMessage(), "Error de commit",
                            ErrorHandler.ERROR_MESSAGE);
                }
            }
        }
    }

    private void rollbackAll() {
        for (Connection conexion : conexiones.values()) {
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException e) {
                    ErrorHandler.showMessage("Error al hacer rollback: " + e.getMessage(), "Error de rollback",
                            ErrorHandler.ERROR_MESSAGE);
                } finally {
                    try {
                        conexion.setAutoCommit(true);
                    } catch (SQLException e) {
                        ErrorHandler.showMessage("Error al restablecer autoCommit: " + e.getMessage(),
                                "Error de conexión", ErrorHandler.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    private List<Map<String, Object>> prepararSentencia(String sentencia, Connection conexion) throws SQLException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try (Statement statement = conexion.createStatement();
                ResultSet resultSet = statement.executeQuery(sentencia)) {

            while (resultSet.next()) {
                Map<String, Object> fila = new HashMap<>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    fila.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                }
                resultados.add(fila);
                System.out.println(fila.toString());
            }
        }
        return resultados;
    }

    private boolean prepararSentenciaUpdate(String sentencia, Connection conexion) throws SQLException {
        try (Statement statement = conexion.createStatement()) {
            return statement.executeUpdate(sentencia) > 0;
        }
    }
}