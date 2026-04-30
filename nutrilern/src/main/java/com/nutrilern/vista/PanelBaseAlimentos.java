package com.nutrilern.vista;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.CategoriaDAO;
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
        setLayout(new BorderLayout(15, 15));
        setBackground(TemaNutrix.FONDO);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- 1. ZONA SUPERIOR: BUSCADOR Y FILTROS ---
        JPanel panelNorte = new JPanel(new BorderLayout(10, 10));
        panelNorte.setOpaque(false);

        // Cabecera con Título y Botón de Volver
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setOpaque(false);

        JLabel lblTitulo = new JLabel("Base de Alimentos Nutrix");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(TemaNutrix.TEXTO);

        JButton btnVolver = crearBoton("⬅ Volver al Menú", Color.DARK_GRAY, Color.WHITE);
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));

        panelCabecera.add(lblTitulo, BorderLayout.WEST);
        panelCabecera.add(btnVolver, BorderLayout.EAST);

        panelNorte.add(panelCabecera, BorderLayout.NORTH);

        // Barra de búsqueda y botones
        JPanel panelControles = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelControles.setOpaque(false);

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Arial", Font.PLAIN, 14));

        comboCategorias = new JComboBox<>();
        comboCategorias.addItem(new ComboItem(0, "Todas las Categorías"));
        Map<Integer, String> categorias = CategoriaDAO.obtenerTodasLasCategorias();
        for (Map.Entry<Integer, String> entry : categorias.entrySet()) {
            comboCategorias.addItem(new ComboItem(entry.getKey(), entry.getValue()));
        }

        JButton btnBuscar = crearBoton("Buscar", TemaNutrix.VERDE_NUTRIX, TemaNutrix.BLANCO);
        btnBuscar.addActionListener(e -> filtrarAlimentos());

        JButton btnTodos = crearBoton("Ver Todos", TemaNutrix.BLANCO, TemaNutrix.VERDE_NUTRIX);
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(AlimentoDAO.obtenerTodosLosAlimentos());
        });

        JButton btnTopProt = crearBoton("Top Proteínas", TemaNutrix.CARBOHIDRATOS, TemaNutrix.BLANCO);
        btnTopProt.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(AlimentoDAO.obtenerTopProteinas());
        });

        JButton btnBajosKcal = crearBoton("Bajos en Kcal", TemaNutrix.CALORIAS, TemaNutrix.BLANCO);
        btnBajosKcal.addActionListener(e -> {
            txtBuscar.setText("");
            comboCategorias.setSelectedIndex(0);
            actualizarTabla(AlimentoDAO.obtenerBajosEnCalorias());
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

        panelNorte.add(panelControles, BorderLayout.CENTER);
        add(panelNorte, BorderLayout.NORTH);

        // --- 2. ZONA CENTRAL: LA TABLA ---
        String[] columnas = { "Nombre", "Marca", "Kcal", "Proteínas (g)", "Carbos (g)", "Grasas (g)" };

        // Evitamos que el usuario edite las celdas directamente haciendo doble clic
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlimentos = new JTable(modeloTabla);
        tablaAlimentos.setRowHeight(25);
        tablaAlimentos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaAlimentos.getTableHeader().setBackground(TemaNutrix.GRIS_CLARO);
        tablaAlimentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tablaAlimentos);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // --- 3. ZONA INFERIOR: DETALLE SELECCIONADO ---
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSur.setBackground(Color.WHITE);
        panelSur.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TemaNutrix.VERDE_NUTRIX),
                "Información Nutricional",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 12),
                TemaNutrix.VERDE_NUTRIX));

        lblDetalle = new JLabel("Selecciona un alimento en la tabla para ver más detalles.");
        lblDetalle.setFont(new Font("Arial", Font.ITALIC, 14));
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
        actualizarTabla(AlimentoDAO.obtenerTodosLosAlimentos());
    }

    // --- MÉTODOS AUXILIARES ---

    // Método para refrescar la tabla con una nueva lista
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

    private void filtrarAlimentos() {
        String query = txtBuscar.getText().trim();
        ComboItem selectedCat = (ComboItem) comboCategorias.getSelectedItem();
        int idCat = (selectedCat != null) ? selectedCat.getId() : 0;

        new Thread(() -> {
            List<Alimento> filtrados = AlimentoDAO.obtenerAlimentosPorFiltro(query, idCat);
            SwingUtilities.invokeLater(() -> actualizarTabla(filtrados));
        }).start();
    }

    // Método para diseñar los botones rápidamente
    private JButton crearBoton(String texto, Color fondo, Color textoColor) {
        JButton btn = new JButton(texto);
        btn.setBackground(fondo);
        btn.setForeground(textoColor);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }
}