package com.nutrilern.vista;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.CategoriaDAO;
import com.nutrilern.modelo.ComboItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class PanelMisComidas extends JPanel {

    private VentanaPrincipal ventanaPadre;
    
    private JTable tablaComidas;
    private DefaultTableModel modeloTabla;
    private Alimento alimentoPlaceholder;
    
    private JComboBox<ComboItem> comboCategoriasTabla;

    public PanelMisComidas(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaTabla(), BorderLayout.CENTER);
        add(crearControlesInferiores(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTitulo = new JLabel("Mi Registro Diario", SwingConstants.CENTER);
        lblTitulo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblTitulo.setForeground(TemaNutrix.TEXTO);
        header.add(lblTitulo, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 20));
        panelDerecho.setBackground(Color.WHITE);

        JButton btnCrearAlimento = crearBotonSecundario("+ Nuevo Alimento");
        btnCrearAlimento.addActionListener(e -> {
            DialogoCrearAlimento dialogo = new DialogoCrearAlimento(ventanaPadre);
            dialogo.setVisible(true);
            if (dialogo.isAlimentoCreado()) {
                recargarDesplegableAlimentos();
            }
        });
        
        JButton btnCrearCategoria = crearBotonSecundario("+ Nueva Categoría");
        btnCrearCategoria.addActionListener(e -> {
            DialogoCrearCategoria dialogoCat = new DialogoCrearCategoria(ventanaPadre);
            dialogoCat.setVisible(true);
        });

        panelDerecho.add(btnCrearAlimento);
        panelDerecho.add(btnCrearCategoria);

        header.add(panelDerecho, BorderLayout.EAST);

        return header;
    }

    private JPanel crearZonaTabla() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 30, 0, 30));

        // AÑADIMOS LA COLUMNA OCULTA "Guardado"
        String[] columnas = {"Alimento (Doble clic)", "Categoría", "Gramos", "Kcal", "Prot (g)", "HC (g)", "Grasas (g)", "Sat. (g)", "Azúcar (g)", "Sal (g)", "Guardado"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 10; // La columna 10 (Guardado) no se edita a mano
            }
        };

        tablaComidas = new JTable(modeloTabla);
        tablaComidas.setRowHeight(35); 
        tablaComidas.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 14));
        tablaComidas.setSelectionBackground(new Color(230, 245, 230)); 
        tablaComidas.setSelectionForeground(TemaNutrix.TEXTO);
        
        JTableHeader th = tablaComidas.getTableHeader();
        th.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 13));
        th.setBackground(TemaNutrix.PRIMARIO);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(100, 40));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 2; i < tablaComidas.getColumnCount() - 1; i++){ 
            tablaComidas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JComboBox<Alimento> comboAlimentos = new JComboBox<>();
        List<Alimento> listaBD = AlimentoDAO.obtenerTodosLosAlimentos();
        alimentoPlaceholder = new Alimento();
        if (listaBD.isEmpty()) {
            alimentoPlaceholder.setNombre("BBDD Vacía");
        } else {
            alimentoPlaceholder.setNombre("Selecciona alimento...");
        }
        comboAlimentos.addItem(alimentoPlaceholder);
        for(Alimento a : listaBD) {
            comboAlimentos.addItem(a); 
        }
        tablaComidas.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboAlimentos));
        tablaComidas.getColumnModel().getColumn(0).setPreferredWidth(220);

        comboCategoriasTabla = new JComboBox<>();
        comboCategoriasTabla.addItem(new ComboItem(0, "Sin Categoría")); 
        Map<Integer, String> categoriasBD = CategoriaDAO.obtenerTodasLasCategorias();
        for (Map.Entry<Integer, String> entry : categoriasBD.entrySet()) {
            comboCategoriasTabla.addItem(new ComboItem(entry.getKey(), entry.getValue()));
        }
        tablaComidas.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboCategoriasTabla));
        tablaComidas.getColumnModel().getColumn(1).setPreferredWidth(130);

        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
                int fila = e.getFirstRow();
                Object obj = modeloTabla.getValueAt(fila, 0);
                if (obj instanceof Alimento) {
                    Alimento al = (Alimento) obj;
                    if (al.getIdAlimento() != 0) {
                        // Usar controlador para cálculos
                        double[] ms = com.nutrilern.controlador.ControladorComidas.calcularMacrosProporcionales(al, 100.0);
                        for(int i = 0; i < ms.length; i++) modeloTabla.setValueAt(ms[i], fila, i + 3);
                    }
                }
            }
        });

        // OCULTAMOS LA COLUMNA DE LÓGICA INTERNA DE LA VISTA DEL USUARIO
        tablaComidas.getColumnModel().removeColumn(tablaComidas.getColumnModel().getColumn(10));

        JScrollPane scrollTabla = new JScrollPane(tablaComidas);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(new LineBorder(TemaNutrix.GRIS_CLARO, 1, true));

        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        return panelCentral;
    }

    private JPanel crearControlesInferiores() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(20, 30, 30, 30));

        JPanel panelGestorFilas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelGestorFilas.setOpaque(false);

        JButton btnAñadirFila = crearBotonSecundario("+ Añadir Fila");
        btnAñadirFila.addActionListener(e -> {
            ComboItem categoriaVacia = comboCategoriasTabla.getItemAt(0); 
            modeloTabla.addRow(new Object[]{alimentoPlaceholder, categoriaVacia, 0, 0, 0, 0, 0, 0, 0, 0, false});
        });

        JButton btnBorrarFila = crearBotonSecundario("- Borrar Fila");
        btnBorrarFila.setForeground(new Color(200, 50, 50)); 
        btnBorrarFila.addActionListener(e -> {
            int filaSeleccionada = tablaComidas.getSelectedRow();
            if (filaSeleccionada != -1) {
                modeloTabla.removeRow(filaSeleccionada);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una fila primero para borrarla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        panelGestorFilas.add(btnAñadirFila);
        panelGestorFilas.add(btnBorrarFila);

        JButton btnGuardar = TemaNutrix.crearBotonEstandar("💾 Guardar Registro Diario");
        btnGuardar.setPreferredSize(new Dimension(250, 45));
        
        btnGuardar.addActionListener(e -> {
            new Thread(() -> {
                int user = ventanaPadre.getUsuarioLogueado().getId();
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if ((boolean) modeloTabla.getValueAt(i, 10)) continue;
                    Alimento al = (Alimento) modeloTabla.getValueAt(i, 0);
                    if (al == null || al.getIdAlimento() == 0) continue;
                    
                    double grams = doubleVal(modeloTabla.getValueAt(i, 2));
                    double[] macros = new double[7];
                    for(int j=0; j<7; j++) macros[j] = doubleVal(modeloTabla.getValueAt(i, j+3));
                    
                    if (com.nutrilern.controlador.ControladorComidas.registrarComida(user, al.getIdAlimento(), grams, macros)) {
                        final int f = i;
                        SwingUtilities.invokeLater(() -> modeloTabla.setValueAt(true, f, 10));
                    }
                }
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "¡Registro guardado con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                });
            }).start();
        });

        panelInferior.add(panelGestorFilas, BorderLayout.WEST);
        panelInferior.add(btnGuardar, BorderLayout.EAST);
        return panelInferior;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 13));
        btn.setForeground(TemaNutrix.PRIMARIO);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(8, 15, 8, 15)));
        return btn;
    }

    private void recargarDesplegableAlimentos() {
        List<Alimento> listaBD = AlimentoDAO.obtenerTodosLosAlimentos();
        JComboBox<Alimento> comboActualizado = new JComboBox<>();
        
        if (listaBD.isEmpty()) {
            alimentoPlaceholder.setNombre("BBDD Vacía - ¡Crea uno nuevo!");
        } else {
            alimentoPlaceholder.setNombre("Selecciona un alimento...");
        }
        
        comboActualizado.addItem(alimentoPlaceholder);
        for(Alimento a : listaBD) {
            comboActualizado.addItem(a);
        }

        tablaComidas.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(comboActualizado));
    }

    private double doubleVal(Object v) {
        if (v == null) return 0.0;
        if (v instanceof Number) return ((Number) v).doubleValue();
        try {
            return Double.parseDouble(v.toString());
        } catch (Exception e) {
            return 0.0;
        }
    }

    public void cargarDatosHoy() {
        if (ventanaPadre.getUsuarioLogueado() == null) return;
        
        modeloTabla.setRowCount(0); // Limpiamos tabla antes de cargar
        int idUser = ventanaPadre.getUsuarioLogueado().getId();
        List<Object[]> filasHoy = AlimentoDAO.obtenerRegistroDiarioHoy(idUser);
        
        for (Object[] fila : filasHoy) {
            // Buscamos el objeto ComboItem correcto de la categoría a partir del ID temporal
            int idCat = (int) fila[1];
            ComboItem itemCatCorrecto = comboCategoriasTabla.getItemAt(0);
            for(int i = 0; i < comboCategoriasTabla.getItemCount(); i++) {
                if(comboCategoriasTabla.getItemAt(i).getId() == idCat) {
                    itemCatCorrecto = comboCategoriasTabla.getItemAt(i);
                    break;
                }
            }
            fila[1] = itemCatCorrecto; 
            
            modeloTabla.addRow(fila);
        }
    }
}
