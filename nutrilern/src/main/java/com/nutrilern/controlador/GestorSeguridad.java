package com.nutrilern.controlador;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Esta clase se encarga de que las contraseñas de los usuarios estén a salvo.
 * En lugar de guardar la contraseña tal cual (lo cual sería peligroso), 
 * la "encriptamos" usando un algoritmo llamado BCrypt.
 */
public class GestorSeguridad {

    /**
     * Transforma una contraseña de texto plano en un "hash" indescifrable.
     * Usamos un factor de seguridad de 12 para que sea muy difícil de hackear 
     * incluso con ordenadores potentes.
     * 
     * @param passwordPlana La contraseña que ha escrito el usuario.
     * @return Una cadena de texto larga y compleja para guardar en la base de datos.
     */
    public static String hashearPassword(String passwordPlana) {
        return BCrypt.hashpw(passwordPlana, BCrypt.gensalt(12));
    }

    /**
     * Comprueba si la contraseña que alguien introduce al entrar coincide con 
     * la que tenemos guardada. 
     * 
     * @param passwordLogin La contraseña que el usuario acaba de escribir.
     * @param hashEnBaseDeDatos El "código" complejo que tenemos guardado en la base de datos.
     * @return true si la contraseña es la correcta; false si no coinciden.
     */
    public static boolean verificarPassword(String passwordLogin, String hashEnBaseDeDatos) {
        try {
            return BCrypt.checkpw(passwordLogin, hashEnBaseDeDatos);
        } catch (Exception e) {
            // Si el código guardado está mal o vacío, devolvemos falso por seguridad.
            return false;
        }
    }
}