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
    
    private final Color COLOR_VERDE_NUTRIX = new Color(34, 139, 34);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    private JTable tablaComidas;
    private DefaultTableModel modeloTabla;
    private Alimento alimentoPlaceholder;
    
    private JComboBox<ComboItem> comboCategoriasTabla;

    public PanelMisComidas(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearZonaTabla(), BorderLayout.CENTER);
        add(crearControlesInferiores(), BorderLayout.SOUTH);
    }

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
        });

        panelDerecho.add(btnCrearAlimento);
        panelDerecho.add(btnCrearCategoria);

        header.add(panelIzquierdo, BorderLayout.WEST);
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
                Object valorElegido = modeloTabla.getValueAt(fila, 0);
                
                if (valorElegido instanceof Alimento) {
                    Alimento al = (Alimento) valorElegido;
                    
                    if (al.getIdAlimento() != 0) {
                        for(int i = 0; i < comboCategoriasTabla.getItemCount(); i++) {
                            ComboItem itemCat = comboCategoriasTabla.getItemAt(i);
                            if(itemCat.getId() == al.getIdCategoriaFk()) {
                                modeloTabla.setValueAt(itemCat, fila, 1);
                                break;
                            }
                        }
                        
                        modeloTabla.setValueAt(100.0, fila, 2); 
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

        // OCULTAMOS LA COLUMNA DE LÓGICA INTERNA DE LA VISTA DEL USUARIO
        tablaComidas.getColumnModel().removeColumn(tablaComidas.getColumnModel().getColumn(10));

        JScrollPane scrollTabla = new JScrollPane(tablaComidas);
        scrollTabla.getViewport().setBackground(Color.WHITE);
        scrollTabla.setBorder(new LineBorder(new Color(220, 220, 220), 1, true));

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
            // Añadimos 'false' al final porque es una fila nueva y sin guardar
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

        JButton btnGuardar = new JButton("💾 Guardar Registro Diario");
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 15));
        btnGuardar.setBackground(COLOR_VERDE_NUTRIX);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setPreferredSize(new Dimension(250, 45));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnGuardar.addActionListener(e -> {
            int numFilas = modeloTabla.getRowCount();
            if (numFilas == 0) {
                JOptionPane.showMessageDialog(this, "La tabla está vacía. Añade alimentos antes de guardar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int idUsuario = ventanaPadre.getUsuarioLogueado().getId();
            java.sql.Date fechaHoy = new java.sql.Date(System.currentTimeMillis());
            
            int guardadasCorrectamente = 0;
            int ignoradas = 0; // Para contar las que ya estaban guardadas
            int errores = 0;

            for (int i = 0; i < numFilas; i++) {
                try {
                    // VERIFICAMOS LA COLUMNA OCULTA (Índice 10)
                    boolean yaGuardado = (boolean) modeloTabla.getValueAt(i, 10);
                    if (yaGuardado) {
                        ignoradas++;
                        continue; 
                    }

                    Object objAl = modeloTabla.getValueAt(i, 0);
                    if (!(objAl instanceof Alimento) || ((Alimento) objAl).getIdAlimento() == 0) continue;
                    Alimento al = (Alimento) objAl;

                    double gramos = doubleVal(modeloTabla.getValueAt(i, 2));
                    double kcal = doubleVal(modeloTabla.getValueAt(i, 3));
                    double prot = doubleVal(modeloTabla.getValueAt(i, 4));
                    double hc = doubleVal(modeloTabla.getValueAt(i, 5));
                    double gra = doubleVal(modeloTabla.getValueAt(i, 6));
                    double sat = doubleVal(modeloTabla.getValueAt(i, 7));
                    double azu = doubleVal(modeloTabla.getValueAt(i, 8));
                    double sal = doubleVal(modeloTabla.getValueAt(i, 9));

                    boolean exito = AlimentoDAO.registrarFilaDiario(
                        idUsuario, al.getIdAlimento(), gramos, "General", fechaHoy,
                        kcal, prot, hc, gra, sat, azu, sal
                    );

                    if (exito) {
                        guardadasCorrectamente++;
                        // Marcamos como guardada para que no se duplique luego
                        modeloTabla.setValueAt(true, i, 10);
                    } else {
                        errores++;
                    }
                } catch (Exception ex) {
                    errores++;
                }
            }

            if (guardadasCorrectamente > 0) {
                JOptionPane.showMessageDialog(this, "¡Éxito! Se han añadido " + guardadasCorrectamente + " nuevos registros a tu diario de hoy.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
            } else if (ignoradas > 0 && errores == 0) {
                JOptionPane.showMessageDialog(this, "No hay alimentos nuevos que guardar. Todo está actualizado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }
            
            if (errores > 0) {
                JOptionPane.showMessageDialog(this, "Hubo errores al guardar " + errores + " filas. Por favor, revisa tu conexión.", "Error", JOptionPane.ERROR_MESSAGE);
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

    // MÉTODO PARA CARGAR LOS DATOS DE HOY AL ENTRAR AL PANEL
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