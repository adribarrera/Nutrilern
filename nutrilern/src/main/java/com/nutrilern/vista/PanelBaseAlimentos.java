package com.nutrilern.vista;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.ComboItem;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelBaseAlimentos extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JTable tablaAlimentos;
    private DefaultTableModel modeloTabla;
    private JLabel lblDetalle;
    private JTextField txtBuscar;
    private JComboBox<ComboItem> comboCategorias;

    public PanelBaseAlimentos(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Base de Alimentos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTitulo.setForeground(TemaNutrix.TEXTO);
        header.add(lblTitulo, BorderLayout.CENTER);
        header.add(Box.createRigidArea(new Dimension(130, 0)), BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // --- CONTENIDO ---
        JPanel panelContenido = new JPanel(new BorderLayout(15, 15));
        panelContenido.setOpaque(false);
        panelContenido.setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. ZONA SUPERIOR: BUSCADOR Y FILTROS ---
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setOpaque(false);

        // Barra de búsqueda y botones
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelControles.setOpaque(false);

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 14));

        comboCategorias = new JComboBox<>();
        comboCategorias.addItem(new ComboItem(0, "Todas las Categorías"));
        Map<Integer, String> categorias = com.nutrilern.controlador.ControladorAlimentos.obtenerCategorias();
        for (Map.Entry<Integer, String> entry : categorias.entrySet()) {
            comboCategorias.addItem(new ComboItem(entry.getKey(), entry.getValue()));
        }

        JButton btnBuscar = crearBoton("Buscar", TemaNutrix.PRIMARIO, TemaNutrix.BLANCO);
        btnBuscar.addActionListener(e -> filtrarAlimentos());

        JButton btnTodos = crearBoton("Ver Todos", TemaNutrix.BLANCO, TemaNutrix.PRIMARIO);
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(com.nutrilern.controlador.ControladorAlimentos.obtenerTodos());
        });

        JButton btnTopProt = crearBoton("Top Proteínas", TemaNutrix.CARBOHIDRATOS, TemaNutrix.BLANCO);
        btnTopProt.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(com.nutrilern.controlador.ControladorAlimentos.obtenerTopProteinas());
        });

        JButton btnBajosKcal = crearBoton("Bajos en Kcal", TemaNutrix.CALORIAS, TemaNutrix.BLANCO);
        btnBajosKcal.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(com.nutrilern.controlador.ControladorAlimentos.obtenerBajosEnCalorias());
        });

        panelControles.add(new JLabel("🔍 Buscar:"));
        panelControles.add(txtBuscar);
        panelControles.add(new JLabel("📂 Categoría:"));
        panelControles.add(comboCategorias);
        panelControles.add(btnBuscar);
        panelControles.add(Box.createRigidArea(new Dimension(10, 0))); // Separador
        panelControles.add(btnTodos);
        panelControles.add(btnTopProt);
        panelControles.add(btnBajosKcal);

        panelControles.add(btnBajosKcal);

        panelNorte.add(panelControles, BorderLayout.CENTER);
        panelContenido.add(panelNorte, BorderLayout.NORTH);

        // --- 2. ZONA CENTRAL: LA TABLA ---
        String[] columnas = { "Nombre", "Marca", "Kcal", "Proteínas (g)", "Carbos (g)", "Grasas (g)" };

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlimentos = new JTable(modeloTabla);
        tablaAlimentos.setRowHeight(25);
        tablaAlimentos.getTableHeader().setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));
        tablaAlimentos.getTableHeader().setBackground(TemaNutrix.GRIS_CLARO);
        tablaAlimentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaAlimentos.setSelectionBackground(new Color(255, 235, 210));
        tablaAlimentos.setSelectionForeground(TemaNutrix.TEXTO);

        JScrollPane scrollPane = new JScrollPane(tablaAlimentos);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelContenido.add(scrollPane, BorderLayout.CENTER);

        // --- 3. ZONA INFERIOR: DETALLE SELECCIONADO ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(new EmptyBorder(10, 0, 0, 0));
        lblDetalle = new JLabel("Selecciona un alimento para ver detalles");
        lblDetalle.setFont(new Font(TemaNutrix.FONT_NAME, Font.ITALIC, 14));
        panelSur.add(lblDetalle);
        panelContenido.add(panelSur, BorderLayout.SOUTH);

        // AÑADIR TODO AL PANEL PRINCIPAL
        add(panelContenido, BorderLayout.CENTER);
        panelSur.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaNutrix.PRIMARIO),
                "Información Nutricional",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font(TemaNutrix.FONT_NAME, Font.BOLD, 12),
                TemaNutrix.PRIMARIO));

        lblDetalle = new JLabel("Selecciona un alimento en la tabla para ver más detalles.");
        lblDetalle.setFont(new Font(TemaNutrix.FONT_NAME, Font.ITALIC, 14));
        panelSur.add(lblDetalle);
        add(panelSur, BorderLayout.SOUTH);

        // Evento al hacer clic en una fila de la tabla
        tablaAlimentos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaAlimentos.getSelectedRow() != -1) {
                int fila = tablaAlimentos.getSelectedRow();
                String nombre = modeloTabla.getValueAt(fila, 0).toString();
                String marca = modeloTabla.getValueAt(fila, 1) != null ? modeloTabla.getValueAt(fila, 1).toString()
                        : "Genérico";
                String kcal = modeloTabla.getValueAt(fila, 2).toString();
                String prot = modeloTabla.getValueAt(fila, 3).toString();

                lblDetalle.setText(String.format("📌 %s (%s) aporta %s Kcal y %s g de proteína por cada 100g.", nombre,
                        marca, kcal, prot));
            }
        });

        // Cargamos todos los alimentos por defecto al entrar al panel
        actualizarTabla(com.nutrilern.controlador.ControladorAlimentos.obtenerTodos());
    }

    private void actualizarTabla(List<Alimento> listaAlimentos) {
        modeloTabla.setRowCount(0); // Vaciamos la tabla actual
        for (Alimento a : listaAlimentos) {
            Object[] fila = {
                    a.getNombre(),
                    a.getMarca(),
                    a.getKcal(),
                    a.getProteinas(),
                    a.getHidratosCarbono(),
                    a.getGrasas()
            };
            modeloTabla.addRow(fila); // Añadimos la fila
        }
        lblDetalle.setText(listaAlimentos.size() + " alimentos encontrados.");
    }

    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = TemaNutrix.crearBotonEstandar(texto);
        return btn;
    }

    private void filtrarAlimentos() {
        String query = txtBuscar.getText().trim();
        ComboItem selectedCat = (ComboItem) comboCategorias.getSelectedItem();
        int idCat = (selectedCat != null) ? selectedCat.getId() : 0;

        new Thread(() -> {
            List<Alimento> filtrados = com.nutrilern.controlador.ControladorAlimentos.buscarAlimentos(query, idCat);
            SwingUtilities.invokeLater(() -> actualizarTabla(filtrados));
        }).start();
    }
}
