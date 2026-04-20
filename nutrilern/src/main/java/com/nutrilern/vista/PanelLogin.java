package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

public class PanelLogin extends JPanel {
    private VentanaPrincipal ventanaPadre;

    public PanelLogin(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;

        // GridBagLayout para dividir la pantalla
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Que los paneles se estiren
        gbc.weighty = 1.0; // Que ocupen el 100% del alto de la ventana

        // PANEL IZQUIERDO - Visual

        // Creamos el JPanel y reescribimos su método de pintado directamente
        JPanel panelImagen = new JPanel() {
            private Image imagen;

            // Bloque de inicialización para cargar la imagen
            {
                URL url = getClass().getResource("/images/fondoLogin.jpg");
                if (url != null) {
                    imagen = new ImageIcon(url).getImage();
                } else {
                    System.err.println("No se encontró la imagen");
                    // Establecer el color de fondo por defecto
                    setBackground(new Color(34, 139, 34));
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    // Dibuja la imagen ocupando todo el ancho y alto del panel
                    g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                } 
                // No llamamos a setBackground aquí porque provoca un bucle infinito
            }
        };

        panelImagen.setLayout(new GridBagLayout()); // Para centrar el slogan

        JLabel lblSlogan = new JLabel("NUTRIX: Nutrición basada en datos. No en mitos");
        lblSlogan.setFont(new Font("Arial", Font.BOLD, 28));
        lblSlogan.setForeground(new Color(34, 139, 34));
        panelImagen.add(lblSlogan);

        gbc.gridx = 0;
        gbc.weightx = 0.66; // Le damos el 66% del ancho para que ocupe dos tercios de la pantalla
        add(panelImagen, gbc);

        // PANEL DERECHO - Formulario

        JPanel panelFormularioContenedor = new JPanel(new GridBagLayout());
        panelFormularioContenedor.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Campos
        JLabel lblEmail = new JLabel("Correo electrónico");
        JTextField txtEmail = new JTextField(20);

        JLabel lblPass = new JLabel("Contraseña");
        JPasswordField txtPass = new JPasswordField(20);

        // Botón
        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(34, 139, 34)); // Verde
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        // Texto que no baile
        Font fuenteNormal = new Font("Arial", Font.PLAIN, 12);

        Map<TextAttribute, Object> atributos = new HashMap<>(fuenteNormal.getAttributes());
        atributos.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font fuenteSubrayada = fuenteNormal.deriveFont(atributos);

        JLabel lblRegistrar = new JLabel("¿No tienes cuenta? ¡Regístrate!");
        lblRegistrar.setForeground(Color.BLACK);
        lblRegistrar.setFont(fuenteNormal);
        lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Efecto hover
        lblRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegistrar.setForeground(new Color(34, 139, 34));
                lblRegistrar.setFont(fuenteSubrayada);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                lblRegistrar.setForeground(Color.BLACK);
                lblRegistrar.setFont(fuenteNormal);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                abrirDialogoRegistro();
            }
        });

        // Centramos todo dentro del formPanel
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtEmail.setMaximumSize(new Dimension(300, 30));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPass.setMaximumSize(new Dimension(300, 30));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(300, 35));

        // Añadimos acción al botón
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Por ahora entra directamente sin validar
                ventanaPadre.cambiarPantalla("MENU");
            }
        });

        // Añadimos con espaciado
        formPanel.add(lblEmail);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtEmail);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(btnLogin);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(lblRegistrar);

        // Metemos el formPanel en su contenedor derecho
        panelFormularioContenedor.add(formPanel);

        gbc.gridx = 1;
        gbc.weightx = 0.34; // Le damos el 34% del ancho, 1 tercio de pantalla
        add(panelFormularioContenedor, gbc);
    }

    // Método para el JDialog
    private void abrirDialogoRegistro() {
        JDialog dialogo = new JDialog(ventanaPadre, "Registro de usuario", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(ventanaPadre);

        // Contenedor principal del diálogo con fondo blanco
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        // Título principal
        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Campo Email
        JLabel lblNewEmail = new JLabel("Nuevo Correo electrónico");
        lblNewEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtNewEmail = new JTextField(20);
        txtNewEmail.setMaximumSize(new Dimension(300, 35));

        // Campo Password
        JLabel lblNewPass = new JLabel("Nueva Contraseña");
        lblNewPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField txtNewPass = new JPasswordField(20);
        txtNewPass.setMaximumSize(new Dimension(300, 35));

        // Botón de Registro
        JButton btnCrear = new JButton("Crear Cuenta");
        btnCrear.setBackground(new Color(34, 139, 34)); // Verde sólido
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.setMaximumSize(new Dimension(200, 40));

        // Añadimos todo con separaciones RigidArea
        container.add(lblTitulo);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(lblNewEmail);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(txtNewEmail);
        container.add(Box.createRigidArea(new Dimension(0, 20)));
        container.add(lblNewPass);
        container.add(Box.createRigidArea(new Dimension(0, 5)));
        container.add(txtNewPass);
        container.add(Box.createRigidArea(new Dimension(0, 30)));
        container.add(btnCrear);

        dialogo.add(container);
        dialogo.setVisible(true);
    }
}