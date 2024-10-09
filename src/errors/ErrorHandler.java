package errors;

import javax.swing.JOptionPane;

public class ErrorHandler {

    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE = JOptionPane.INFORMATION_MESSAGE;
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;

    private ErrorHandler() {
    }

    public static void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(null, message, title, messageType);
    }

}
