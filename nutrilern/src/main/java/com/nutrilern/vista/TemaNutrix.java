package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;

public class TemaNutrix {
    // Colores Principales
    public static final Color VERDE_NUTRIX = new Color(34, 139, 34);
    public static final Color FONDO = new Color(245, 247, 250);
    public static final Color TEXTO = new Color(50, 50, 50);
    public static final Color BLANCO = Color.WHITE;
    public static final Color GRIS_CLARO = new Color(230, 230, 230);
    public static final Color GRIS_TEXTO = new Color(120, 120, 120);

    // Colores Macros
    public static final Color CALORIAS = Color.ORANGE;
    public static final Color CARBOHIDRATOS = new Color(74, 144, 226); // Azul
    public static final Color PROTEINAS = new Color(255, 127, 80);    // Naranja rojizo
    public static final Color GRASAS = new Color(50, 205, 50);        // Verde claro

    // Botones Auxiliares Comunes
    public static JButton crearBotonEstandar(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(VERDE_NUTRIX);
        btn.setForeground(BLANCO);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
