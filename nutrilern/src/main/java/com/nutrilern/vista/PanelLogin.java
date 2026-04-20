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
    private String codigoSecretoGenerado;
    private String emailTemporal;
    private String passTemporal;

    public PanelLogin(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // PANEL IZQUIERDO - Visual
        JPanel panelImagen = new JPanel() {
            private Image imagen;
            {
                URL url = getClass().getResource("/images/fondoLogin.jpg");
                if (url != null) {
                    imagen = new ImageIcon(url).getImage();
                } else {
                    setBackground(new Color(34, 139, 34));
                }
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (imagen != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };

        panelImagen.setLayout(new GridBagLayout());
        JLabel lblSlogan = new JLabel("NUTRIX: Nutrición basada en datos. No en mitos");
        lblSlogan.setFont(new Font("Arial", Font.BOLD, 28));
        lblSlogan.setForeground(new Color(34, 139, 34));
        panelImagen.add(lblSlogan);

        gbc.gridx = 0;
        gbc.weightx = 0.66;
        add(panelImagen, gbc);

        // PANEL DERECHO - Formulario Login
        JPanel panelFormularioContenedor = new JPanel(new GridBagLayout());
        panelFormularioContenedor.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        JLabel lblEmail = new JLabel("Correo electrónico");
        JTextField txtEmail = new JTextField(20);

        JLabel lblPass = new JLabel("Contraseña");
        JPasswordField txtPass = new JPasswordField(20);

        JButton btnLogin = new JButton("Entrar");
        btnLogin.setBackground(new Color(34, 139, 34));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));

        Font fuenteNormal = new Font("Arial", Font.PLAIN, 12);
        Map<TextAttribute, Object> atributos = new HashMap<>(fuenteNormal.getAttributes());
        atributos.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font fuenteSubrayada = fuenteNormal.deriveFont(atributos);

        JLabel lblRegistrar = new JLabel("¿No tienes cuenta? ¡Regístrate!");
        lblRegistrar.setForeground(Color.BLACK);
        lblRegistrar.setFont(fuenteNormal);
        lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtEmail.setMaximumSize(new Dimension(300, 30));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPass.setMaximumSize(new Dimension(300, 30));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(300, 35));

        btnLogin.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));

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

        panelFormularioContenedor.add(formPanel);

        gbc.gridx = 1;
        gbc.weightx = 0.34;
        add(panelFormularioContenedor, gbc);
    }

    // DIÁLOGO DE REGISTRO
    private void abrirDialogoRegistro() {
        JDialog dialogo = new JDialog(ventanaPadre, "Registro de usuario", true);
        dialogo.setSize(450, 550);
        dialogo.setLocationRelativeTo(ventanaPadre);

        CardLayout cardLayout = new CardLayout();
        JPanel panelContenedorCartas = new JPanel(cardLayout);

        // --- CARTA 1: CREAR CUENTA ---
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new BoxLayout(panelFormulario, BoxLayout.Y_AXIS));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTitulo = new JLabel("Crear Nueva Cuenta");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNewEmail = new JLabel("Nuevo Correo electrónico");
        lblNewEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtNewEmail = new JTextField(20);
        txtNewEmail.setMaximumSize(new Dimension(300, 35));

        JLabel lblNewPass = new JLabel("Nueva Contraseña");
        lblNewPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPasswordField txtNewPass = new JPasswordField(20);
        txtNewPass.setMaximumSize(new Dimension(300, 35));

        JButton btnCrear = new JButton("Enviar Código");
        btnCrear.setBackground(new Color(34, 139, 34));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(new Font("Arial", Font.BOLD, 14));
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.setMaximumSize(new Dimension(200, 40));

        panelFormulario.add(Box.createVerticalGlue());
        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 30)));
        panelFormulario.add(lblNewEmail);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelFormulario.add(txtNewEmail);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFormulario.add(lblNewPass);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelFormulario.add(txtNewPass);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 40)));
        panelFormulario.add(btnCrear);
        panelFormulario.add(Box.createVerticalGlue());

        // --- CARTA 2: VERIFICACIÓN ---
        JPanel panelVerificacion = new JPanel();
        panelVerificacion.setLayout(new BoxLayout(panelVerificacion, BoxLayout.Y_AXIS));
        panelVerificacion.setBackground(Color.WHITE);
        panelVerificacion.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

        JLabel lblTituloVerif = new JLabel("Verifica tu correo");
        lblTituloVerif.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloVerif.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtCodigo = new JTextField(8);
        txtCodigo.setMaximumSize(new Dimension(200, 40));
        txtCodigo.setFont(new Font("Arial", Font.BOLD, 24));
        txtCodigo.setHorizontalAlignment(JTextField.CENTER);
        txtCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVerificar = new JButton("Verificar Código");
        btnVerificar.setBackground(new Color(34, 139, 34));
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFocusPainted(false);
        btnVerificar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerificar.setMaximumSize(new Dimension(200, 40));

        panelVerificacion.add(Box.createVerticalGlue());
        panelVerificacion.add(lblTituloVerif);
        panelVerificacion.add(Box.createRigidArea(new Dimension(0, 40)));
        panelVerificacion.add(txtCodigo);
        panelVerificacion.add(Box.createRigidArea(new Dimension(0, 40)));
        panelVerificacion.add(btnVerificar);
        panelVerificacion.add(Box.createVerticalGlue());

        // --- CARTA 3: PERFIL ---
        JPanel panelPerfil = new JPanel();
        panelPerfil.setLayout(new BoxLayout(panelPerfil, BoxLayout.Y_AXIS));
        panelPerfil.setBackground(Color.WHITE);
        panelPerfil.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel lblTituloPerfil = new JLabel("Cuéntanos sobre ti");
        lblTituloPerfil.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblObjetivo = new JLabel("¿Cuál es tu objetivo?");
        lblObjetivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        String[] opciones = { "Perder Peso", "Mantener Peso", "Ganar Músculo" };
        JComboBox<String> comboObjetivo = new JComboBox<>(opciones);
        comboObjetivo.setMaximumSize(new Dimension(300, 35));

        JLabel lblEdad = new JLabel("Edad");
        lblEdad.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtEdad = new JTextField();
        txtEdad.setMaximumSize(new Dimension(300, 35));

        JLabel lblPeso = new JLabel("Peso actual (kg)");
        lblPeso.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtPeso = new JTextField();
        txtPeso.setMaximumSize(new Dimension(300, 35));

        JLabel lblAltura = new JLabel("Altura (cm)");
        lblAltura.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField txtAltura = new JTextField();
        txtAltura.setMaximumSize(new Dimension(300, 35));

        JButton btnFinalizar = new JButton("Comenzar mi cambio");
        btnFinalizar.setBackground(new Color(34, 139, 34));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFocusPainted(false);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnFinalizar.setMaximumSize(new Dimension(200, 40));

        panelPerfil.add(lblTituloPerfil);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPerfil.add(lblObjetivo);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPerfil.add(comboObjetivo);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPerfil.add(lblEdad);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPerfil.add(txtEdad);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPerfil.add(lblPeso);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPerfil.add(txtPeso);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 15)));
        panelPerfil.add(lblAltura);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPerfil.add(txtAltura);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 30)));
        panelPerfil.add(btnFinalizar);

        // --- CARTA 4: CARGANDO ---
        JPanel panelCarga = new JPanel(new BorderLayout());
        panelCarga.setBackground(Color.WHITE);
        PanelCargando spinner = new PanelCargando();

        JLabel lblCargando = new JLabel("Configurando tu plan nutricional...", SwingConstants.CENTER);
        lblCargando.setFont(new Font("Arial", Font.BOLD, 14));
        lblCargando.setForeground(new Color(34, 139, 34));
        lblCargando.setBorder(BorderFactory.createEmptyBorder(0, 0, 80, 0));

        panelCarga.add(spinner, BorderLayout.CENTER);
        panelCarga.add(lblCargando, BorderLayout.SOUTH);

        // METEMOS LAS 4 CARDS
        panelContenedorCartas.add(panelFormulario, "PANTALLA_FORMULARIO");
        panelContenedorCartas.add(panelVerificacion, "PANTALLA_VERIFICACION");
        panelContenedorCartas.add(panelPerfil, "PANTALLA_PERFIL");
        panelContenedorCartas.add(panelCarga, "PANTALLA_CARGA");

        // LÓGICA DE BOTONES (Transiciones)
        btnCrear.addActionListener(e -> {
            emailTemporal = txtNewEmail.getText().trim();
            passTemporal = new String(txtNewPass.getPassword());

            if (emailTemporal.isEmpty() || passTemporal.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Rellena todos los campos.", "Aviso",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            btnCrear.setText("Enviando...");
            btnCrear.setEnabled(false);
            codigoSecretoGenerado = generarCodigoSeguridad();

            new Thread(() -> {
                boolean enviado = com.nutrilern.controlador.ServicioCorreo.enviarCodigoVerificacion(emailTemporal,
                        codigoSecretoGenerado);
                SwingUtilities.invokeLater(() -> {
                    if (enviado) {
                        cardLayout.show(panelContenedorCartas, "PANTALLA_VERIFICACION");
                        System.out.println("CHIVATO: " + codigoSecretoGenerado);
                    } else {
                        JOptionPane.showMessageDialog(dialogo, "Error al enviar el correo.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        btnCrear.setText("Enviar Código");
                        btnCrear.setEnabled(true);
                    }
                });
            }).start();
        });

        btnVerificar.addActionListener(e -> {
            String codigoIntroducido = txtCodigo.getText().trim();
            if (codigoIntroducido.equals(codigoSecretoGenerado)) {
                // ACERTÓ -> Pasamos a la carta del perfil (NO HACEMOS DISPOSE AÚN)
                cardLayout.show(panelContenedorCartas, "PANTALLA_PERFIL");
            } else {
                JOptionPane.showMessageDialog(dialogo, "El código no es correcto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnFinalizar.addActionListener(e -> {
            try {
                // Comprobamos que metan números
                int edad = Integer.parseInt(txtEdad.getText().trim());
                double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
                double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));
                String objetivo = (String) comboObjetivo.getSelectedItem();

                // Cambiamos a la animación
                cardLayout.show(panelContenedorCartas, "PANTALLA_CARGA");
                spinner.iniciar();

                // Simulamos el guardado en BBDD
                new Thread(() -> {
                    try {
                        Thread.sleep(3000); // 3 segundos de carga falsa

                        SwingUtilities.invokeLater(() -> {
                            spinner.detener();
                            dialogo.dispose(); // Ahora sí cerramos el diálogo
                            ventanaPadre.cambiarPantalla("MENU"); // Entramos
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Usa números válidos en Edad, Peso y Altura.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        dialogo.add(panelContenedorCartas);
        dialogo.setVisible(true);
    }

    private String generarCodigoSeguridad() {
        int numero = (int) (Math.random() * 90000000) + 10000000;
        return String.valueOf(numero);
    }

    // SPINNER DE CARGA
    class PanelCargando extends JPanel {
        private int angulo = 0;
        private Timer timer;

        public PanelCargando() {
            setBackground(Color.WHITE);
            timer = new Timer(15, e -> {
                angulo = (angulo + 8) % 360;
                repaint();
            });
        }

        public void iniciar() {
            timer.start();
        }

        public void detener() {
            timer.stop();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int ancho = getWidth();
            int alto = getHeight();
            int diametro = 60;

            g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(235, 235, 235));
            g2d.drawOval((ancho - diametro) / 2, (alto - diametro) / 2, diametro, diametro);

            g2d.setColor(new Color(34, 139, 34));
            g2d.drawArc((ancho - diametro) / 2, (alto - diametro) / 2, diametro, diametro, -angulo, 120);
        }
    }
}