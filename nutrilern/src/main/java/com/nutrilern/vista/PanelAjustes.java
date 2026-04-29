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

    // Etiquetas que queremos actualizar dinámicamente
    private JLabel lblNombreValor;
    private JLabel lblEmailValor;
    private JLabel lblEdadValor;
    private JLabel lblAlturaValor;

    // Colores
    private final Color COLOR_PRINCIPAL = new Color(34, 139, 34); 
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

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

        JLabel lblTituloHeader = new JLabel("Configuración de Perfil", SwingConstants.CENTER);
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
        contentGrid.add(crearSeccionSeguridad(), gbc);

        gbc.gridy = 2;
        contentGrid.add(crearSeccionSalida(), gbc);

        return new JScrollPane(contentGrid);
    }

    private JPanel crearSeccionInformacion() {
        JPanel infoPanel = crearContenedorSeccion("Información del Usuario");
        
        lblNombreValor = new JLabel("Cargando...");
        lblEmailValor = new JLabel("Cargando...");
        lblEdadValor = new JLabel("Cargando...");
        lblAlturaValor = new JLabel("Cargando...");

        agregarFilaDatoDinamica(infoPanel, "Nombre completo:", lblNombreValor);
        agregarFilaDatoDinamica(infoPanel, "Email registrado:", lblEmailValor);
        agregarFilaDatoDinamica(infoPanel, "Edad:", lblEdadValor);
        agregarFilaDatoDinamica(infoPanel, "Altura:", lblAlturaValor);
        
        return infoPanel;
    }

    private JPanel crearSeccionSeguridad() {
        JPanel seguridadPanel = crearContenedorSeccion("Seguridad y Cuenta");

        JButton btnCambiarEmail = new JButton("Cambiar Correo Electrónico");
        estilizarBotonAccion(btnCambiarEmail);
        
        btnCambiarEmail.addActionListener(e -> {
            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user == null) {
                JOptionPane.showMessageDialog(this, "No hay sesión activa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoEmail = JOptionPane.showInputDialog(this, "Nuevo correo:", user.getEmail());
            if (nuevoEmail != null && !nuevoEmail.trim().isEmpty()) {
                if (UsuarioDAO.actualizarEmail(user.getId(), nuevoEmail.trim())) {
                    user.setEmail(nuevoEmail.trim());
                    refrescarDatos();
                    JOptionPane.showMessageDialog(this, "Email actualizado correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar en Base de Datos.");
                }
            }
        });

        seguridadPanel.add(btnCambiarEmail);
        seguridadPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnCambiarPass = new JButton("Cambiar Contraseña");
        estilizarBotonAccion(btnCambiarPass);
        seguridadPanel.add(btnCambiarPass);

        return seguridadPanel;
    }

    /**
     * Este método rellena las etiquetas con los datos del usuario real
     */
    public void refrescarDatos() {
        Usuario user = ventanaPadre.getUsuarioLogueado();
        if (user != null) {
            lblNombreValor.setText(user.getNombre() + " " + user.getApellidos());
            lblEmailValor.setText(user.getEmail());
            lblEdadValor.setText(user.getEdad() + " años");
            lblAlturaValor.setText(user.getAltura() + " cm");
        }
    }

    private JPanel crearSeccionSalida() {
        JPanel salidaPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salidaPanel.setOpaque(false);
        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(220, 53, 69));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.setFocusPainted(false);
        btnLogout.setPreferredSize(new Dimension(200, 45));
        btnLogout.setBorder(new LineBorder(new Color(180, 40, 55), 1, true));
        btnLogout.addActionListener(e -> {
            ventanaPadre.setUsuarioLogueado(null);
            ventanaPadre.cambiarPantalla("LOGIN");
        });
        salidaPanel.add(btnLogout);
        return salidaPanel;
    }

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
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));
        seccion.add(new JSeparator());
        seccion.add(Box.createRigidArea(new Dimension(0, 15)));
        return seccion;
    }

    private void agregarFilaDatoDinamica(JPanel panel, String etiqueta, JLabel lblValor) {
        JPanel fila = new JPanel(new BorderLayout());
        fila.setOpaque(false);
        fila.setMaximumSize(new Dimension(1000, 30));
        JLabel lblEt = new JLabel(etiqueta);
        lblEt.setFont(new Font("Arial", Font.BOLD, 14));
        lblEt.setForeground(new Color(100, 100, 100));
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
        btn.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COLOR_PRINCIPAL, 1, true), new EmptyBorder(8, 15, 8, 15)));
    }
}
