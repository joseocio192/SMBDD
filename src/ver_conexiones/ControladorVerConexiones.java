package ver_conexiones;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import errors.ErrorHandler;
import raven.toast.Notifications;

public class ControladorVerConexiones implements ActionListener {

    VistaVerConexiones vistaVerConexiones;
    ModeloVerConexiones modeloVerConexiones;

    public ControladorVerConexiones(VistaVerConexiones vistaVerConexiones, ModeloVerConexiones modeloVerConexiones) {
        this.vistaVerConexiones = vistaVerConexiones;
        this.modeloVerConexiones = modeloVerConexiones;
        showFragmentos();
        escuchadores();
        vistaVerConexiones.setVisible(true);
    }

    private void showFragmentos() {
        try {
            vistaVerConexiones.showFragmentos(modeloVerConexiones.getFragmentos());
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al obtener los fragmentos", "Error", 0);
        }
    }

    private void escuchadores() {
        vistaVerConexiones.getBtnAgregarFragmento().addActionListener(this);
        vistaVerConexiones.getBtnEliminarFragmento().addActionListener(this);
        vistaVerConexiones.getBtnEditarFragmento().addActionListener(this);
        vistaVerConexiones.getBtnGuardarFragmento().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaVerConexiones.getBtnEliminarFragmento()) {
            if (modeloVerConexiones.deleteFragment(vistaVerConexiones.getIDAtSelectedRow())) {
                vistaVerConexiones.eliminarFragmento();
                vistaVerConexiones.showNotification("Fragmento eliminado correctamente", Notifications.Type.SUCCESS);
            } else {
                vistaVerConexiones.showNotification("No se pudo eliminar el fragmento", Notifications.Type.ERROR);
            }
        }
        if (vistaVerConexiones.getTablaConexiones() != null && vistaVerConexiones.isLastRowEmpty()) {
            vistaVerConexiones.showNotification("Primero Complete la fila actual.", Notifications.Type.INFO);
            return;
        }
        if (e.getSource() == vistaVerConexiones.getBtnAgregarFragmento()) {
            vistaVerConexiones.enableFragmentEditing();
            return;
        }
        if (e.getSource() == vistaVerConexiones.getBtnEditarFragmento()) {
            vistaVerConexiones.enableEditionSelectedRow();
            return;
        }

        if (e.getSource() == vistaVerConexiones.getBtnGuardarFragmento()) {
            if (modeloVerConexiones.addFragment(vistaVerConexiones.getFragmentos()))
                vistaVerConexiones.showNotification("Fragmento guardado correctamente", Notifications.Type.SUCCESS);
            else
                vistaVerConexiones.showNotification("No se pudo guardar el fragmento", Notifications.Type.ERROR);
        }
    }

}
