package layouts;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

public class ContentLayout implements LayoutManager {

    @Override
    public void addLayoutComponent(String name, Component comp) {

    }

    @Override
    public void removeLayoutComponent(Component comp) {

    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return null;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return null;
    }

    @Override
    public void layoutContainer(Container miContenedor) {
        int contador = 0;
        int nComponentes = miContenedor.getComponentCount();

        int x = 20; // Reiniciar x al inicio de la iteración
        int y = 0; // Reiniciar y al inicio de la iteración

        for (int i = 0; i < nComponentes; i++) {
            contador++;

            Component componente = miContenedor.getComponent(i);

            int anchoComponente = (int) (miContenedor.getWidth() * 0.33);
            int altoComponente = (int) (miContenedor.getHeight() * 0.15);

            if (contador % 2 == 0) {
                anchoComponente = (int) (miContenedor.getWidth() * 0.6);
            }

            componente.setBounds(x, y, anchoComponente, altoComponente);

            x += anchoComponente;
            if (contador % 2 == 0) {
                x = 20;
                y += altoComponente * 1.15;
            }
            componente.setFont(componente.getFont().deriveFont((float) (altoComponente * 0.5)));
        }
    }

}
