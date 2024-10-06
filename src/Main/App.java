package Main;
import javax.swing.*;

import controlador.Controlador;
import modelo.Modelo;
import vista.Vista;

public class App {
    public static void main(String[] args) throws Exception {
        Vista vista = new Vista();
        Modelo modelo = new Modelo();
        new Controlador(vista, modelo);
    }
}
