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
                    String result = selectWOWhere(sentencia, DatabaseModel1.conexion);
                    String result2 = selectWOWhere(sentencia, DatabaseModel2.connection);
                    String result3 = selectWOWhere(sentencia, DatabaseModel3.conexion);
                    System.out.println(result);
                    System.out.println(result2);
                    System.out.println(result3);
                } else {
                    ResultSet rs1 = DatabaseModel1.conexion.prepareStatement(sentencia).executeQuery();
                    while (rs1.next()) {
                        for (int i = 1; i <= rs1.getMetaData().getColumnCount(); i++) {
                            System.out.print(rs1.getString(i) + " ");
                        }
                        System.out.println();
                    }
                }
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

    public String selectWOWhere(String sentencia, Connection conexion) throws SQLException {
        //sql server
        StringBuilder jsonBuilder = new StringBuilder();
        ResultSet rs2 = conexion.prepareStatement(sentencia).executeQuery();
        while (rs2.next()) {
            jsonBuilder.append("{");
            for (int i = 1; i <= rs2.getMetaData().getColumnCount(); i++) {
                String columnName = rs2.getMetaData().getColumnName(i);
                String columnValue = rs2.getString(i);
                jsonBuilder.append("\"").append(columnName).append("\": \"").append(columnValue).append("\"");
                if (i < rs2.getMetaData().getColumnCount()) {
                    jsonBuilder.append(", ");
                }
            }
            jsonBuilder.append("}");
        }
        //mysql
        
        //postgres
        
        return jsonBuilder.toString();
    }
}
