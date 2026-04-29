package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.Usuario;

public class PanelMisComidas extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private final Color colorPrincipal = new Color(34, 139, 34);
    private final Color colorFondo = new Color(245, 247, 250);
    
    private JTextField txtNombre;
    private JTextField txtKcal;
    private JComboBox<String> comboDia;
    private JLabel lblFechaInfo;

    public PanelMisComidas(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(colorFondo);

        // --- ENCABEZADO ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        JButton btnVolver = new JButton("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);
        
        JLabel lblTitulo = new JLabel("Mis Comidas Semanales", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        header.add(lblTitulo, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        // --- CONTENIDO ---
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(30, 50, 30, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulario
        JPanel form = new JPanel(new GridLayout(0, 1, 10, 10));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        form.add(new JLabel("¿Qué has comido? (Obligatorio)"));
        txtNombre = new JTextField();
        form.add(txtNombre);

        form.add(new JLabel("Calorías (kcal) - Por defecto 0"));
        txtKcal = new JTextField("0");
        form.add(txtKcal);

        form.add(new JLabel("¿Qué día fue?"));
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        comboDia = new JComboBox<>(dias);
        comboDia.addActionListener(e -> actualizarFechaInfo());
        form.add(comboDia);

        lblFechaInfo = new JLabel("Fecha: " + obtenerFechaParaDia(comboDia.getSelectedIndex() + 1));
        lblFechaInfo.setForeground(colorPrincipal);
        form.add(lblFechaInfo);

        JButton btnGuardar = new JButton("Añadir Alimento");
        btnGuardar.setBackground(colorPrincipal);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> guardarComida());
        form.add(btnGuardar);

        gbc.gridx = 0;
        gbc.gridy = 0;
        content.add(form, gbc);

        add(content, BorderLayout.CENTER);
    }

    /**
     * Calcula la fecha correspondiente a un día de la semana actual.
     * @param dia 1 (Lunes) a 7 (Domingo)
     */
    private String obtenerFechaParaDia(int dia) {
        LocalDate hoy = LocalDate.now();
        // Buscamos el lunes de esta semana
        LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        // Sumamos los días necesarios
        LocalDate fechaBuscada = lunes.plusDays(dia - 1);
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return fechaBuscada.format(formatter);
    }

    private void actualizarFechaInfo() {
        lblFechaInfo.setText("Fecha: " + obtenerFechaParaDia(comboDia.getSelectedIndex() + 1));
    }

    private void guardarComida() {
        String nombre = txtNombre.getText().trim();
        String kcalStr = txtKcal.getText().trim();
        int diaIndex = comboDia.getSelectedIndex() + 1;

        // Validación de Not Null (Nombre)
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre del alimento es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int kcal = kcalStr.isEmpty() ? 0 : Integer.parseInt(kcalStr);
            int idCategoria = 30020; // Valor por defecto solicitado
            
            // Calculamos la fecha real como LocalDate
            LocalDate hoy = LocalDate.now();
            LocalDate lunes = hoy.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate fechaReal = lunes.plusDays(diaIndex - 1);

            Usuario user = ventanaPadre.getUsuarioLogueado();
            if (user == null) {
                JOptionPane.showMessageDialog(this, "Debes iniciar sesión para guardar comidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (AlimentoDAO.registrarConsumo(user.getId(), nombre, kcal, idCategoria, fechaReal)) {
                JOptionPane.showMessageDialog(this, "¡Alimento guardado con éxito para el " + obtenerFechaParaDia(diaIndex) + "!");
                txtNombre.setText("");
                txtKcal.setText("0");
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Las calorías deben ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
