package base_de_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import errors.ErrorHandler;

public class DatabaseModelSQLServer extends DatabaseModel {

    public DatabaseModelSQLServer(String servidor, String basededatos, String usuario, String password) {
        super(servidor, basededatos, usuario, password);
    }

    @Override
    public Connection getConexion() {
        String url = "jdbc:sqlserver://" + servidor + ";database=" + basededatos + ";user=" + usuario + ";password="
                + password + ";trustServerCertificate=true;loginTimeout=5;";
        try {
            conexion = DriverManager.getConnection(url);
            if (conexion != null) {
                ErrorHandler.showMessage("Conectado a la base de datos SQL Server", "Conexión exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error en la conexión a SQL Server: " + e.getMessage(), "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }
}
