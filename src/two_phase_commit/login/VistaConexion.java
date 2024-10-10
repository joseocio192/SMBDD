package two_phase_commit.login;

import javax.swing.*;

import com.formdev.flatlaf.FlatClientProperties;

import layouts.Contenedorlbl_txtLayout;
import raven.toast.Notifications;

import java.awt.event.*;
import java.awt.*;

import java.util.logging.Logger;
import two_phase_commit.Vista;

public class VistaConexion extends JPanel implements ComponentListener {

    transient Logger logger = Logger.getLogger(VistaConexion.class.getName());
    private JButton btnConectar;

    private JLabel lblGestor;
    private JLabel lblLogin;
    private JLabel lblServidor;
    private JLabel lblBasedeDatos;
    private JLabel lblUsuario;
    private JLabel lblPassword;

    private JTextField txtGestor;
    private JTextField txtServidor;
    private JTextField txtBasedeDatos;
    private JTextField txtUsuario;
    private JPasswordField txtPassword;

    private JPanel panel;

    public VistaConexion() {
        createInterface();
        setMinimumSize(new Dimension(350, 350));

        addComponentListener(this);
    }

    private void createInterface() {
        setLayout(null);

        panel = new JPanel();

        panel.setLayout(new Contenedorlbl_txtLayout());
        panel.putClientProperty(FlatClientProperties.STYLE, ""
                + "border:20,2,2,2;"
                + "background:$Menu.background;"
                + "arc:10");
        add(panel);

        lblLogin = new JLabel("Login");
        panel.add(lblLogin);

        lblGestor = new JLabel("Gestor");
        panel.add(lblGestor);

        txtGestor = new JTextField("");
        panel.add(txtGestor);

        lblServidor = new JLabel("Servidor");
        panel.add(lblServidor);

        txtServidor = new JTextField("localhost");
        panel.add(txtServidor);

        lblBasedeDatos = new JLabel("Base de Datos");
        panel.add(lblBasedeDatos);

        txtBasedeDatos = new JTextField("despacho");
        panel.add(txtBasedeDatos);

        lblUsuario = new JLabel("Usuario");
        panel.add(lblUsuario);

        txtUsuario = new JTextField("sa");
        panel.add(txtUsuario);

        lblPassword = new JLabel("Contraseña");
        panel.add(lblPassword);

        txtPassword = new JPasswordField("123456");
        panel.add(txtPassword);

        btnConectar = new JButton("Conectar");
        panel.add(btnConectar);

        setVisible(true);
    }

    public JButton getBtnConectar() {
        return btnConectar;
    }

    public JTextField getTxtGestor() {
        return txtGestor;
    }

    public JTextField getTxtServidor() {
        return txtServidor;
    }

    public JTextField getTxtBasedeDatos() {
        return txtBasedeDatos;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public JLabel getLblLogin() {
        return lblLogin;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();
        System.out.println("w: " + w + " h: " + h);

        panel.setBounds((short) (w * .1), (short) (h * .1), (short) (w * .8), (short) (h * .7));

        Font fontTitle = new Font("Roboto", Font.BOLD, (short) (14 + getWidth() * .01));
        lblLogin.setFont(fontTitle);
        Font fontLbl = new Font("Roboto", Font.BOLD, (short) (10 + getWidth() * .01));
        lblServidor.setFont(fontLbl);
        lblBasedeDatos.setFont(fontLbl);
        lblUsuario.setFont(fontLbl);
        lblPassword.setFont(fontLbl);
        btnConectar.setFont(fontLbl);

        Font fontTxt = new Font("Roboto", Font.PLAIN, (short) (10 + getWidth() * .01));
        txtServidor.setFont(fontTxt);
        txtBasedeDatos.setFont(fontTxt);
        txtUsuario.setFont(fontTxt);
        txtPassword.setFont(fontTxt);
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component move events.
    }

    @Override
    public void componentShown(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component shown events.
    }

    @Override
    public void componentHidden(ComponentEvent e) {
        // This method is intentionally left empty because we do not need to handle
        // component hidden events.
    }
}