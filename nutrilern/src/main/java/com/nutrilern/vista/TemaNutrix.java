package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

/**
 * Definición de colores, fuentes y estilos globales.
 */
public class TemaNutrix {
    
    // Paleta de colores principal
    public static final Color PRIMARIO = new Color(55, 71, 79);      
    public static final Color ACCENTO = new Color(255, 112, 67);     
    public static final Color ACCENTO_CLARO = new Color(255, 234, 221); 
    public static final Color FONDO = new Color(241, 246, 248);      
    public static final Color TEXTO = new Color(38, 50, 56);         
    public static final Color BLANCO = Color.WHITE;
    public static final Color GRIS_CLARO = new Color(207, 216, 220); 
    public static final Color GRIS_TEXTO = new Color(84, 110, 122);  

    public static final String FONT_NAME = "Segoe UI";

    // Colores para gráficos nutricionales
    public static final Color CALORIAS = new Color(255, 160, 0);     
    public static final Color CARBOHIDRATOS = new Color(30, 136, 229); 
    public static final Color PROTEINAS = new Color(216, 27, 96);    
    public static final Color GRASAS = new Color(124, 179, 66);      

    /**
     * Crea un botón con el estilo visual de la aplicación.
     */
    public static JButton crearBotonEstandar(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) g2.setColor(PRIMARIO.darker());
                else if (getModel().isRollover()) g2.setColor(PRIMARIO.brighter());
                else g2.setColor(PRIMARIO);
                
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btn.setFont(new Font(FONT_NAME, Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    /**
     * Crea un botón de retorno para la navegación.
     */
    public static JButton crearBotonVolver(String texto) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) g2.setColor(ACCENTO.darker());
                else if (getModel().isRollover()) g2.setColor(ACCENTO.brighter());
                else g2.setColor(ACCENTO);
                
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font(FONT_NAME, Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new java.awt.Dimension(140, 80));
        return btn;
    }
}
