package com.nutrilern.vista;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

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

                if (getModel().isPressed())
                    g2.setColor(PRIMARIO.darker());
                else if (getModel().isRollover())
                    g2.setColor(PRIMARIO.brighter());
                else
                    g2.setColor(PRIMARIO);

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

                if (getModel().isPressed())
                    g2.setColor(ACCENTO.darker());
                else if (getModel().isRollover())
                    g2.setColor(ACCENTO.brighter());
                else
                    g2.setColor(ACCENTO);

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

    /**
     * Obtiene el icono personalizado para los diálogos de la aplicación.
     */
    public static ImageIcon obtenerIconoDialogo() {
        try {
            java.net.URL url = TemaNutrix.class.getResource("/images/logoDialogos.png");
            if (url != null) {
                ImageIcon iconoOriginal = new ImageIcon(url);
                // Aumentamos a 96px para mejor calidad y usamos escalado suave
                Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(96, 96, Image.SCALE_SMOOTH);
                return new ImageIcon(imagenEscalada);
            }
        } catch (Exception e) {
            System.err.println("Error al cargar el icono de diálogos: " + e.getMessage());
        }
        return null;
    }

    /**
     * Utilidad para escalar una imagen manteniendo su relación de aspecto con
     * máxima nitidez.
     * Implementa escalado multi-paso para evitar pixelación en imágenes grandes.
     */
    public static ImageIcon escalarImagenProporcional(ImageIcon original, int anchoMax, int altoMax) {
        Image img = original.getImage();
        int anchoOriginal = img.getWidth(null);
        int altoOriginal = img.getHeight(null);

        double ratio = (double) anchoOriginal / altoOriginal;
        int nuevoAncho = anchoMax;
        int nuevoAlto = (int) (nuevoAncho / ratio);

        if (nuevoAlto > altoMax) {
            nuevoAlto = altoMax;
            nuevoAncho = (int) (nuevoAlto * ratio);
        }

        // Si la imagen es mucho más grande que el destino, escalamos en pasos para
        // mantener nitidez
        BufferedImage tempImg = toBufferedImage(img);
        int w = anchoOriginal;
        int h = altoOriginal;

        while (w > nuevoAncho * 2 && h > nuevoAlto * 2) {
            w /= 2;
            h /= 2;
            tempImg = aplicarEscaladoCalidad(tempImg, w, h);
        }

        return new ImageIcon(aplicarEscaladoCalidad(tempImg, nuevoAncho, nuevoAlto));
    }

    private static BufferedImage aplicarEscaladoCalidad(Image img, int w, int h) {
        BufferedImage dimg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = dimg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
        return dimg;
    }

    private static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage)
            return (BufferedImage) img;
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        return bimage;
    }
}
