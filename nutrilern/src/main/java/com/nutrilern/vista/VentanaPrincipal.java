package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor; // Panel que agrupa a los demás
    private com.nutrilern.modelo.Usuario usuarioLogueado; // Sesión del usuario actual
    private PanelAjustes panelAjustes;

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
        PanelLogin panelLogin = new PanelLogin(this);
        PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(this);
        panelAjustes = new PanelAjustes(this);

        // Añadimos los paneles al CardLayout
        panelContenedor.add(panelLogin, "LOGIN");
        panelContenedor.add(panelMenu, "MENU");
        panelContenedor.add(panelAjustes, "AJUSTES");

        // Añado el panel al JFrame
        add(panelContenedor);

        cardLayout.show(panelContenedor, "LOGIN");
    }

    public void setUsuarioLogueado(com.nutrilern.modelo.Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public com.nutrilern.modelo.Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void cambiarPantalla(String nombrePantalla) {
        // Si entramos en ajustes, obligamos a refrescar las etiquetas con los datos reales
        if (nombrePantalla.equals("AJUSTES")) {
            panelAjustes.refrescarDatos();
        }
        cardLayout.show(panelContenedor, nombrePantalla);
    }
}
