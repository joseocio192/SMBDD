package ver_conexiones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;

import com.formdev.flatlaf.FlatClientProperties;

import raven.toast.Notifications;
import two_phase_commit.Vista;

public class VistaVerConexiones extends JPanel implements ComponentListener {

    private JTable tablaConexiones;
    private JScrollPane scrollPane;
    private CustomTableModel modeloTabla;

    private JButton btnAgregarFragmento;
    private JButton btnEliminarFragmento;
    private JButton btnEditarFragmento;
    private JButton btnGuardarFragmento;

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

        btnAgregarFragmento = new JButton("Agregar fragmento");
        add(btnAgregarFragmento);

        btnEliminarFragmento = new JButton("Eliminar fragmento");
        add(btnEliminarFragmento);

        btnEditarFragmento = new JButton("Editar fragmento");
        add(btnEditarFragmento);

        btnGuardarFragmento = new JButton("Guardar fragmento");
        add(btnGuardarFragmento);

        modeloTabla = new CustomTableModel();
        modeloTabla.addColumn("Fragmento");
        modeloTabla.addColumn("Base de datos");
        modeloTabla.addColumn("Criterio");
        modeloTabla.addColumn("Atributos");
        modeloTabla.addColumn("Gestor");
        modeloTabla.addColumn("Servidor");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Contraseña");

        tablaConexiones = new JTable(modeloTabla);

        DefaultTableCellRenderer centrarTexto = new DefaultTableCellRenderer();
        centrarTexto.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < tablaConexiones.getColumnCount(); i++) {
            tablaConexiones.getColumnModel().getColumn(i).setCellRenderer(centrarTexto);
        }

        for (int i = 0; i < tablaConexiones.getColumnCount(); i++) {
            tablaConexiones.getColumnModel().getColumn(i).setMinWidth(100);
        }
        scrollPane = new JScrollPane(tablaConexiones);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        tablaConexiones.setPreferredScrollableViewportSize(new Dimension(800, 200));

        tablaConexiones.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void enableFragmentEditing() {
        modeloTabla.addRow(new Object[tablaConexiones.getColumnCount()]);
        modeloTabla.setLastRowEditable();
        int newRow = modeloTabla.getRowCount() - 1;
        tablaConexiones.setRowSelectionInterval(newRow, newRow);
        tablaConexiones.requestFocus();
    }

    public boolean isLastRowEmpty() {
        int lastRow = modeloTabla.getRowCount() - 1;
        for (int i = 0; i < tablaConexiones.getColumnCount(); i++) {
            if (tablaConexiones.getValueAt(lastRow, i) == null) {
                return true;
            }
        }
        return false;
    }

    public void enableEditionSelectedRow() {
        int filaSeleccionada = tablaConexiones.getSelectedRow();

        if (filaSeleccionada != -1) {
            modeloTabla.setFilaEditable(filaSeleccionada);
            tablaConexiones.setEnabled(true); // Habilita la tabla para edición
            tablaConexiones.editCellAt(filaSeleccionada, 0); // Enfoca la primera celda para edición
            tablaConexiones.requestFocus();
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, selecciona una fila para editar.");
        }
    }

    public void showNotification(String message, Notifications.Type type) {
        Notifications.getInstance().show(type, message);
    }

    public JTable getTablaConexiones() {
        if (tablaConexiones.getRowCount() == 0) {
            return null;
        }
        return tablaConexiones;
    }

    public List<Fragmento> getFragmentos() {
        List<Fragmento> listaFragmentos = new ArrayList<>();

        for (int i = 0; i < modeloTabla.getRowCount(); i++) {
            String fragmento = (String) modeloTabla.getValueAt(i, 0);
            String baseDeDatos = (String) modeloTabla.getValueAt(i, 1);
            String criterio = (String) modeloTabla.getValueAt(i, 2);
            String atributos = (String) modeloTabla.getValueAt(i, 3);
            String gestor = (String) modeloTabla.getValueAt(i, 4);
            String servidor = (String) modeloTabla.getValueAt(i, 5);
            String usuario = (String) modeloTabla.getValueAt(i, 6);
            String contrasena = (String) modeloTabla.getValueAt(i, 7);

            Fragmento fragmentoObj = new Fragmento(fragmento, baseDeDatos, criterio, atributos, gestor, servidor,
                    usuario, contrasena);

            listaFragmentos.add(fragmentoObj);
        }
        return listaFragmentos;
    }

    public String getIDAtSelectedRow() {
        int selectedRow = tablaConexiones.getSelectedRow();
        if (selectedRow != -1) {
            return (String) modeloTabla.getValueAt(selectedRow, 0);
        }
        return null;
    }

    public void enableTableEditing(boolean enable) {
        tablaConexiones.setEnabled(enable);
        if (enable) {
            modeloTabla.addRow(new Object[tablaConexiones.getColumnCount()]);
        }
    }

    public void eliminarFragmento() {
        int selectedRow = tablaConexiones.getSelectedRow();
        if (selectedRow != -1) {
            modeloTabla.removeRow(selectedRow);
        }
    }

    public void showFragmentos(List<Fragmento> fragmentos) {
        for (Fragmento fragmento : fragmentos) {
            Object[] fila = { fragmento.getFragmento(), fragmento.getBaseDeDatos(), fragmento.getCriterio(),
                    fragmento.getAtributos(), fragmento.getGestor(), fragmento.getServidor(), fragmento.getUsuario(),
                    fragmento.getContrasena() };
            modeloTabla.addRow(fila);
        }
    }

    public JButton getBtnAgregarFragmento() {
        return btnAgregarFragmento;
    }

    public JButton getBtnEliminarFragmento() {
        return btnEliminarFragmento;
    }

    public JButton getBtnEditarFragmento() {
        return btnEditarFragmento;
    }

    public JButton getBtnGuardarFragmento() {
        return btnGuardarFragmento;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();

        btnAgregarFragmento.setBounds((short) (w * 0.05), (short) (h * 0.05), (short) (w * 0.2), (short) (h * 0.1));
        btnEliminarFragmento.setBounds((short) (btnAgregarFragmento.getX() + btnAgregarFragmento.getWidth() * 1.16),
                btnAgregarFragmento.getY(), btnAgregarFragmento.getWidth(), btnAgregarFragmento.getHeight());
        btnEditarFragmento.setBounds((short) (btnEliminarFragmento.getX() + btnEliminarFragmento.getWidth() * 1.16),
                btnEliminarFragmento.getY(), btnEliminarFragmento.getWidth(), btnEliminarFragmento.getHeight());

        btnGuardarFragmento.setBounds((short) (btnEditarFragmento.getX() + btnEditarFragmento.getWidth() * 1.16),
                btnEditarFragmento.getY(), btnEditarFragmento.getWidth(), btnEditarFragmento.getHeight());

        scrollPane.setBounds((short) (btnAgregarFragmento.getX()),
                (short) (btnAgregarFragmento.getY() + btnAgregarFragmento.getHeight() * 1.1),
                (short) (w * 0.9), (short) (h * 0.8));

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
