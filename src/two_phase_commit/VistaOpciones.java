package two_phase_commit;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;

public class VistaOpciones extends JPanel implements ComponentListener {

    public VistaOpciones() {
        createInterface();
        setMinimumSize(new Dimension(350, 350));
    }

    private void createInterface() {
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
