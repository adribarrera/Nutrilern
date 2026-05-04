package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import com.nutrilern.modelo.Usuario;
import com.nutrilern.controlador.ControladorVistas;
import java.awt.*;
import java.util.List;

public class PanelAdminUsuarios extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTabla;

    public PanelAdminUsuarios(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        add(crearHeader(), BorderLayout.NORTH);
        add(crearCuerpo(), BorderLayout.CENTER);
    }

    private JPanel crearHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTit = new JLabel("Gestión de Usuarios", JLabel.CENTER);
        lblTit.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTit.setForeground(TemaNutrix.TEXTO);
        header.add(lblTit, BorderLayout.CENTER);

        JButton btnNuevo = new JButton("+ Nuevo Usuario");
        btnNuevo.setBackground(TemaNutrix.PRIMARIO);
        btnNuevo.setForeground(Color.WHITE);
        btnNuevo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));
        btnNuevo.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnNuevo.addActionListener(e -> mostrarDialogoEdicion(null));
        header.add(btnNuevo, BorderLayout.EAST);

        return header;
    }

    private JPanel crearCuerpo() {
        JPanel cuerpo = new JPanel(new BorderLayout());
        cuerpo.setOpaque(false);
        cuerpo.setBorder(new EmptyBorder(30, 40, 30, 40));

        String[] columnas = { "ID", "Email", "Nombre", "Apellidos", "Rol" };
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaUsuarios = new JTable(modeloTabla);
        tablaUsuarios.setRowHeight(40);
        tablaUsuarios.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 14));
        tablaUsuarios.getTableHeader().setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tablaUsuarios);
        cuerpo.add(scroll, BorderLayout.CENTER);

        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelAcciones.setOpaque(false);

        JButton btnModificar = crearBotonSecundario("Modificar");
        btnModificar.setForeground(TemaNutrix.ACCENTO);
        btnModificar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaNutrix.ACCENTO, 1, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        btnModificar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila != -1) {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                Usuario u = buscarUsuarioEnTabla(id);
                mostrarDialogoEdicion(u);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona un usuario de la tabla", "Aviso", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
            }
        });

        JButton btnEliminar = crearBotonSecundario("Borrar");
        btnEliminar.addActionListener(e -> {
            int fila = tablaUsuarios.getSelectedRow();
            if (fila != -1) {
                int id = (int) modeloTabla.getValueAt(fila, 0);
                if (JOptionPane.showConfirmDialog(this,
                        "¿Seguro que quieres borrar al usuario con ID " + id + "?", 
                        "Confirmar eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo()) == JOptionPane.YES_OPTION) {
                    if (ControladorVistas.eliminarUsuarioAdmin(id)) {
                        refrescarTabla();
                    }
                }
            }
        });

        panelAcciones.add(btnModificar);
        panelAcciones.add(btnEliminar);
        cuerpo.add(panelAcciones, BorderLayout.SOUTH);

        return cuerpo;
    }

    public void refrescarTabla() {
        modeloTabla.setRowCount(0);
        List<Usuario> lista = ControladorVistas.listarUsuariosAdmin();
        for (Usuario u : lista) {
            modeloTabla.addRow(new Object[] {
                    u.getId(), u.getEmail(), u.getNombre(), u.getApellidos(), u.getRol()
            });
        }
    }

    private Usuario buscarUsuarioEnTabla(int id) {
        return ControladorVistas.listarUsuariosAdmin().stream()
                .filter(u -> u.getId() == id)
                .findFirst().orElse(null);
    }

    private void mostrarDialogoEdicion(Usuario u) {
        boolean esNuevo = (u == null);
        Usuario tempUser = esNuevo ? new Usuario() : u;

        JTextField txtNombre = new JTextField(tempUser.getNombre());
        JTextField txtApellidos = new JTextField(tempUser.getApellidos());
        JTextField txtEmail = new JTextField(tempUser.getEmail());
        JTextField txtPass = new JPasswordField();
        JTextField txtEdad = new JTextField(String.valueOf(tempUser.getEdad()));
        JTextField txtAltura = new JTextField(String.valueOf(tempUser.getAltura()));
        JComboBox<String> comboRol = new JComboBox<>(new String[] { "USUARIO", "ADMIN" });
        comboRol.setSelectedItem(tempUser.getRol() != null ? tempUser.getRol() : "USUARIO");

        // La BD guarda CHAR(1): 'H' o 'M'
        JComboBox<String> comboSexo = new JComboBox<>(new String[] { "H", "M" });
        comboSexo.setSelectedItem(tempUser.getSexo() != null ? tempUser.getSexo() : "H");
        comboSexo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setText("H".equals(value) ? "Hombre" : "Mujer");
                return this;
            }
        });

        Object[] message = {
                "Nombre:", txtNombre,
                "Apellidos:", txtApellidos,
                "Email:", txtEmail,
                "Contraseña " + (esNuevo ? ":" : "(vacío para no cambiar):"), txtPass,
                "Edad:", txtEdad,
                "Altura (cm):", txtAltura,
                "Sexo:", comboSexo,
                "Rol:", comboRol
        };

        int option = JOptionPane.showConfirmDialog(this, message,
                esNuevo ? "Crear Nuevo Usuario" : "Editar Perfil", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());

        if (option == JOptionPane.OK_OPTION) {
            try {
                tempUser.setNombre(txtNombre.getText());
                tempUser.setApellidos(txtApellidos.getText());
                tempUser.setEmail(txtEmail.getText());
                tempUser.setEdad(Integer.parseInt(txtEdad.getText()));
                tempUser.setAltura(Double.parseDouble(txtAltura.getText()));
                tempUser.setRol((String) comboRol.getSelectedItem());
                tempUser.setSexo((String) comboSexo.getSelectedItem());

                if (ControladorVistas.guardarUsuarioAdmin(tempUser, txtPass.getText(), esNuevo)) {
                    refrescarTabla();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al guardar los cambios.", "Error", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Datos inválidos: " + ex.getMessage(), "Error de formato", JOptionPane.PLAIN_MESSAGE, TemaNutrix.obtenerIconoDialogo());
            }
        }
    }
    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 13));
        btn.setForeground(TemaNutrix.PRIMARIO);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        return btn;
    }
}
