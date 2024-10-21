package two_phase_commit;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import java.util.ArrayList;

import errors.ErrorHandler;

public class TwoPhaseCommitManager {

    private static final long TIMEOUT = 10000;
    private Map<String, AtomicBoolean> fragmentStatus;
    private AtomicBoolean allPrepared;
    private Map<Zona, Connection> conexiones;
    private Semaforo semaforo;
    private CountDownLatch latch;
    private volatile boolean commitDecision;

    public TwoPhaseCommitManager(Map<Zona, Connection> conexiones) {
        this.conexiones = conexiones;
        this.fragmentStatus = new ConcurrentHashMap<>();
        this.allPrepared = new AtomicBoolean(false);
    }

    public void ejecutarTransaccion(String sentencia, List<String> targetFragments, ConnectionManager connectionManager)
            throws ErrorHandler {
        fragmentStatus.clear();
        for (String fragmento : targetFragments) {
            fragmentStatus.put(fragmento, new AtomicBoolean(false));
        }

        semaforo = new Semaforo(0);
        latch = new CountDownLatch(targetFragments.size());
        commitDecision = false;

        List<Thread> threads = new ArrayList<>();
        Thread supervisorThread = new Thread(this::supervisorThread);
        threads.add(supervisorThread);

        for (String fragmento : targetFragments) {
            Thread fragmentThread = new Thread(() -> fragmentThread(sentencia, fragmento, connectionManager));
            threads.add(fragmentThread);
        }

        threads.forEach(Thread::start);

        try {
            supervisorThread.join(TIMEOUT);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (allPrepared.get()) {
            System.out.println("Transacción completada con éxito");
            ErrorHandler.showMessage("Transacción completada con éxito", "Transacción completada",
                    ErrorHandler.INFORMATION_MESSAGE);
        } else {
            System.err.println("Transacción abortada. Los cambios han sido revertidos.");
            ErrorHandler.showMessage("Transacción abortada. Los cambios han sido revertidos.", "Transacción abortada",
                    ErrorHandler.ERROR_MESSAGE);
        }
    }

    private void fragmentThread(String sentencia, String fragmento, ConnectionManager connectionManager) {
        try {
            if (connectionManager.crearConexion(fragmento))
                prepararFragmento(sentencia, fragmento);
        } catch (SQLException e) {
            SwingUtilities.invokeLater(() -> {
                ErrorHandler.showMessage(
                        "Error al crear la conexión para el fragmento " + fragmento + ": " + e.getMessage(),
                        "Error de conexión", ErrorHandler.ERROR_MESSAGE);
            });
        } finally {
            latch.countDown();
            semaforo.espera();
            if (commitDecision) {
                commitFragmento(fragmento);
            } else {
                rollbackFragmento(fragmento);
            }
        }
    }

    private void supervisorThread() {
        try {
            boolean allDone = latch.await(TIMEOUT, TimeUnit.MILLISECONDS);
            if (allDone) {
                commitDecision = true;
                for (AtomicBoolean status : fragmentStatus.values()) {
                    if (!status.get()) {
                        commitDecision = false;
                        break;
                    }
                }
            } else {
                commitDecision = false;
            }
            allPrepared.set(commitDecision);

            for (int i = 0; i < fragmentStatus.size(); i++) {
                semaforo.libera();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void prepararFragmento(String sentencia, String fragmento) throws SQLException {
        System.out.println("Preparando fragmento " + fragmento);
        try {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona == null) {
                System.out.println("No se encontró la zona para el fragmento " + fragmento);
                return;
            }
            Connection conexion = conexiones.get(zona);
            conexion.setAutoCommit(false);
            if (prepararSentenciaUpdate(sentencia, conexion)) {
                fragmentStatus.get(fragmento).set(true);
            }
        } catch (SQLException e) {
            throw new SQLException(e.getLocalizedMessage());
        }
    }

    private void commitFragmento(String fragmento) {
        try {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null) {
                Connection conexion = conexiones.get(zona);
                if (conexion != null) {
                    conexion.commit();
                    conexion.close();
                }
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al hacer commit en el fragmento " + fragmento + ": " + e.getMessage(),
                    "Error de commit", ErrorHandler.ERROR_MESSAGE);
        }
    }

    private void rollbackFragmento(String fragmento) {
        try {
            Zona zona = obtenerZonaPorNombre(fragmento);
            if (zona != null) {
                Connection conexion = conexiones.get(zona);
                if (conexion != null) {
                    conexion.rollback();
                    conexion.setAutoCommit(true);
                    conexion.close();
                }
            }
        } catch (SQLException e) {
            ErrorHandler.showMessage("Error al hacer rollback en el fragmento " + fragmento + ": " + e.getMessage(),
                    "Error de rollback", ErrorHandler.ERROR_MESSAGE);
        }
    }

    public Zona obtenerZonaPorNombre(String nombre) {
        for (Zona zona : Zona.values()) {
            if (zona.name().equalsIgnoreCase(nombre)) {
                return zona;
            }
        }
        return null;
    }

    private boolean prepararSentenciaUpdate(String sentencia, Connection conexion) throws SQLException {
        Statement statement = conexion.createStatement();
        statement.executeUpdate(sentencia);
        return true;
    }
}