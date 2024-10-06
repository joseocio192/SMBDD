package modelo;
// mariadb
import java.net.ConnectException;
import java.sql.*;

import main.App;

public class DatabaseModel3 {
    private String servidor, basededatos, usuario, password;
    private Connection conexion;

    public DatabaseModel3(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public Connection getConexion(){
        String conexionUrl = "jdbc:sqlserver://" + servidor + ";"
        + "database=" + basededatos + ";"
        + "user=" + usuario + ";"
        + "password=" + password + ";"
        + "trustServerCertificate=true;"
        + "loginTimeout=5;";
       try {
            conexion = DriverManager.getConnection(conexionUrl);
            if (conexion != null) {
                System.out.println("Conectado a la base de datos");
                return conexion;
            }
        } catch (SQLException e) {
            System.err.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
        return null;
    }
}
