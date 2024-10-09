package two_phase_commit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import errors.ErrorHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class ModeloBD2PC {

    private Connection conexionMysql;
    private Connection conexionSQLServer;
    private Connection conexionPostgres;

    private static final int MYSQL = 0;
    private static final int POSTGRESQL = 1;
    private static final int SQLSERVER = 2;

    List<Map<String, Object>> resultadosSQLServer; // reiniciarlos a vacio***
    List<Map<String, Object>> resultadosMysql;
    List<Map<String, Object>> resultadosPostgres;

    public void ejecutar2PC(String sentencia) throws SQLException {
        if (conexionMysql == null && conexionSQLServer == null && conexionPostgres == null) {
            ErrorHandler.showMessage("No hay conexiones activas", "Error de conexión",
                    ErrorHandler.INFORMATION_MESSAGE);
            return;
        }
        try {
            if (fasePreparacion(sentencia)) {
                faseCommit();
                System.out.println("Transacción completada con éxito en todas las bases de datos.");
            } else {
                faseAbort();
                System.err.println("Transacción abortada. Los cambios han sido revertidos.");
            }
        } catch (SQLException e) {
            faseAbort();
            throw new SQLException("Error en la transacción. Rollback ejecutado en todas las bases de datos.", e);
        }
    }

    private boolean fasePreparacion(String sentencia) {
        try {
            if (conexionSQLServer != null) {
                conexionSQLServer.setAutoCommit(false);
                resultadosSQLServer = prepararSentencia(sentencia, conexionSQLServer);
            }
            if (conexionMysql != null) {
                conexionMysql.setAutoCommit(false);
                resultadosMysql = prepararSentencia(sentencia, conexionMysql);
            }
            if (conexionPostgres != null) {
                conexionPostgres.setAutoCommit(false);
                resultadosPostgres = prepararSentencia(sentencia, conexionPostgres);
            }

            System.out.println("Fase de preparación completada exitosamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error en la fase de preparación: " + e.getMessage());
            return false;
        }
    }

    private List<Map<String, Object>> prepararSentencia(String sentencia, Connection conexion) throws SQLException {
        List<Map<String, Object>> resultados = new ArrayList<>();
        try {
            if (sentencia.trim().toUpperCase().startsWith("SELECT")) {
                ResultSet rs = conexion.prepareStatement(sentencia).executeQuery();

                int columnCount = rs.getMetaData().getColumnCount();

                while (rs.next()) {
                    Map<String, Object> fila = new HashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        String columnName = rs.getMetaData().getColumnName(i);
                        Object columnValue = rs.getObject(i);
                        fila.put(columnName, columnValue);
                    }
                    resultados.add(fila);
                }
            } else {
                int result = conexion.prepareStatement(sentencia).executeUpdate();
                System.out.println("Filas afectadas: " + result);
            }

            return resultados;

        } catch (SQLException e) {
            System.err.println("Error al ejecutar la sentencia: " + e.getMessage());
            ErrorHandler.showMessage("Error al ejecutar la sentencia: " + e.getMessage(), "Error de ejecución",
                    ErrorHandler.ERROR_MESSAGE);
            throw e;
        }
    }

    private void faseCommit() throws SQLException {
        try {
            if (conexionSQLServer != null) {
                conexionSQLServer.commit();
            }
            if (conexionMysql != null) {
                conexionMysql.commit();
            }
            if (conexionPostgres != null) {
                conexionPostgres.commit();
            }
        } catch (SQLException e) {
            throw new SQLException("Error al realizar el commit en alguna base de datos", e);
        } finally {
            if (conexionSQLServer != null) {
                conexionSQLServer.setAutoCommit(true);
            }
            if (conexionMysql != null) {
                conexionMysql.setAutoCommit(true);
            }
            if (conexionPostgres != null) {
                conexionPostgres.setAutoCommit(true);
            }
        }
    }

    private void faseAbort() {
        try {
            if (conexionSQLServer != null) {
                conexionSQLServer.rollback();
            }
            if (conexionMysql != null) {
                conexionMysql.rollback();
            }
            if (conexionPostgres != null) {
                conexionPostgres.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Error al hacer rollback: " + e.getMessage());
        } finally {
            try {
                if (conexionSQLServer != null) {
                    conexionSQLServer.setAutoCommit(true);
                }
                if (conexionMysql != null) {
                    conexionMysql.setAutoCommit(true);
                }
                if (conexionPostgres != null) {
                    conexionPostgres.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.err.println("Error al restaurar auto-commit: " + e.getMessage());
            }
        }
    }

    public int getMYSQL() {
        return MYSQL;
    }

    public int getPOSTGRESQL() {
        return POSTGRESQL;
    }

    public int getSQLSERVER() {
        return SQLSERVER;
    }

    public Connection getConexionMysql() {
        return conexionMysql;
    }

    public Connection getConexionSQLServer() {
        return conexionSQLServer;
    }

    public Connection getConexionPostgres() {
        return conexionPostgres;
    }

    public List<Map<String, Object>> getResultadosSQLServer() {
        if (resultadosSQLServer == null) {
            resultadosSQLServer = new ArrayList<>();
        }
        return resultadosSQLServer;
    }

    public List<Map<String, Object>> getResultadosMysql() {
        if (resultadosMysql == null) {
            resultadosMysql = new ArrayList<>();
        }
        return resultadosMysql;
    }

    public List<Map<String, Object>> getResultadosPostgres() {
        if (resultadosPostgres == null) {
            resultadosPostgres = new ArrayList<>();
        }
        return resultadosPostgres;
    }

    public void setConexionMysql(Connection conexionMysql) {
        this.conexionMysql = conexionMysql;
    }

    public void setConexionSQLServer(Connection conexionSQLServer) {
        this.conexionSQLServer = conexionSQLServer;
    }

    public void setConexionPostgres(Connection conexionPostgres) {
        this.conexionPostgres = conexionPostgres;
    }
}
