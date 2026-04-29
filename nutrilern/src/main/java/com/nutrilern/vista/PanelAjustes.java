package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.nutrilern.modelo.UsuarioDAO;
import com.nutrilern.modelo.Usuario;

public class PanelAjustes extends JPanel {

    private VentanaPrincipal ventanaPadre;

    // Etiquetas dinámicas
    private JLabel lblNombreValor;
    private JLabel lblEmailValor;
    private JLabel lblEdadValor;
    private JLabel lblAlturaValor;
    private JLabel lblObjetivoValor;

    // Colores (Añadido el rojo de peligro y ajustados grises)
    private final Color COLOR_PRINCIPAL = new Color(34, 139, 34); 
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Color COLOR_PELIGRO = new Color(220, 53, 69);

    public PanelAjustes(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearCuerpoPrincipal(), BorderLayout.CENTER);
    }

    private JPanel crearEncabezado() {
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

        JLabel lblTituloHeader = new JLabel("Ajustes de mi Cuenta", SwingConstants.CENTER);
        lblTituloHeader.setFont(new Font("Arial", Font.BOLD, 22));
        lblTituloHeader.setForeground(COLOR_TEXTO);
        header.add(lblTituloHeader, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);
        return header;
    }

    private JComponent crearCuerpoPrincipal() {
        JPanel contentGrid = new JPanel(new GridBagLayout());
        contentGrid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridy = 0;
        contentGrid.add(crearSeccionInformacion(), gbc);

        gbc.gridy = 1;
        contentGrid.add(crearSeccionNutricion(), gbc);

        gbc.gridy = 2;
        contentGrid.add(crearSeccionSeguridad(), gbc);

        gbc.gridy = 3;
        contentGrid.add(crearSeccionPeligro(), gbc);

        // Añadimos un scroll por si la pantalla es pequeña
        JScrollPane scroll = new JScrollPane(contentGrid);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        return scroll;
    }

    // --- SECCIÓN 1: DATOS PERSONALES ---
    private JPanel crearSeccionInformacion() {
        JPanel infoPanel = crearContenedorSeccion("Información Personal");
        
        lblNombreValor = new JLabel("-");
        lblEmailValor = new JLabel("-");
        lblEdadValor = new JLabel("-");
        lblAlturaValor = new JLabel("-");

        agregarFilaDatoDinamica(infoPanel, "Nombre completo:", lblNombreValor);
        agregarFilaDatoDinamica(infoPanel, "Email registrado:", lblEmailValor);
        agregarFilaDatoDinamica(infoPanel, "Edad:", lblEdadValor);
        agregarFilaDatoDinamica(infoPanel, "Altura:", lblAlturaValor);
        
        infoPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton btnEditarPerfil = new JButton("Editar mis datos físicos");
        estilizarBotonAccion(btnEditarPerfil);
        btnEditarPerfil.addActionListener(e -> {
            // AQUÍ IRÁ LA LÓGICA PARA ACTUALIZAR EDAD, ALTURA, ETC.
            JOptionPane.showMessageDialog(this, "Próximamente: Ventana para cambiar edad y altura");
        });
        infoPanel.add(btnEditarPerfil);
        
        return infoPanel;
    }

    // --- SECCIÓN 2: NUTRICIÓN ---
    private JPanel crearSeccionNutricion() {
        JPanel nutriPanel = crearContenedorSeccion("Mi Plan Nutricional");
        
        lblObjetivoValor = new JLabel("-");
        agregarFilaDatoDinamica(nutriPanel, "Objetivo Actual:", lblObjetivoValor);
        
        nutriPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton btnCambiarObjetivo = new JButton("Cambiar mi objetivo");
        estilizarBotonAccion(btnCambiarObjetivo);
        btnCambiarObjetivo.addActionListener(e -> {
            // AQUÍ IRÁ LA LÓGICA PARA CAMBIAR ENTRE PERDER PESO, GANAR VOLUMEN...
            JOptionPane.showMessageDialog(this, "Próximamente: Desplegable para actualizar el id_objetivo_fk");
        });
        nutriPanel.add(btnCambiarObjetivo);

        return nutriPanel;
    }

    // --- SECCIÓN 3: SEGURIDAD ---
    private JPanel crearSeccionSeguridad() {
        JPanel seguridadPanel = crearContenedorSeccion("Seguridad");

        JButton btnCambiarEmail = new JButton("Cambiar Correo Electrónico");
        estilizarBotonAccion(btnCambiarEmail);
        btnCambiarEmail.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user != null) {
                String nuevoEmail = JOptionPane.showInputDialog(this, "Nuevo correo:", user.getEmail());
                if (nuevoEmail != null && !nuevoEmail.trim().isEmpty()) {
                    // Cuidado: Asumo que tu compi creó actualizarEmail en el DAO
                    if (UsuarioDAO.actualizarEmail(user.getId(), nuevoEmail.trim())) {
                        user.setEmail(nuevoEmail.trim());
                        refrescarDatos();
                        JOptionPane.showMessageDialog(this, "Email actualizado correctamente.");
                    } else {
                        JOptionPane.showMessageDialog(this, "Error al actualizar en Base de Datos.");
                    }
                }
            }
        });

        JButton btnCambiarPass = new JButton("Cambiar Contraseña");
        estilizarBotonAccion(btnCambiarPass);
        btnCambiarPass.addActionListener(e -> {
            // AQUÍ IRÁ LA LÓGICA PARA PEDIR PASS ANTIGUA, NUEVA Y HASHEARLA
            JOptionPane.showMessageDialog(this, "Próximamente: Diálogo para cambiar contraseña");
        });

        seguridadPanel.add(btnCambiarEmail);
        seguridadPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        seguridadPanel.add(btnCambiarPass);

        return seguridadPanel;
    }

    // --- SECCIÓN 4: ZONA DE PELIGRO ---
    private JPanel crearSeccionPeligro() {
        JPanel peligroPanel = crearContenedorSeccion("Zona de Peligro");

        JButton btnLogout = new JButton("Cerrar Sesión");
        estilizarBotonAccion(btnLogout); // Botón normal gris
        btnLogout.addActionListener(e -> {
            ventanaPadre.setUsuarioLogueado(null);
            ventanaPadre.cambiarPantalla("LOGIN");
        });

        JButton btnBorrarCuenta = new JButton("Eliminar Cuenta Permanentemente");
        // Estilo especial rojo para este botón
        btnBorrarCuenta.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnBorrarCuenta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnBorrarCuenta.setFont(new Font("Arial", Font.BOLD, 13));
        btnBorrarCuenta.setForeground(COLOR_PELIGRO);
        btnBorrarCuenta.setBackground(new Color(255, 235, 238));
        btnBorrarCuenta.setFocusPainted(false);
        btnBorrarCuenta.setBorder(new LineBorder(COLOR_PELIGRO, 1, true));
        
        btnBorrarCuenta.addActionListener(e -> {
            int confirmacion = JOptionPane.showConfirmDialog(this, 
                "¿Estás seguro de que quieres borrar tu cuenta? Esta acción es irreversible y perderás todos tus registros.", 
                "¡ATENCIÓN!", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
            if (confirmacion == JOptionPane.YES_OPTION) {
                // AQUÍ IRÁ LA LÓGICA PARA HACER EL DELETE EN LA BBDD
                JOptionPane.showMessageDialog(this, "Próximamente: Borrado de cuenta (ON DELETE CASCADE)");
            }
        });

        peligroPanel.add(btnLogout);
        peligroPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        peligroPanel.add(btnBorrarCuenta);

        return peligroPanel;
    }

    /**
     * Rellena las etiquetas con los datos del usuario real cada vez que entramos al panel
     */
    public void refrescarDatos() {
        // NOTA: Asegúrate de que en VentanaPrincipal el método se llame getUsuarioLogueado() o getUsuarioActual()
        Usuario user = ventanaPadre.getUsuarioLogueado(); 
        if (user != null) {
            lblNombreValor.setText(user.getNombre() + " " + user.getApellidos());
            lblEmailValor.setText(user.getEmail());
            lblEdadValor.setText(user.getEdad() + " años");
            lblAlturaValor.setText(user.getAltura() + " cm");
            
            // Asumiendo que 1=Perder Grasa, 2=Mantener, 3=Ganar Volumen según tu BBDD
            String objStr = "Desconocido";
            if (user.getIdObjetivo() == 1) objStr = "Perder Grasa";
            else if (user.getIdObjetivo() == 2) objStr = "Mantener";
            else if (user.getIdObjetivo() == 3) objStr = "Ganar Volumen";
            
            lblObjetivoValor.setText(objStr);
        }
    }

    // --- MÉTODOS DE DISEÑO (UI) ---
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
        seccion.add(Box.createRigidArea(new Dimension(0, 10)));
        seccion.add(new JSeparator());
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));
        
        return seccion;
    }

    private void agregarFilaDatoDinamica(JPanel panel, String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(1000, 25));
        
        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font("Arial", Font.BOLD, 14));
        lblEt.setForeground(new Color(120, 120, 120));
        
        lblValor.setFont(new Font("Arial", Font.PLAIN, 14));
        lblValor.setForeground(COLOR_TEXTO);
        
        fila.add(lblEt, BorderLayout.WEST);
        fila.add(lblValor, BorderLayout.EAST);
        
        panel.add(fila);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void estilizarBotonAccion(JButton btn) {
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(COLOR_TEXTO);
        btn.setBackground(new Color(248, 249, 250));
        btn.setFocusPainted(false);
        btn.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));
        
        // Efecto Hover (cambia de color al pasar el ratón)
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(new Color(233, 236, 239)); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(new Color(248, 249, 250)); }
        });
    }

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