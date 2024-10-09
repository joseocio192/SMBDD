package ver_conexiones;

import java.sql.SQLException;

import errors.ErrorHandler;

public class ControladorVerConexiones {

    VistaVerConexiones vistaVerConexiones;
    ModeloVerConexiones modeloVerConexiones;

    public ControladorVerConexiones(VistaVerConexiones vistaVerConexiones, ModeloVerConexiones modeloVerConexiones) {
        this.vistaVerConexiones = vistaVerConexiones;
        this.modeloVerConexiones = modeloVerConexiones;
        llenarTablaConexiones();
        vistaVerConexiones.setVisible(true);
    }

    private void llenarTablaConexiones() {
        try {
            vistaVerConexiones.actualizarTabla(modeloVerConexiones.getDataConexiones());
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al obtener datos de las conexiones: " + e.getMessage(), "Error de conexión",
                    ErrorHandler.ERROR_MESSAGE);
        }
    }

}
