package two_phase_commit.login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import base_de_datos.DatabaseModel;
import base_de_datos.DatabaseModelMysql;
import base_de_datos.DatabaseModelPostgres;
import base_de_datos.DatabaseModelSQLServer;
import raven.toast.Notifications;
import two_phase_commit.ModeloBD2PC;
import two_phase_commit.Controlador;

public class ControladorConexion implements ActionListener {

    VistaConexion vistaConexion;
    Controlador controlador;
    ModeloBD2PC modeloBD;

    public ControladorConexion(VistaConexion vistaConexion, Controlador controlador,
            ModeloBD2PC modeloBD) {
        this.vistaConexion = vistaConexion;
        this.controlador = controlador;
        this.modeloBD = modeloBD;
        escucharEventos();
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
            if (vistaConexion.getTxtGestor().getText().equalsIgnoreCase("SQLServer")) {
                DatabaseModel conexion = new DatabaseModelSQLServer(servidor, basededatos, usuario, password);
                modeloBD.setConexion(conexion.getConexion());

                controlador.vistaConexiones(modeloBD.getConexion());
            } else if (vistaConexion.getTxtGestor().getText().equalsIgnoreCase("MySQL")) {
                DatabaseModel conexion = new DatabaseModelMysql(servidor, basededatos, usuario, password);
                modeloBD.setConexion(conexion.getConexion());

                controlador.vistaConexiones(modeloBD.getConexion());
            } else if (vistaConexion.getTxtGestor().getText().equalsIgnoreCase("Postgres")) {
                DatabaseModel conexion = new DatabaseModelPostgres(servidor, basededatos, usuario, password);
                modeloBD.setConexion(conexion.getConexion());

                controlador.vistaConexiones(modeloBD.getConexion());
            } else {
                Notifications.getInstance().show(Notifications.Type.ERROR, Notifications.Location.TOP_CENTER,
                        "Gestor de base de datos no válido");
            }

        }
    }
}