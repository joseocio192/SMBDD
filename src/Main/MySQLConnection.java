package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class MySQLConnection {

    public static void main(String[] args) {
        // URL de conexión a la base de datos
        String url = "jdbc:mysql://26.236.255.233:3306/laravel"; // Cambia 'nombreBaseDatos' por tu BD
        String user = "Jose2";

        // Crear conexión
        try (Connection connection = DriverManager.getConnection(url, user, "OuSi2IaohyRu04Yk")) {
            System.out.println("Conexión exitosa a MySQL");

            // Crear una sentencia SQL
            String query = "SELECT * FROM pais"; // Cambia 'tablaEjemplo' por tu tabla
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Procesar los resultados
            while (resultSet.next()) {
                // Por ejemplo, obtener el valor de la columna "id" y "nombre"
                int id = resultSet.getInt("id");
                String nombre = resultSet.getString("nombre");
                System.out.println("ID: " + id + ", Nombre: " + nombre);
            }

        } catch (SQLException e) {
            System.out.println("Error al conectarse a MySQL: " + e.getMessage());
        }
    }
}
