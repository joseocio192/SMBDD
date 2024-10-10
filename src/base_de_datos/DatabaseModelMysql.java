package base_de_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

import errors.ErrorHandler;

public class DatabaseModelMysql extends DatabaseModel {

    public DatabaseModelMysql(String servidor, String basededatos, String usuario, String password) {
        super(servidor, basededatos, usuario, password);
    }

    @Override
    public Connection getConexion() {
        String url = "jdbc:mysql://" + servidor + ":3306/" + basededatos + "?connectTimeout=1000";
        try {
            conexion = DriverManager.getConnection(url, usuario, password);
            if (conexion != null) {
                ErrorHandler.showMessage("Conectado a la base de datos MySQL", "Conexión exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error en la conexión a MySQL: " + e.getMessage(), "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }
}
