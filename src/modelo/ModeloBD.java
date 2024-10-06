package modelo;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModeloBD {

    public void consultasql(String sentencia) throws SQLException {
        
        switch (sentencia.split(" ")[0].toLowerCase()) {
            case "select":
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
                    Statement stmt1 = DatabaseModel1.conexion.createStatement();
                    Statement stmt2 = DatabaseModel2.conexion.createStatement();
                    Statement stmt3 = DatabaseModel3.conexion.createStatement();

                    ResultSet rs = stmt1.executeQuery(sentencia);
                    ResultSet rs2 = stmt2.executeQuery(sentencia);
                    ResultSet rs3 = stmt3.executeQuery(sentencia);

                    while (rs.next()) {
                        System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
                    }
                    while (rs2.next()) {
                        System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3));
                    }
                    while (rs3.next()) {
                        System.out.println(rs3.getString(1) + " " + rs3.getString(2) + " " + rs3.getString(3));
                    }
                    break;
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
}
