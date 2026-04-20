package com.nutrilern.vista;

import javax.swing.*;

import com.nutrilern.controlador.ServicioCorreo;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;

public class PanelLogin extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private String codigoSecretoGenerado; // Guarda el código temporalmente
    private String emailTemporal; // Guarda el correo a registrar
    private String passTemporal; // Guarda la contraseña a registrar

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
    // Método para el JDialog con CardLayout (Dos pantallas en una)
    private void abrirDialogoRegistro() {
        JDialog dialogo = new JDialog(ventanaPadre, "Registro de usuario", true);
        dialogo.setSize(400, 450);
        dialogo.setLocationRelativeTo(ventanaPadre);

        // --- EL MOTOR DEL DIÁLOGO (CardLayout) ---
        CardLayout cardLayout = new CardLayout();
        JPanel panelContenedorCartas = new JPanel(cardLayout);

        // CARTA 1 FORMULARIO DE REGISTRO
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

        panelFormulario.add(lblTitulo);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 30)));
        panelFormulario.add(lblNewEmail);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelFormulario.add(txtNewEmail);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 20)));
        panelFormulario.add(lblNewPass);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 5)));
        panelFormulario.add(txtNewPass);
        panelFormulario.add(Box.createRigidArea(new Dimension(0, 30)));
        panelFormulario.add(btnCrear);

        // CARTA 2: VERIFICACIÓN DEL CÓDIGO
        JPanel panelVerificacion = new JPanel();
        panelVerificacion.setLayout(new BoxLayout(panelVerificacion, BoxLayout.Y_AXIS));
        panelVerificacion.setBackground(Color.WHITE);
        panelVerificacion.setBorder(BorderFactory.createEmptyBorder(40, 40, 20, 40));

        JLabel lblTituloVerif = new JLabel("Verifica tu correo");
        lblTituloVerif.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloVerif.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtituloVerif = new JLabel("Hemos enviado un código de 8 dígitos.");
        lblSubtituloVerif.setFont(new Font("Arial", Font.PLAIN, 12));
        lblSubtituloVerif.setForeground(Color.GRAY);
        lblSubtituloVerif.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField txtCodigo = new JTextField(8);
        txtCodigo.setMaximumSize(new Dimension(200, 40));
        txtCodigo.setFont(new Font("Arial", Font.BOLD, 24));
        txtCodigo.setHorizontalAlignment(JTextField.CENTER); // Texto centrado
        txtCodigo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVerificar = new JButton("Completar Registro");
        btnVerificar.setBackground(new Color(34, 139, 34));
        btnVerificar.setForeground(Color.WHITE);
        btnVerificar.setFocusPainted(false);
        btnVerificar.setFont(new Font("Arial", Font.BOLD, 14));
        btnVerificar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerificar.setMaximumSize(new Dimension(200, 40));

        panelVerificacion.add(lblTituloVerif);
        panelVerificacion.add(Box.createRigidArea(new Dimension(0, 5)));
        panelVerificacion.add(lblSubtituloVerif);
        panelVerificacion.add(Box.createRigidArea(new Dimension(0, 30)));
        panelVerificacion.add(txtCodigo);
        panelVerificacion.add(Box.createRigidArea(new Dimension(0, 30)));
        panelVerificacion.add(btnVerificar);

        // Añadimos las dos "cartas"
        panelContenedorCartas.add(panelFormulario, "PANTALLA_FORMULARIO");
        panelContenedorCartas.add(panelVerificacion, "PANTALLA_VERIFICACION");

        // ACCIÓN 1 - Al pulsar "Enviar Código"
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
            codigoSecretoGenerado = generarCodigoSeguridad(); // Generamos el código

            // Hilo en segundo plano para no congelar la pantalla
            new Thread(() -> {
                // USAMOS SERVICIO DE CORREO REAL
                boolean enviado = com.nutrilern.controlador.ServicioCorreo.enviarCodigoVerificacion(emailTemporal,
                        codigoSecretoGenerado);

                SwingUtilities.invokeLater(() -> {
                    if (enviado) {
                        // ¡MAGIA! Volteamos la carta a la pantalla de verificación
                        cardLayout.show(panelContenedorCartas, "PANTALLA_VERIFICACION");
                        // Chivato en consola por si te da pereza abrir el correo probando:
                        System.out.println("CHIVATO: El código secreto es " + codigoSecretoGenerado);
                    } else {
                        JOptionPane.showMessageDialog(dialogo, "Error al enviar el correo.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        btnCrear.setText("Enviar Código");
                        btnCrear.setEnabled(true);
                    }
                });
            }).start();
        });

        // ACCIÓN 2 - Al pulsar "Completar Registro"
        btnVerificar.addActionListener(e -> {
            String codigoIntroducido = txtCodigo.getText().trim();

            if (codigoIntroducido.equals(codigoSecretoGenerado)) {
                // ¡ÉXITO TOTAL!
                JOptionPane.showMessageDialog(dialogo, "¡Verificación correcta! Cuenta creada con éxito.", "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE);

                // TODO: AQUÍ METERÁS EL CÓDIGO PARA GUARDAR EL USUARIO EN LA BASE DE DATOS
                // (TiDB)
                System.out.println("Guardando en BD -> Email: " + emailTemporal + " | Pass: " + passTemporal);

                // Cerramos el diálogo porque ya hemos terminado
                dialogo.dispose();
            } else {
                // FALLO
                JOptionPane.showMessageDialog(dialogo, "El código no es correcto. Inténtalo de nuevo.",
                        "Código erróneo", JOptionPane.ERROR_MESSAGE);
                txtCodigo.setText(""); // Limpiamos el campo
            }
        });

        dialogo.add(panelContenedorCartas);
        dialogo.setVisible(true);
    }

    // Método para generar el código aleatorio de 8 dígitos
    private String generarCodigoSeguridad() {
        int numero = (int) (Math.random() * 90000000) + 10000000;
        return String.valueOf(numero);
    }
}