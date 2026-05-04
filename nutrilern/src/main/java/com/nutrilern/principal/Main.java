package com.nutrilern.principal;

import javax.swing.SwingUtilities;
import com.nutrilern.vista.VentanaPrincipal;

/**
 * Punto de entrada de la aplicación.
 */
public class Main {

    public static void main(String[] args) {
        // Arrancar la interfaz en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaPrincipal ventana = new VentanaPrincipal();
                ventana.setVisible(true);
            }
        });

    }
}