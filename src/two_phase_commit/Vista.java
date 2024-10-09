package two_phase_commit;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Vista extends JFrame {

    private JMenuItem[] itemConn;
    private JMenuItem[] opciones;

    public Vista() {
        Interfaz();
    }

    public void Interfaz() {
        setTitle("SGBDD");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setMinimumSize(new Dimension(350, 350));
        setLocationRelativeTo(null);

        JMenuBar mb = new JMenuBar();
        setJMenuBar(mb);

        JMenu menuConexiones = new JMenu("Conexiones");
        mb.add(menuConexiones);
        itemConn = new JMenuItem[4];
        itemConn[0] = new JMenuItem("Conectar  MySQL");
        itemConn[1] = new JMenuItem("Conectar Postgres");
        itemConn[2] = new JMenuItem("Conectar SQL Server");
        itemConn[3] = new JMenuItem("Ver conexiones");
        for (int i = 0; i < itemConn.length; i++) {
            menuConexiones.add(itemConn[i]);
        }

        JMenu menuOpciones = new JMenu("Opciones");
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
}
