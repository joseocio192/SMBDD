package layouts;

import java.awt.*;

public class Contenedorlbl_txtLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // Puedes implementar lógica de adición de componentes si es necesario
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // Puedes implementar lógica de eliminación de componentes si es necesario
    }

    @Override
    public void layoutContainer(Container miContenedor) {
        int contador = 0;
        int nComponentes = miContenedor.getComponentCount();

        int x = 20; // Reiniciar x al inicio de la iteración
        int y = 5; // Reiniciar y al inicio de la iteración

        if (contador == 0) {

            int anchoComponente = (int) (miContenedor.getWidth() / 2);
            int altoComponente = (int) (miContenedor.getHeight() * 0.15);

            miContenedor.getComponent(0).setBounds((int) ((miContenedor.getWidth() - anchoComponente) / 1.5), y,
                    anchoComponente,
                    altoComponente);
            y += altoComponente * 1.3;
        }

        for (int i = 1; i < nComponentes; i++) {
            contador++;

            Component componente = miContenedor.getComponent(i);

            // if (i == nComponentes - 1) {

            // int anchoComponente = (int) (miContenedor.getWidth() / 2);
            // int altoComponente = (int) (miContenedor.getHeight() * 0.12);

            // componente.setBounds(miContenedor.getWidth() / 4, y, anchoComponente,
            // altoComponente);
            // break;
            // }
            int anchoComponente = (int) (miContenedor.getWidth() * 0.4);
            if (contador % 2 == 0) {
                anchoComponente = (int) (miContenedor.getWidth() * 0.5);
            }
            int altoComponente = (int) (miContenedor.getHeight() * 0.12);

            componente.setBounds(x, y, anchoComponente, altoComponente);

            x += anchoComponente;
            if (contador % 2 == 0) {
                x = 20;
                y += altoComponente * 1.3;
            }
        }
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        // Puedes implementar este método si necesitas establecer un tamaño preferido
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        // Puedes implementar este método si necesitas establecer un tamaño mínimo
        return null;
    }
}
