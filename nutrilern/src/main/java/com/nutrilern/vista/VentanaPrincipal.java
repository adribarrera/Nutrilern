package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor; // Panel que agrupa a los demás

    public VentanaPrincipal() {
        // Config básica
        setTitle("NUTRIX");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // No redimensionable
        setLocationRelativeTo(null); // Centro en pantalla

        // Inicializo el CardLayout y el panel que lo va a usar
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        // Creamos los paneles
        // Le pasamos la ventana para que los paneles cambiar de vista
        PanelLogin panelLogin = new PanelLogin(this);
        PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(this);

        // Añadimos los paneles al CardLayout
        panelContenedor.add(panelLogin, "LOGIN");
        panelContenedor.add(panelMenu, "MENU");

        // Añado el panel al JFrame
        add(panelContenedor);

        cardLayout.show(panelContenedor, "LOGIN");
    }

    public void cambiarPantalla(String nombrePantalla) {
        cardLayout.show(panelContenedor, nombrePantalla);
    }
}
