package errors;

import javax.swing.JOptionPane;

public class ErrorHandler extends RuntimeException {

    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;

    public ErrorHandler(String message) {
        super(message);
    }

    public static void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

    public static void manejarTimeout(String fragmento) {
        ErrorHandler.showMessage(
                "Error en la ejecución de la transacción: Se acabó el tiempo de espera para el fragmento " + fragmento,
                "Error de transacción",
                ErrorHandler.ERROR_MESSAGE);
    }

    public static void manejarErrorEjecucion(String fragmento, Exception e) {
        ErrorHandler.showMessage(
                "Error en la ejecución de la transacción para el fragmento " + fragmento + ": " + e.getMessage(),
                "Error de transacción",
                ErrorHandler.ERROR_MESSAGE);
    }

}
