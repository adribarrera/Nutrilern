package com.nutrilern.vista;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.CategoriaDAO;
import com.nutrilern.modelo.ComboItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class DialogoCrearAlimento extends JDialog {



    // Campos del formulario
    private JTextField txtNombre, txtMarca, txtKcal;
    private JTextField txtProteinas, txtHidratos, txtGrasas, txtSaturadas, txtAzucar, txtSal;
    
    // El desplegable de categorías
    private JComboBox<ComboItem> comboCategorias; 
    
    // Un booleano para saber si el usuario guardó algo y debemos actualizar la tabla
    private boolean alimentoCreado = false; 

    public DialogoCrearAlimento(Frame parent) {
        super(parent, "Registrar Nuevo Alimento", true); // true = bloquea la ventana de atrás hasta que cierres esto
        
        setSize(450, 580);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));

        // 1. Título
        JLabel lblTitulo = new JLabel("Registrar Nuevo Alimento");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(TemaNutrix.VERDE_NUTRIX);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPrincipal.add(new JLabel("Añade los datos base por cada 100g de producto"));
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));

        // 2. Formulario de Datos
        JPanel panelForm = new JPanel(new GridLayout(10, 2, 10, 10));
        panelForm.setBackground(Color.WHITE);

        txtNombre = new JTextField();
        txtMarca = new JTextField();
        txtKcal = new JTextField();
        txtProteinas = new JTextField();
        txtHidratos = new JTextField();
        txtGrasas = new JTextField();
        txtSaturadas = new JTextField();
        txtAzucar = new JTextField();
        txtSal = new JTextField();

        panelForm.add(crearEtiqueta("Nombre (*):")); panelForm.add(txtNombre);
        
        comboCategorias = new JComboBox<>();
        // Llamamos al DAO para que nos traiga la lista de la BBDD
        Map<Integer, String> categoriasBD = CategoriaDAO.obtenerTodasLasCategorias();
        for (Map.Entry<Integer, String> entry : categoriasBD.entrySet()) {
            // Metemos cada categoría en la caja ComboItem
            comboCategorias.addItem(new ComboItem(entry.getKey(), entry.getValue()));
        }
        panelForm.add(crearEtiqueta("Categoría (*):")); panelForm.add(comboCategorias);
        // ------------------------------------------

        panelForm.add(crearEtiqueta("Marca:")); panelForm.add(txtMarca);
        panelForm.add(crearEtiqueta("Calorías (Kcal) (*):")); panelForm.add(txtKcal);
        panelForm.add(crearEtiqueta("Proteínas (g):")); panelForm.add(txtProteinas);
        panelForm.add(crearEtiqueta("Hidratos (g):")); panelForm.add(txtHidratos);
        panelForm.add(crearEtiqueta("  - Azúcares (g):")); panelForm.add(txtAzucar);
        panelForm.add(crearEtiqueta("Grasas (g):")); panelForm.add(txtGrasas);
        panelForm.add(crearEtiqueta("  - Saturadas (g):")); panelForm.add(txtSaturadas);
        panelForm.add(crearEtiqueta("Sal (g):")); panelForm.add(txtSal);

        panelPrincipal.add(panelForm);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 25)));

        // 3. Botones (Cancelar / Guardar)
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose()); // Cierra la ventana sin hacer nada

        JButton btnGuardar = new JButton("💾 Guardar Alimento");
        btnGuardar.setBackground(TemaNutrix.VERDE_NUTRIX);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        btnGuardar.addActionListener(e -> guardarAlimentoBD());

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelPrincipal.add(panelBotones);

        add(panelPrincipal);
    }

    private JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(TemaNutrix.TEXTO);
        return lbl;
    }

    private void guardarAlimentoBD() {
        // 1. Validaciones básicas
        if (txtNombre.getText().trim().isEmpty() || txtKcal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El Nombre y las Calorías son obligatorios.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 2. Creamos la caja (Objeto)
            Alimento nuevo = new Alimento();
            nuevo.setNombre(txtNombre.getText().trim());
            nuevo.setMarca(txtMarca.getText().trim());
            
            // Convertimos los textos a números. Si está vacío, le ponemos un 0.0
            nuevo.setKcal(parsearDouble(txtKcal.getText()));
            nuevo.setProteinas(parsearDouble(txtProteinas.getText()));
            nuevo.setHidratosCarbono(parsearDouble(txtHidratos.getText()));
            nuevo.setAzucares(parsearDouble(txtAzucar.getText()));
            nuevo.setGrasas(parsearDouble(txtGrasas.getText()));
            nuevo.setGrasasSaturadas(parsearDouble(txtSaturadas.getText()));
            nuevo.setSal(parsearDouble(txtSal.getText()));
            
            // Leemos el objeto ComboItem que el usuario ha dejado seleccionado
            ComboItem itemSeleccionado = (ComboItem) comboCategorias.getSelectedItem();
            
            if (itemSeleccionado != null) {
                // Le sacamos su ID oculto y se lo pasamos al Alimento
                nuevo.setIdCategoriaFk(itemSeleccionado.getId());
            } else {
                nuevo.setIdCategoriaFk(1); // Por si la BBDD está vacía y no hay nada seleccionado
            }

            // 3. Mandamos el DAO a guardarlo en TiDB
            boolean exito = AlimentoDAO.crearAlimentoGlobal(nuevo);

            if (exito) {
                JOptionPane.showMessageDialog(this, "¡Alimento guardado correctamente en la Base de Datos!");
                alimentoCreado = true; // Avisamos de que hubo éxito
                dispose(); // Cerramos esta ventanita emergente
            } else {
                JOptionPane.showMessageDialog(this, "Error de SQL al intentar guardar el alimento.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, introduce solo números válidos para los valores nutricionales (ej: 12.5).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Un método auxiliar para no repetir código si la celda está en blanco
    private double parsearDouble(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return 0.0;
        }
        // Cambiamos comas por puntos por si acaso
        return Double.parseDouble(texto.replace(",", ".").trim());
    }

    // Getter para que el panel principal sepa si tiene que recargar la tabla
    public boolean isAlimentoCreado() {
        return alimentoCreado;
    }
}