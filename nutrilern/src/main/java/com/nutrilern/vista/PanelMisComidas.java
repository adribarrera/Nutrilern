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
    
    // Colores corporativos
    private final Color COLOR_VERDE_NUTRIX = new Color(34, 139, 34);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    // Componentes de la tabla
    private JTable tablaComidas;
    private DefaultTableModel modeloTabla;
    private Alimento alimentoPlaceholder;
    
    // Guardamos el combo de categorías a nivel de clase para poder usarlo en el autocompletado
    private JComboBox<ComboItem> comboCategoriasTabla;

    public PanelMisComidas(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaTabla(), BorderLayout.CENTER);
        add(crearControlesInferiores(), BorderLayout.SOUTH);
    }

    // =================================================================================
    // 1. CABECERA
    // =================================================================================
    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                new EmptyBorder(15, 30, 15, 30)));

        JPanel panelIzquierdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panelIzquierdo.setBackground(Color.WHITE);

        JButton btnVolver = new JButton("← Menú");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(COLOR_VERDE_NUTRIX);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));

        JLabel lblTitulo = new JLabel("Mi Registro Diario");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO);

        panelIzquierdo.add(btnVolver);
        panelIzquierdo.add(lblTitulo);

        JPanel panelDerecho = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
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
            // NOTA: Si creamos una categoría, deberíamos recargar el combo de categorías de la tabla
            // Lo dejaremos para afinar detalles luego si quieres.
        });

        panelDerecho.add(btnCrearAlimento);
        panelDerecho.add(btnCrearCategoria);

        header.add(panelIzquierdo, BorderLayout.WEST);
        header.add(panelDerecho, BorderLayout.EAST);

        return header;
    }

    // =================================================================================
    // 2. EL "EXCEL" (JTABLE) 
    // =================================================================================
    private JPanel crearZonaTabla() {
        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.setOpaque(false);
        panelCentral.setBorder(new EmptyBorder(20, 30, 0, 30));

        // AÑADIDA LA COLUMNA DE CATEGORÍA
        String[] columnas = {"Alimento (Doble clic)", "Categoría", "Gramos", "Kcal", "Prot (g)", "HC (g)", "Grasas (g)", "Sat. (g)", "Azúcar (g)", "Sal (g)"};
        
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true; 
            }
        };

        tablaComidas = new JTable(modeloTabla);
        tablaComidas.setRowHeight(35); 
        tablaComidas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaComidas.setSelectionBackground(new Color(230, 245, 230)); 
        tablaComidas.setSelectionForeground(COLOR_TEXTO);
        
        JTableHeader th = tablaComidas.getTableHeader();
        th.setFont(new Font("Arial", Font.BOLD, 13));
        th.setBackground(COLOR_VERDE_NUTRIX);
        th.setForeground(Color.WHITE);
        th.setPreferredSize(new Dimension(100, 40));
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 2; i < tablaComidas.getColumnCount(); i++){ // Centramos desde Gramos en adelante
            tablaComidas.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // --- COLUMNA 0: DESPLEGABLE DE ALIMENTOS ---
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

        // --- COLUMNA 1: DESPLEGABLE DE CATEGORÍAS ---
        comboCategoriasTabla = new JComboBox<>();
        comboCategoriasTabla.addItem(new ComboItem(0, "Sin Categoría")); // Opción por defecto
        Map<Integer, String> categoriasBD = CategoriaDAO.obtenerTodasLasCategorias();
        for (Map.Entry<Integer, String> entry : categoriasBD.entrySet()) {
            comboCategoriasTabla.addItem(new ComboItem(entry.getKey(), entry.getValue()));
        }
        tablaComidas.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboCategoriasTabla));
        tablaComidas.getColumnModel().getColumn(1).setPreferredWidth(130);

        // --- EL CEREBRO DEL EXCEL (AUTOCOMPLETADO ACTUALIZADO) ---
        modeloTabla.addTableModelListener(e -> {
            if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 0) {
                int fila = e.getFirstRow();
                Object valorElegido = modeloTabla.getValueAt(fila, 0);
                
                if (valorElegido instanceof Alimento) {
                    Alimento al = (Alimento) valorElegido;
                    
                    if (al.getIdAlimento() != 0) {
                        
                        // 1. Buscamos y autocompletamos la Categoría (Columna 1)
                        for(int i = 0; i < comboCategoriasTabla.getItemCount(); i++) {
                            ComboItem itemCat = comboCategoriasTabla.getItemAt(i);
                            if(itemCat.getId() == al.getIdCategoriaFk()) {
                                modeloTabla.setValueAt(itemCat, fila, 1);
                                break;
                            }
                        }
                        
                        // 2. Autocompletamos los macros (Ojo, ahora están desplazados una columna)
                        modeloTabla.setValueAt(100.0, fila, 2); // Gramos
                        modeloTabla.setValueAt(al.getKcal(), fila, 3);
                        modeloTabla.setValueAt(al.getProteinas(), fila, 4);
                        modeloTabla.setValueAt(al.getHidratosCarbono(), fila, 5);
                        modeloTabla.setValueAt(al.getGrasas(), fila, 6);
                        modeloTabla.setValueAt(al.getGrasasSaturadas(), fila, 7);
                        modeloTabla.setValueAt(al.getAzucares(), fila, 8);
                        modeloTabla.setValueAt(al.getSal(), fila, 9);
                    }
                }
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tablaComidas);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        return panelCentral;
    }

    // =================================================================================
    // 3. CONTROLES INFERIORES
    // =================================================================================
    private JPanel crearControlesInferiores() {
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setOpaque(false);
        panelInferior.setBorder(new EmptyBorder(20, 30, 30, 30));

        JPanel panelGestorFilas = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelGestorFilas.setOpaque(false);

        JButton btnAñadirFila = crearBotonSecundario("+ Añadir Fila");
        btnAñadirFila.addActionListener(e -> {
            // Añadimos una fila con la categoría vacía por defecto
            ComboItem categoriaVacia = comboCategoriasTabla.getItemAt(0); 
            modeloTabla.addRow(new Object[]{alimentoPlaceholder, categoriaVacia, 0, 0, 0, 0, 0, 0, 0, 0});
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

        JButton btnGuardar = new JButton("💾 Guardar Registro Diario");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 15));
        btnGuardar.setBackground(COLOR_VERDE_NUTRIX);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(250, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> {
            int filas = modeloTabla.getRowCount();
            if(filas == 0) {
                JOptionPane.showMessageDialog(this, "La tabla está vacía. Añade alimentos antes de guardar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Próximamente: Recorriendo " + filas + " filas y guardando en TiDB...");
            }
        });

        panelInferior.add(panelGestorFilas, BorderLayout.WEST);
        panelInferior.add(btnGuardar, BorderLayout.EAST);

        return panelInferior;
    }

    private JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(COLOR_VERDE_NUTRIX);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(8, 15, 8, 15)));
        return btn;
    }

    // =================================================================================
    // MÉTODOS DE ACTUALIZACIÓN
    // =================================================================================
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
}