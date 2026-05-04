package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.nutrilern.modelo.Usuario;

/**
 * Panel de configuración de cuenta organizado por pestañas.
 */
public class PanelAjustes extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JLabel lblNombreValor, lblEmailValor, lblEdadValor, lblAlturaValor, lblSexoValor, lblObjetivoValor;
    private JButton btnNavDatos, btnNavNutricion, btnNavSeguridad, btnNavPeligro;
    private JPanel panelContenedorCartas;
    private CardLayout cardLayout;

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        add(crearCabeceraYNav(), BorderLayout.NORTH);

        cardLayout = new CardLayout();
        panelContenedorCartas = new JPanel(cardLayout);
        panelContenedorCartas.setBackground(TemaNutrix.FONDO);

        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaDatos()), "DATOS");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaNutricion()), "NUTRICION");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaSeguridad()), "SEGURIDAD");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaPeligro()), "PELIGRO");

        add(panelContenedorCartas, BorderLayout.CENTER);
        cambiarPestana(btnNavDatos, "DATOS");
    }

    private JPanel crearCabeceraYNav() {
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBackground(Color.WHITE);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTituloHeader = new JLabel("Ajustes de mi Cuenta", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTituloHeader.setForeground(TemaNutrix.TEXTO);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST);

        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        btnNavDatos = crearBotonNav("Datos Físicos");
        btnNavNutricion = crearBotonNav("Mi Nutrición");
        btnNavSeguridad = crearBotonNav("Seguridad");
        btnNavPeligro = crearBotonNav("Mi cuenta");

        btnNavDatos.addActionListener(e -> cambiarPestana(btnNavDatos, "DATOS"));
        btnNavNutricion.addActionListener(e -> cambiarPestana(btnNavNutricion, "NUTRICION"));
        btnNavSeguridad.addActionListener(e -> cambiarPestana(btnNavSeguridad, "SEGURIDAD"));
        btnNavPeligro.addActionListener(e -> cambiarPestana(btnNavPeligro, "PELIGRO"));

        navBar.add(btnNavDatos);
        navBar.add(btnNavNutricion);
        navBar.add(btnNavSeguridad);
        navBar.add(btnNavPeligro);

        panelTop.add(header);
        panelTop.add(navBar);

        return panelTop;
    }

    private JButton crearBotonNav(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 15));
        btn.setForeground(TemaNutrix.GRIS_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 5, 10, 5));
        return btn;
    }

    private void cambiarPestana(JButton botonActivo, String nombreCarta) {
        JButton[] todos = { btnNavDatos, btnNavNutricion, btnNavSeguridad, btnNavPeligro };
        for (JButton btn : todos) {
            if (btn == botonActivo) {
                btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 15));
                btn.setForeground(TemaNutrix.PRIMARIO);
                btn.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 3, 0, TemaNutrix.PRIMARIO),
                        new EmptyBorder(10, 5, 7, 5)));
            } else {
                btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 15));
                btn.setForeground(TemaNutrix.GRIS_TEXTO);
                btn.setBorder(new EmptyBorder(10, 5, 10, 5));
            }
        }
        cardLayout.show(panelContenedorCartas, nombreCarta);
    }

    private JPanel crearPantallaCentrada(JPanel tarjetaBlanca) {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(TemaNutrix.FONDO);
        fondo.add(tarjetaBlanca);
        return fondo;
    }

    private JPanel crearTarjetaDatos() {
        JPanel tarjeta = crearTarjetaBase("Mis Datos Físicos");

        lblNombreValor = new JLabel("-");
        lblEmailValor = new JLabel("-");
        lblEdadValor = new JLabel("-");
        lblAlturaValor = new JLabel("-");
        lblSexoValor = new JLabel("-");

        tarjeta.add(crearFilaDato("Nombre:", lblNombreValor));
        tarjeta.add(crearFilaDato("Email:", lblEmailValor));
        tarjeta.add(crearFilaDato("Edad:", lblEdadValor));
        tarjeta.add(crearFilaDato("Altura:", lblAlturaValor));
        tarjeta.add(crearFilaDato("Sexo:", lblSexoValor));

        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnEditar = crearBotonCentrado("Actualizar Datos Físicos");
        btnEditar.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user != null) {
                JPanel panelCampos = new JPanel(new GridLayout(3, 2, 5, 5));
                JTextField txtEdad = new JTextField(String.valueOf(user.getEdad()));
                JTextField txtPeso = new JTextField(String.valueOf(user.getPesoInicial()));
                JTextField txtAltura = new JTextField(String.valueOf(user.getAltura()));

                panelCampos.add(new JLabel("Edad:"));
                panelCampos.add(txtEdad);
                panelCampos.add(new JLabel("Peso (kg):"));
                panelCampos.add(txtPeso);
                panelCampos.add(new JLabel("Altura (cm):"));
                panelCampos.add(txtAltura);

                int result = JOptionPane.showConfirmDialog(this, panelCampos, "Actualizar Datos",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int edad = Integer.parseInt(txtEdad.getText().trim());
                        double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
                        double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));

                        if (com.nutrilern.controlador.ControladorUsuario.actualizarDatosFisicos(user.getId(), edad,
                                peso, altura)) {
                            user.setEdad(edad);
                            user.setPeso(peso);
                            user.setAltura(altura);
                            refrescarDatos();
                            JOptionPane.showMessageDialog(this, "Datos actualizados.", "Éxito", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                        }
                    } catch (Exception ex) {}
                }
            }
        });
        tarjeta.add(btnEditar);
        return tarjeta;
    }

    private JPanel crearTarjetaNutricion() {
        JPanel tarjeta = crearTarjetaBase("Plan Nutricional");
        lblObjetivoValor = new JLabel("-");
        tarjeta.add(crearFilaDato("Objetivo Actual:", lblObjetivoValor));
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)));

        JButton btnObjetivo = crearBotonCentrado("Cambiar Objetivo");
        btnObjetivo.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user != null) {
                String[] opciones = { "Perder Peso", "Mantener Peso", "Ganar Músculo" };
                JComboBox<String> combo = new JComboBox<>(opciones);
                combo.setSelectedIndex(user.getIdObjetivo() - 1);

                int result = JOptionPane.showConfirmDialog(this, combo, "Nuevo objetivo",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                if (result == JOptionPane.OK_OPTION) {
                    int idObjNuevo = combo.getSelectedIndex() + 1;
                    if (com.nutrilern.controlador.ControladorUsuario.actualizarObjetivo(user.getId(), idObjNuevo)) {
                        user.setIdObjetivo(idObjNuevo);
                        refrescarDatos();
                        JOptionPane.showMessageDialog(this, "Objetivo actualizado.", "Éxito", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                    }
                }
            }
        });
        tarjeta.add(btnObjetivo);
        return tarjeta;
    }

    private JPanel crearTarjetaSeguridad() {
        JPanel tarjeta = crearTarjetaBase("Seguridad");
        
        JButton btnEmail = crearBotonCentrado("Cambiar Email");
        btnEmail.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user == null) return;

            String nuevoEmail = (String) JOptionPane.showInputDialog(this, 
                "Introduce tu nuevo correo electrónico:", "Cambiar Email", 
                JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo(), null, user.getEmail());

            if (nuevoEmail != null) {
                nuevoEmail = nuevoEmail.trim();
                if (nuevoEmail.isEmpty() || nuevoEmail.equals(user.getEmail())) return;

                if (!com.nutrilern.controlador.ControladorUsuario.esEmailValido(nuevoEmail)) {
                    JOptionPane.showMessageDialog(this, "El formato del correo no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    // VERIFICACIÓN POR CORREO
                    if (mostrarDialogoVerificacion(nuevoEmail)) {
                        if (com.nutrilern.controlador.ControladorUsuario.actualizarEmail(user.getId(), nuevoEmail)) {
                            user.setEmail(nuevoEmail);
                            refrescarDatos();
                            JOptionPane.showMessageDialog(this, "Correo electrónico actualizado correctamente.", "Éxito", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo actualizar el correo. Es posible que ya esté en uso.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        JButton btnPass = crearBotonCentrado("Cambiar Contraseña");
        btnPass.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user == null) return;

            JPanel panelPass = new JPanel(new GridLayout(4, 1, 5, 5));
            JLabel lblNew = new JLabel("Nueva Contraseña (mín. 8 caracteres):");
            JPasswordField txtNew = new JPasswordField();
            JLabel lblConf = new JLabel("Confirmar Contraseña:");
            JPasswordField txtConf = new JPasswordField();
            
            panelPass.add(lblNew);
            panelPass.add(txtNew);
            panelPass.add(lblConf);
            panelPass.add(txtConf);

            int result = JOptionPane.showConfirmDialog(this, panelPass, "Cambiar Contraseña",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());

            if (result == JOptionPane.OK_OPTION) {
                String pass = new String(txtNew.getPassword());
                String conf = new String(txtConf.getPassword());

                if (pass.isEmpty()) return;

                if (!pass.equals(conf)) {
                    JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Error", JOptionPane.ERROR_MESSAGE);
                } else if (!com.nutrilern.controlador.ControladorUsuario.esPasswordValida(pass)) {
                    JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 8 caracteres.", "Seguridad", JOptionPane.WARNING_MESSAGE);
                } else {
                    // VERIFICACIÓN POR CORREO (al email actual)
                    if (mostrarDialogoVerificacion(user.getEmail())) {
                        if (com.nutrilern.controlador.ControladorUsuario.actualizarPassword(user.getId(), pass)) {
                            JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.", "Éxito", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        tarjeta.add(btnEmail);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(btnPass);
        return tarjeta;
    }

    /**
     * Muestra un diálogo para verificar un código enviado por email.
     */
    private boolean mostrarDialogoVerificacion(String emailDestino) {
        String codigoGenerado = com.nutrilern.controlador.ControladorUsuario.generarCodigoVerificacion();
        
        // Diálogo de carga no modal para no bloquear el envío
        JOptionPane paneCarga = new JOptionPane("Enviando código de verificación a:\n" + emailDestino, 
                JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, TemaNutrix.obtenerIconoDialogo(), new Object[]{}, null);
        JDialog dialogCarga = paneCarga.createDialog(this, "Enviando...");
        
        final boolean[] enviado = {false};
        new Thread(() -> {
            if (com.nutrilern.controlador.ControladorUsuario.enviarCodigo(emailDestino, codigoGenerado)) {
                enviado[0] = true;
                SwingUtilities.invokeLater(dialogCarga::dispose);
            } else {
                SwingUtilities.invokeLater(() -> {
                    dialogCarga.dispose();
                    JOptionPane.showMessageDialog(this, "Error al enviar el código de verificación.", "Error", JOptionPane.ERROR_MESSAGE);
                });
            }
        }).start();
        dialogCarga.setVisible(true);

        if (!enviado[0]) return false;

        String input = (String) JOptionPane.showInputDialog(this, 
                "Introduce el código de 8 dígitos enviado a su correo:", 
                "Verificación requerida", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo(), null, "");
        
        if (input != null && input.equals(codigoGenerado)) {
            return true;
        } else if (input != null) {
            JOptionPane.showMessageDialog(this, "Código incorrecto. No se realizaron cambios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    private JPanel crearTarjetaPeligro() {
        JPanel tarjeta = crearTarjetaBase("Zona de Peligro");
        JButton btnLogout = crearBotonCentrado("Cerrar Sesión");
        btnLogout.setBackground(new Color(100, 100, 100));
        btnLogout.addActionListener(e -> {
            ventanaPadre.setUsuarioLogueado(null);
            ventanaPadre.cambiarPantalla("LOGIN");
        });

        JButton btnBorrar = crearBotonCentrado("Eliminar Cuenta");
        btnBorrar.setBackground(new Color(220, 53, 69));
        btnBorrar.addActionListener(e -> {
            int res = JOptionPane.showConfirmDialog(this, "¿Borrar cuenta permanentemente?", "¡Atención!",
                    JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
            if (res == JOptionPane.YES_OPTION) {
                Usuario user = ventanaPadre.getUsuarioLogueado();
                if (user != null && com.nutrilern.controlador.ControladorUsuario.eliminarCuenta(user.getId())) {
                    JOptionPane.showMessageDialog(this, "Cuenta eliminada.", "Info", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                    ventanaPadre.setUsuarioLogueado(null);
                    ventanaPadre.cambiarPantalla("LOGIN");
                }
            }
        });

        tarjeta.add(btnLogout);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(btnBorrar);
        return tarjeta;
    }

    private JPanel crearTarjetaBase(String titulo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setPreferredSize(new Dimension(450, 420));
        tarjeta.setMaximumSize(new Dimension(450, 420));

        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(30, 40, 30, 40)));

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTit.setForeground(TemaNutrix.PRIMARIO);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        tarjeta.add(lblTit);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(350, 2));
        tarjeta.add(sep);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 20)));
        return tarjeta;
    }

    private JPanel crearFilaDato(String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(350, 30));

        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));
        lblEt.setForeground(new Color(140, 140, 140));

        lblValor.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 15));
        lblValor.setForeground(TemaNutrix.TEXTO);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT);

        fila.add(lblEt, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);

        JPanel contenedorCentrado = new JPanel();
        contenedorCentrado.setLayout(new BoxLayout(contenedorCentrado, BoxLayout.Y_AXIS));
        contenedorCentrado.setOpaque(false);
        contenedorCentrado.setAlignmentX(Component.CENTER_ALIGNMENT);
        contenedorCentrado.add(fila);
        contenedorCentrado.add(Box.createRigidArea(new Dimension(0, 10)));
        return contenedorCentrado;
    }

    private JButton crearBotonCentrado(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(TemaNutrix.PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setMaximumSize(new Dimension(250, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void refrescarDatos() {
        Usuario user = ventanaPadre.getUsuarioLogueado();
        if (user != null) {
            lblNombreValor.setText(user.getNombre() + " " + user.getApellidos());
            lblEmailValor.setText(user.getEmail());
            lblEdadValor.setText(user.getEdad() + " años");
            lblAlturaValor.setText(user.getAltura() + " cm");
            lblSexoValor.setText(user.getSexo() != null && user.getSexo().equals("F") ? "Mujer" : "Hombre");
            
            String objStr = "No definido";
            if (user.getIdObjetivo() == 1) objStr = "Perder Grasa";
            else if (user.getIdObjetivo() == 2) objStr = "Mantener";
            else if (user.getIdObjetivo() == 3) objStr = "Ganar Volumen";
            
            lblObjetivoValor.setText(objStr);
        }
    }
}
