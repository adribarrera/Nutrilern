package com.nutrilern.controlador;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.nutrilern.modelo.Usuario;

/**
 * Pruebas enfocadas en verificar que los cálculos de IMC y de macronutrientes
 * (Calorías, HC, Proteínas, Grasas) sean correctos según el perfil del usuario.
 */
public class PruebasCalculosNutritivos {

    @Test
    public void testCalcularIMC() {
        double imc = CalculadoraNutricional.calcularIMC(70.0, 175.0);
        assertEquals(22.85, imc, 0.01, "El IMC para 70kg y 175cm debería ser ~22.85");
    }

    @Test
    public void testGetClasificacionIMC() {
        assertEquals("Normal", CalculadoraNutricional.getClasificacionIMC(22.0));
        assertEquals("Sobrepeso", CalculadoraNutricional.getClasificacionIMC(27.0));
        assertEquals("Bajo peso", CalculadoraNutricional.getClasificacionIMC(17.0));
        assertEquals("Obesidad I", CalculadoraNutricional.getClasificacionIMC(32.0));
    }

    @Test
    public void testCalcularMacrosHombreMantener() {
        Usuario u = new Usuario();
        u.setPeso(70.0);
        u.setAltura(175.0);
        u.setEdad(25);
        u.setSexo("HOMBRE");
        u.setIdObjetivo(2); // Mantener

        double[] macros = CalculadoraNutricional.calcularMacros(u);
        
        assertTrue(macros[0] > 2200 && macros[0] < 2400, "Calorías deberían estar en rango razonable");
        assertEquals(70.0 * 2.0, macros[2], 0.1, "Proteínas deberían ser 2g/kg");
        assertEquals(70.0 * 1.0, macros[3], 0.1, "Grasas deberían ser 1g/kg");
    }
}
