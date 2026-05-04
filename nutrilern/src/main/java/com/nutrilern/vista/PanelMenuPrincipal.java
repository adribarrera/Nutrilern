package com.nutrilern.vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import com.nutrilern.modelo.Usuario;
import com.nutrilern.modelo.AlimentoDAO;
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

        JLabel lblLogo = new JLabel("NUTRIX");
        lblLogo.setFont(new Font("Arial", Font.BOLD, 26));
        lblLogo.setForeground(TemaNutrix.VERDE_NUTRIX);
        header.add(lblLogo, BorderLayout.WEST);

        JPanel userActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 25));
        userActions.setOpaque(false);

        String nombre = (usuario != null && usuario.getNombre() != null) ? usuario.getNombre() : "Invitado";
        String apellidos = (usuario != null && usuario.getApellidos() != null) ? usuario.getApellidos() : "";

        JLabel lblUser = new JLabel("Bienvenido, " + nombre + " " + apellidos);
        lblUser.setFont(new Font("Arial", Font.ITALIC, 14));
        lblUser.setForeground(TemaNutrix.TEXTO);
        userActions.add(lblUser);

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
        lblOps.setFont(new Font("Arial", Font.BOLD, 22));
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
            double[] macrosHoy = AlimentoDAO.obtenerMacrosHoy(usuario.getId());
            // [0]=kcal, [1]=hc, [2]=prot, [3]=fat

            // Calculamos los macros reales del usuario
            double[] macrosObjetivo = com.nutrilern.controlador.CalculadoraNutricional.calcularMacros(usuario);
            int maxKcal = (int) macrosObjetivo[0];
            int maxHC = (int) macrosObjetivo[1];
            int maxProt = (int) macrosObjetivo[2];
            int maxFat = (int) macrosObjetivo[3];
            
            SwingUtilities.invokeLater(() -> {
                lblCalVal.setText((int)macrosHoy[0] + " kcal / " + maxKcal);
                barCal.setValue((int)((macrosHoy[0]/maxKcal)*100));
                
                lblHCVal.setText((int)macrosHoy[1] + " g / " + maxHC);
                barHC.setValue((int)((macrosHoy[1]/maxHC)*100));

                lblProtVal.setText((int)macrosHoy[2] + " g / " + maxProt);
                barProt.setValue((int)((macrosHoy[2]/maxProt)*100));

                lblFatVal.setText((int)macrosHoy[3] + " g / " + maxFat);
                barFat.setValue((int)((macrosHoy[3]/maxFat)*100));

                // 5. Calculamos IMC
                double imc = com.nutrilern.controlador.CalculadoraNutricional.calcularIMC(usuario.getPesoInicial(), usuario.getAltura());
                String clasificacion = com.nutrilern.controlador.CalculadoraNutricional.getClasificacionIMC(imc);
                lblIMCVal.setText(String.format("%.1f (%s)", imc, clasificacion));
                
                // Normalizar barra IMC (de 15 a 40)
                int progresoIMC = (int) (((imc - 15) / (40 - 15)) * 100);
                barIMC.setValue(Math.min(100, Math.max(0, progresoIMC)));
            });
        }).start();
    }

    private JPanel crearSeccionResumenHoy() {
        JPanel seccion = new JPanel(new BorderLayout());
        seccion.setOpaque(false);
        seccion.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titulo = new JLabel("Resumen de Hoy");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(TemaNutrix.TEXTO);
        seccion.add(titulo, BorderLayout.NORTH);

        JPanel cardsContainer = new JPanel(new GridLayout(1, 5, 10, 0)); // 5 tarjetas ahora
        cardsContainer.setOpaque(false);
        cardsContainer.setBorder(new EmptyBorder(15, 0, 0, 0));

        // Creamos las tarjetas y guardamos referencias a los labels/bars
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

        JPanel pIMC = crearStatMiniCard("Mi IMC", new Color(147, 112, 219)); // Color violeta
        lblIMCVal = (JLabel) pIMC.getClientProperty("valLabel");
        barIMC = (JProgressBar) pIMC.getClientProperty("progressBar");

        cardsContainer.add(pCal);
        cardsContainer.add(pHC);
        cardsContainer.add(pProt);
        cardsContainer.add(pFat);
        cardsContainer.add(pIMC);

        seccion.add(cardsContainer, BorderLayout.CENTER);
        return seccion;
    }

    private JPanel crearStatMiniCard(String label, Color colorBarra) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(15, 15, 15, 15)));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        lbl.setForeground(new Color(100, 100, 100));

        JLabel val = new JLabel("0 / --"); // Texto inicial
        val.setFont(new Font("Arial", Font.BOLD, 16));
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
            g.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JPanel crearTarjetaMenu(String titulo, String imagePath, String descripcion, Runnable accion) {
        JPanel tarjeta = new JPanel();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                new EmptyBorder(25, 20, 25, 20)));

        JLabel lblIcono = new JLabel();
        try {
            java.net.URL url = getClass().getResource(imagePath);
            if (url != null) {
                Image imgOriginal = javax.imageio.ImageIO.read(url);
                Image imgEscalada = imgOriginal.getScaledInstance(64, 64, Image.SCALE_SMOOTH);
                lblIcono.setIcon(new ImageIcon(imgEscalada));
            }
        } catch (Exception ex) { }
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTit = new JLabel(titulo);
        lblTit.setFont(new Font("Arial", Font.BOLD, 20));
        lblTit.setForeground(TemaNutrix.TEXTO);
        lblTit.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel("<html><center>" + descripcion + "</center></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 13));
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
                tarjeta.setBackground(new Color(250, 255, 250));
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(TemaNutrix.VERDE_NUTRIX, 2, true),
                        new EmptyBorder(24, 19, 24, 19)));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                tarjeta.setBackground(Color.WHITE);
                tarjeta.setBorder(BorderFactory.createCompoundBorder(
                        new LineBorder(TemaNutrix.GRIS_CLARO, 1, true),
                        new EmptyBorder(25, 20, 25, 20)));
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (accion != null) accion.run();
            }
        });

        return tarjeta;
    }
}
