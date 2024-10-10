package two_phase_commit;

import java.sql.Connection;
import java.sql.SQLException;

import errors.ErrorHandler;

public class Modelo {

    public static final int TRANSACCIONES = 0;
    public static final int CONSULTAS = 1;

    private Connection conexion;

    public void transacciones(int tipo, String query) {
        if (tipo == TRANSACCIONES) {
            SQLparser parser = new SQLparser();
            parser.ejecutarSelect(query);
        } else if (tipo == CONSULTAS) {
            SQLparser parser = new SQLparser();
            try {
                parser.ejecutarTransacion(query);
            } catch (SQLException e) {
                ErrorHandler.showMessage("Error en la transacción: " + e.getMessage(), "Error de transacción",
                        0);
            }
        }
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
