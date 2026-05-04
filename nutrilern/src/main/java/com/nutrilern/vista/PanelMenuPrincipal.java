package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import com.nutrilern.modelo.Usuario;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PanelMenuPrincipal extends JPanel {
    private VentanaPrincipal ventanaPadre;
    private Usuario usuario;


    private Image imagenFondo;

    // Componentes para actualizar datos reales
    private JLabel lblCalVal, lblProtVal, lblHCVal, lblFatVal, lblIMCVal;
    private JProgressBar barCal, barProt, barHC, barFat, barIMC;

    public PanelMenuPrincipal(VentanaPrincipal ventana, Usuario usuarioLogueado) {
        this.ventanaPadre = ventana;
        this.usuario = usuarioLogueado;

        setLayout(new BorderLayout());

        try {
            java.net.URL url = getClass().getResource("/images/fondoMenuPrincipal.jpg");
            if (url != null) {
                imagenFondo = javax.imageio.ImageIO.read(url);
            } else {
                setBackground(TemaNutrix.FONDO);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            setBackground(TemaNutrix.FONDO);
        }

        // --- HEADER ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(0, 80));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, TemaNutrix.GRIS_CLARO),
                BorderFactory.createEmptyBorder(0, 30, 0, 30)));

        JLabel lblLogo = new JLabel();
        try {
            java.net.URL urlLogo = getClass().getResource("/images/logo.png");
            if (urlLogo != null) {
                ImageIcon iconoOriginal = new ImageIcon(urlLogo);
                // Escalado proporcional para la barra superior (máximo 60px de alto)
                ImageIcon logoEscalado = TemaNutrix.escalarImagenProporcional(iconoOriginal, 150, 60);
                lblLogo.setIcon(logoEscalado);
            } else {
                lblLogo.setText("NUTRIX");
                lblLogo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 26));
                lblLogo.setForeground(TemaNutrix.PRIMARIO);
            }
        } catch (Exception e) {
            lblLogo.setText("NUTRIX");
        }
        header.add(lblLogo, BorderLayout.WEST);

        JPanel userActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 25));
        userActions.setOpaque(false);

        String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Invitado";
        String apellidos = (usuario != null && usuario.getApellidos() != null) ? usuario.getApellidos() : "";

        JLabel lblUser = new JLabel("Bienvenido, " + nombre + " " + apellidos);
        lblUser.setFont(new Font(TemaNutrix.FONT_NAME, Font.ITALIC, 14));
        lblUser.setForeground(TemaNutrix.TEXTO);
        userActions.add(lblUser);

        // Botón de Admin (Solo si el rol es ADMIN)
        if (usuario != null && "ADMIN".equalsIgnoreCase(usuario.getRol())) {
            JButton btnAdmin = new JButton("Gestionar Usuarios");
            btnAdmin.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 12));
            btnAdmin.setForeground(TemaNutrix.PRIMARIO);
            btnAdmin.setBackground(Color.WHITE);
            btnAdmin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaNutrix.PRIMARIO, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            btnAdmin.setFocusPainted(false);
            btnAdmin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btnAdmin.addActionListener(e -> ventanaPadre.cambiarPantalla("ADMIN_USUARIOS"));
            userActions.add(btnAdmin);
        }

        header.add(userActions, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // --- CONTENIDO SCROLLABLE ---
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 60, 30, 60));

        // Section: Resumen de hoy
        wrapper.add(crearSeccionResumenHoy());
        wrapper.add(Box.createRigidArea(new Dimension(0, 40)));

        // Section: Acceso directo (Grid)
        JLabel lblOps = new JLabel("Acciones Principales");
        lblOps.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        lblOps.setForeground(TemaNutrix.TEXTO);
        lblOps.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(lblOps);
        wrapper.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel gridPanel = new JPanel(new GridLayout(2, 2, 30, 30));
        gridPanel.setOpaque(false);
        gridPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        gridPanel.add(crearTarjetaMenu("Mis Comidas", "/images/misComidas.png", "Registra tu ingesta diaria y macros",
                () -> ventanaPadre.cambiarPantalla("COMIDAS")));
        gridPanel.add(crearTarjetaMenu("Mi Evolución", "/images/miEvo.png", "Gráficas de peso y composición", 
                () -> ventanaPadre.cambiarPantalla("EVOLUCION")));
        gridPanel.add(crearTarjetaMenu("Base de Alimentos", "/images/miGestor.png",
                "Base de datos nutricional completa", () -> ventanaPadre.cambiarPantalla("BASE_ALIMENTOS")));
        gridPanel.add(crearTarjetaMenu("Ajustes", "/images/ajustes.png", "Configura tu perfil y objetivos",
                () -> ventanaPadre.cambiarPantalla("AJUSTES")));

        wrapper.add(gridPanel);

        JScrollPane scrollPane = new JScrollPane(wrapper);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // --- CARGAR DATOS REALES ---
        cargarDatosReales();
    }

    private void cargarDatosReales() {
        if (usuario == null) return;
        
        new Thread(() -> {
            // Llamamos al controlador para obtener los datos procesados
            java.util.Map<String, Object> datos = com.nutrilern.controlador.ControladorVistas.obtenerDatosMenuPrincipal(usuario);
            
            double[] macrosHoy = (double[]) datos.get("macrosHoy");
            double[] macrosObjetivo = (double[]) datos.get("macrosObjetivo");
            double imc = (double) datos.get("imc");
            String clasificacion = (String) datos.get("imcClasificacion");

            int maxKcal = (int) macrosObjetivo[0];
            int maxHC = (int) macrosObjetivo[1];
            int maxProt = (int) macrosObjetivo[2];
            int maxFat = (int) macrosObjetivo[3];
            
            SwingUtilities.invokeLater(() -> {
                actualizarInterfaz(macrosHoy, maxKcal, maxHC, maxProt, maxFat, imc, clasificacion);
            });
        }).start();
    }

    private void actualizarInterfaz(double[] hoy, int mK, int mH, int mP, int mF, double imc, String clas) {
        double pctKcal = (hoy[0] / mK) * 100;
        
        lblCalVal.setText((int)hoy[0] + " kcal / " + mK);
        barCal.setValue((int)Math.min(100, pctKcal));
        
        lblHCVal.setText((int)hoy[1] + " g / " + mH);
        barHC.setValue((int)Math.min(100, (hoy[1]/mH)*100));

        lblProtVal.setText((int)hoy[2] + " g / " + mP);
        barProt.setValue((int)Math.min(100, (hoy[2]/mP)*100));

        lblFatVal.setText((int)hoy[3] + " g / " + mF);
        barFat.setValue((int)Math.min(100, (hoy[3]/mF)*100));

        lblIMCVal.setText(String.format("%.1f (%s)", imc, clas));
        int progresoIMC = (int) (((imc - 15) / (40 - 15)) * 100);
        barIMC.setValue(Math.min(100, Math.max(0, progresoIMC)));

        // --- LÓGICA DE MENSAJE DE ESTADO ---
        barProgresoGeneral.setValue((int)Math.min(100, pctKcal));
        if (pctKcal < 50) {
            lblProgresoMsg.setText("¡Ánimo! Aún tienes margen para completar tu objetivo de hoy.");
            barProgresoGeneral.setForeground(new Color(255, 167, 38)); // Naranja claro
        } else if (pctKcal <= 100) {
            lblProgresoMsg.setText("¡Vas genial! Estás muy cerca de cumplir tu meta diaria.");
            barProgresoGeneral.setForeground(TemaNutrix.PRIMARIO);
        } else {
            lblProgresoMsg.setText("Has superado tu objetivo hoy. ¡Ojo con los excesos!");
            barProgresoGeneral.setForeground(new Color(239, 83, 80)); // Rojo suave
        }
    }

    private JLabel lblProgresoMsg;
    private JProgressBar barProgresoGeneral;

    private JPanel crearSeccionResumenHoy() {
        JPanel seccion = new JPanel(new BorderLayout());
        seccion.setOpaque(false);
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Resumen de Hoy");
        titulo.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 22));
        titulo.setForeground(TemaNutrix.TEXTO);
        seccion.add(titulo, BorderLayout.NORTH);

        JPanel mainStats = new JPanel();
        mainStats.setLayout(new BoxLayout(mainStats, BoxLayout.Y_AXIS));
        mainStats.setOpaque(false);
        mainStats.setBorder(new EmptyBorder(15, 0, 0, 0));

        // --- TARJETA DE ESTADO GLOBAL ---
        JPanel cardEstado = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(TemaNutrix.GRIS_CLARO);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                g2.dispose();
            }
        };
        cardEstado.setOpaque(false);
        cardEstado.setLayout(new BorderLayout(20, 0));
        cardEstado.setBorder(new EmptyBorder(20, 25, 20, 25));
        cardEstado.setMaximumSize(new Dimension(2000, 100));

        lblProgresoMsg = new JLabel("Calculando tu progreso...");
        lblProgresoMsg.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 15));
        lblProgresoMsg.setForeground(TemaNutrix.TEXTO);

        barProgresoGeneral = new JProgressBar(0, 100);
        barProgresoGeneral.setPreferredSize(new Dimension(300, 15));
        barProgresoGeneral.setForeground(TemaNutrix.PRIMARIO);
        barProgresoGeneral.setBackground(new Color(240, 240, 240));
        barProgresoGeneral.setBorderPainted(false);

        cardEstado.add(lblProgresoMsg, BorderLayout.CENTER);
        cardEstado.add(barProgresoGeneral, BorderLayout.EAST);
        
        mainStats.add(cardEstado);
        mainStats.add(Box.createRigidArea(new Dimension(0, 20)));

        // --- GRID DE MINI CARDS ---
        JPanel cardsContainer = new JPanel(new GridLayout(1, 5, 12, 0)); 
        cardsContainer.setOpaque(false);

        JPanel pCal = crearStatMiniCard("Calorías", TemaNutrix.CALORIAS);
        lblCalVal = (JLabel) pCal.getClientProperty("valLabel");
        barCal = (JProgressBar) pCal.getClientProperty("progressBar");

        JPanel pHC = crearStatMiniCard("Carbo", TemaNutrix.CARBOHIDRATOS);
        lblHCVal = (JLabel) pHC.getClientProperty("valLabel");
        barHC = (JProgressBar) pHC.getClientProperty("progressBar");

        JPanel pProt = crearStatMiniCard("Protes", TemaNutrix.PROTEINAS);
        lblProtVal = (JLabel) pProt.getClientProperty("valLabel");
        barProt = (JProgressBar) pProt.getClientProperty("progressBar");

        JPanel pFat = crearStatMiniCard("Grasas", TemaNutrix.GRASAS);
        lblFatVal = (JLabel) pFat.getClientProperty("valLabel");
        barFat = (JProgressBar) pFat.getClientProperty("progressBar");

        JPanel pIMC = crearStatMiniCard("Mi IMC", new Color(147, 112, 219));
        lblIMCVal = (JLabel) pIMC.getClientProperty("valLabel");
        barIMC = (JProgressBar) pIMC.getClientProperty("progressBar");

        cardsContainer.add(pCal);
        cardsContainer.add(pHC);
        cardsContainer.add(pProt);
        cardsContainer.add(pFat);
        cardsContainer.add(pIMC);

        mainStats.add(cardsContainer);

        seccion.add(mainStats, BorderLayout.CENTER);
        return seccion;
    }

    private JPanel crearStatMiniCard(String label, Color colorBarra) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(TemaNutrix.GRIS_CLARO);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));

        JLabel val = new JLabel("0 / --"); // Texto inicial
        val.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 16));
        val.setForeground(TemaNutrix.TEXTO);

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setValue(0);
        bar.setForeground(colorBarra);
        bar.setBackground(new Color(240, 240, 240));
        bar.setBorderPainted(false);
        bar.setPreferredSize(new Dimension(0, 10));

        card.add(lbl);
        card.add(Box.createRigidArea(new Dimension(0, 5)));
        card.add(val);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(bar);

        // Guardamos las referencias en propiedades del componente para recuperarlas
        card.putClientProperty("valLabel", val);
        card.putClientProperty("progressBar", bar);

        return card;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imagenFondo != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            double imgAspect = (double) imagenFondo.getWidth(null) / imagenFondo.getHeight(null);
            double panelAspect = (double) getWidth() / getHeight();
            
            int drawW, drawH, x, y;
            if (panelAspect > imgAspect) {
                drawW = getWidth();
                drawH = (int) (drawW / imgAspect);
                x = 0;
                y = (getHeight() - drawH) / 2;
            } else {
                drawH = getHeight();
                drawW = (int) (drawH * imgAspect);
                x = (getWidth() - drawW) / 2;
                y = 0;
            }
            g2d.drawImage(imagenFondo, x, y, drawW, drawH, this);
            g2d.dispose();
        }
    }

    private JPanel crearTarjetaMenu(String titulo, String imagePath, String descripcion, Runnable accion) {
        JPanel tarjeta = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(TemaNutrix.GRIS_CLARO);
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 25, 25);
                g2.dispose();
            }
        };
        tarjeta.setOpaque(false);
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tarjeta.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel lblIcono = new JLabel();
        try {
            java.net.URL url = getClass().getResource(imagePath);
            if (url != null) {
                Image imgOriginal = javax.imageio.ImageIO.read(url);
                Image imgEscalada = imgOriginal.getScaledInstance(96, 96, Image.SCALE_SMOOTH);
                lblIcono.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception ex) { }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font(TemaNutrix.FONT_NAME, Font.BOLD, 20));
        lblTit.setForeground(TemaNutrix.TEXTO);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>");
        lblDesc.setFont(new Font(TemaNutrix.FONT_NAME, Font.PLAIN, 13));
        lblDesc.setForeground(TemaNutrix.GRIS_TEXTO);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setMaximumSize(new Dimension(220, 50));

        tarjeta.add(lblIcono);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 15)));
        tarjeta.add(lblTit);
        tarjeta.add(Box.createRigidArea(new Dimension(0, 10)));
        tarjeta.add(lblDesc);

        tarjeta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                tarjeta.setBackground(TemaNutrix.ACCENTO_CLARO);
                tarjeta.repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.repaint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (accion != null) accion.run();
            }
        });

        return tarjeta;
    }
}
