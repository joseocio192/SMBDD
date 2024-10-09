package two_phase_commit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import errors.ErrorHandler;
import two_phase_commit.login.VistaConexion;
import ver_conexiones.ControladorVerConexiones;
import ver_conexiones.ModeloVerConexiones;
import ver_conexiones.VistaVerConexiones;
import two_phase_commit.login.ControladorConexion;

public class Controlador implements ActionListener {
    Vista vista;
    ModeloBD2PC modeloBD;

    public Controlador(Vista vista, ModeloBD2PC modeloBD) {
        this.vista = vista;
        this.modeloBD = modeloBD;
        escucharEventos();
        vista.setVisible(true);
    }

    public void escucharEventos() {
        for (int i = 0; i < vista.getItemConn().length; i++) {
            vista.getItemConn()[i].addActionListener(this);
        }
        for (int i = 0; i < vista.getOpciones().length; i++) {
            vista.getOpciones()[i].addActionListener(this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getItemConn()[0]) { // MySQL
            vistaConexion(modeloBD.getMYSQL());
            return;
        }
        if (e.getSource() == vista.getItemConn()[1]) { // Postgres
            vistaConexion(modeloBD.getPOSTGRESQL());
            return;
        }
        if (e.getSource() == vista.getItemConn()[2]) { // SQL Server
            vistaConexion(modeloBD.getSQLSERVER());
            return;
        }
        if (e.getSource() == vista.getItemConn()[3]) { // Ver conexiones
            vistaConexiones();
            return;
        }
        // -------------------------------------------
        if (e.getSource() == vista.getOpciones()[0]) { // Transacciones
            return;
        }
        if (e.getSource() == vista.getOpciones()[1]) { // Consultas
            try {
                modeloBD.ejecutar2PC("select * from clientes");
                System.out.println(
                        modeloBD.getResultadosSQLServer().toString() + "\n" + modeloBD.getResultadosMysql().toString());

            } catch (SQLException e1) {
                e1.printStackTrace();
                ErrorHandler.showMessage(e1.getLocalizedMessage(), "Error", 0);
            }
        }
    }

    public void vistaConexion(int database) {
        VistaConexion vistaConexion = new VistaConexion(vista);
        new ControladorConexion(vistaConexion, modeloBD, database);
        vista.setContentPane(vistaConexion);
        SwingUtilities.updateComponentTreeUI(vista);
        SwingUtilities.invokeLater(() -> {
            vista.setContentPane(vistaConexion);
            vista.repaint();
        });
    }

    public void vistaConexiones() {
        VistaVerConexiones vistaVerConexiones = new VistaVerConexiones(vista);
        ModeloVerConexiones modeloVerConexiones = new ModeloVerConexiones(modeloBD.getConexionMysql(),
                modeloBD.getConexionPostgres(), modeloBD.getConexionSQLServer());
        new ControladorVerConexiones(vistaVerConexiones, modeloVerConexiones);
        vista.setContentPane(vistaVerConexiones);
        SwingUtilities.updateComponentTreeUI(vista);
        SwingUtilities.invokeLater(() -> {
            vista.setContentPane(vistaVerConexiones);
            vista.repaint();
        });
    }
}
