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
 * PANEL DE EVOLUCIÓN
 * -------------------
 * Esta pantalla es el centro analítico del usuario. Permite:
 * 1. Consultar un calendario interactivo para ver detalles de días pasados.
 * 2. Visualizar gráficas de barras (Calorías semanales).
 * 3. Analizar la distribución de macros de un día específico (Gráfica circular).
 * 4. Observar la tendencia de peso a largo plazo (Gráfica lineal).
 * 
 * TODO el sistema de gráficas se dibuja MIEMBRO A MIEMBRO con Java 2D (paintComponent),
 * lo que permite total control estético sin depender de librerías externas.
 */
public class PanelEvolucion extends JPanel {

    private VentanaPrincipal ventanaPadre;
    
    // Paleta de colores para mantener la estética

    // Componentes del calendario
    private JPanel panelCalendario;
    private JLabel lblMesAno;
    private YearMonth mesActual;
    private LocalDate diaSeleccionado;

    // Componentes de gráficos (los guardamos para poder actualizarlos)
    private TarjetaGrafico graficoCalorias;
    private TarjetaGrafico graficoMacros;
    private TarjetaGrafico graficoPeso;

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
            // Obtenemos todos los datos procesados desde el controlador, pasando la fecha seleccionada
            java.util.Map<String, Object> datos = com.nutrilern.controlador.ControladorVistas.obtenerDatosEvolucion(user, diaSeleccionado);
            
            int[] calSemana = (int[]) datos.get("caloriasSemana");
            int[] macrosHoy = (int[]) datos.get("macrosHoyPorcentaje");
            @SuppressWarnings("unchecked")
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
                graficoMacros.setTitulo("Macros: " + diaSeleccionado.getDayOfMonth() + "/" + diaSeleccionado.getMonthValue());
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
            
            LocalDate fechaBoton = mesActual.atDay(dia);
            
            if (fechaBoton.equals(LocalDate.now())) {
                btnDia.setBorder(new LineBorder(TemaNutrix.VERDE_NUTRIX, 2));
                btnDia.setFont(new Font("Arial", Font.BOLD, 14));
            }
            
            if (fechaBoton.equals(diaSeleccionado)) {
                btnDia.setBackground(new Color(230, 245, 230));
                btnDia.setForeground(TemaNutrix.VERDE_NUTRIX);
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
        private JLabel lblTitulo;

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
                new EmptyBorder(15, 15, 35, 15) // Más margen abajo para las fechas
            ));
            setLayout(new BorderLayout());
            
            lblTitulo = new JLabel(titulo);
            lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
            lblTitulo.setForeground(TemaNutrix.TEXTO);
            add(lblTitulo, BorderLayout.NORTH);
        }

        public void setTitulo(String t) {
            lblTitulo.setText(t);
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
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (datos == null || datos.length == 0) {
                g2d.setColor(TemaNutrix.GRIS_TEXTO);
                g2d.setFont(new Font("Arial", Font.ITALIC, 14));
                g2d.drawString("No hay registros para este día", getWidth()/2 - 90, getHeight()/2 + 10);
                return;
            }

            int w = getWidth() - 60;
            int h = getHeight() - 100; // Espacio para etiquetas y título
            int x0 = 30;
            int y0 = 60;

            if (etiquetas != null) { // --- MODO: GRÁFICA LINEAL (Evolución de Peso) ---
                int pointW = w / (datos.length > 1 ? datos.length - 1 : 1); // Distancia entre puntos
                
                // Buscamos min y max reales para ajustar la escala vertical dinámicamente
                double min = datos[0], max = datos[0];
                for(double d : datos) {
                    if(d < min) min = d;
                    if(d > max) max = d;
                }
                
                double range = max - min;
                if(range < 5) range = 5; // Aseguramos un rango mínimo para que no se vea plana
                double padding = range * 0.2; // Añadimos margen superior e inferior
                double scaleMin = min - padding;
                double scaleMax = max + padding;
                double scaleRange = scaleMax - scaleMin;

                int[] xPoints = new int[datos.length];
                int[] yPoints = new int[datos.length];

                for (int i = 0; i < datos.length; i++) {
                    // Calculamos coordenadas X e Y proporcionales
                    xPoints[i] = x0 + i * pointW;
                    yPoints[i] = y0 + h - (int) (((datos[i] - scaleMin) / scaleRange) * h);
                    
                    // 1. Dibujamos el punto (Ovalo verde)
                    g2d.setColor(TemaNutrix.VERDE_NUTRIX);
                    g2d.fillOval(xPoints[i] - 5, yPoints[i] - 5, 10, 10);
                    
                    // 2. Escribimos el valor del peso encima del punto
                    g2d.setColor(TemaNutrix.TEXTO);
                    g2d.setFont(new Font("Arial", Font.BOLD, 11));
                    g2d.drawString(String.format("%.1f", datos[i]), xPoints[i] - 12, yPoints[i] - 12);
                    
                    // 3. Escribimos la fecha debajo del eje X
                    if (i < etiquetas.length) {
                        g2d.setColor(TemaNutrix.TEXTO);
                        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                        g2d.drawString(etiquetas[i], xPoints[i] - 12, y0 + h + 20);
                    }
                }
                
                // 4. Conectamos todos los puntos con una línea gruesa semitransparente
                g2d.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.setColor(new Color(34, 139, 34, 150));
                g2d.drawPolyline(xPoints, yPoints, datos.length);

            } else if (datos.length == 3) { // --- MODO: GRÁFICA CIRCULAR (Macros) ---
                int size = Math.min(w, h) - 20;
                int startAngle = 90; // Empezamos a las 12 en punto
                Color[] colores = {TemaNutrix.CARBOHIDRATOS, TemaNutrix.PROTEINAS, TemaNutrix.GRASAS};
                String[] nombres = {"HC", "Prot", "Grasas"};
                
                int centerX = x0 + w / 2;
                int centerY = y0 + h / 2;

                for (int i = 0; i < datos.length; i++) {
                    // Convertimos el porcentaje (0-100) a grados (0-360)
                    int arcAngle = (int) (datos[i] * 3.6); 
                    g2d.setColor(colores[i]);
                    g2d.fillArc(centerX - size/2, centerY - size/2, size, size, startAngle, arcAngle);
                    
                    // Si el trozo es suficientemente grande, dibujamos el % dentro
                    if (datos[i] > 5) {
                        double midAngle = Math.toRadians(startAngle + arcAngle / 2.0);
                        int labelR = size / 3;
                        int labelX = (int) (centerX + labelR * Math.cos(midAngle)) - 10;
                        int labelY = (int) (centerY - labelR * Math.sin(midAngle)) + 5;
                        
                        g2d.setColor(Color.WHITE);
                        g2d.setFont(new Font("Arial", Font.BOLD, 12));
                        g2d.drawString((int)datos[i] + "%", labelX, labelY);
                    }
                    
                    startAngle += arcAngle; // Avanzamos el ángulo para el siguiente trozo
                }
                // Dibujamos la leyenda (cuadraditos de colores con nombres)
                g2d.setFont(new Font("Arial", Font.PLAIN, 11));
                for(int i=0; i<3; i++) {
                    g2d.setColor(colores[i]);
                    g2d.fillRect(x0 + i*60, y0 + h + 15, 10, 10);
                    g2d.setColor(TemaNutrix.TEXTO);
                    g2d.drawString(nombres[i], x0 + i*60 + 15, y0 + h + 25);
                }
            } else { // --- MODO: GRÁFICA DE BARRAS (Calorías) ---
                int barW = w / datos.length - 10; // Ancho dinámico de barra
                double max = 0;
                for(double d : datos) if(d > max) max = d;
                if(max < 2000) max = 2000; // Escala mínima fija para referencia visual
                
                for (int i = 0; i < datos.length; i++) {
                    int barH = (int) ((datos[i] / max) * h); // Altura proporcional
                    g2d.setColor(TemaNutrix.VERDE_NUTRIX);
                    g2d.fillRect(x0 + i * (barW + 10), y0 + (h - barH), barW, barH);
                    
                    // Escribimos el número de calorías justo encima de la barra
                    g2d.setColor(TemaNutrix.GRIS_TEXTO);
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    g2d.drawString(String.valueOf((int)datos[i]), x0 + i * (barW + 10), y0 + (h - barH) - 5);
                }
            }
        }
    }
}
