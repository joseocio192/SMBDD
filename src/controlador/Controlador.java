package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import modelo.Modelo;
import vista.Vista;

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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getItemConn()[0]) {
            System.out.println("PUTO 0");
            return;
        }
    }
}
