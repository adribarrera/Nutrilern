package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import com.nutrilern.controlador.GestorSeguridad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones de acceso a datos para la entidad Usuario.
 */
public class UsuarioDAO {

    /**
     * Registra un nuevo usuario hasheando su contraseña.
     */
    public static boolean registrarUsuario(Usuario nuevoUsuario) {
        String hashSeguro = GestorSeguridad.hashearPassword(nuevoUsuario.getPassword());

        String sql = "INSERT INTO usuario (email, passwd, nombre, apellidos, edad, altura, peso_inicial, rol, id_objetivo_fk, sexo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // ... (resto del código igual)

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

            if (nuevoUsuario.getIdObjetivo() > 0) {
                pstmt.setInt(9, nuevoUsuario.getIdObjetivo());
            } else {
                pstmt.setNull(9, java.sql.Types.INTEGER);
            }

            pstmt.setString(10, nuevoUsuario.getSexo());

            int filas = pstmt.executeUpdate();

            if (filas > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
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
     * Valida credenciales e inicia sesión.
     */
    public static Usuario iniciarSesion(String email, String passwordPlana) {
        String sql = "SELECT * FROM usuario WHERE email = ?";

        try (Connection con = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("passwd");
                    if (GestorSeguridad.verificarPassword(passwordPlana, hashGuardado)) {
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
                        usu.setSexo(rs.getString("sexo"));
                        return usu;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error en login: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el email de un usuario.
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

    /**
     * Actualiza edad, peso y altura.
     */
    public static boolean actualizarDatosFisicos(int idUsuario, int edad, double peso, double altura) {
        String sql = "UPDATE usuario SET edad = ?, peso_inicial = ?, altura = ? WHERE id_usuario = ?";
        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, edad);
            pstmt.setDouble(2, peso);
            pstmt.setDouble(3, altura);
            pstmt.setInt(4, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al actualizar datos físicos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza el ID del objetivo personal.
     */
    public static boolean actualizarObjetivo(int idUsuario, int idObjetivo) {
        String sql = "UPDATE usuario SET id_objetivo_fk = ? WHERE id_usuario = ?";
        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idObjetivo);
            pstmt.setInt(2, idUsuario);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al actualizar objetivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un usuario por ID.
     */
    public static boolean eliminarUsuario(int idUsuario) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todos los usuarios ordenador por nombre.
     */
    public static List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuario ORDER BY nombre ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setId(rs.getInt("id_usuario"));
                u.setEmail(rs.getString("email"));
                u.setNombre(rs.getString("nombre"));
                u.setApellidos(rs.getString("apellidos"));
                u.setEdad(rs.getInt("edad"));
                u.setAltura(rs.getDouble("altura"));
                u.setPeso(rs.getDouble("peso_inicial"));
                u.setRol(rs.getString("rol"));
                u.setIdObjetivo(rs.getInt("id_objetivo_fk"));
                u.setSexo(rs.getString("sexo"));
                lista.add(u);
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al listar: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza todos los datos de un usuario (Admin).
     */
    public static boolean actualizarUsuarioCompleto(Usuario u) {
        String sql = "UPDATE usuario SET email=?, nombre=?, apellidos=?, edad=?, altura=?, peso_inicial=?, rol=?, id_objetivo_fk=?, sexo=? WHERE id_usuario=?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, u.getEmail());
            pstmt.setString(2, u.getNombre());
            pstmt.setString(3, u.getApellidos());
            pstmt.setInt(4, u.getEdad());
            pstmt.setDouble(5, u.getAltura());
            pstmt.setDouble(6, u.getPesoInicial());
            pstmt.setString(7, u.getRol());

            if (u.getIdObjetivo() > 0) {
                pstmt.setInt(8, u.getIdObjetivo());
            } else {
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }

            pstmt.setString(9, u.getSexo());
            pstmt.setInt(10, u.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza la contraseña hasheada.
     */
    public static boolean actualizarPassword(int idUsuario, String hashNuevo) {
        String sql = "UPDATE usuario SET passwd = ? WHERE id_usuario = ?";
        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashNuevo);
            pstmt.setInt(2, idUsuario);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error de password: " + e.getMessage());
            return false;
        }
    }
}