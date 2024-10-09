package ver_conexiones;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloVerConexiones {

    private Connection conexionMysql;
    private Connection conexionPostgres;
    private Connection conexionSQLServer;

    public ModeloVerConexiones(Connection conexionMysql, Connection conexionPostgres, Connection conexionSQLServer) {
        this.conexionMysql = conexionMysql;
        this.conexionPostgres = conexionPostgres;
        this.conexionSQLServer = conexionSQLServer;
    }

    public List<String[]> getDataConexiones() throws SQLException {
        List<String[]> data = new ArrayList<>();

        if (conexionMysql != null) {
            ResultSet rs = conexionMysql.getMetaData().getColumns(null, null, "clientes", null);
            data.add(new String[] { "MySQL", conexionMysql.getCatalog(), "Zona", getColumns(rs) });
        }
        if (conexionPostgres != null) {
            ResultSet rs = conexionPostgres.getMetaData().getColumns(null, null, "clientes", null);
            data.add(new String[] { "Postgres", conexionPostgres.getCatalog(), "Zona", getColumns(rs) });
        }

        if (conexionSQLServer != null) {
            ResultSet rs = conexionSQLServer.getMetaData().getColumns(null, null, "clientes", null);
            data.add(new String[] { "SQL Server", conexionSQLServer.getCatalog(), "Zona", getColumns(rs) });
        }

        return data;
    }

    private String getColumns(ResultSet rs) throws SQLException {
        StringBuilder sb = new StringBuilder();
        while (rs.next()) {
            sb.append("\"" + rs.getString("COLUMN_NAME") + "\"");
            sb.append("; ");
        }
        return sb.toString();
    }

}
