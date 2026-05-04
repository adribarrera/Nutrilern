package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;

/**
 * Cálculos matemáticos nutricionales para calorías y macros.
 */
public class CalculadoraNutricional {

    /**
     * Calcula calorías totales y distribución de macros según el perfil de usuario.
     */
    public static double[] calcularMacros(Usuario usuario) {
        double peso = usuario.getPesoInicial();
        double altura = usuario.getAltura();
        int edad = usuario.getEdad();
        String sexo = usuario.getSexo() != null ? usuario.getSexo().toUpperCase() : "M";

        // Cálculo de TMB usando Mifflin-St Jeor
        double tmb;
        if (sexo.equals("M") || sexo.equals("HOMBRE")) {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
        } else {
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
        }

        // Multiplicador de actividad (moderada)
        double caloriasMantenimiento = tmb * 1.375;

        // Ajuste por objetivo (1: Perder, 3: Ganar)
        int idObjetivo = usuario.getIdObjetivo();
        double caloriasObjetivo = caloriasMantenimiento;

        if (idObjetivo == 1) {
            caloriasObjetivo -= 500;
        } else if (idObjetivo == 3) {
            caloriasObjetivo += 300;
        }

        // Límites mínimos de seguridad
        if (sexo.equals("M") || sexo.equals("HOMBRE")) {
            if (caloriasObjetivo < 1500) caloriasObjetivo = 1500;
        } else {
            if (caloriasObjetivo < 1200) caloriasObjetivo = 1200;
        }

        // Distribución: 2g proteína/kg, 1g grasa/kg, resto hidratos
        double gramosProt = peso * 2.0;
        double gramosGrasa = peso * 1.0;

        double kcalProt = gramosProt * 4;
        double kcalGrasa = gramosGrasa * 9;
        double kcalRestantes = caloriasObjetivo - (kcalProt + kcalGrasa);

        double gramosHC = kcalRestantes / 4.0;
        if (gramosHC < 0) gramosHC = 0;

        return new double[] { caloriasObjetivo, gramosHC, gramosProt, gramosGrasa };
    }

    /**
     * Calcula el Índice de Masa Corporal.
     */
    public static double calcularIMC(double peso, double alturaCm) {
        if (alturaCm <= 0) return 0;
        double alturaM = alturaCm / 100.0;
        return peso / (alturaM * alturaM);
    }

    /**
     * Devuelve la clasificación según el IMC (OMS).
     */
    public static String getClasificacionIMC(double imc) {
        if (imc <= 0) return "Desconocido";
        if (imc < 18.5) return "Bajo peso";
        if (imc < 25.0) return "Normal";
        if (imc < 30.0) return "Sobrepeso";
        if (imc < 35.0) return "Obesidad I";
        if (imc < 40.0) return "Obesidad II";
        return "Obesidad III";
    }
}
