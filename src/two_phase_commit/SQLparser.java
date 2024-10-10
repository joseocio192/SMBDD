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

public class SQLparser {

    private enum Zona {
        NORTE("Baja California"),
        CENTRO("Jalisco"),
        SUR("Chiapas");

        private final String nombre;

        Zona(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
    }

    private Connection conexionFragmentos;
    private Map<Zona, Connection> conexiones;

    public SQLparser(Connection conexionFragmentos) {
        this.conexionFragmentos = conexionFragmentos;
        this.conexiones = new HashMap<>();
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
            if (query.toLowerCase().contains(zona.getNombre().toLowerCase())) {
                targetFragments.add(zona.getNombre());
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
            targetFragments.add(zona.getNombre());
        }
    }

    public List<Map<String, Object>> ejecutarSelect(String sentencia) throws SQLException {
        List<String> targetFragments = parseQuery(sentencia);
        crearConexiones(targetFragments);

        List<Map<String, Object>> resultados = new ArrayList<>();
        for (String fragmento : targetFragments) {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null) {
                Connection conexion = conexiones.get(zona);
                if (conexion != null) {
                    resultados.addAll(prepararSentencia(sentencia, conexion));
                }
            }
        }
        return resultados;
    }

    private void crearConexiones(List<String> targetFragments) throws SQLException {
        Statement statement = conexionFragmentos.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM fragmentos");

        while (resultSet.next()) {
            String fragmento = resultSet.getString("Fragmento");
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null && targetFragments.contains(zona.getNombre())) {
                String servidor = resultSet.getString("IP");
                String gestor = resultSet.getString("gestor");
                String basededatos = resultSet.getString("basededatos");
                String usuario = resultSet.getString("usuario");
                String password = resultSet.getString("Contraseña");
                conexiones.put(zona, asignarConexion(servidor, gestor, basededatos, usuario, password));
            }
        }
    }

    private Zona obtenerZonaPorNombre(String nombre) {
        for (Zona zona : Zona.values()) {
            if (zona.getNombre().equalsIgnoreCase(nombre)) {
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
        if (fasePreparacion(sentencia)) {
            faseCommit();
            System.out.println("Transacción completada con éxito");
        } else {
            faseAbort();
            System.err.println("Transacción abortada. Los cambios han sido revertidos.");
        }
    }

    private boolean fasePreparacion(String sentencia) {
        try {
            List<String> targetFragments = parseQuery(sentencia);
            for (String fragmento : targetFragments) {
                Zona zona = obtenerZonaPorNombre(fragmento);
                if (zona != null) {
                    Connection conexion = conexiones.get(zona);
                    if (conexion != null) {
                        conexion.setAutoCommit(false);
                        prepararSentencia(sentencia, conexion);
                    }
                }
            }
            return true;
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error en la fase de preparación: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
            return false;
        }
    }

    private void faseCommit() throws SQLException {
        for (Connection conexion : conexiones.values()) {
            if (conexion != null) {
                conexion.commit();
            }
        }
    }

    private void faseAbort() throws SQLException {
        for (Connection conexion : conexiones.values()) {
            if (conexion != null) {
                conexion.rollback();
                conexion.setAutoCommit(true);
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
            }
        }
        return resultados;
    }
}