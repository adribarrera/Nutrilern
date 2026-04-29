package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AlimentoDAO {

    /**
     * Registra un alimento consumido.
     * Basado en los requisitos: nombre, kcal e id_categoria_fk son NOT NULL.
     */
    public static boolean registrarConsumo(int idUsuario, String nombre, int kcal, int idCategoria, LocalDate fecha) {
        if (nombre == null || nombre.trim().isEmpty()) return false;

        // Usamos los nombres exactos proporcionados por el usuario
        String sql = "INSERT INTO registro_diario (id_usuario, nombre, kcal, id_categoria_fk, fecha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setString(2, nombre);
            pstmt.setInt(3, kcal);
            pstmt.setInt(4, idCategoria);
            pstmt.setObject(5, java.sql.Date.valueOf(fecha));

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al guardar alimento: " + e.getMessage());
            // Si el error es que la tabla no existe, el usuario debería ver el mensaje en consola
            return false;
        }
    }
}
