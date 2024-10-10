package two_phase_commit;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.TextArea;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.ui.FlatListCellBorder.Default;

import java.util.LinkedHashMap;

import javax.swing.JScrollPane;
import javax.swing.JPanel;

import java.util.List;
import java.util.Map;

public class VistaOpciones extends JPanel implements ComponentListener {

    private JLabel lblQuery;
    private TextArea textArea;

    private JButton btnExcetute;
    private JScrollPane scroll;
    private DefaultTableModel model;

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

        //show results of the query 
        model = new DefaultTableModel();
        JTable table = new JTable(model);
        scroll = new JScrollPane(table);    
        scroll.setWheelScrollingEnabled(true);
        btnExcetute = new JButton("Ejecutar");
        add(scroll);
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

        lblQuery.setBounds((short) (w * .1), (short) (h * .025), (short) (w * .7), (short) (h * .025));

        textArea.setBounds((short) (w * .1), (short) (h * .05), (short) (w * .7), (short) (h * .3));
        btnExcetute.setBounds(textArea.getX(), (short) (textArea.getY() + textArea.getHeight() * 1.1),
        textArea.getWidth(), (short) (h * .05));
        scroll.setBounds((short) (w * .1), (short) (h * .5), (short) (w * .7), (short) (h * .4));
        
    }

    public void llenarTabla(List<Map<String, Object>> resultado) {
        model.setRowCount(0); // Clear existing rows

        if (resultado != null && !resultado.isEmpty()) {
            // Use LinkedHashMap to maintain order
            LinkedHashMap<String, Object> firstRow = new LinkedHashMap<>(resultado.get(0));
            model.setColumnIdentifiers(firstRow.keySet().toArray());

            for (Map<String, Object> row : resultado) {
                LinkedHashMap<String, Object> orderedRow = new LinkedHashMap<>(row);
                Object[] rowData = orderedRow.values().toArray();
                model.addRow(rowData);
            }
        }
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

    public TextArea getTextArea() {
        return textArea;
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public void setModel(DefaultTableModel model) {
        this.model = model;
    }

}
