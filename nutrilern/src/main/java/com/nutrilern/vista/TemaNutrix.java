package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

public class TemaNutrix {
    // Colores Principales (Subtle & Premium)
    public static final Color VERDE_NUTRIX = new Color(46, 125, 50); // Bosque suave
    public static final Color VERDE_CLARO = new Color(232, 245, 233); // Fondo verde muy tenue
    public static final Color FONDO = new Color(250, 251, 252);
    public static final Color TEXTO = new Color(33, 37, 41);
    public static final Color BLANCO = Color.WHITE;
    public static final Color GRIS_CLARO = new Color(233, 236, 239);
    public static final Color GRIS_TEXTO = new Color(108, 117, 125);

    // Colores Macros (Pastel-ish but vibrant)
    public static final Color CALORIAS = new Color(255, 179, 0);
    public static final Color CARBOHIDRATOS = new Color(66, 165, 245);
    public static final Color PROTEINAS = new Color(239, 83, 80);
    public static final Color GRASAS = new Color(102, 187, 106);

    /**
     * Crea un botón con un estilo minimalista y moderno.
     */
    public static JButton crearBotonEstandar(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(VERDE_NUTRIX.darker());
                else if (getModel().isRollover()) g2.setColor(new Color(60, 140, 60));
                else g2.setColor(VERDE_NUTRIX);
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Crea el botón "Volver" estándar: verde, texto blanco en negrita,
     * ocupa toda la altura del header de forma cuadrada.
     */
    public static JButton crearBotonVolver(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2.setColor(VERDE_NUTRIX.darker());
                else if (getModel().isRollover()) g2.setColor(new Color(60, 140, 60));
                else g2.setColor(VERDE_NUTRIX);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new java.awt.Dimension(130, 80));
        return btn;
    }
}
