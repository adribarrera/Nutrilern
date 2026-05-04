package com.nutrilern.controlador;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Pruebas enfocadas en verificar que el sistema de encriptación de seguridad
 * funcione correctamente para el login y registro de usuarios.
 */
public class PruebasSeguridadAccesos {

    @Test
    public void testHashAndVerify() {
        String pass = "password123";
        String hash = GestorSeguridad.hashearPassword(pass);
        
        assertNotNull(hash);
        assertNotEquals(pass, hash);
        assertTrue(GestorSeguridad.verificarPassword(pass, hash));
        assertFalse(GestorSeguridad.verificarPassword("wrongpass", hash));
    }
}
