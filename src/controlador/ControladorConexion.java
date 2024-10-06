package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.DatabaseModel1;
import modelo.DatabaseModel2;
import modelo.DatabaseModel3;
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
            DatabaseModel1 db1 = new DatabaseModel1("localhost", "Despacho", "sa", "123456");
            db1.getConexion();

            DatabaseModel2 db2 = new DatabaseModel2("localhost", "Despacho", "sa", "123456");
            db2.getConexion();

            DatabaseModel3 db3 = new DatabaseModel3("localhost", "Despacho", "sa", "123456");
            db3.getConexion();
        }
    }
}