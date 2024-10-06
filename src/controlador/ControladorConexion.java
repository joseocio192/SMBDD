package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.ModeloConexion;
import vista.VistaConexion;

public class ControladorConexion implements ActionListener{

    VistaConexion vistaConexion;
    ModeloConexion modeloConexion;

    public ControladorConexion(VistaConexion vistaConexion, ModeloConexion modeloConexion) {
        this.vistaConexion = vistaConexion;
        this.modeloConexion = modeloConexion;
        escucharEventos();
        vistaConexion.repaint();
    }

    private void escucharEventos() {
        vistaConexion.getBtnConectar().addActionListener(this);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == vistaConexion.getBtnConectar()) {
            System.out.println("Conectando...");
        }
	}


}