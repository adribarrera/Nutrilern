package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelAjustes extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private final Color colorPrincipal = new Color(34, 139, 34); // Verde NUTRIX
    private final Color colorFondo = new Color(245, 247, 250);
    private final Color colorTexto = new Color(50, 50, 50);

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(colorFondo);

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(0, 30, 0, 30)));

        JButton btnVolver = new JButton("← Volver al Menú");
        estilizarBotonSecundario(btnVolver);
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTituloHeader = new JLabel("Configuración de Perfil", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloHeader.setForeground(colorTexto);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        // Espaciador para centrar el título
        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- CONTENIDO ---
        JPanel contentGrid = new JPanel(new GridBagLayout());
        contentGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Panel de Información Personal
        JPanel infoPanel = crearSeccion("Información del Usuario");
        agregarDato(infoPanel, "Usuario:", "Dario Rumí");
        agregarDato(infoPanel, "Fecha de Nacimiento:", "15/04/1998");
        agregarDato(infoPanel, "Edad:", "26 años");
        agregarDato(infoPanel, "Miembro desde:", "20/04/2024");

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        contentGrid.add(infoPanel, gbc);

        // Panel de Seguridad
        JPanel seguridadPanel = crearSeccion("Seguridad y Cuenta");
        
        JButton btnCambiarEmail = new JButton("Cambiar Correo Electrónico");
        estilizarBotonAccion(btnCambiarEmail);
        seguridadPanel.add(btnCambiarEmail);
        seguridadPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnCambiarPass = new JButton("Cambiar Contraseña");
        estilizarBotonAccion(btnCambiarPass);
        seguridadPanel.add(btnCambiarPass);
        
        gbc.gridy = 1;
        contentGrid.add(seguridadPanel, gbc);

        // Panel de Salida
        JPanel salidaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salidaPanel.setOpaque(false);
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69)); // Rojo
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(200, 45));
        btnLogout.setBorder(new LineBorder(new Color(180, 40, 55), 1, true));
        btnLogout.addActionListener(e -> ventanaPadre.cambiarPantalla("LOGIN"));
        salidaPanel.add(btnLogout);

        gbc.gridy = 2;
        contentGrid.add(salidaPanel, gbc);

        add(new JScrollPane(contentGrid), BorderLayout.CENTER);
    }

    private JPanel crearSeccion(String titulo) {
        JPanel seccion = new JPanel();
        seccion.setLayout(new BoxLayout(seccion, BoxLayout.Y_AXIS));
        seccion.setBackground(Color.WHITE);
        seccion.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(20, 25, 20, 25)));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setForeground(colorPrincipal);
        lblTit.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        seccion.add(lblTit);
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));
        seccion.add(new JSeparator());
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));

        return seccion;
    }

    private void agregarDato(JPanel panel, String etiqueta, String valor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(1000, 30));

        JLabel lblEtiqueta = new JLabel(etiqueta);
        lblEtiqueta.setFont(new Font("Arial", Font.BOLD, 14));
        lblEtiqueta.setForeground(new Color(100, 100, 100));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValor.setForeground(colorTexto);

        fila.add(lblEtiqueta, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        panel.add(fila);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void estilizarBotonAccion(JButton btn) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(colorTexto);
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

    private void estilizarBotonSecundario(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(colorPrincipal);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(colorPrincipal, 1, true),
                new EmptyBorder(8, 15, 8, 15)));
    }
}
