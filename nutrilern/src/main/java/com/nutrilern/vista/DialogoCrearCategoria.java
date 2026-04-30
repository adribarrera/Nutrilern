package com.nutrilern.vista;

import com.nutrilern.modelo.CategoriaDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class DialogoCrearCategoria extends JDialog {


    private JTextField txtNombre;

    public DialogoCrearCategoria(Frame parent) {
        super(parent, "Nueva Categoría", true);
        
        setSize(350, 220);
        setLocationRelativeTo(parent);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));
        panelPrincipal.setBackground(Color.WHITE);
        panelPrincipal.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel lblTitulo = new JLabel("Registrar Categoría");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(TemaNutrix.VERDE_NUTRIX);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtNombre = new JTextField();
        txtNombre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        JButton btnGuardar = new JButton("💾 Guardar");
        btnGuardar.setBackground(TemaNutrix.VERDE_NUTRIX);
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (CategoriaDAO.crearCategoria(nombre)) {
                JOptionPane.showMessageDialog(this, "Categoría creada con éxito.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear la categoría.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        panelPrincipal.add(lblTitulo);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(new JLabel("Nombre de la categoría:"));
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 5)));
        panelPrincipal.add(txtNombre);
        panelPrincipal.add(Box.createRigidArea(new Dimension(0, 20)));
        panelPrincipal.add(panelBotones);

        add(panelPrincipal);
    }
}