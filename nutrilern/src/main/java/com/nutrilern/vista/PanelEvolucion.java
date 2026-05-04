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
import com.nutrilern.modelo.PesoDAO;
import java.util.List;

/**
 * Este panel muestra la evolución nutricional del usuario.
 * Incluye un calendario interactivo y gráficos de barras y circulares
 * dibujados manualmente con Java 2D, conectados a datos reales de TiDB.
 */
public class PanelEvolucion extends JPanel {

    private VentanaPrincipal ventanaPadre;
    
    // Paleta de colores para mantener la estética

    // Componentes del calendario
    private JPanel panelCalendario;
    private JLabel lblMesAno;
    private YearMonth mesActual;

    // Componentes de gráficos (los guardamos para poder actualizarlos)
    private TarjetaGrafico graficoCalorias;
    private TarjetaGrafico graficoMacros;
    private TarjetaGrafico graficoPeso;

    public PanelEvolucion(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        this.mesActual = YearMonth.now();
        
        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

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
        content.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // --- SECCIÓN PESO ---
        graficoPeso = new TarjetaGrafico("Evolución de Peso (kg)", new double[0]);
        content.add(graficoPeso);

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
            // Obtenemos todos los datos procesados desde el controlador
            java.util.Map<String, Object> datos = com.nutrilern.controlador.ControladorVistas.obtenerDatosEvolucion(user);
            
            int[] calSemana = (int[]) datos.get("caloriasSemana");
            int[] macrosHoy = (int[]) datos.get("macrosHoyPorcentaje");
            java.util.List<Object[]> historial = (java.util.List<Object[]>) datos.get("historialPeso");

            // Convertimos el historial a arrays para los gráficos
            double[] pesosArr = new double[historial.size()];
            String[] fechasArr = new String[historial.size()];
            for (int i = 0; i < historial.size(); i++) {
                pesosArr[i] = (double) historial.get(i)[0];
                fechasArr[i] = (String) historial.get(i)[1];
            }

            // Actualizamos la interfaz en el hilo de Swing
            SwingUtilities.invokeLater(() -> {
                graficoCalorias.actualizarDatos(calSemana);
                graficoMacros.actualizarDatos(macrosHoy);
                graficoPeso.actualizarDatosLineal(pesosArr, fechasArr);
            });
        }).start();
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = new JButton("← Volver");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 14));
        btnVolver.setForeground(TemaNutrix.VERDE_NUTRIX);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTit = new JLabel("Mi Evolución Nutricional", SwingConstants.CENTER);
        lblTit.setFont(new Font("Arial", Font.BOLD, 24));
        lblTit.setForeground(TemaNutrix.TEXTO);
        header.add(lblTit, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);
        return header;
    }

    private JPanel crearSeccionCalendario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
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
                btnDia.setBorder(new LineBorder(TemaNutrix.VERDE_NUTRIX, 2));
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

    class TarjetaGrafico extends JPanel {
        private double[] datos;
        private String[] etiquetas;

        public TarjetaGrafico(String titulo, int[] datosInt) {
            this.datos = new double[datosInt.length];
            for(int i=0; i<datosInt.length; i++) this.datos[i] = datosInt[i];
            init(titulo);
        }

        public TarjetaGrafico(String titulo, double[] datos) {
            this.datos = datos;
            init(titulo);
        }

        private void init(String titulo) {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(0, 250));
            setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(15, 15, 25, 15) // Más margen abajo para las fechas
            ));
            setLayout(new BorderLayout());
            
            JLabel lbl = new JLabel(titulo);
            lbl.setFont(new Font("Arial", Font.BOLD, 15));
            lbl.setForeground(TemaNutrix.TEXTO);
            add(lbl, BorderLayout.NORTH);
        }

        public void actualizarDatos(int[] nuevosDatos) {
            this.datos = new double[nuevosDatos.length];
            for(int i=0; i<nuevosDatos.length; i++) this.datos[i] = nuevosDatos[i];
            repaint();
        }

        public void actualizarDatos(double[] nuevosDatos) {
            this.datos = nuevosDatos;
            repaint();
        }
        
        public void actualizarDatosLineal(double[] nuevosDatos, String[] nuevasEtiquetas) {
            this.datos = nuevosDatos;
            this.etiquetas = nuevasEtiquetas;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (datos == null || datos.length == 0) {
                g.setColor(TemaNutrix.GRIS_TEXTO);
                g.drawString("Sin datos registrados", getWidth()/2 - 50, getHeight()/2);
                return;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth() - 60;
            int h = getHeight() - 100; // Espacio para etiquetas y título
            int x0 = 30;
            int y0 = 60;

            if (datos.length > 3 && datos.length <= 7) { // BARRAS (Calorías)
                int barW = w / datos.length - 10;
                double max = 0;
                for(double d : datos) if(d > max) max = d;
                if(max < 2000) max = 2000; 
                
                for (int i = 0; i < datos.length; i++) {
                    int barH = (int) ((datos[i] / max) * h);
                    g2d.setColor(TemaNutrix.VERDE_NUTRIX);
                    g2d.fillRect(x0 + i * (barW + 10), y0 + (h - barH), barW, barH);
                    g2d.setColor(TemaNutrix.GRIS_TEXTO);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.drawString(String.valueOf((int)datos[i]), x0 + i * (barW + 10), y0 + (h - barH) - 5);
                }
            } else if (datos.length == 3) { // CIRCULAR (Macros)
                int size = Math.min(w, h);
                int startAngle = 90;
                Color[] colores = {TemaNutrix.CARBOHIDRATOS, TemaNutrix.PROTEINAS, TemaNutrix.GRASAS};
                
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
            } else { // LÍNEA (Evolución de Peso)
                int pointW = w / (datos.length > 1 ? datos.length - 1 : 1);
                
                // Buscar min y max reales para la escala elástica
                double min = datos[0], max = datos[0];
                for(double d : datos) {
                    if(d < min) min = d;
                    if(d > max) max = d;
                }
                
                double range = max - min;
                if(range < 5) range = 5; // Escala mínima de 5kg para que no sea plana
                double padding = range * 0.2; // 20% de margen arriba y abajo
                double scaleMin = min - padding;
                double scaleMax = max + padding;
                double scaleRange = scaleMax - scaleMin;

                int[] xPoints = new int[datos.length];
                int[] yPoints = new int[datos.length];

                for (int i = 0; i < datos.length; i++) {
                    xPoints[i] = x0 + i * pointW;
                    yPoints[i] = y0 + h - (int) (((datos[i] - scaleMin) / scaleRange) * h);
                    
                    // Dibujar punto
                    g2d.setColor(TemaNutrix.VERDE_NUTRIX);
                    g2d.fillOval(xPoints[i] - 5, yPoints[i] - 5, 10, 10);
                    
                    // Valor del peso encima del punto
                    g2d.setColor(TemaNutrix.TEXTO);
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    String val = String.format("%.1f", datos[i]);
                    g2d.drawString(val, xPoints[i] - 12, yPoints[i] - 12);
                    
                    // Fecha debajo del punto (fuera del eje)
                    if (etiquetas != null && i < etiquetas.length) {
                        g2d.setColor(TemaNutrix.GRIS_TEXTO);
                        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                        g2d.drawString(etiquetas[i], xPoints[i] - 12, y0 + h + 25);
                    }
                }
                
                // Dibujar la línea conectora
                g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(new Color(34, 139, 34, 150)); // Verde semitransparente
                g2d.drawPolyline(xPoints, yPoints, datos.length);
            }
        }
    }
}
