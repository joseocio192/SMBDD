package two_phase_commit;

public class Semaforo {
    private int recursos;

    public Semaforo(int recursos) {
        this.recursos = recursos;
    }

    public synchronized void espera() {
        while (recursos == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        recursos--;
    }

    public synchronized void libera() {
        recursos++;
        notifyAll();
    }

}
