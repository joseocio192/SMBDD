package two_phase_commit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VistaOpciones extends JPanel implements ComponentListener {

    private JLabel lblQuery;
    private TextArea textArea;
    private JButton btnExcetute;

    public VistaOpciones() {
        createInterface();
        setMinimumSize(new Dimension(350, 350));
        addComponentListener(this);
    }

    private void createInterface() {
        setLayout(null);

        lblQuery = new JLabel();
        add(lblQuery);

        textArea = new TextArea();
        textArea.setForeground(Color.BLACK);
        add(textArea);

        btnExcetute = new JButton("Ejecutar");
        add(btnExcetute);

    }

    public String getQuery() {
        return textArea.getText();
    }

    public JButton getBtnExcetute() {
        return btnExcetute;
    }

    public JLabel getLblQuery() {
        return lblQuery;
    }

    @Override
    public void componentResized(ComponentEvent e) {
        short w = (short) getWidth();
        short h = (short) getHeight();

        lblQuery.setBounds((short) (w * .1), (short) (h * .05), (short) (w * .7), (short) (h * .05));

        textArea.setBounds((short) (w * .1), (short) (h * .1), (short) (w * .7), (short) (h * .6));
        btnExcetute.setBounds(textArea.getX(), (short) (textArea.getY() + textArea.getHeight() * 1.1),
                textArea.getWidth(), (short) (h * .1));
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
