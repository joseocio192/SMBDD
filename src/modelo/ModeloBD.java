package modelo;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModeloBD {

    public void consultasql(String sentencia) throws SQLException {

        switch (sentencia.split(" ")[0].toLowerCase()) {
            case "select":
                if (!sentencia.toUpperCase().contains("WHERE")) {
                    selectWOWhere(sentencia, DatabaseModel1.conexion);
                }
                if (!sentencia.toUpperCase().contains("WHERE")) {
                    ResultSet rs2 = DatabaseModel1.conexion.prepareStatement(sentencia).executeQuery();
                    while (rs2.next()) {
                        for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                            System.out.print(rs2.getString(i) + " ");
                        }
                        System.out.println();
                    }
                    break;
                }

                String criterio = sentencia.substring(sentencia.toUpperCase().indexOf("WHERE") + 5);
                System.out.println(criterio);
                if (!criterio.equalsIgnoreCase("estado")) {

                    break;
                }
                // get which database to use

                break;
            case "insert":
                if (sentencia.toUpperCase().contains("WHERE")) {
                    System.out.println("The SQL statement contains a WHERE clause.");
                } else {
                    System.out.println("The SQL statement does not contain a WHERE clause.");
                }
                break;
            case "update":
                if (sentencia.toUpperCase().contains("WHERE")) {
                    System.out.println("The SQL statement contains a WHERE clause.");
                } else {
                    System.out.println("The SQL statement does not contain a WHERE clause.");
                }
                break;
            case "delete":

                break;
            default:
                break;
        }
    }

    public void Transacciones(String sentencia) {
        switch (sentencia.split(" ")[0]) {
            case "SELECT":

                break;
            case "INSERT":

                break;
            case "UPDATE":

                break;
            case "DELETE":

                break;
            default:
                break;
        }
    }

    public void selectWOWhere(String sentencia, Connection conexion) throws SQLException {
        // SQL SERVER
        ResultSet rs2 = conexion.prepareStatement(sentencia).executeQuery();
        while (rs2.next()) {
            for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                System.out.print(rs2.getString(i) + " ");
            }
            System.out.println();
        }
        // MYSQL

        // POSTGRESQL

    }
}
