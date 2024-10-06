package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.sql.ResultSet;

public class MyPostgreConnection {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://26.46.40.58:1212/Despacho";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "123456");
        props.setProperty("ssl", "false");
        Connection conn;
        try {
            conn = DriverManager.getConnection(url, props);
            System.out.println("Conexión exitosa a PostgreSQL");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
