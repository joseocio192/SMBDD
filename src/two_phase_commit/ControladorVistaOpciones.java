package two_phase_commit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ControladorVistaOpciones implements ActionListener {

    VistaOpciones vistaOpciones;
    Modelo modelo;
    int tipo;

    public ControladorVistaOpciones(VistaOpciones vistaOpciones, Modelo modeloBD, int tipo) {
        this.vistaOpciones = vistaOpciones;
        this.modelo = modeloBD;
        this.tipo = tipo;
        actualizarVista();
        escucharEventos();
    }

    private void actualizarVista() {
        if (tipo == Modelo.TRANSACCIONES)
            vistaOpciones.getLblQuery().setText("Transacción:");
        else if (tipo == Modelo.CONSULTAS)
            vistaOpciones.getLblQuery().setText("Consulta:");
    }

    private void escucharEventos() {
        vistaOpciones.getBtnExcetute().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vistaOpciones.getBtnExcetute()) {
            modelo.processTransactions(tipo, vistaOpciones.getQuery());
            vistaOpciones.llenarTabla(modelo.getResultados());
        }
    }

}