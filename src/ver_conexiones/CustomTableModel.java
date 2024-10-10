
package ver_conexiones;

import javax.swing.table.DefaultTableModel;

public class CustomTableModel extends DefaultTableModel {

    private int filaEditable = getRowCount() - 1;

    public CustomTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    public CustomTableModel() {
        super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return row == filaEditable;
    }

    public void setFilaEditable(int fila) {
        this.filaEditable = fila;
        fireTableRowsUpdated(fila, fila);
    }

    public void setLastRowEditable() {
        setFilaEditable(getRowCount() - 1);
    }
}