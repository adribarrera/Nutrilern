package com.nutrilern.vista;

import javax.swing.*;
import com.nutrilern.modelo.Usuario;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private com.nutrilern.modelo.Usuario usuarioLogueado; 
    
    // Paneles que necesitan refrescarse desde otros sitios
    private PanelAjustes panelAjustes;
    private PanelEvolucion panelEvo; 
    private PanelMisComidas panelComidas; // NUEVO

    public VentanaPrincipal() {
        setTitle("NUTRIX");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); 
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        PanelLogin panelLogin = new PanelLogin(this);
        this.panelAjustes = new PanelAjustes(this);
        this.panelComidas = new PanelMisComidas(this); // Inicializamos el global
        this.panelEvo = new PanelEvolucion(this);
        PanelBaseAlimentos panelBase = new PanelBaseAlimentos(this);

        panelContenedor.add(panelLogin, "LOGIN");
        panelContenedor.add(this.panelAjustes, "AJUSTES");
        panelContenedor.add(this.panelComidas, "COMIDAS"); // Añadimos el global
        panelContenedor.add(this.panelEvo, "EVOLUCION");
        panelContenedor.add(panelBase, "BASE_ALIMENTOS");

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
            Usuario usuarioActual = this.getUsuarioLogueado(); 
            PanelMenuPrincipal panelMenu = new PanelMenuPrincipal(this, usuarioActual);
            panelContenedor.add(panelMenu, "MENU");
            
        } else if (nombrePantalla.equals("AJUSTES")) {
            panelAjustes.refrescarDatos();
        } else if (nombrePantalla.equals("EVOLUCION")) {
            panelEvo.cargarDatosReales();
        } else if (nombrePantalla.equals("COMIDAS")) {
            // Cuando entramos en la tabla, cargamos los datos de hoy
            panelComidas.cargarDatosHoy();
        }
        
        cardLayout.show(panelContenedor, nombrePantalla);
    }
}