package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

import com.nutrilern.modelo.Usuario;

/**
 * Pantalla de inicio de sesión y registro.
 */
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

        // Panel lateral con imagen de fondo
        JPanel panelImagen = new JPanel() {
            private Image imagen;
            {
                try {
                    URL url = getClass().getResource("/images/fondoLogin.jpg");
                    if (url != null) {
                        imagen = javax.imageio.ImageIO.read(url);
                    } else {
                        setBackground(TemaNutrix.PRIMARIO);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    setBackground(TemaNutrix.PRIMARIO);
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

                    double imgAspect = (double) imagen.getWidth(null) / imagen.getHeight(null);
                    double panelAspect = (double) getWidth() / getHeight();

                    int drawW, drawH, x, y;
                    if (panelAspect > imgAspect) {
                        drawW = getWidth();
                        drawH = (int) (drawW / imgAspect);
                        x = 0;
                        y = (getHeight() - drawH) / 2;
                    } else {
                        drawH = getHeight();
                        drawW = (int) (drawH * imgAspect);
                        x = (getWidth() - drawW) / 2;
                        y = 0;
                    }
                    g2d.drawImage(imagen, x, y, drawW, drawH, this);
                }
            }
        };

        panelImagen.setLayout(new GridBagLayout());
        JLabel lblSlogan = new JLabel("NUTRIX: Nutrición basada en datos. No en mitos");
        lblSlogan.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 28));
        lblSlogan.setForeground(TemaNutrix.PRIMARIO);
        panelImagen.add(lblSlogan);

        gbc.gridx = 0;
        gbc.weightx = 0.66;
        add(panelImagen, gbc);

        // Formulario de login
        JPanel panelFormularioContenedor = new JPanel(new GridBagLayout());
        panelFormularioContenedor.setBackground(Color.WHITE);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        // Añadimos márgenes laterales para que no sea tan ancho
        formPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JLabel lblEmail = new JLabel("Correo electrónico");
        JTextField txtEmail = new JTextField(20);
        JLabel lblPass = new JLabel("Contraseña");
        JPasswordField txtPass = new JPasswordField(20);

        JButton btnLogin = TemaNutrix.crearBotonEstandar("Entrar");

        txtEmail.addActionListener(e -> btnLogin.doClick());
        txtPass.addActionListener(e -> btnLogin.doClick());

        Font fuenteNormal = new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 14);
        Map<TextAttribute, Object> attributes = new HashMap<>(fuenteNormal.getAttributes());
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        Font fuenteSubrayada = new Font(attributes);

        JLabel lblRegistrar = new JLabel("¿No tienes cuenta? ¡Regístrate!");
        lblRegistrar.setForeground(Color.BLACK);
        lblRegistrar.setFont(fuenteNormal);
        lblRegistrar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblRegistrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                lblRegistrar.setForeground(TemaNutrix.PRIMARIO);
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

        // Configuración de tamaños y alineación
        lblEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtEmail.setMaximumSize(new Dimension(300, 30));
        lblPass.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPass.setMaximumSize(new Dimension(300, 30));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(300, 35));

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText().trim();
            String password = new String(txtPass.getPassword());
            Usuario user = com.nutrilern.controlador.ControladorUsuario.autenticar(email, password);
            if (user != null) {
                ventanaPadre.setUsuarioLogueado(user);
                ventanaPadre.cambiarPantalla("MENU");
            } else {
                JOptionPane.showMessageDialog(this, "Email o contraseña incorrectos.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logo superior en el formulario
        try {
            java.net.URL urlLogo = getClass().getResource("/images/logo.png");
            if (urlLogo != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlLogo);
                // Tamaño un poco más refinado
                // Espacio fijo arriba para bajar la posición del logo
                formPanel.add(Box.createRigidArea(new Dimension(0, 80))); 
                ImageIcon logoEscalado = TemaNutrix.escalarImagenProporcional(iconoOriginal, 250, 120);
                JLabel lblLogo = new JLabel(logoEscalado);
                lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
                formPanel.add(lblLogo);
                formPanel.add(Box.createVerticalGlue()); // Espacio entre logo y campos
            }
        } catch (Exception e) {
        }

        formPanel.add(lblEmail);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtEmail);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(lblPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        formPanel.add(txtPass);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        formPanel.add(btnLogin);
        formPanel.add(Box.createVerticalGlue()); // Espacio entre botón y registro
        formPanel.add(lblRegistrar);
        formPanel.add(Box.createVerticalGlue()); // Empuja hacia arriba desde el fondo

        // Hacemos que el panel del formulario ocupe todo el alto para que el Glue
        // funcione
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.fill = GridBagConstraints.BOTH;
        gbcForm.weighty = 1.0;
        gbcForm.weightx = 1.0;
        panelFormularioContenedor.add(formPanel, gbcForm);
        gbc.gridx = 1;
        gbc.weightx = 0.34;
        add(panelFormularioContenedor, gbc);
    }

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
        lblTitulo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNewEmail = new JTextField(20);
        txtNewEmail.setMaximumSize(new Dimension(300, 35));
        JPasswordField txtNewPass = new JPasswordField(20);
        txtNewPass.setMaximumSize(new Dimension(300, 35));

        JButton btnCrear = TemaNutrix.crearBotonEstandar("Enviar Código");
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEmailReg = new JLabel("Email:");
        lblEmailReg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEmailReg.setMaximumSize(new Dimension(300, 20));
        lblEmailReg.setHorizontalAlignment(SwingConstants.LEFT);

        JLabel lblPassReg = new JLabel("Contraseña:");
        lblPassReg.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPassReg.setMaximumSize(new Dimension(300, 20));
        lblPassReg.setHorizontalAlignment(SwingConstants.LEFT);

        txtNewEmail.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtNewPass.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelFormulario.add(Box.createVerticalGlue());
        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 30)));
        panelFormulario.add(lblEmailReg);
        panelFormulario.add(txtNewEmail);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFormulario.add(lblPassReg);
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
        lblTituloVerif.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTituloVerif.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtCodigo = new JTextField(8);
        txtCodigo.setMaximumSize(new Dimension(200, 40));
        txtCodigo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 24));
        txtCodigo.setHorizontalAlignment(JTextField.CENTER);
        txtCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVerificar = TemaNutrix.crearBotonEstandar("Verificar Código");
        btnVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);

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

        JLabel lblTituloPerfil = new JLabel("Datos Personales");
        lblTituloPerfil.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTituloPerfil.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(300, 30));
        JTextField txtApellidos = new JTextField();
        txtApellidos.setMaximumSize(new Dimension(300, 30));

        String[] opciones = { "Perder Peso", "Mantener Peso", "Ganar Músculo" };
        JComboBox<String> comboObjetivo = new JComboBox<>(opciones);
        comboObjetivo.setMaximumSize(new Dimension(300, 30));

        String[] sexos = { "Hombre", "Mujer" };
        JComboBox<String> comboSexo = new JComboBox<>(sexos);
        comboSexo.setMaximumSize(new Dimension(300, 30));

        JTextField txtEdad = new JTextField();
        txtEdad.setMaximumSize(new Dimension(300, 30));
        JTextField txtPeso = new JTextField();
        txtPeso.setMaximumSize(new Dimension(300, 30));
        JTextField txtAltura = new JTextField();
        txtAltura.setMaximumSize(new Dimension(300, 30));

        JButton btnFinalizar = TemaNutrix.crearBotonEstandar("Comenzar");
        btnFinalizar.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtApellidos.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboObjetivo.setAlignmentX(Component.CENTER_ALIGNMENT);
        comboSexo.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtEdad.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtPeso.setAlignmentX(Component.CENTER_ALIGNMENT);
        txtAltura.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelPerfil.add(lblTituloPerfil);
        panelPerfil.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel lblNom = new JLabel("Nombre:");
        lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblNom.setMaximumSize(new Dimension(300, 20));
        lblNom.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblNom);
        panelPerfil.add(txtNombre);

        JLabel lblApe = new JLabel("Apellidos:");
        lblApe.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblApe.setMaximumSize(new Dimension(300, 20));
        lblApe.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblApe);
        panelPerfil.add(txtApellidos);

        JLabel lblObj = new JLabel("Objetivo:");
        lblObj.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblObj.setMaximumSize(new Dimension(300, 20));
        lblObj.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblObj);
        panelPerfil.add(comboObjetivo);

        JLabel lblSex = new JLabel("Sexo:");
        lblSex.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblSex.setMaximumSize(new Dimension(300, 20));
        lblSex.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblSex);
        panelPerfil.add(comboSexo);

        JLabel lblEda = new JLabel("Edad:");
        lblEda.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblEda.setMaximumSize(new Dimension(300, 20));
        lblEda.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblEda);
        panelPerfil.add(txtEdad);

        JLabel lblPes = new JLabel("Peso (kg):");
        lblPes.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblPes.setMaximumSize(new Dimension(300, 20));
        lblPes.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblPes);
        panelPerfil.add(txtPeso);

        JLabel lblAlt = new JLabel("Altura (cm):");
        lblAlt.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblAlt.setMaximumSize(new Dimension(300, 20));
        lblAlt.setHorizontalAlignment(SwingConstants.LEFT);
        panelPerfil.add(lblAlt);
        panelPerfil.add(txtAltura);

        panelPerfil.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPerfil.add(btnFinalizar);

        // --- CARTA 4: CARGANDO ---
        JPanel panelCarga = new JPanel(new BorderLayout());
        panelCarga.setBackground(Color.WHITE);
        PanelCargando spinner = new PanelCargando();
        panelCarga.add(spinner, BorderLayout.CENTER);
        panelCarga.add(new JLabel("Configurando tu plan...", SwingConstants.CENTER), BorderLayout.SOUTH);

        panelContenedorCartas.add(panelFormulario, "1");
        panelContenedorCartas.add(panelVerificacion, "2");
        panelContenedorCartas.add(panelPerfil, "3");
        panelContenedorCartas.add(panelCarga, "4");

        btnCrear.addActionListener(e -> {
            emailTemporal = txtNewEmail.getText().trim();
            passTemporal = new String(txtNewPass.getPassword());
            
            if (emailTemporal.isEmpty() || passTemporal.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor, rellena todos los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!com.nutrilern.controlador.ControladorUsuario.esEmailValido(emailTemporal)) {
                JOptionPane.showMessageDialog(dialogo, "El formato del correo no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!com.nutrilern.controlador.ControladorUsuario.esPasswordValida(passTemporal)) {
                JOptionPane.showMessageDialog(dialogo, "La contraseña debe tener al menos 8 caracteres.", "Seguridad", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Estado de carga en el botón
            btnCrear.setEnabled(false);
            btnCrear.setText("Enviando código...");

            codigoSecretoGenerado = com.nutrilern.controlador.ControladorUsuario.generarCodigoVerificacion();
            new Thread(() -> {
                boolean enviado = com.nutrilern.controlador.ControladorUsuario.enviarCodigo(emailTemporal, codigoSecretoGenerado);
                SwingUtilities.invokeLater(() -> {
                    if (enviado) {
                        cardLayout.show(panelContenedorCartas, "2");
                    } else {
                        JOptionPane.showMessageDialog(dialogo, "Error al enviar el código de verificación.", "Error", JOptionPane.ERROR_MESSAGE);
                        // Restaurar botón si hay error
                        btnCrear.setEnabled(true);
                        btnCrear.setText("Enviar Código");
                    }
                });
            }).start();
        });

        btnVerificar.addActionListener(e -> {
            if (txtCodigo.getText().equals(codigoSecretoGenerado))
                cardLayout.show(panelContenedorCartas, "3");
        });

        btnFinalizar.addActionListener(e -> {
            try {
                String nom = txtNombre.getText().trim();
                String ape = txtApellidos.getText().trim();
                int edad = Integer.parseInt(txtEdad.getText());
                double peso = Double.parseDouble(txtPeso.getText());
                double alt = Double.parseDouble(txtAltura.getText());
                int obj = comboObjetivo.getSelectedIndex() + 1;
                String sex = comboSexo.getSelectedIndex() == 0 ? "M" : "F";

                cardLayout.show(panelContenedorCartas, "4");
                spinner.iniciar();

                new Thread(() -> {
                    Usuario nU = new Usuario(emailTemporal, passTemporal, nom, ape, edad, alt, peso, "USUARIO", obj,
                            sex);
                    if (com.nutrilern.controlador.ControladorUsuario.registrarNuevoUsuario(nU)) {
                        SwingUtilities.invokeLater(() -> {
                            ventanaPadre.setUsuarioLogueado(nU);
                            dialogo.dispose();
                            ventanaPadre.cambiarPantalla("MENU");
                        });
                    }
                }).start();
            } catch (Exception ex) {
            }
        });

        dialogo.add(panelContenedorCartas);
        dialogo.setVisible(true);
    }

    // Spinner visual para estados de carga
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
            g2d.setColor(TemaNutrix.GRIS_CLARO);
            g2d.drawOval((ancho - diametro) / 2, (alto - diametro) / 2, diametro, diametro);
            g2d.setColor(TemaNutrix.PRIMARIO);
            g2d.drawArc((ancho - diametro) / 2, (alto - diametro) / 2, diametro, diametro, -angulo, 120);
        }
    }
}
