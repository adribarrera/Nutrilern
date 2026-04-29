package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelAjustes extends JPanel {

    // Referencia a la ventana de la aplicación
    private VentanaPrincipal ventanaPadre;

    // Colores
    private final Color COLOR_PRINCIPAL = new Color(34, 139, 34); // Verde NUTRIX
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;

        // Configuración básica del panel principal
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpoPrincipal(), BorderLayout.CENTER);
    }

    /**
     * Barra superior con el botón de volver y el título.
     */
    private JPanel crearEncabezado() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(0, 30, 0, 30)));

        // Botón de navegación hacia atrás
        JButton btnVolver = new JButton("← Volver al Menú");
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        // Título central
        JLabel lblTituloHeader = new JLabel("Configuración de Perfil", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloHeader.setForeground(COLOR_TEXTO);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        // Espaciador para que el título quede perfectamente centrado
        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);

        return header;
    }

    /**
     * Contenedor principal donde irán todas las secciones de ajustes.
     */
    private JComponent crearCuerpoPrincipal() {
        JPanel contentGrid = new JPanel(new GridBagLayout());
        contentGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // SECCIÓN 1: Información Personal
        gbc.gridy = 0;
        contentGrid.add(crearSeccionInformacion(), gbc);

        // SECCIÓN 2: Seguridad y Cuenta
        gbc.gridy = 1;
        contentGrid.add(crearSeccionSeguridad(), gbc);

        // SECCIÓN 3: Botón de Salida (Cerrar Sesión)
        gbc.gridy = 2;
        contentGrid.add(crearSeccionSalida(), gbc);

        return new JScrollPane(contentGrid);
    }

    /**
     * Bloque con los datos personales del usuario.
     */
    private JPanel crearSeccionInformacion() {
        JPanel infoPanel = crearContenedorSeccion("Información del Usuario");
        agregarFilaDato(infoPanel, "Usuario:", "Dario Rumí");
        agregarFilaDato(infoPanel, "Fecha de Nacimiento:", "15/04/1998");
        agregarFilaDato(infoPanel, "Edad:", "26 años");
        agregarFilaDato(infoPanel, "Miembro desde:", "20/04/2024");
        return infoPanel;
    }

    /**
     * Bloque con las acciones de seguridad (cambio de email/pass).
     */
    private JPanel crearSeccionSeguridad() {
        JPanel seguridadPanel = crearContenedorSeccion("Seguridad y Cuenta");

        JButton btnCambiarEmail = new JButton("Cambiar Correo Electrónico");
        estilizarBotonAccion(btnCambiarEmail);
        seguridadPanel.add(btnCambiarEmail);

        seguridadPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnCambiarPass = new JButton("Cambiar Contraseña");
        estilizarBotonAccion(btnCambiarPass);
        seguridadPanel.add(btnCambiarPass);

        return seguridadPanel;
    }

    /**
     * Botón de cierre de sesión al final del panel.
     */
    private JPanel crearSeccionSalida() {
        JPanel salidaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salidaPanel.setOpaque(false);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69)); // Color Rojo
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(200, 45));
        btnLogout.setBorder(new LineBorder(new Color(180, 40, 55), 1, true));

        btnLogout.addActionListener(e -> ventanaPadre.cambiarPantalla("LOGIN"));

        salidaPanel.add(btnLogout);
        return salidaPanel;
    }

    /** Crea un panel blanco con borde y título para agrupar elementos */
    private JPanel crearContenedorSeccion(String titulo) {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(Color.WHITE);
        seccion.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 25, 20, 25)));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(COLOR_PRINCIPAL);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);

        seccion.add(lblTit);
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));
        seccion.add(new JSeparator());
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));

        return seccion;
    }

    /** Añade una fila con etiqueta (izq) y valor (der) a una sección */
    private void agregarFilaDato(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(1000, 30));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        lblEtiqueta.setForeground(new Color(100, 100, 100));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValor.setForeground(COLOR_TEXTO);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panel.add(fila);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    /** Estiliza los botones de opciones dentro de las secciones */
    private void estilizarBotonAccion(JButton btn) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(new Color(248, 249, 250));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(233, 236, 239));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(248, 249, 250));
            }
        });
    }

    /** Estiliza el botón de volver */
    private void estilizarBotonSecundario(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(COLOR_PRINCIPAL);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(COLOR_PRINCIPAL, 1, true),
                new EmptyBorder(8, 15, 8, 15)));
    }
}
