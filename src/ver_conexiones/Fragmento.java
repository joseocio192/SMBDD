package ver_conexiones;

public class Fragmento {
    private String fragmento;
    private String baseDeDatos;
    private String criterio;
    private String atributos;
    private String gestor;
    private String servidor;
    private String usuario;
    private String contrasena;

    public Fragmento(String fragmento, String baseDeDatos, String criterio, String atributos, String gestor,
            String servidor, String usuario, String contrasena) {
        this.fragmento = fragmento;
        this.baseDeDatos = baseDeDatos;
        this.criterio = criterio;
        this.atributos = atributos;
        this.gestor = gestor;
        this.servidor = servidor;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    public String getFragmento() {
        return fragmento;
    }

    public String getBaseDeDatos() {
        return baseDeDatos;
    }

    public String getCriterio() {
        return criterio;
    }

    public String getAtributos() {
        return atributos;
    }

    public String getGestor() {
        return gestor;
    }

    public String getServidor() {
        return servidor;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

}
