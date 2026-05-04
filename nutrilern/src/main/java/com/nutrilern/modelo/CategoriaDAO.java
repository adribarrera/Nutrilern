package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class CategoriaDAO {

    /**
     * Guarda una categoría nueva en TiDB
     */
    public static boolean crearCategoria(String nombre) {
        String sql = "INSERT INTO Categoria_Alimento (nombre) VALUES (?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nombre);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al crear categoría: " + e.getMessage());
            return false;
        }
    }

    /**
     * Trae todas las categorías
     */
    public static Map<Integer, String> obtenerTodasLasCategorias() {
        Map<Integer, String> mapaCategorias = new HashMap<>();
        String sql = "SELECT * FROM Categoria_Alimento ORDER BY nombre ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                mapaCategorias.put(rs.getInt("id_categoria"), rs.getString("nombre"));
            }

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al cargar categorías: " + e.getMessage());
        }
        return mapaCategorias;
    }
}