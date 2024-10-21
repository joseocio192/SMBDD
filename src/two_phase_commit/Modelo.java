package two_phase_commit;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TransferQueue;

import javax.swing.table.DefaultTableModel;

import errors.ErrorHandler;

public class Modelo {

    public static final int TRANSACCIONES = 0;
    public static final int CONSULTAS = 1;
    private List<Map<String, Object>> resultados;

    private Connection conexion;

    public void processTransactions(int tipo, String query) {
        resultados = null;
        if (tipo == CONSULTAS) {
            QueryExecutor parser = new QueryExecutor(conexion);
            try {
                resultados = parser.ejecutarSelect(query);
            } catch (SQLException | ErrorHandler e) {
                ErrorHandler.showMessage("Error en la consulta: " + e.getMessage(), "Error de consulta",
                        ErrorHandler.ERROR_MESSAGE);
            }
        } else if (tipo == TRANSACCIONES) {
            QueryExecutor parser = new QueryExecutor(conexion);
            try {
                parser.ejecutarTransaccion(query);
            } catch (SQLException | ErrorHandler e) {
                ErrorHandler.showMessage("Error en la transacción: " + e.getMessage(), "Error de transacción",
                        ErrorHandler.ERROR_MESSAGE);
            }
        }
    }

    public List<Map<String, Object>> getResultados() {
        return resultados;
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }

    public void cerrarConexion() {
        this.conexion = null;
    }

}
