package controlador;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import modelo.DatabaseModel1;
import modelo.DatabaseModel2;
import modelo.DatabaseModel3;

public class ControladorBD {

    public void consultasql(String sentencia) {
        switch (sentencia.split(" ")[0]) {
            case "SELECT":
                if (sentencia.toUpperCase().contains("WHERE")) {
                    System.out.println("The SQL statement contains a WHERE clause.");

                } else {
                    System.out.println("The SQL statement does not contain a WHERE clause.");
                }
                break;
            case "INSERT":
                if (sentencia.toUpperCase().contains("WHERE")) {
                    System.out.println("The SQL statement contains a WHERE clause.");
                } else {
                    System.out.println("The SQL statement does not contain a WHERE clause.");
                }
                break;
            case "UPDATE":
                if (sentencia.toUpperCase().contains("WHERE")) {
                    System.out.println("The SQL statement contains a WHERE clause.");
                } else {
                    System.out.println("The SQL statement does not contain a WHERE clause.");
                }
                break;
            case "DELETE":

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
