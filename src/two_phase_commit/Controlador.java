package two_phase_commit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import errors.ErrorHandler;
import two_phase_commit.login.ControladorConexion;
import two_phase_commit.login.VistaConexion;
import ver_conexiones.ControladorVerConexiones;
import ver_conexiones.ModeloVerConexiones;
import ver_conexiones.VistaVerConexiones;

public class Controlador implements ActionListener {
    Vista vista;
    ModeloBD2PC modeloBD;

    public Controlador(Vista vista, ModeloBD2PC modeloBD) {
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
            if (modeloBD.getConexion() == null) {
                ErrorHandler.showMessage("No hay conexión", "Error", 0);
                return;
            }
            SQLparser parser = new SQLparser(modeloBD.getConexion());
            try {
                parser.ejecutarTransacion("update clientes set estado = 'Baja California' where estado = 'Baja Californa'");
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return;
        }
        if (e.getSource() == vista.getOpciones()[1]) { // Consultas
            if (modeloBD.getConexion() == null) {
                ErrorHandler.showMessage("No hay conexión", "Error", 0);
                return;
            }
            SQLparser parser = new SQLparser(modeloBD.getConexion());
            List<Map<String, Object>> x = parser.ejecutarSelect("select * from clientes");
            
            System.out.println(x.toString());
        }
    }

    // public void vistaConexion(int database) {
    // VistaConexion vistaConexion = new VistaConexion(vista);
    // new ControladorConexion(vistaConexion, modeloBD, database);
    // vista.setContentPane(vistaConexion);
    // SwingUtilities.updateComponentTreeUI(vista);
    // SwingUtilities.invokeLater(() -> {
    // vista.setContentPane(vistaConexion);
    // vista.repaint();
    // });
    // }

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
