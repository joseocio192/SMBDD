package vista;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

public class Vista extends JFrame implements ComponentListener {

    private JMenuBar mb;
    private JMenu menuConexiones, menuOpciones;
    private JMenuItem[] itemConn;
    private JMenuItem[] opciones;

    public Vista() {
        Interfaz();
        addComponentListener(this);
    }

    public void Interfaz() {
        setTitle("SGBDD");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        mb = new JMenuBar();
        setJMenuBar(mb);

        menuConexiones = new JMenu("Conexiones");
        mb.add(menuConexiones);
        itemConn = new JMenuItem[3];
        itemConn[0] = new JMenuItem("Conectar  MySQL");
        itemConn[1] = new JMenuItem("Conectar SQL Server");
        itemConn[2] = new JMenuItem("Conectar MariaDB");
        for (int i = 0; i < itemConn.length; i++) {
            menuConexiones.add(itemConn[i]);
        }

        menuOpciones = new JMenu("Opciones");
        opciones = new JMenuItem[2];
        opciones[0] = new JMenuItem("Transacciones");
        opciones[1] = new JMenuItem("Consultas");
        for (int i = 0; i < opciones.length; i++) {
            menuOpciones.add(opciones[i]);
        }
        mb.add(menuOpciones);
    }

    public JMenuItem[] getItemConn() {
        return itemConn;
    }

    public JMenuItem[] getOpciones() {
        return opciones;
    }

    @Override
    public void componentResized(ComponentEvent e) {

    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
