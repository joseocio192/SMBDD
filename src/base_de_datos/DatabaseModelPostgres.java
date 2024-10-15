package base_de_datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;

import errors.ErrorHandler;

public class DatabaseModelPostgres extends DatabaseModel {

    public DatabaseModelPostgres(String servidor, String basededatos, String usuario, String password) {
        super(servidor, basededatos, usuario, password);
    }

    @Override
    public Connection getConexion() {
        String url = "jdbc:postgresql://" + servidor + ":1212/" + basededatos;
        Properties props = new Properties();
        props.setProperty("user", usuario);
        props.setProperty("password", password);
        props.setProperty("ssl", "false");

        try {
            conexion = DriverManager.getConnection(url, props);
            if (conexion != null) {
                System.out.println("Conexión a PostgreSQL exitosa");
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error en la conexión a PostgreSQL: " + e.getMessage(), "Error de conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
        return conexion;
    }
}
