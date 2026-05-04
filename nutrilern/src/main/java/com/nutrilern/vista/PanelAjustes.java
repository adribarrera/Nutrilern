package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.nutrilern.modelo.Usuario;

public class PanelAjustes extends JPanel {

    private VentanaPrincipal ventanaPadre;

    // Etiquetas dinámicas de datos
    private JLabel lblNombreValor, lblEmailValor, lblEdadValor, lblAlturaValor, lblSexoValor, lblObjetivoValor;



    // Navegación Superior (Tabs)
    private JButton btnNavDatos, btnNavNutricion, btnNavSeguridad, btnNavPeligro;
    
    // Contenedor de las pestañas
    private JPanel panelContenedorCartas;
    private CardLayout cardLayout;

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        // 1. Cabecera (Título, Botón Volver y Barra de Navegación)
        add(crearCabeceraYNav(), BorderLayout.NORTH);

        // 2. Contenedor Central con CardLayout (Pestañas)
        cardLayout = new CardLayout();
        panelContenedorCartas = new JPanel(cardLayout);
        panelContenedorCartas.setBackground(TemaNutrix.FONDO);

        // Añadimos las 4 pantallas centradas
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaDatos()), "DATOS");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaNutricion()), "NUTRICION");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaSeguridad()), "SEGURIDAD");
        panelContenedorCartas.add(crearPantallaCentrada(crearTarjetaPeligro()), "PELIGRO");

        add(panelContenedorCartas, BorderLayout.CENTER);
        
        // Iniciamos en la primera pestaña
        cambiarPestana(btnNavDatos, "DATOS");
    }

    // =================================================================================
    // 1. CABECERA Y BARRA DE NAVEGACIÓN SUPERIOR
    // =================================================================================
    private JPanel crearCabeceraYNav() {
        JPanel panelTop = new JPanel();
        panelTop.setLayout(new BoxLayout(panelTop, BoxLayout.Y_AXIS));
        panelTop.setBackground(Color.WHITE);

        // --- Parte 1: Título y Volver ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTituloHeader = new JLabel("Ajustes de mi Cuenta", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloHeader.setForeground(TemaNutrix.TEXTO);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST);

        // --- Parte 2: Botones de Pestañas ---
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO)); // Línea gris inferior

        btnNavDatos = crearBotonNav("Datos Físicos");
        btnNavNutricion = crearBotonNav("Mi Nutrición");
        btnNavSeguridad = crearBotonNav("Seguridad");
        btnNavPeligro = crearBotonNav("Zona de Peligro");

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
        btn.setFont(new Font("Arial", Font.PLAIN, 15));
        btn.setForeground(TemaNutrix.GRIS_TEXTO);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 5, 10, 5)); // Padding interno
        return btn;
    }

    // Lógica visual para subrayar en verde la pestaña activa
    private void cambiarPestana(JButton botonActivo, String nombreCarta) {
        JButton[] todos = {btnNavDatos, btnNavNutricion, btnNavSeguridad, btnNavPeligro};
        for (JButton btn : todos) {
            if (btn == botonActivo) {
                btn.setFont(new Font("Arial", Font.BOLD, 15));
                btn.setForeground(TemaNutrix.VERDE_NUTRIX);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, TemaNutrix.VERDE_NUTRIX), // Subrayado grueso verde
                    new EmptyBorder(10, 5, 7, 5)
                ));
            } else {
                btn.setFont(new Font("Arial", Font.PLAIN, 15));
                btn.setForeground(TemaNutrix.GRIS_TEXTO);
                btn.setBorder(new EmptyBorder(10, 5, 10, 5));
            }
        }
        cardLayout.show(panelContenedorCartas, nombreCarta);
    }

    // =================================================================================
    // 2. ENVOLTORIO PARA CENTRAR LAS PANTALLAS
    // =================================================================================
    // Este método usa GridBagLayout para dejar la tarjeta blanca perfectamente en el medio
    private JPanel crearPantallaCentrada(JPanel tarjetaBlanca) {
        JPanel fondo = new JPanel(new GridBagLayout());
        fondo.setBackground(TemaNutrix.FONDO);
        fondo.add(tarjetaBlanca);
        return fondo;
    }

    // =================================================================================
    // 3. DISEÑO DE LAS TARJETAS (PANELES INTERIORES)
    // =================================================================================
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
                
                panelCampos.add(new JLabel("Nueva Edad:"));
                panelCampos.add(txtEdad);
                panelCampos.add(new JLabel("Nuevo Peso (kg):"));
                panelCampos.add(txtPeso);
                panelCampos.add(new JLabel("Nueva Altura (cm):"));
                panelCampos.add(txtAltura);
                
                int result = JOptionPane.showConfirmDialog(this, panelCampos, "Actualizar Datos Físicos", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        int edad = Integer.parseInt(txtEdad.getText().trim());
                        double peso = Double.parseDouble(txtPeso.getText().trim().replace(",", "."));
                        double altura = Double.parseDouble(txtAltura.getText().trim().replace(",", "."));
                        
                        if (edad <= 0 || edad > 120 || peso < 20 || peso > 500 || altura < 50 || altura > 300) {
                            JOptionPane.showMessageDialog(this, "Valores no válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        if (com.nutrilern.controlador.ControladorUsuario.actualizarDatosFisicos(user.getId(), edad, peso, altura)) {
                            user.setEdad(edad);
                            user.setPeso(peso);
                            user.setAltura(altura);
                            refrescarDatos();
                            JOptionPane.showMessageDialog(this, "Datos actualizados.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Introduce solo números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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
        
        JButton btnObjetivo = crearBotonCentrado("Cambiar mi Objetivo");
        btnObjetivo.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user != null) {
                String[] opciones = { "Perder Peso", "Mantener Peso", "Ganar Músculo" };
                JComboBox<String> combo = new JComboBox<>(opciones);
                combo.setSelectedIndex(user.getIdObjetivo() - 1);
                
                int result = JOptionPane.showConfirmDialog(this, combo, "Selecciona tu nuevo objetivo", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    int idObjNuevo = combo.getSelectedIndex() + 1;
                    if (com.nutrilern.controlador.ControladorUsuario.actualizarObjetivo(user.getId(), idObjNuevo)) {
                        user.setIdObjetivo(idObjNuevo);
                        refrescarDatos();
                        JOptionPane.showMessageDialog(this, "Objetivo actualizado.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        tarjeta.add(btnObjetivo);
        
        return tarjeta;
    }

    private JPanel crearTarjetaSeguridad() {
        JPanel tarjeta = crearTarjetaBase("Seguridad de la Cuenta");

        JButton btnEmail = crearBotonCentrado("Cambiar Correo Electrónico");
        btnEmail.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user != null) {
                String nuevoEmail = JOptionPane.showInputDialog(this, "Nuevo correo:", user.getEmail());
                if (nuevoEmail != null && !nuevoEmail.trim().isEmpty()) {
                    if (!nuevoEmail.trim().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                        JOptionPane.showMessageDialog(this, "El formato del correo no es válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (com.nutrilern.controlador.ControladorUsuario.actualizarEmail(user.getId(), nuevoEmail.trim())) {
                        user.setEmail(nuevoEmail.trim());
                        refrescarDatos();
                        JOptionPane.showMessageDialog(this, "Email actualizado.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo actualizar el email.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        JButton btnPass = crearBotonCentrado("Cambiar Contraseña");
        btnPass.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Cambiar contraseña"));

        tarjeta.add(btnEmail);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(btnPass);
        
        return tarjeta;
    }

    private JPanel crearTarjetaPeligro() {
        JPanel tarjeta = crearTarjetaBase("Zona de Peligro");

        JButton btnLogout = crearBotonCentrado("Cerrar Sesión");
        btnLogout.setBackground(new Color(100, 100, 100)); // Gris oscuro
        btnLogout.addActionListener(e -> {
            ventanaPadre.setUsuarioLogueado(null); 
            ventanaPadre.cambiarPantalla("LOGIN");
        });

       JButton btnBorrar = crearBotonCentrado("Eliminar mi Cuenta");
        btnBorrar.setBackground(new Color(220, 53, 69)); // Rojo fuerte
        btnBorrar.addActionListener(e -> {
            
            // 1. Pedimos confirmación seria al usuario
            int res = JOptionPane.showConfirmDialog(this, 
                "¿Estás totalmente seguro de que quieres borrar tu cuenta?\nEsta acción es irreversible y perderás todos tus registros de comidas y evolución.", 
                "¡CUIDADO!", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
                
            if (res == JOptionPane.YES_OPTION) {
                // 2. Recuperamos el ID del usuario actual
                Usuario user = ventanaPadre.getUsuarioLogueado();
                
                if (user != null) {
                    // 3. Llamamos a la base de datos para borrarlo
                    if (com.nutrilern.controlador.ControladorUsuario.eliminarCuenta(user.getId())) {
                        JOptionPane.showMessageDialog(this, "Cuenta eliminada correctamente.");
                        ventanaPadre.setUsuarioLogueado(null); 
                        ventanaPadre.cambiarPantalla("LOGIN");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al eliminar la cuenta.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        tarjeta.add(btnLogout);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(btnBorrar);
        
        return tarjeta;
    }

    // =================================================================================
    // 4. MÉTODOS DE DIBUJO Y ALINEACIÓN CENTRADA
    // =================================================================================
    private JPanel crearTarjetaBase(String titulo) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        // Le damos un tamaño fijo a la caja blanca para que no se estire
        tarjeta.setPreferredSize(new Dimension(450, 420)); 
        tarjeta.setMaximumSize(new Dimension(450, 420));
        
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 22));
        lblTit.setForeground(TemaNutrix.VERDE_NUTRIX);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT); // Centramos el título
        
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
        fila.setMaximumSize(new Dimension(350, 30)); // Limitamos el ancho para que quede centrado
        
        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font("Arial", Font.BOLD, 14));
        lblEt.setForeground(new Color(140, 140, 140));
        
        lblValor.setFont(new Font("Arial", Font.PLAIN, 15));
        lblValor.setForeground(TemaNutrix.TEXTO);
        lblValor.setHorizontalAlignment(SwingConstants.RIGHT); // Alineamos el dato a la derecha
        
        fila.add(lblEt, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.CENTER);
        
        // Empaquetamos la fila en un contenedor centrado
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
        btn.setBackground(TemaNutrix.VERDE_NUTRIX);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT); // Botón 100% centrado
        btn.setMinimumSize(new Dimension(250, 45));
        btn.setMaximumSize(new Dimension(250, 45)); // Tamaño fijo para todos los botones
        btn.setPreferredSize(new Dimension(250, 45));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // =================================================================================
    // 5. CARGA DE DATOS
    // =================================================================================
    public void refrescarDatos() {
        Usuario user = ventanaPadre.getUsuarioLogueado(); 
        if (user != null) {
            lblNombreValor.setText(user.getNombre() + " " + user.getApellidos());
            lblEmailValor.setText(user.getEmail());
            lblEdadValor.setText(user.getEdad() + " años");
            lblAlturaValor.setText(user.getAltura() + " cm");
            lblSexoValor.setText(user.getSexo() != null && user.getSexo().equals("F") ? "Mujer" : "Hombre");
            
            String objStr = "Desconocido";
            if (user.getIdObjetivo() == 1) objStr = "Perder Grasa";
            else if (user.getIdObjetivo() == 2) objStr = "Mantener";
            else if (user.getIdObjetivo() == 3) objStr = "Ganar Volumen";
            
            lblObjetivoValor.setText(objStr);
        }
    }
}