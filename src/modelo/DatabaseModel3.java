package modelo;

// postgres
import java.net.ConnectException;
import java.sql.*;

import main.App;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSet;

public class DatabaseModel3 {
    private String servidor, basededatos, usuario, password;
    public static Connection conexion;

    public DatabaseModel3(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public void getConexion() {
        // String url = "jdbc:postgresql://"+servidor+":1212/"+basededatos;
        String url = "jdbc:postgresql://26.46.40.58:1212/Despacho";
        Properties props = new Properties();
        // setProperty("user", usuario);
        // props.setProperty("password", password);
        props.setProperty("user", "postgres");
        props.setProperty("password", "123456");
        props.setProperty("ssl", "false");
        
        try {
            conexion = DriverManager.getConnection(url, props);
            if (conexion != null) {
                System.out.println("Conectado a la base de datos Postgres");
            }
        } catch (SQLException e) {
            System.err.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }
}
