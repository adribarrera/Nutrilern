package com.nutrilern.vista;

import javax.swing.*;
import com.nutrilern.modelo.Usuario;
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

        // Creamos los paneles (EXCEPTO EL MENÚ)
        PanelLogin panelLogin = new PanelLogin(this);
        panelAjustes = new PanelAjustes(this);
        PanelMisComidas panelComidas = new PanelMisComidas(this);

        // Añadimos los paneles al CardLayout
        panelContenedor.add(panelLogin, "LOGIN");
        panelContenedor.add(panelAjustes, "AJUSTES");
        panelContenedor.add(panelComidas, "COMIDAS");
        // ATENCIÓN: He borrado de aquí la línea del menú.

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
        
        if (nombrePantalla.equals("MENU")) {
            // Rescatamos el usuario loggeado
            Usuario usuarioActual = this.getUsuarioLogueado(); 
            
            // Creamos el panel del menú AHORA, pasándole los datos reales
            PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(this, usuarioActual);
            
            // Lo añadimos a la baraja del CardLayout en este preciso momento
            panelContenedor.add(panelMenu, "MENU");
            
        } else if (nombrePantalla.equals("AJUSTES")) {
            // Si entramos en ajustes, obligamos a refrescar las etiquetas con los datos reales
            panelAjustes.refrescarDatos();
        }
        
        // El CardLayout se encarga de mostrar la pantalla (sea el menú, o cualquier otra)
        cardLayout.show(panelContenedor, nombrePantalla);
    }
}