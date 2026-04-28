package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import com.nutrilern.controlador.GestorSeguridad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UsuarioDAO {
    
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        String hashSeguro = GestorSeguridad.hashearPassword(nuevoUsuario.getPassword());

        String sql = "INSERT INTO usuario (email, passwd, nombre, apellidos, edad, altura, peso_inicial, rol, id_objetivo_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nuevoUsuario.getEmail());
            pstmt.setString(2, hashSeguro);
            pstmt.setString(3, nuevoUsuario.getNombre());
            pstmt.setString(4, nuevoUsuario.getApellidos());
            pstmt.setInt(5, nuevoUsuario.getEdad());
            pstmt.setDouble(6, nuevoUsuario.getAltura());
            pstmt.setDouble(7, nuevoUsuario.getPesoInicial());
            pstmt.setString(8, nuevoUsuario.getRol() != null ? nuevoUsuario.getRol() : "USUARIO");
            
            pstmt.setInt(9, nuevoUsuario.getIdObjetivo());

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public static Usuario iniciarSesion(String email, String passwordPlana) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection con = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, email);
            
            // Usamos ResultSet para leer lo que nos devuelve TiDB
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // El usuario existe, sacamos el hash guardado
                    String hashGuardado = rs.getString("password");
                    
                    // Comprobamos si la contraseña coincide con el hash
                    if (GestorSeguridad.verificarPassword(passwordPlana, hashGuardado)) {
                        // ¡Login correcto! Construimos el objeto Usuario con todos sus datos
                        Usuario usu = new Usuario();
                        usu.setId(rs.getInt("id"));
                        usu.setEmail(rs.getString("email"));
                        usu.setPassword(hashGuardado);
                        usu.setNombre(rs.getString("nombre"));
                        usu.setApellidos(rs.getString("apellidos"));
                        usu.setEdad(rs.getInt("edad"));
                        usu.setAltura(rs.getDouble("altura"));
                        usu.setPeso(rs.getDouble("peso_inicial"));
                        usu.setRol(rs.getString("rol"));
                        
                        return usu; // Devolvemos el usuario completo
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al iniciar sesión: " + e.getMessage());
        }
        
        // Si el email no existe, o la clave está mal, o hay un error, devolvemos null
        return null; 
    }
}