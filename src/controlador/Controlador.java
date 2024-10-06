package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;

import modelo.Modelo;
import vista.Vista;
import vista.VistaOpciones;
import vista.VistaConexion;
import modelo.ModeloConexion;

public class Controlador implements ActionListener {
    Vista vista;
    Modelo modelo;

    public Controlador(Vista vista, Modelo modelo) {
        this.vista = vista;
        this.modelo = modelo;
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
        if (e.getSource() == vista.getItemConn()[0]) {
            ModeloConexion modeloConexion = new ModeloConexion();
            VistaConexion vistaConexion = new VistaConexion(vista);
            new ControladorConexion(vistaConexion, modeloConexion);
            vista.setContentPane(vistaConexion);
            vistaConexion.applyComponentOrientation(vista.getComponentOrientation());
            SwingUtilities.updateComponentTreeUI(vista);
            return;
        }
        if (e.getSource() == vista.getOpciones()[0]) {
            System.out.println("Transacciones");
            return;
        }
        if (e.getSource() == vista.getOpciones()[1]) {
            System.out.println("Consultas");
            VistaOpciones vistaOpciones = new VistaOpciones();
            vistaOpciones.setVisible(true);
            return;
        }
    }
}
