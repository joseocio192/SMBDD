package base_de_datos;

import java.sql.Connection;

public abstract class DatabaseModel {
    protected String servidor;
    protected String basededatos;
    protected String usuario;
    protected String password;
    protected Connection conexion;

    protected DatabaseModel(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public abstract Connection getConexion();
}
