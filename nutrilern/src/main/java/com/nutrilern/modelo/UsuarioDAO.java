package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import com.nutrilern.controlador.GestorSeguridad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Registra un nuevo usuario hasheando su contraseña antes de guardarla.
     */
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        // 1. Hasheamos la contraseña plana antes de enviarla a la BBDD
        String hashSeguro = GestorSeguridad.hashearPassword(nuevoUsuario.getPassword());

        String sql = "INSERT INTO usuario (email, passwd, nombre, apellidos, edad, altura, peso_inicial, rol, id_objetivo_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, nuevoUsuario.getEmail());
            pstmt.setString(2, hashSeguro);
            pstmt.setString(3, nuevoUsuario.getNombre());
            pstmt.setString(4, nuevoUsuario.getApellidos());
            pstmt.setInt(5, nuevoUsuario.getEdad());
            pstmt.setDouble(6, nuevoUsuario.getAltura());
            pstmt.setDouble(7, nuevoUsuario.getPesoInicial());
            pstmt.setString(8, nuevoUsuario.getRol() != null ? nuevoUsuario.getRol() : "USUARIO");
            pstmt.setInt(9, nuevoUsuario.getIdObjetivo());

            int filas = pstmt.executeUpdate();
            
            if (filas > 0) {
                // Recuperamos el ID generado por la BBDD
                try (java.sql.ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        nuevoUsuario.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al registrar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inicia sesión verificando el email y comparando la contraseña plana
     * contra el hash de la base de datos usando BCrypt.
     */
    public static Usuario iniciarSesion(String email, String passwordPlana) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection con = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 1. Obtenemos el hash de la contraseña guardado en la columna 'passwd'
                    String hashGuardado = rs.getString("passwd");

                    // 2. Verificamos si la contraseña que escribió el usuario coincide con el hash
                    if (GestorSeguridad.verificarPassword(passwordPlana, hashGuardado)) {
                        
                        // ¡Éxito! Creamos el objeto Usuario con los datos de la fila
                        Usuario usu = new Usuario();
                        usu.setId(rs.getInt("id_usuario"));
                        usu.setEmail(rs.getString("email"));
                        usu.setNombre(rs.getString("nombre"));
                        usu.setApellidos(rs.getString("apellidos"));
                        usu.setEdad(rs.getInt("edad"));
                        usu.setAltura(rs.getDouble("altura"));
                        usu.setPeso(rs.getDouble("peso_inicial"));
                        usu.setRol(rs.getString("rol"));
                        usu.setIdObjetivo(rs.getInt("id_objetivo_fk"));

                        return usu;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error en login: " + e.getMessage());
        }
        return null; // Credenciales inválidas o error
    }

    /**
     * Actualiza el correo electrónico de un usuario.
     */
    public static boolean actualizarEmail(int idUsuario, String nuevoEmail) {
        String sql = "UPDATE usuario SET email = ? WHERE id_usuario = ?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoEmail);
            pstmt.setInt(2, idUsuario);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al actualizar email: " + e.getMessage());
            return false;
        }
    }
}