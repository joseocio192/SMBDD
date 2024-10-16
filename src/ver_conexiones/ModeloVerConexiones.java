package ver_conexiones;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import raven.toast.Notifications;

public class ModeloVerConexiones {

    private static final String FRAGMENTOS = "fragmentos";

    private Connection conexion;

    public ModeloVerConexiones(Connection conexion) {
        this.conexion = conexion;
    }

    public boolean addFragment(List<Fragmento> fragmentos) {
        try {
            List<Fragmento> fragmentosT = getFragmentos();
            for (Fragmento fragmento : fragmentos) {
                if (fragmentos.get(0).getFragmento().equals(fragmentosT.get(0).getFragmento())) {
                    return editFragment(fragmento);
                } else {
                    return safeFragment(fragmento);
                }
            }
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
        }
        return false;
    }

    public boolean safeFragment(Fragmento fragmento) {
        if (fragmento == null) {
            return false;
        }
        String query = "INSERT INTO " + FRAGMENTOS + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, fragmento.getFragmento());
            ps.setString(2, fragmento.getBaseDeDatos());
            ps.setString(3, fragmento.getCriterio());
            ps.setString(4, fragmento.getAtributos());
            ps.setString(5, fragmento.getGestor());
            ps.setString(6, fragmento.getServidor());
            ps.setString(7, fragmento.getUsuario());
            ps.setString(8, fragmento.getContrasena());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            return false;
        }
    }

    public boolean editFragment(Fragmento fragmento) {
        if (fragmento == null) {
            return false;
        }
        String query = "UPDATE " + FRAGMENTOS + " SET base_de_datos = ?, criterio = ?, atributos = ?, gestor = ?, "
                + "servidor = ?, usuario = ?, contrasena = ? WHERE fragmento = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, fragmento.getBaseDeDatos());
            ps.setString(2, fragmento.getCriterio());
            ps.setString(3, fragmento.getAtributos());
            ps.setString(4, fragmento.getGestor());
            ps.setString(5, fragmento.getServidor());
            ps.setString(6, fragmento.getUsuario());
            ps.setString(7, fragmento.getContrasena());
            ps.setString(8, fragmento.getFragmento());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            return false;
        }
    }

    public List<Fragmento> getFragmentos() throws SQLException {
        List<Fragmento> fragmentos = new ArrayList<>();
        String query = "SELECT * FROM " + FRAGMENTOS;

        PreparedStatement ps = conexion.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String fragmento = rs.getString(1);
            String baseDeDatos = rs.getString(2);
            String criterio = rs.getString(3);
            String atributos = rs.getString(4);
            String gestor = rs.getString(5);
            String servidor = rs.getString(6);
            String usuario = rs.getString(7);
            String contrasena = rs.getString(8);

            fragmentos.add(new Fragmento(fragmento, baseDeDatos, criterio, atributos, gestor, servidor, usuario,
                    contrasena));
        }
        return fragmentos;
    }

    public boolean deleteFragment(String fragmentID) {
        if (fragmentID == null) {
            return false;
        }
        String query = "DELETE FROM " + FRAGMENTOS + " WHERE fragmento = ?";

        try (PreparedStatement ps = conexion.prepareStatement(query)) {
            ps.setString(1, fragmentID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Notifications.getInstance().show(Notifications.Type.ERROR, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}
