package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMenuPrincipal extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private final Color colorPrincipal = new Color(34, 139, 34); // Verde NUTRIX
    private final Color colorFondo = new Color(245, 247, 250);
    private final Color colorTexto = new Color(50, 50, 50);
    private Image imagenFondo;

    public PanelMenuPrincipal(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());

        java.net.URL url = getClass().getResource("/images/fondoMenuPrincipal.jpg");
        if (url != null) {
            imagenFondo = new ImageIcon(url).getImage();
        } else {
            setBackground(colorFondo);
        }

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(0, 30, 0, 30)));

        JLabel lblLogo = new JLabel("NUTRIX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 26));
        lblLogo.setForeground(colorPrincipal);
        header.add(lblLogo, BorderLayout.WEST);

        // Panel para el usuario y botón salir
        JPanel userActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 25));
        userActions.setOpaque(false);

        JLabel lblUser = new JLabel("Bienvenido, Usuario");
        lblUser.setFont(new Font("Arial", Font.ITALIC, 14));
        lblUser.setForeground(colorTexto);
        userActions.add(lblUser);

        JButton btnSalir = new JButton("Cerrar Sesión");
        estilizarBotonSecundario(btnSalir);
        btnSalir.addActionListener(e -> ventanaPadre.cambiarPantalla("LOGIN"));
        userActions.add(btnSalir);

        header.add(userActions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- CUERPO PRINCIPAL ---
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setOpaque(false);
        contentPanel.setBorder(new EmptyBorder(40, 40, 40, 40));

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        gridPanel.setOpaque(false);

        gridPanel.add(crearTarjetaMenu("Mis Comidas", "/images/misComidas.png", "Registra tu ingesta diaria y macros"));
        gridPanel.add(crearTarjetaMenu("Mi Evolución", "/images/miEvo.png", "Gráficas de peso y composición"));
        gridPanel.add(
                crearTarjetaMenu("Base de Alimentos", "/images/miGestor.png", "Base de datos nutricional completa"));
        gridPanel.add(crearTarjetaMenu("Ajustes", "/images/ajustes.png", "Configura tu perfil y objetivos"));

        contentPanel.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JPanel crearTarjetaMenu(String titulo, String imagePath, String descripcion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(280, 220));
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tarjeta.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Borde inicial
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(230, 230, 230), 1, true),
                new EmptyBorder(25, 20, 25, 20)));

        JLabel lblIcono = new JLabel();
        java.net.URL url = getClass().getResource(imagePath);
        if (url != null) {
            ImageIcon iconoOriginal = new ImageIcon(url);
            Image imgEscalada = iconoOriginal.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
            lblIcono.setIcon(new ImageIcon(imgEscalada));
        } else {
            lblIcono.setText("ICON");
        }
        lblIcono.setHorizontalAlignment(SwingConstants.CENTER);
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel(titulo, SwingConstants.CENTER);
        lblTit.setFont(new Font("Arial", Font.BOLD, 20));
        lblTit.setForeground(colorTexto);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 13));
        lblDesc.setForeground(new Color(120, 120, 120));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setMaximumSize(new Dimension(220, 50));

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(lblTit);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjeta.add(lblDesc);

        // Efectos Hover
        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(new Color(250, 255, 250));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(colorPrincipal, 2, true),
                        new EmptyBorder(24, 19, 24, 19)));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(new Color(230, 230, 230), 1, true),
                        new EmptyBorder(25, 20, 25, 20)));
            }
        });

        return tarjeta;
    }

    private void estilizarBotonSecundario(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setForeground(colorPrincipal);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(colorPrincipal, 1, true),
                new EmptyBorder(8, 15, 8, 15)));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(colorPrincipal);
                btn.setForeground(Color.WHITE);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(colorPrincipal);
            }
        });
    }
}
