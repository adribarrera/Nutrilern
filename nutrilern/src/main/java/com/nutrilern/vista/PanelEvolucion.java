package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import com.nutrilern.modelo.Usuario;

/**
 * Vista de evolución con gráficos de calorías, macros y peso.
 */
public class PanelEvolucion extends JPanel {

    private VentanaPrincipal ventanaPadre;
    private JPanel panelCalendario;
    private JLabel lblMesAno;
    private YearMonth mesActual;
    private LocalDate diaSeleccionado;

    private TarjetaGrafico graficoCalorias;
    private TarjetaGrafico graficoMacros;
    private TarjetaGrafico graficoPeso;

    /**
     * Constructor del panel de evolución.
     * @param ventana Referencia a la ventana principal.
     */
    public PanelEvolucion(VentanaPrincipal ventana) {
        this.ventanaPadre = ventana;
        this.mesActual = YearMonth.now();
        this.diaSeleccionado = LocalDate.now();

        setLayout(new BorderLayout());
        setBackground(TemaNutrix.FONDO);

        add(crearCabecera(), BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(20, 30, 20, 30));

        content.add(crearSeccionCalendario());
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        content.add(crearSeccionGraficos());
        content.add(Box.createRigidArea(new Dimension(0, 30)));

        graficoPeso = new TarjetaGrafico("Evolución de Peso (kg)", new double[0]);
        content.add(graficoPeso);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(20);
        add(scroll, BorderLayout.CENTER);
    }

    /**
     * Carga datos de evolución de forma asíncrona.
     */
    public void cargarDatosReales() {
        Usuario user = ventanaPadre.getUsuarioLogueado();
        if (user == null) return;

        new Thread(() -> {
            java.util.Map<String, Object> datos = com.nutrilern.controlador.ControladorVistas
                    .obtenerDatosEvolucion(user, diaSeleccionado);

            int[] calSemana = (int[]) datos.get("caloriasSemana");
            int[] macrosHoy = (int[]) datos.get("macrosHoyPorcentaje");
            @SuppressWarnings("unchecked")
            java.util.List<Object[]> historial = (java.util.List<Object[]>) datos.get("historialPeso");

            double[] pesosArr = new double[historial.size()];
            String[] fechasArr = new String[historial.size()];
            for (int i = 0; i < historial.size(); i++) {
                pesosArr[i] = (double) historial.get(i)[0];
                fechasArr[i] = (String) historial.get(i)[1];
            }

            SwingUtilities.invokeLater(() -> {
                graficoCalorias.actualizarDatos(calSemana);
                graficoMacros.setTitulo("Distribución de Macros (Hoy)");
                graficoMacros.actualizarDatos(macrosHoy);
                graficoPeso.actualizarDatosLineal(pesosArr, fechasArr);
            });
        }).start();
    }

    /**
     * Crea la cabecera del panel con el botón de volver y el título.
     * @return Panel de cabecera.
     */
    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO));

        JButton btnVolver = TemaNutrix.crearBotonVolver("← Volver");
        btnVolver.addActionListener(e -> ventanaPadre.cambiarPantalla("MENU"));
        header.add(btnVolver, BorderLayout.WEST);

        JLabel lblTit = new JLabel("Mi Evolución Nutricional", SwingConstants.CENTER);
        lblTit.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 24));
        lblTit.setForeground(TemaNutrix.TEXTO);
        header.add(lblTit, BorderLayout.CENTER);

        header.add(Box.createRigidArea(new Dimension(150, 0)), BorderLayout.EAST);
        return header;
    }

    /**
     * Crea la sección que contiene el calendario interactivo.
     * @return Panel con el calendario.
     */
    private JPanel crearSeccionCalendario() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(20, 20, 20, 20)));
        container.setMaximumSize(new Dimension(1000, 400));

        JPanel controles = new JPanel(new BorderLayout());
        controles.setOpaque(false);

        JButton btnPrev = new JButton("<");
        btnPrev.addActionListener(e -> {
            mesActual = mesActual.minusMonths(1);
            actualizarCalendario();
        });
        JButton btnNext = new JButton(">");
        btnNext.addActionListener(e -> {
            mesActual = mesActual.plusMonths(1);
            actualizarCalendario();
        });

        lblMesAno = new JLabel("", SwingConstants.CENTER);
        lblMesAno.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 18));

        controles.add(btnPrev, BorderLayout.WEST);
        controles.add(lblMesAno, BorderLayout.CENTER);
        controles.add(btnNext, BorderLayout.EAST);
        container.add(controles, BorderLayout.NORTH);

        JPanel panelDiasSemana = new JPanel(new GridLayout(1, 7));
        panelDiasSemana.setOpaque(false);
        String[] nombresDias = { "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom" };
        for (String d : nombresDias) {
            JLabel l = new JLabel(d, SwingConstants.CENTER);
            l.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 12));
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

    /**
     * Regenera los componentes visuales del calendario basándose en el mes seleccionado.
     */
    private void actualizarCalendario() {
        lblMesAno.setText(
                mesActual.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("es-ES")).toUpperCase() + " "
                        + mesActual.getYear());
        panelCalendario.removeAll();

        LocalDate primeroMes = mesActual.atDay(1);
        int diaInicio = primeroMes.getDayOfWeek().getValue();
        int diasEnMes = mesActual.lengthOfMonth();

        for (int i = 1; i < diaInicio; i++) panelCalendario.add(new JLabel(""));

        for (int dia = 1; dia <= diasEnMes; dia++) {
            JButton btnDia = new JButton(String.valueOf(dia));
            btnDia.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 14));
            btnDia.setBackground(Color.WHITE);
            btnDia.setFocusPainted(false);
            btnDia.setBorder(new LineBorder(new Color(240, 240, 240)));
            btnDia.setPreferredSize(new Dimension(40, 40));
            btnDia.setCursor(new Cursor(Cursor.HAND_CURSOR));

            LocalDate fechaBoton = mesActual.atDay(dia);

            if (fechaBoton.equals(LocalDate.now())) {
                btnDia.setBorder(new LineBorder(TemaNutrix.PRIMARIO, 2));
                btnDia.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 14));
            }

            if (fechaBoton.equals(diaSeleccionado)) {
                btnDia.setBackground(TemaNutrix.ACCENTO_CLARO);
                btnDia.setForeground(TemaNutrix.ACCENTO);
            }

            int diaClick = dia;
            btnDia.addActionListener(e -> {
                diaSeleccionado = mesActual.atDay(diaClick);
                actualizarCalendario();
                cargarDatosReales();
            });

            panelCalendario.add(btnDia);
        }
        panelCalendario.revalidate();
        panelCalendario.repaint();
    }

    /**
     * Crea el contenedor para las tarjetas de gráficos (calorías y macros).
     * @return Panel con los gráficos.
     */
    private JPanel crearSeccionGraficos() {
        JPanel container = new JPanel(new GridLayout(1, 2, 30, 0));
        container.setOpaque(false);
        container.setMaximumSize(new Dimension(1000, 300));

        graficoCalorias = new TarjetaGrafico("Calorías Semanales", new int[] { 0, 0, 0, 0, 0, 0, 0 });
        graficoMacros = new TarjetaGrafico("Distribución de Macros (Hoy)", new int[] { 0, 0, 0 });

        container.add(graficoCalorias);
        container.add(graficoMacros);
        return container;
    }

    /**
     * Componente para la representación visual de datos.
     */
    class TarjetaGrafico extends JPanel {
        private double[] datos;
        private String[] etiquetas;
        private JLabel lblTitulo;

        public TarjetaGrafico(String titulo, int[] datosInt) {
            this.datos = new double[datosInt.length];
            for (int i = 0; i < datosInt.length; i++) this.datos[i] = datosInt[i];
            init(titulo);
        }

        public TarjetaGrafico(String titulo, double[] datos) {
            this.datos = datos;
            init(titulo);
        }

        private void init(String titulo) {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(300, 250));
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                    new EmptyBorder(15, 20, 20, 20)));
            setLayout(new BorderLayout());

            lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 15));
            lblTitulo.setForeground(TemaNutrix.TEXTO);
            add(lblTitulo, BorderLayout.NORTH);
        }

        public void setTitulo(String t) { lblTitulo.setText(t); }

        public void actualizarDatos(int[] nuevosDatos) {
            this.datos = new double[nuevosDatos.length];
            for (int i = 0; i < nuevosDatos.length; i++) this.datos[i] = nuevosDatos[i];
            this.etiquetas = null;
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
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Fondo blanco redondeado para la tarjeta
            g2d.setColor(Color.WHITE);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2d.setColor(TemaNutrix.GRIS_CLARO);
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

            // Verificar si hay datos significativos
            boolean tieneDatos = false;
            if (datos != null && datos.length > 0) {
                for (double d : datos) if (d > 0) { tieneDatos = true; break; }
            }

            if (!tieneDatos) {
                g2d.setColor(TemaNutrix.GRIS_TEXTO);
                g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.ITALIC, 13));
                g2d.drawString("Sin datos registrados", getWidth() / 2 - 60, getHeight() / 2 + 10);
                g2d.dispose();
                return;
            }

            // Margen para el dibujo de la gráfica
            int x0 = 40, y0 = 60;
            int w = getWidth() - 80;
            int h = getHeight() - 120;

            if (etiquetas != null) {
                dibujarGraficaLineal(g2d, x0, y0, w, h);
            } else if (datos.length == 3) {
                dibujarGraficaCircular(g2d, x0, y0, w, h);
            } else {
                dibujarGraficaBarras(g2d, x0, y0, w, h);
            }
            g2d.dispose();
        }

        private void dibujarGraficaLineal(Graphics2D g2d, int x0, int y0, int w, int h) {
            double max = 0, min = 1000;
            for (double d : datos) {
                if (d > max) max = d;
                if (d < min) min = d;
            }
            max += 5; min -= 5;
            
            int n = datos.length;
            int pointW = (n > 1) ? w / (n - 1) : 0;
            int[] xPoints = new int[n], yPoints = new int[n];

            for (int i = 0; i < n; i++) {
                xPoints[i] = x0 + (n > 1 ? i * pointW : w / 2);
                yPoints[i] = y0 + h - (int) (((datos[i] - min) / (max - min)) * h);
                
                // Punto
                g2d.setColor(TemaNutrix.PRIMARIO);
                g2d.fillOval(xPoints[i] - 4, yPoints[i] - 4, 8, 8);
                
                // Etiqueta de fecha (abajo)
                if (etiquetas != null && i < etiquetas.length) {
                    g2d.setColor(TemaNutrix.GRIS_TEXTO);
                    g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 10));
                    g2d.drawString(etiquetas[i], xPoints[i] - 15, y0 + h + 25);
                }
                
                // Valor (arriba del punto)
                g2d.setColor(TemaNutrix.TEXTO);
                g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 10));
                g2d.drawString(String.format("%.1f", datos[i]), xPoints[i] - 12, yPoints[i] - 10);
            }
            
            if (n > 1) {
                g2d.setStroke(new BasicStroke(2.5f));
                g2d.setColor(new Color(TemaNutrix.PRIMARIO.getRed(), TemaNutrix.PRIMARIO.getGreen(), TemaNutrix.PRIMARIO.getBlue(), 120));
                g2d.drawPolyline(xPoints, yPoints, n);
            }
        }

        private void dibujarGraficaCircular(Graphics2D g2d, int x0, int y0, int w, int h) {
            int size = Math.min(w, h - 20);
            int startAngle = 90;
            Color[] colores = { TemaNutrix.CARBOHIDRATOS, TemaNutrix.PROTEINAS, TemaNutrix.GRASAS };
            String[] nombres = { "Carbo", "Prot", "Grasas" };
            int centerX = x0 + w / 2, centerY = y0 + size / 2;

            for (int i = 0; i < datos.length; i++) {
                int arcAngle = (int) (datos[i] * 3.6);
                g2d.setColor(colores[i]);
                g2d.fillArc(centerX - size / 2, centerY - size / 2, size, size, startAngle, -arcAngle);
                
                // Leyenda horizontal inferior
                int lx = x0 + i * (w/3);
                int ly = y0 + size + 25;
                g2d.fillRect(lx, ly, 12, 12);
                g2d.setColor(TemaNutrix.TEXTO);
                g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 10));
                g2d.drawString(nombres[i] + " (" + (int)datos[i] + "%)", lx + 16, ly + 10);
                
                startAngle -= arcAngle;
            }
            
            // Efecto Donut
            g2d.setColor(Color.WHITE);
            int inner = size / 2;
            g2d.fillOval(centerX - inner / 2, centerY - inner / 2, inner, inner);
        }

        private void dibujarGraficaBarras(Graphics2D g2d, int x0, int y0, int w, int h) {
            int barW = (w / datos.length) - 10;
            double max = 0;
            for (double d : datos) if (d > max) max = d;
            max = Math.max(max, 2000); // Mínimo 2000 kcal de escala

            // Generar etiquetas dinámicas basadas en el día actual
            String[] nombresDias = {"L", "M", "X", "J", "V", "S", "D"};
            String[] etiquetasDias = new String[7];
            int hoy = java.time.LocalDate.now().getDayOfWeek().getValue(); // 1=Lunes, 7=Domingo
            
            for (int i = 0; i < 7; i++) {
                // El índice i=6 es hoy, i=5 ayer, etc.
                // Calculamos qué día de la semana corresponde a cada posición de la barra
                int diaSemanaIdx = (hoy - 1 - (6 - i) + 7 * 10) % 7;
                etiquetasDias[i] = nombresDias[diaSemanaIdx];
            }

            for (int i = 0; i < datos.length; i++) {
                int barH = (int) ((datos[i] / max) * h);
                int bx = x0 + i * (barW + 10);
                int by = y0 + h - barH;

                // Barra de fondo (guía)
                g2d.setColor(new Color(245, 245, 245));
                g2d.fillRoundRect(bx, y0, barW, h, 8, 8);

                // Barra real
                g2d.setColor(TemaNutrix.PRIMARIO);
                g2d.fillRoundRect(bx, by, barW, barH, 8, 8);

                // Etiquetas de días
                g2d.setColor(TemaNutrix.GRIS_TEXTO);
                g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 11));
                g2d.drawString(etiquetasDias[i], bx + barW / 2 - 4, y0 + h + 25);
                
                // Valor numérico (si es > 0)
                if (datos[i] > 0) {
                    g2d.setColor(TemaNutrix.TEXTO);
                    g2d.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 9));
                    String val = String.valueOf((int)datos[i]);
                    g2d.drawString(val, bx + barW/2 - (val.length() * 3), by - 5);
                }
            }
        }
    }
}
