package modelo;
//my sql
import java.net.ConnectException;


import main.App;
import raven.toast.Notifications;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
public class DatabaseModel2 {
    private String servidor, basededatos, usuario, password;
    public static Connection connection;

    public DatabaseModel2(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public void getConexion() {
        String url = "jdbc:mysql://26.236.255.233:3306/Despacho"; // Cambia 'nombreBaseDatos' por tu BD
        String user = "Jose2";
        String password = "OuSi2IaohyRu04Yk";

        // Crear conexión
        try {
            connection = DriverManager.getConnection(url, user, password);
            if (connection != null) {
                System.out.println("Conectado a la base de datos MySQL");
            }
        } catch (SQLException e) {
            System.out.println("Error en la conexión a la base de datos: " + e.getMessage());
        }
    }
    
}
