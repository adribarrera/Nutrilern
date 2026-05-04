package com.nutrilern.vista;

import javax.swing.*;
import com.nutrilern.modelo.Usuario;
import java.awt.*;

/**
 * Ventana principal que actúa como contenedor de todas las pantallas de la aplicación.
 * Utiliza un CardLayout para gestionar la navegación.
 */
public class VentanaPrincipal extends JFrame {

    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private com.nutrilern.modelo.Usuario usuarioLogueado;

    // Paneles que necesitan refrescarse desde otros sitios
    private PanelAjustes panelAjustes;
    private PanelEvolucion panelEvo;
    private PanelMisComidas panelComidas;
    private PanelAdminUsuarios panelAdmin;

    /**
     * Constructor de la ventana principal. Configura el layout y añade las pantallas.
     */
    public VentanaPrincipal() {
        setTitle("NUTRIX");
        setSize(1080, 720);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        // Icono de la aplicación
        try {
            java.net.URL urlIcono = getClass().getResource("/images/icono.png");
            if (urlIcono != null) {
                setIconImage(new ImageIcon(urlIcono).getImage());
            }
        } catch (Exception e) {
            System.err.println("No se pudo cargar el icono de la ventana: " + e.getMessage());
        }

        // Mensaje de despedida al cerrar el programa
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                JOptionPane.showMessageDialog(null,
                        "¡Gracias por usar Nutrix!\nEsperamos verte pronto para seguir cumpliendo tus objetivos.",
                        "Finalizando Nutrix",
                        JOptionPane.PLAIN_MESSAGE,
                        TemaNutrix.obtenerIconoDialogo());
                System.exit(0);
            }
        });

        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        PanelLogin panelLogin = new PanelLogin(this);
        this.panelAjustes = new PanelAjustes(this);
        this.panelComidas = new PanelMisComidas(this);
        this.panelEvo = new PanelEvolucion(this);
        this.panelAdmin = new PanelAdminUsuarios(this);
        PanelBaseAlimentos panelBase = new PanelBaseAlimentos(this);

        panelContenedor.add(panelLogin, "LOGIN");
        panelContenedor.add(this.panelAjustes, "AJUSTES");
        panelContenedor.add(this.panelComidas, "COMIDAS"); // Añadimos el global
        panelContenedor.add(this.panelEvo, "EVOLUCION");
        panelContenedor.add(panelBase, "BASE_ALIMENTOS");
        panelContenedor.add(this.panelAdmin, "ADMIN_USUARIOS");

        add(panelContenedor);

        cardLayout.show(panelContenedor, "LOGIN");
    }

    /**
     * Establece el usuario que ha iniciado sesión globalmente.
     * @param usuario Datos del usuario.
     */
    public void setUsuarioLogueado(com.nutrilern.modelo.Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    /**
     * Obtiene el usuario actualmente logueado.
     * @return El usuario en sesión.
     */
    public com.nutrilern.modelo.Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    /**
     * Gestiona la navegación entre pantallas.
     * @param nombrePantalla Identificador de la pantalla de destino.
     */
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
        } else if (nombrePantalla.equals("ADMIN_USUARIOS")) {
            panelAdmin.refrescarTabla();
        }

        cardLayout.show(panelContenedor, nombrePantalla);
    }
}
