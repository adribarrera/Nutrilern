package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

public class PanelMenuPrincipal extends JPanel {
    private VentanaPrincipal ventanaPadre;

    public PanelMenuPrincipal(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(34, 139, 34));
        header.setPreferredSize(new Dimension(0, 70));
        header.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        JLabel lblTitulo = new JLabel("NUTRIX - Menú Principal");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        header.add(lblTitulo, BorderLayout.WEST);

        JButton btnSalir = new JButton("Cerrar Sesión");
        btnSalir.addActionListener(e -> ventanaPadre.cambiarPantalla("LOGIN"));
        header.add(btnSalir, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // Contenido Principal (Botones de acceso rápido)
        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        gridPanel.setBackground(new Color(245, 245, 245));

        gridPanel.add(crearBotonMenu("Mis Comidas", "📝", "Registra lo que comes cada día"));
        gridPanel.add(crearBotonMenu("Mi Evolución", "📈", "Consulta tus progresos físicos"));
        gridPanel.add(crearBotonMenu("Base de Alimentos", "🍎", "Busca información nutricional"));
        gridPanel.add(crearBotonMenu("Configuración", "⚙️", "Ajusta tus datos personales"));

        add(gridPanel, BorderLayout.CENTER);
    }

    private JPanel crearBotonMenu(String titulo, String emoji, String desc) {
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Arial", Font.PLAIN, 40));
        lblEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 18));
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(desc);
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPanel.add(lblEmoji);
        btnPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        btnPanel.add(lblTit);
        btnPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        btnPanel.add(lblDesc);

        // Simular interactividad
        btnPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return btnPanel;
    }
}
