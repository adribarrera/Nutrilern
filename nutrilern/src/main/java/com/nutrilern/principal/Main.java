package com.nutrilern.principal;

import javax.swing.SwingUtilities;
import com.nutrilern.vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        
        // Es una buena práctica en Java arrancar las interfaces gráficas 
        // dentro de este "hilo" especial para evitar cuelgues
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // 1. Creamos la ventana principal (el marco de vuestra app)
                VentanaPrincipal ventana = new VentanaPrincipal();
                
                // 2. La hacemos visible en la pantalla
                ventana.setVisible(true);
            }
        });
        
    }
}