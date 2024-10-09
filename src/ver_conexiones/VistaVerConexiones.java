package ver_conexiones;

import javax.swing.JPanel;

import raven.toast.Notifications;
import two_phase_commit.Vista;

import com.formdev.flatlaf.FlatClientProperties;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.Dimension;

public class VistaVerConexiones extends JPanel implements ComponentListener {

    private JTable tablaConexiones;
    private JScrollPane scrollPane;
    private DefaultTableModel modeloTabla;

    public VistaVerConexiones(Vista vista) {
        createInterface();
        setMinimumSize(new Dimension(350, 350));

        addComponentListener(this);
        Notifications.getInstance().setJFrame(vista);
    }

    private void createInterface() {
        setLayout(null);
        putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Fragmento");
        modeloTabla.addColumn("Base de datos");
        modeloTabla.addColumn("Criterio");
        modeloTabla.addColumn("Atributos");
        modeloTabla.addRow(new Object[] { "Fragmento 1", "MySQL", "Criterio 1", "Atributo 1" });

        tablaConexiones = new JTable(modeloTabla);
        tablaConexiones.setDefaultEditor(Object.class, null);

        DefaultTableCellRenderer centrarTexto = new DefaultTableCellRenderer();
        centrarTexto.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaConexiones.getColumnCount(); i++) {
            tablaConexiones.getColumnModel().getColumn(i).setCellRenderer(centrarTexto);
        }

        tablaConexiones.getColumnModel().getColumn(0).setPreferredWidth(100);
        tablaConexiones.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaConexiones.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaConexiones.getColumnModel().getColumn(3).setPreferredWidth(250);
        scrollPane = new JScrollPane(tablaConexiones, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    public void actualizarTabla(List<String[]> conexiones) {
        modeloTabla.setRowCount(0);
        for (String[] conexion : conexiones) {
            modeloTabla.addRow(conexion);
        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();

        scrollPane.setBounds((short) (w * 0.05), (short) (h * 0.05), (short) (w * 0.9), (short) (h * 0.9));

        revalidate();

    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // not used
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // not used
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // not used
    }

}
