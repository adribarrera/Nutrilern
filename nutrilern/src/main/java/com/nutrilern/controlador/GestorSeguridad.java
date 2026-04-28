package com.nutrilern.controlador;

import org.mindrot.jbcrypt.BCrypt;

public class GestorSeguridad {

    /**
     * Coge una contraseña normal y la convierte en un hash
     * 
     * @param passwordPlana La contraseña que escribe el usuario
     * @return El hash
     */
    public static String hashearPassword(String passwordPlana) {
        // El "12" es el factor de trabajo (Work Factor).
        // Cuanto más alto, más seguro, pero más tarda en calcularse.
        return BCrypt.hashpw(passwordPlana, BCrypt.gensalt(12));
    }

    /**
     * Compara la contraseña que acaba de escribir el usuario en el Login
     * con el hash que tenemos guardado en la Base de Datos.
     * 
     * @param passwordLogin     La contraseña que intenta usar para entrar.
     * @param hashEnBaseDeDatos El hash guardado en la BBDD.
     * @return true si coinciden, false si se ha equivocado.
     */
    public static boolean verificarPassword(String passwordLogin, String hashEnBaseDeDatos) {
        try {
            return BCrypt.checkpw(passwordLogin, hashEnBaseDeDatos);
        } catch (Exception e) {
            // Por si el hash de la BBDD está corrupto o vacío
            return false;
        }
    }

    // --- MÉTODO TEMPORAL DE PRUEBA ---
    public static void main(String[] args) {
        String miPasswordSecreta = "Nutrix2026!";

        System.out.println("1. Contraseña original: " + miPasswordSecreta);

        // Hasheamos la contraseña
        String hashGenerado = hashearPassword(miPasswordSecreta);
        System.out.println("2. Hash a guardar en TiDB: " + hashGenerado);

        // Simulamos un intento de login CORRECTO
        System.out.println("\n¿Coincide con 'Nutrix2026!'? " +
                verificarPassword("Nutrix2026!", hashGenerado));

        // Simulamos un intento de login INCORRECTO
        System.out.println("¿Coincide con 'nutrix2026!'? (minúscula): " +
                verificarPassword("nutrix2026!", hashGenerado));
    }
}