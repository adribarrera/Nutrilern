package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;

/**
 * Esta clase es el "cerebro" matemático de Nutrix.
 * Aquí es donde calculamos cuántas calorías y qué cantidad de cada nutriente 
 * necesita una persona basándonos en su cuerpo y en lo que quiere conseguir 
 * (adelgazar, mantenerse o ganar músculo).
 */
public class CalculadoraNutricional {

    /**
     * Este método hace toda la magia para saber cuántas calorías debe comer el usuario al día.
     * Usamos la fórmula de Mifflin-St Jeor (una de las más precisas hoy en día) para 
     * calcular el metabolismo basal y luego ajustamos según si es hombre o mujer 
     * y cuál es su objetivo personal.
     * 
     * @param usuario El perfil del usuario (peso, altura, edad, etc.)
     * @return Devuelve una lista de números: [Calorías Totales, gramos de Carbohidratos, gramos de Proteína, gramos de Grasa]
     */
    public static double[] calcularMacros(Usuario usuario) {
        double peso = usuario.getPesoInicial();
        double altura = usuario.getAltura();
        int edad = usuario.getEdad();
        String sexo = usuario.getSexo() != null ? usuario.getSexo().toUpperCase() : "M";

        // Primero calculamos el Metabolismo Basal (lo que el cuerpo quema solo por estar vivo)
        double tmb;
        if (sexo.equals("M") || sexo.equals("HOMBRE")) { 
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) + 5;
        } else { 
            tmb = (10 * peso) + (6.25 * altura) - (5 * edad) - 161;
        }

        // Le aplicamos un multiplicador de actividad física moderada 
        double caloriasMantenimiento = tmb * 1.375;

        // Ajustamos las calorías según el plan:
        // Objetivo 1: Perder peso (comemos 500 kcal menos)
        // Objetivo 3: Ganar músculo (comemos 300 kcal más)
        int idObjetivo = usuario.getIdObjetivo();
        double caloriasObjetivo = caloriasMantenimiento;

        if (idObjetivo == 1) {
            caloriasObjetivo -= 500;
        } else if (idObjetivo == 3) {
            caloriasObjetivo += 300;
        }

        // Seguridad: nunca bajamos de un mínimo vital para evitar problemas de salud
        if (sexo.equals("M") || sexo.equals("HOMBRE")) {
            if (caloriasObjetivo < 1500) caloriasObjetivo = 1500;
        } else {
            if (caloriasObjetivo < 1200) caloriasObjetivo = 1200;
        }

        // Ahora repartimos esas calorías en nutrientes:
        // Ponemos 2g de proteína por cada kilo de peso (para cuidar el músculo)
        // Ponemos 1g de grasa por cada kilo de peso (temas hormonales y salud)
        double gramosProt = peso * 2.0;
        double gramosGrasa = peso * 1.0;

        // Calculamos cuántas calorías nos quedan para los hidratos de carbono
        double kcalProt = gramosProt * 4;
        double kcalGrasa = gramosGrasa * 9;
        double kcalRestantes = caloriasObjetivo - (kcalProt + kcalGrasa);
        
        double gramosHC = kcalRestantes / 4.0;
        if (gramosHC < 0) gramosHC = 0;

        return new double[] { caloriasObjetivo, gramosHC, gramosProt, gramosGrasa };
    }

    /**
     * Calcula el IMC (Índice de Masa Corporal).
     * Es una forma rápida de ver si el peso de alguien es adecuado para su altura.
     */
    public static double calcularIMC(double peso, double alturaCm) {
        if (alturaCm <= 0) return 0;
        double alturaM = alturaCm / 100.0;
        return peso / (alturaM * alturaM);
    }

    /**
     * Traduce el número del IMC a una etiqueta que todos entendamos (Normal, Sobrepeso, etc.).
     * Lo basamos en los rangos oficiales de la Organización Mundial de la Salud (OMS).
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
