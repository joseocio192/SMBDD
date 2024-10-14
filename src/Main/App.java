package main;

import javax.swing.*;

import java.awt.Font;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import java.util.logging.Logger;

import two_phase_commit.Controlador;
import two_phase_commit.Modelo;
import two_phase_commit.Vista;

public class App {
    private static final Logger logger = Logger.getLogger(App.class.getName());

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            logger.severe("Failed to initialize LaF");
        }
        FlatRobotoFont.install();
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacLightLaf.setup();

        Vista vista = new Vista();
        Modelo modelo = new Modelo();
        new Controlador(vista, modelo);
    }
}
