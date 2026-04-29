package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import com.nutrilern.modelo.UsuarioDAO;
import com.nutrilern.modelo.Usuario;

public class PanelAjustes extends JPanel {

    private VentanaPrincipal ventanaPadre;

    // Etiquetas dinámicas de datos
    private JLabel lblNombreValor, lblEmailValor, lblEdadValor, lblAlturaValor, lblObjetivoValor;

    // Colores
    private final Color COLOR_VERDE_NUTRIX = new Color(34, 139, 34);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    // Navegación Superior (Tabs)
    private JButton btnNavDatos, btnNavNutricion, btnNavSeguridad, btnNavPeligro;
    
    // Contenedor de las pestañas
    private JPanel panelContenedorCartas;
    private CardLayout cardLayout;

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        // 1. Cabecera (Título, Botón Volver y Barra de Navegación)
        add(crearCabeceraYNav(), BorderLayout.NORTH);

        // 2. Contenedor Central con CardLayout (Pestañas)
        cardLayout = new CardLayout();
        panelContenedorCartas = new JPanel(cardLayout);
        panelContenedorCartas.setBackground(COLOR_FONDO);

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
        header.setBorder(new EmptyBorder(15, 30, 15, 30));

        JButton btnVolver = new JButton("← Volver al Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(COLOR_VERDE_NUTRIX);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTituloHeader = new JLabel("Ajustes de mi Cuenta", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloHeader.setForeground(COLOR_TEXTO);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST); // Balance

        // --- Parte 2: Botones de Pestañas ---
        JPanel navBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        navBar.setBackground(Color.WHITE);
        navBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220))); // Línea gris inferior

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
        btn.setForeground(new Color(120, 120, 120));
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
                btn.setForeground(COLOR_VERDE_NUTRIX);
                btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 3, 0, COLOR_VERDE_NUTRIX), // Subrayado grueso verde
                    new EmptyBorder(10, 5, 7, 5)
                ));
            } else {
                btn.setFont(new Font("Arial", Font.PLAIN, 15));
                btn.setForeground(new Color(120, 120, 120));
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
        fondo.setBackground(COLOR_FONDO);
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

        tarjeta.add(crearFilaDato("Nombre:", lblNombreValor));
        tarjeta.add(crearFilaDato("Email:", lblEmailValor));
        tarjeta.add(crearFilaDato("Edad:", lblEdadValor));
        tarjeta.add(crearFilaDato("Altura:", lblAlturaValor));
        
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JButton btnEditar = crearBotonCentrado("Actualizar Datos Físicos");
        btnEditar.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Editar datos físicos"));
        tarjeta.add(btnEditar);
        
        return tarjeta;
    }

    private JPanel crearTarjetaNutricion() {
        JPanel tarjeta = crearTarjetaBase("Plan Nutricional");

        lblObjetivoValor = new JLabel("-");
        tarjeta.add(crearFilaDato("Objetivo Actual:", lblObjetivoValor));
        
        tarjeta.add(Box.createRigidArea(new Dimension(0, 25)));
        
        JButton btnObjetivo = crearBotonCentrado("Cambiar mi Objetivo");
        btnObjetivo.addActionListener(e -> JOptionPane.showMessageDialog(this, "Próximamente: Cambiar objetivo"));
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
                    if (UsuarioDAO.actualizarEmail(user.getId(), nuevoEmail.trim())) {
                        user.setEmail(nuevoEmail.trim());
                        refrescarDatos();
                        JOptionPane.showMessageDialog(this, "Email actualizado correctamente.");
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
                    boolean exito = UsuarioDAO.eliminarUsuario(user.getId());
                    
                    if (exito) {
                        JOptionPane.showMessageDialog(this, 
                            "Tu cuenta y todos tus datos han sido eliminados correctamente.\n¡Esperamos volver a verte pronto!", 
                            "Cuenta Eliminada", 
                            JOptionPane.INFORMATION_MESSAGE);
                            
                        // 4. Limpiamos la sesión y volvemos al Login
                        ventanaPadre.setUsuarioLogueado(null); 
                        ventanaPadre.cambiarPantalla("LOGIN");
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Hubo un error de conexión al intentar eliminar la cuenta.", 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
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
        tarjeta.setPreferredSize(new Dimension(450, 350)); 
        tarjeta.setMaximumSize(new Dimension(450, 350));
        
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        
        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 22));
        lblTit.setForeground(COLOR_VERDE_NUTRIX);
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
        lblValor.setForeground(COLOR_TEXTO);
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
        btn.setBackground(COLOR_VERDE_NUTRIX);
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
            
            String objStr = "Desconocido";
            if (user.getIdObjetivo() == 1) objStr = "Perder Grasa";
            else if (user.getIdObjetivo() == 2) objStr = "Mantener";
            else if (user.getIdObjetivo() == 3) objStr = "Ganar Volumen";
            
            lblObjetivoValor.setText(objStr);
        }
    }
}