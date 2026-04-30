package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.Usuario;

/**
 * Este panel muestra la evolución nutricional del usuario.
 * Incluye un calendario interactivo y gráficos de barras y circulares
 * dibujados manualmente con Java 2D, conectados a datos reales de TiDB.
 */
public class PanelEvolucion extends JPanel {

    private VentanaPrincipal ventanaPadre;
    
    // Paleta de colores para mantener la estética Nutrix
    private final Color COLOR_VERDE_NUTRIX = new Color(34, 139, 34);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);

    // Componentes del calendario
    private JPanel panelCalendario;
    private JLabel lblMesAno;
    private YearMonth mesActual;

    // Componentes de gráficos (los guardamos para poder actualizarlos)
    private TarjetaGrafico graficoCalorias;
    private TarjetaGrafico graficoMacros;

    public PanelEvolucion(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        this.mesActual = YearMonth.now();
        
        setLayout(new BorderLayout());
        setBackground(COLOR_FONDO);

        add(crearCabecera(), BorderLayout.NORTH);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        // --- SECCIÓN CALENDARIO ---
        content.add(crearSeccionCalendario());
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- SECCIÓN GRÁFICOS ---
        content.add(crearSeccionGraficos());

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Carga los datos reales desde la base de datos de forma asíncrona.
     */
    public void cargarDatosReales() {
        Usuario user = ventanaPadre.getUsuarioLogueado();
        if (user == null) return;

        new Thread(() -> {
            // 1. Obtenemos calorías de los últimos 7 días
            int[] calSemana = AlimentoDAO.obtenerCaloriasUltimos7Dias(user.getId());
            
            // 2. Obtenemos macros de hoy
            double[] macrosHoyRaw = AlimentoDAO.obtenerMacrosHoy(user.getId());
            // Convertimos a porcentajes para el gráfico circular (si hay datos)
            int[] macrosHoy = new int[]{33, 33, 34}; // Por defecto si está vacío
            double total = macrosHoyRaw[0] + macrosHoyRaw[1] + macrosHoyRaw[2];
            if (total > 0) {
                macrosHoy[0] = (int) ((macrosHoyRaw[0] / total) * 100);
                macrosHoy[1] = (int) ((macrosHoyRaw[1] / total) * 100);
                macrosHoy[2] = 100 - macrosHoy[0] - macrosHoy[1];
            }

            // 3. Actualizamos la interfaz en el hilo de Swing
            SwingUtilities.invokeLater(() -> {
                graficoCalorias.actualizarDatos(calSemana);
                graficoMacros.actualizarDatos(macrosHoy);
            });
        }).start();
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(COLOR_VERDE_NUTRIX);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTit = new JLabel("Mi Evolución Nutricional", SwingConstants.CENTER);
        lblTit.setFont(new Font("Arial", Font.BOLD, 24));
        lblTit.setForeground(COLOR_TEXTO);
        header.add(lblTit, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);
        return header;
    }

    private JPanel crearSeccionCalendario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));
        container.setMaximumSize(new Dimension(1000, 400));

        JPanel controles = new JPanel(new BorderLayout());
        controles.setOpaque(false);

        JButton btnPrev = new JButton("<");
        btnPrev.addActionListener(e -> { mesActual = mesActual.minusMonths(1); actualizarCalendario(); });
        JButton btnNext = new JButton(">");
        btnNext.addActionListener(e -> { mesActual = mesActual.plusMonths(1); actualizarCalendario(); });

        lblMesAno = new JLabel("", SwingConstants.CENTER);
        lblMesAno.setFont(new Font("Arial", Font.BOLD, 18));
        
        controles.add(btnPrev, BorderLayout.WEST);
        controles.add(lblMesAno, BorderLayout.CENTER);
        controles.add(btnNext, BorderLayout.EAST);
        container.add(controles, BorderLayout.NORTH);

        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        panelDiasSemana.setOpaque(false);
        String[] nombresDias = {"Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"};
        for (String d : nombresDias) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(new Font("Arial", Font.BOLD, 12));
            l.setForeground(new Color(150, 150, 150));
            panelDiasSemana.add(l);
        }

        panelCalendario = new JPanel(new GridLayout(0, 7, 5, 5));
        panelCalendario.setOpaque(false);
        
        JPanel gridWrapper = new JPanel(new BorderLayout());
        gridWrapper.setOpaque(false);
        gridWrapper.add(panelDiasSemana, BorderLayout.NORTH);
        gridWrapper.add(panelCalendario, BorderLayout.CENTER);
        
        container.add(gridWrapper, BorderLayout.CENTER);

        actualizarCalendario();
        return container;
    }

    private void actualizarCalendario() {
        lblMesAno.setText(mesActual.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES")).toUpperCase() + " " + mesActual.getYear());
        panelCalendario.removeAll();

        LocalDate primeroMes = mesActual.atDay(1);
        int diaInicio = primeroMes.getDayOfWeek().getValue();
        int diasEnMes = mesActual.lengthOfMonth();

        for (int i = 1; i < diaInicio; i++) panelCalendario.add(new JLabel(""));

        for (int dia = 1; dia <= diasEnMes; dia++) {
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setFont(new Font("Arial", Font.PLAIN, 14));
            btnDia.setBackground(Color.WHITE);
            btnDia.setFocusPainted(false);
            btnDia.setBorder(new LineBorder(new Color(240, 240, 240)));
            btnDia.setPreferredSize(new Dimension(40, 40));
            
            if (mesActual.getYear() == LocalDate.now().getYear() && 
                mesActual.getMonth() == LocalDate.now().getMonth() && 
                dia == LocalDate.now().getDayOfMonth()) {
                btnDia.setBorder(new LineBorder(COLOR_VERDE_NUTRIX, 2));
                btnDia.setFont(new Font("Arial", Font.BOLD, 14));
            }
            panelCalendario.add(btnDia);
        }
        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    private JPanel crearSeccionGraficos() {
        JPanel container = new JPanel(new GridLayout(1, 2, 30, 0));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(1000, 300));

        // Inicializamos los gráficos con datos de "Cargando..." (0s)
        graficoCalorias = new TarjetaGrafico("Calorías Semanales", new int[]{0, 0, 0, 0, 0, 0, 0});
        graficoMacros = new TarjetaGrafico("Distribución de Macros (Hoy)", new int[]{33, 33, 34});

        container.add(graficoCalorias);
        container.add(graficoMacros);

        return container;
    }

    /**
     * Clase interna para el dibujo de gráficos.
     */
    class TarjetaGrafico extends JPanel {
        private int[] datos;

        public TarjetaGrafico(String titulo, int[] datos) {
            this.datos = datos;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(220, 220, 220), 1, true),
                new EmptyBorder(15, 15, 15, 15)
            ));
            setLayout(new BorderLayout());
            
            JLabel lbl = new JLabel(titulo);
            lbl.setFont(new Font("Arial", Font.BOLD, 15));
            lbl.setForeground(COLOR_TEXTO);
            add(lbl, BorderLayout.NORTH);
        }

        public void actualizarDatos(int[] nuevosDatos) {
            this.datos = nuevosDatos;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - 40;
            int h = getHeight() - 80;
            int x0 = 20;
            int y0 = 60;

            if (datos.length > 3) { // BARRAS
                int barW = w / datos.length - 10;
                int max = 0;
                for(int d : datos) if(d > max) max = d;
                if(max < 2000) max = 2000; // Escala mínima
                
                for (int i = 0; i < datos.length; i++) {
                    int barH = (int) ((datos[i] / (double) max) * h);
                    g2d.setColor(COLOR_VERDE_NUTRIX);
                    g2d.fillRect(x0 + i * (barW + 10), y0 + (h - barH), barW, barH);
                    g2d.setColor(new Color(120, 120, 120));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.drawString(String.valueOf(datos[i]), x0 + i * (barW + 10), y0 + (h - barH) - 5);
                }
            } else { // CIRCULAR
                int size = Math.min(w, h);
                int startAngle = 90;
                Color[] colores = {new Color(74, 144, 226), new Color(255, 127, 80), new Color(50, 205, 50)};
                
                for (int i = 0; i < datos.length; i++) {
                    int arcAngle = (int) (datos[i] * 3.6); 
                    g2d.setColor(colores[i]);
                    g2d.fillArc(x0 + (w - size) / 2, y0 + (h - size) / 2, size, size, startAngle, arcAngle);
                    startAngle += arcAngle;
                }
                // Leyenda pequeña
                g2d.setFont(new Font("Arial", Font.PLAIN, 11));
                g2d.setColor(colores[0]); g2d.drawString("HC", x0, y0 + h + 15);
                g2d.setColor(colores[1]); g2d.drawString("Prot", x0 + 40, y0 + h + 15);
                g2d.setColor(colores[2]); g2d.drawString("Grasas", x0 + 85, y0 + h + 15);
            }
        }
    }
}
