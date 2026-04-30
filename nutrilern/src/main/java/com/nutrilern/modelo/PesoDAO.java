package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PesoDAO {

    /**
     * Registra un nuevo peso en el historial.
     */
    public static boolean registrarPeso(int idUsuario, double peso) {
        String sql = "INSERT INTO historial_peso (id_usuario_fk, peso) VALUES (?, ?)";
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setDouble(2, peso);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al registrar peso: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene los últimos N pesos registrados con sus fechas para la gráfica.
     * Devuelve una lista de Object[] donde [0] es Double (peso) y [1] es String (fecha).
     */
    public static List<Object[]> obtenerHistorialPesos(int idUsuario, int limite) {
        List<Object[]> historial = new ArrayList<>();
        String sql = "SELECT peso, DATE_FORMAT(fecha, '%d/%m') as fecha_formateada FROM historial_peso WHERE id_usuario_fk = ? ORDER BY fecha DESC, id_historial DESC LIMIT ?";
        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, limite);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    historial.add(new Object[]{ rs.getDouble("peso"), rs.getString("fecha_formateada") });
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al obtener historial de pesos: " + e.getMessage());
        }
        // Invertimos para que la gráfica vaya de pasado a presente (izquierda a derecha)
        Collections.reverse(historial);
        return historial;
    }
}
