package com.nutrilern.controlador;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utilidades para el cifrado y verificación de contraseñas con BCrypt.
 */
public class GestorSeguridad {

    /**
     * Genera un hash seguro para la contraseña.
     */
    public static String hashearPassword(String passwordPlana) {
        return BCrypt.hashpw(passwordPlana, BCrypt.gensalt(12));
    }

    /**
     * Verifica si la contraseña coincide con el hash almacenado.
     */
    public static boolean verificarPassword(String passwordLogin, String hashEnBaseDeDatos) {
        try {
            return BCrypt.checkpw(passwordLogin, hashEnBaseDeDatos);
        } catch (Exception e) {
            return false;
        }
    }
}