package two_phase_commit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.SwingUtilities;

import errors.ErrorHandler;
import two_phase_commit.login.ControladorConexion;
import two_phase_commit.login.VistaConexion;
import ver_conexiones.ControladorVerConexiones;
import ver_conexiones.ModeloVerConexiones;
import ver_conexiones.VistaVerConexiones;
import two_phase_commit.ControladorVistaOpciones;

public class Controlador implements ActionListener {
    Vista vista;
    Modelo modeloBD;

    public Controlador(Vista vista, Modelo modeloBD) {
        this.vista = vista;
        this.modeloBD = modeloBD;
        escucharEventos();
        panelLogin();
        vista.setVisible(true);
    }

    private void panelLogin() {
        VistaConexion vistaConexion = new VistaConexion();
        new ControladorConexion(vistaConexion, this, modeloBD);
        vista.add(vistaConexion);
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
        if (e.getSource() == vista.getItemConn()[0]) { // Ver conexiones
            if (modeloBD.getConexion() == null) {
                ErrorHandler.showMessage("No hay conexión", "Error", 0);
                return;
            }
            vistaConexiones(modeloBD.getConexion());
            return;
        }
        if (e.getSource() == vista.getItemConn()[1]) { // Login
            modeloBD.cerrarConexion();
            vistaLogin();
            return;
        }
        // -------------------------------------------
        if (e.getSource() == vista.getOpciones()[0]) { // Transacciones
            vistaBoard(Modelo.TRANSACCIONES);
            return;
        }
        if (e.getSource() == vista.getOpciones()[1]) { // Consultas
            vistaBoard(Modelo.CONSULTAS);

        }
    }

    public void vistaBoard(int tipo) {
        VistaOpciones vistaOpciones = new VistaOpciones();
        new ControladorVistaOpciones(vistaOpciones, modeloBD, tipo);
        vista.setContentPane(vistaOpciones);
        SwingUtilities.updateComponentTreeUI(vista);
        SwingUtilities.invokeLater(() -> {
            vista.setContentPane(vistaOpciones);
            vista.repaint();
        });
    }

    public void vistaConexiones(Connection conexion) {
        VistaVerConexiones vistaVerConexiones = new VistaVerConexiones(vista);
        ModeloVerConexiones modeloVerConexiones = new ModeloVerConexiones(conexion);
        new ControladorVerConexiones(vistaVerConexiones, modeloVerConexiones);
        vista.setContentPane(vistaVerConexiones);
        SwingUtilities.updateComponentTreeUI(vista);
        SwingUtilities.invokeLater(() -> {
            vista.setContentPane(vistaVerConexiones);
            vista.repaint();
        });
    }

    public void vistaLogin() {
        VistaConexion vistaConexion = new VistaConexion();
        new ControladorConexion(vistaConexion, this, modeloBD);
        vista.setContentPane(vistaConexion);
        SwingUtilities.updateComponentTreeUI(vista);
        SwingUtilities.invokeLater(() -> {
            vista.setContentPane(vistaConexion);
            vista.repaint();
        });
    }
}
