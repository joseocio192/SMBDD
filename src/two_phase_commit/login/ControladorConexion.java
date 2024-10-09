package two_phase_commit.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import base_de_datos.DatabaseModel;
import base_de_datos.DatabaseModelMysql;
import base_de_datos.DatabaseModelPostgres;
import base_de_datos.DatabaseModelSQLServer;
import two_phase_commit.ModeloBD2PC;

public class ControladorConexion implements ActionListener {

    VistaConexion vistaConexion;
    ModeloBD2PC modeloBD;
    int database;

    public ControladorConexion(VistaConexion vistaConexion, ModeloBD2PC modeloBD, int database) {
        this.vistaConexion = vistaConexion;
        this.modeloBD = modeloBD;
        this.database = database;
        initLogin();
        escucharEventos();
    }

    private void initLogin() {
        if (database == 0)
            vistaConexion.getLblLogin().setText("Login MySQL");
        else if (database == 1)
            vistaConexion.getLblLogin().setText("Login Postgres");
        else if (database == 2)
            vistaConexion.getLblLogin().setText("Login SQL Server");
    }

    private void escucharEventos() {
        vistaConexion.getBtnConectar().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String servidor = vistaConexion.getTxtServidor().getText();
        String basededatos = vistaConexion.getTxtBasedeDatos().getText();
        String usuario = vistaConexion.getTxtUsuario().getText();
        char[] passwordChars = vistaConexion.getTxtPassword().getPassword();
        String password = new String(passwordChars);

        // if (servidor.equals("") || basededatos.equals("") || usuario.equals("") ||
        // password.equals("")) {
        // logger.info("Faltan datos de conexión");
        // Notifications.getInstance().show(Notifications.Type.INFO,
        // Notifications.Location.TOP_CENTER,
        // "Escribir los datos de conexión");
        // return;
        // }

        if (e.getSource() == vistaConexion.getBtnConectar()) {
            if (database == 0) {
                DatabaseModel conexion = new DatabaseModelMysql(servidor, basededatos, usuario, password);
                modeloBD.setConexionMysql(conexion.getConexion());
            } else if (database == 1) {
                DatabaseModel conexion = new DatabaseModelPostgres(servidor, basededatos, usuario, password);
                modeloBD.setConexionPostgres(conexion.getConexion());
            } else if (database == 2) {
                DatabaseModel conexion = new DatabaseModelSQLServer(servidor, basededatos, usuario, password);
                modeloBD.setConexionSQLServer(conexion.getConexion());
            }
        }
    }
}