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
import errors.ErrorHandler;

public class SQLparser {

    private static final String ZONA_NORTE = "Baja California";
    private static final String ZONA_CENTRO = "Jalisco";
    private static final String ZONA_SUR = "Chiapas";

    private Connection conexionNorte;
    private Connection conexionCentro;
    private Connection conexionSur;

    private List<Map<String, Object>> resultadosSQLServer;
    private List<Map<String, Object>> resultadosMysql;
    private List<Map<String, Object>> resultadosPostgres;

    public List<String> parseQuery(String query) {
        List<String> targetFragments = new ArrayList<>();

        // Check if the query contains a WHERE clause
        Pattern wherePattern = Pattern.compile("(?i)\\bWHERE\\b");
        Matcher whereMatcher = wherePattern.matcher(query);

        // Check if the query contains a insert clause
        Pattern insertPattern = Pattern.compile("(?i)\\bINSERT\\b");
        Matcher insertMatcher = insertPattern.matcher(query);

        if (insertMatcher.find()) {
            // get the zone from the insert clause
            if (query.toLowerCase().contains(ZONA_NORTE.toLowerCase())) {
                targetFragments.add(ZONA_NORTE);
            }
            if (query.toLowerCase().contains(ZONA_CENTRO.toLowerCase())) {
                targetFragments.add(ZONA_CENTRO);
            }
            if (query.toLowerCase().contains(ZONA_SUR.toLowerCase())) {
                targetFragments.add(ZONA_SUR);
            }
            return targetFragments;
        } else {
            if (!whereMatcher.find()) {
                // No WHERE clause, send to all fragments
                targetFragments.add(ZONA_NORTE);
                targetFragments.add(ZONA_CENTRO);
                targetFragments.add(ZONA_SUR);
            } else {
                // Check if the WHERE clause contains 'Estado'
                Pattern estadoPattern = Pattern.compile("(?i)\\bWHERE\\b.*\\bEstado\\b");
                Matcher estadoMatcher = estadoPattern.matcher(query);

                if (estadoMatcher.find()) {
                    // Check for specific zone conditions
                    if (query.toLowerCase().contains(ZONA_NORTE.toLowerCase())) {
                        targetFragments.add(ZONA_NORTE);
                    }
                    if (query.toLowerCase().contains(ZONA_CENTRO.toLowerCase())) {
                        targetFragments.add(ZONA_CENTRO);
                    }
                    if (query.toLowerCase().contains(ZONA_SUR.toLowerCase())) {
                        targetFragments.add(ZONA_SUR);
                    }
                }

                // If no specific zone condition is found, send to all fragments
                if (targetFragments.isEmpty()) {
                    targetFragments.add(ZONA_NORTE);
                    targetFragments.add(ZONA_CENTRO);
                    targetFragments.add(ZONA_SUR);
                }
            }
            return targetFragments;
        }
    }

    public List<Map<String, Object>> ejecutarSelect(String sentencia) {
        List<String> targetFragments = parseQuery(sentencia);
        if (conexionNorte != null && targetFragments.contains(ZONA_NORTE)) {
            resultadosSQLServer = prepararSentencia(sentencia, conexionNorte, targetFragments);
        }
        if (conexionCentro != null && targetFragments.contains(ZONA_CENTRO)) {
            resultadosMysql = prepararSentencia(sentencia, conexionCentro, targetFragments);
        }
        if (conexionSur != null && targetFragments.contains(ZONA_SUR)) {
            resultadosPostgres = prepararSentencia(sentencia, conexionSur, targetFragments);
        }

        List<Map<String, Object>> resultados = new ArrayList<>();
        if (resultadosSQLServer != null) {
            resultados.addAll(resultadosSQLServer);
        }
        if (resultadosMysql != null) {
            resultados.addAll(resultadosMysql);
        }
        if (resultadosPostgres != null) {
            resultados.addAll(resultadosPostgres);
        }
        return resultados;

    }

    public void ejecutarTransacion(String sentencia) throws SQLException {
        if (conexionNorte == null && conexionCentro == null && conexionSur == null) {
            ErrorHandler.showMessage("No hay conexiones activas", "Error de conexión",
                    ErrorHandler.INFORMATION_MESSAGE);
            return;
        }
        if (fasePreparacion(sentencia)) {
            faseCommit();
            System.out.println("Transacción completada con éxito en todas las bases de datos.");
        } else {
            faseAbort();
            System.err.println("Transacción abortada. Los cambios han sido revertidos.");
        }
    }

    private boolean fasePreparacion(String sentencia) {
        List<String> targetFragments = parseQuery(sentencia);
        try {
            if (conexionNorte != null && targetFragments.contains(ZONA_NORTE)) {
                conexionNorte.setAutoCommit(false);
                resultadosSQLServer = prepararSentencia(sentencia, conexionNorte, targetFragments);
            }
            if (conexionCentro != null && targetFragments.contains(ZONA_CENTRO)) {
                conexionCentro.setAutoCommit(false);
                resultadosMysql = prepararSentencia(sentencia, conexionCentro, targetFragments);
            }
            if (conexionSur != null && targetFragments.contains(ZONA_SUR)) {
                conexionSur.setAutoCommit(false);
                resultadosPostgres = prepararSentencia(sentencia, conexionSur, targetFragments);
            }
            return true;
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error en la transacción: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
            return false;
        }
    }

    private void faseCommit() {
        try {
            if (conexionNorte != null) {
                conexionNorte.commit();
            }
            if (conexionCentro != null) {
                conexionCentro.commit();
            }
            if (conexionSur != null) {
                conexionSur.commit();
            }
        } catch (Exception e) {
            if (conexionNorte != null) {
                try {
                    conexionNorte.rollback();
                } catch (SQLException e1) {
                    ErrorHandler.showMessage("Error al realizar el rollback: " + e1.getMessage(), "Error de conexión",
                            ErrorHandler.ERROR_MESSAGE);
                }
            }
            if (conexionCentro != null) {
                try {
                    conexionCentro.rollback();
                } catch (SQLException e1) {
                    ErrorHandler.showMessage("Error al realizar el rollback: " + e1.getMessage(), "Error de conexión",
                            ErrorHandler.ERROR_MESSAGE);
                }
            }
            if (conexionSur != null) {
                try {
                    conexionSur.rollback();
                } catch (SQLException e1) {
                    ErrorHandler.showMessage("Error al realizar el rollback: " + e1.getMessage(), "Error de conexión",
                            ErrorHandler.ERROR_MESSAGE);
                }
            }
            ErrorHandler.showMessage("Error al realizar el commit: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
        }
    }

    private void faseAbort() {
        try {
            if (conexionNorte != null) {
                conexionNorte.rollback();
            }
            if (conexionCentro != null) {
                conexionCentro.rollback();
            }
            if (conexionSur != null) {
                conexionSur.rollback();
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al realizar el rollback: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
        } finally {
            try {
                if (conexionNorte != null) {
                    conexionNorte.setAutoCommit(true);
                }
                if (conexionCentro != null) {
                    conexionCentro.setAutoCommit(true);
                }
                if (conexionSur != null) {
                    conexionSur.setAutoCommit(true);
                }
            } catch (SQLException e) {
                ErrorHandler.showMessage("Error al realizar el rollback: " + e.getMessage(), "Error de conexión",
                        ErrorHandler.ERROR_MESSAGE);
            }
        }
    }

    private List<Map<String, Object>> prepararSentencia(String sentencia, Connection ConexionSql,
            List<String> targetFragments) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try {
            Statement statement = ConexionSql.createStatement();
            ResultSet resultSet = statement.executeQuery(sentencia);
            while (resultSet.next()) {
                Map<String, Object> fila = new HashMap<>();
                for (String fragment : targetFragments) {
                    fila.put(fragment, resultSet.getObject(fragment));
                }
                resultados.add(fila);
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al ejecutar la sentencia: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
        }
        return resultados;
    }

    public Connection getConexionNorte() {
        return conexionNorte;
    }

    public void setConexionNorte(Connection conexionNorte) {
        this.conexionNorte = conexionNorte;
    }

    public Connection getConexionCentro() {
        return conexionCentro;
    }

    public void setConexionCentro(Connection conexionCentro) {
        this.conexionCentro = conexionCentro;
    }

    public Connection getConexionSur() {
        return conexionSur;
    }

    public void setConexionSur(Connection conexionSur) {
        this.conexionSur = conexionSur;
    }

    public static void main(String[] args) {
        SQLparser parser = new SQLparser();
        String query = "SELECT Estado FROM clientes WHERE Estado = 'Jalisco'";
            parser.ejecutarSelect(query);
        //show results
        for (Map<String, Object> result : parser.resultadosSQLServer) {
            System.out.println(result);
        }
    }
}