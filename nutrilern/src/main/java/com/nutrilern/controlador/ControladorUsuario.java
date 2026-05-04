package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;
import com.nutrilern.modelo.UsuarioDAO;
import com.nutrilern.modelo.PesoDAO;
import java.security.SecureRandom;

/**
 * Gestiona la lógica de autenticación, registro y verificación de usuarios.
 */
public class ControladorUsuario {

    private static final SecureRandom random = new SecureRandom();

    /**
     * Intenta iniciar sesión con las credenciales proporcionadas.
     * 
     * @return El objeto Usuario si tiene éxito, null en caso contrario.
     */
    public static Usuario autenticar(String email, String password) {
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            return null;
        }
        return UsuarioDAO.iniciarSesion(email, password);
    }

    /**
     * Genera un código numérico de 8 dígitos para la verificación por email.
     */
    public static String generarCodigoVerificacion() {
        int numero = random.nextInt(90000000) + 10000000;
        return String.valueOf(numero);
    }

    /**
     * Envía el código de verificación al email del usuario.
     */
    public static boolean enviarCodigo(String email, String codigo) {
        return ServicioCorreo.enviarCodigoVerificacion(email, codigo);
    }

    /**
     * Registra un nuevo usuario en la base de datos.
     */
    public static boolean registrarNuevoUsuario(Usuario usuario) {
        return UsuarioDAO.registrarUsuario(usuario);
    }

    /**
     * Actualiza los datos físicos del usuario.
     */
    public static boolean actualizarDatosFisicos(int id, int edad, double peso, double altura) {
        if (UsuarioDAO.actualizarDatosFisicos(id, edad, peso, altura)) {
            PesoDAO.registrarPeso(id, peso);
            return true;
        }
        return false;
    }

    /**
     * Actualiza el objetivo nutricional del usuario.
     */
    public static boolean actualizarObjetivo(int id, int idObj) {
        return UsuarioDAO.actualizarObjetivo(id, idObj);
    }

    /**
     * Actualiza el email del usuario.
     */
    public static boolean actualizarEmail(int id, String email) {
        if (!esEmailValido(email))
            return false;
        return UsuarioDAO.actualizarEmail(id, email);
    }

    /**
     * Actualiza la contraseña del usuario.
     */
    public static boolean actualizarPassword(int id, String password) {
        if (!esPasswordValida(password))
            return false;
        String hash = GestorSeguridad.hashearPassword(password);
        return UsuarioDAO.actualizarPassword(id, hash);
    }

    /**
     * Elimina permanentemente la cuenta del usuario.
     */
    public static boolean eliminarCuenta(int id) {
        return UsuarioDAO.eliminarUsuario(id);
    }

    /**
     * Valida el formato del email.
     */
    public static boolean esEmailValido(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    }

    /**
     * Valida que la contraseña cumpla los requisitos mínimos (mínimo 8 caracteres).
     */
    public static boolean esPasswordValida(String password) {
        return password != null && password.length() >= 8;
    }

    /**
     * Comprueba si un email existe en el sistema y devuelve su ID.
     */
    public static int obtenerIdPorEmail(String email) {
        if (!esEmailValido(email))
            return -1;
        return UsuarioDAO.obtenerIdPorEmail(email);
    }
}
