package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;

public class CalculadoraNutricional {

    /**
     * Calcula los macros diarios (Kcal, HC, Prot, Grasas) basados en los datos del
     * usuario.
     * Utiliza la fórmula revisada de Harris-Benedict para el Metabolismo Basal.
     * 
     * @param usuario El objeto usuario con peso, altura, edad, sexo y objetivo.
     * @return Array con [Kcal_Totales, Kcal_HC, Kcal_Prot, Kcal_Grasas]
     */
    public static double[] calcularMacros(Usuario usuario) {
        double peso = usuario.getPesoInicial();
        double altura = usuario.getAltura();
        int edad = usuario.getEdad();
        String sexo = usuario.getSexo() != null ? usuario.getSexo().toUpperCase() : "M";

        // 1. Tasa Metabólica Basal (TMB) - Ecuación de Mifflin-St Jeor o Harris
        // Benedict
        double tmb;
        if (sexo.equals("M") || sexo.equals("HOMBRE")) { // Hombre
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
        } else { // Mujer
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
        }

        // 2. Multiplicador de actividad física
        // Asumimos un factor moderado/ligero estándar para la app de momento: 1.375
        double caloriasMantenimiento = tmb * 1.375;

        // 3. Ajuste según el objetivo
        int idObjetivo = usuario.getIdObjetivo();
        double caloriasObjetivo = caloriasMantenimiento;

        if (idObjetivo == 1) {
            // Perder Peso (-500 kcal)
            caloriasObjetivo -= 500;
        } else if (idObjetivo == 3) {
            // Ganar Músculo (+300 kcal)
            caloriasObjetivo += 300;
        }

        // Nunca dejar que las calorías bajen de un umbral
        if (sexo.equals("M") || sexo.equals("HOMBRE")) {
            if (caloriasObjetivo < 1500)
                caloriasObjetivo = 1500;
        } else {
            if (caloriasObjetivo < 1200)
                caloriasObjetivo = 1200;
        }

        // 4. Repartición de Macros (En gramos)
        // Proteína: 2.0g por Kg de peso
        double gramosProt = peso * 2.0;
        // Grasa: 1.0g por Kg de peso
        double gramosGrasa = peso * 1.0;

        // Pasamos a calorías (1g Prot = 4 kcal, 1g Grasa = 9 kcal)
        double kcalProt = gramosProt * 4;
        double kcalGrasa = gramosGrasa * 9;

        // Hidratos: el resto de calorías (1g HC = 4 kcal)
        double kcalRestantes = caloriasObjetivo - (kcalProt + kcalGrasa);
        double gramosHC = kcalRestantes / 4.0;

        if (gramosHC < 0) {
            // Caso extremo donde prot+grasa superan las calorias objetivo
            gramosHC = 0;
            kcalRestantes = 0;
        }

        // Devolvemos los macros para que encaje con la interfaz
        return new double[] { caloriasObjetivo, gramosHC, gramosProt, gramosGrasa };
    }

    /**
     * Calcula el Índice de Masa Corporal (IMC).
     * IMC = Peso / Altura^2 (en metros)
     */
    public static double calcularIMC(double peso, double alturaCm) {
        if (alturaCm <= 0) return 0;
        double alturaM = alturaCm / 100.0;
        return peso / (alturaM * alturaM);
    }

    /**
     * Devuelve la clasificación de la OMS para un valor de IMC.
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
