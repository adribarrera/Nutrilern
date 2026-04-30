package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlimentoDAO {
    // 1. OBTIENE TODOS LOS ALIMENTOS
    public static List<Alimento> obtenerTodosLosAlimentos() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento ORDER BY nombre ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Alimento a = new Alimento(
                    rs.getInt("id_alimento"),
                    rs.getString("nombre"),
                    rs.getString("marca"),
                    rs.getDouble("kcal"),
                    rs.getDouble("grasas"),
                    rs.getDouble("grasas_saturadas"),
                    rs.getDouble("hidratos_carbono"),
                    rs.getDouble("azucares"),
                    rs.getDouble("proteinas"),
                    rs.getDouble("sal"),
                    rs.getInt("id_categoria_fk")
                );
                lista.add(a);
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al cargar los alimentos: " + e.getMessage());
        }
        return lista;
    }

    
     // 2. CREA UN ALIMENTO NUEVO EN LA BASE DE DATOS (Para el botón "+ Nuevo Alimento")

    public static boolean crearAlimentoGlobal(Alimento nuevoAlimento) {
        String sql = "INSERT INTO alimento (nombre, marca, kcal, grasas, grasas_saturadas, hidratos_carbono, azucares, proteinas, sal, id_categoria_fk) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevoAlimento.getNombre());
            pstmt.setString(2, nuevoAlimento.getMarca());
            pstmt.setDouble(3, nuevoAlimento.getKcal());
            pstmt.setDouble(4, nuevoAlimento.getGrasas());
            pstmt.setDouble(5, nuevoAlimento.getGrasasSaturadas());
            pstmt.setDouble(6, nuevoAlimento.getHidratosCarbono());
            pstmt.setDouble(7, nuevoAlimento.getAzucares());
            pstmt.setDouble(8, nuevoAlimento.getProteinas());
            pstmt.setDouble(9, nuevoAlimento.getSal());
            pstmt.setInt(10, nuevoAlimento.getIdCategoriaFk());

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al crear alimento global: " + e.getMessage());
            return false;
        }
    }


    // 3. GUARDA UNA FILA DEL "EXCEL" EN EL REGISTRO DIARIO DEL USUARIO

    public static boolean registrarFilaDiario(int idUsuario, int idAlimento, double gramos, String tipoComida, Date fecha, 
                                              double kcal, double prot, double hc, double grasas, double sat, double azucar, double sal) {
        
        // Guardamos los macros modificados para el registro diario
        String sql = "INSERT INTO registro_diario " +
                     "(id_usuario_fk, id_alimento_fk, cantidad_gramos, tipo_comida, fecha, kcal, proteinas, hidratos_carbono, grasas, grasas_saturadas, azucares, sal) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idAlimento);
            pstmt.setDouble(3, gramos);
            pstmt.setString(4, tipoComida != null ? tipoComida : "General");
            pstmt.setDate(5, fecha);
            
            // Los macros que el usuario haya escrito a mano en el JTable
            pstmt.setDouble(6, kcal);
            pstmt.setDouble(7, prot);
            pstmt.setDouble(8, hc);
            pstmt.setDouble(9, grasas);
            pstmt.setDouble(10, sat);
            pstmt.setDouble(11, azucar);
            pstmt.setDouble(12, sal);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al guardar la fila en el diario: " + e.getMessage());
            return false;
        }
    }

    // 4. OBTIENE LAS CALORÍAS TOTALES DE LOS ÚLTIMOS 7 DÍAS
    public static int[] obtenerCaloriasUltimos7Dias(int idUsuario) {
        int[] calorias = new int[7];
        String sql = "SELECT fecha, SUM(kcal) as total_kcal FROM registro_diario " +
                     "WHERE id_usuario_fk = ? AND fecha >= DATE_SUB(CURDATE(), INTERVAL 6 DAY) " +
                     "GROUP BY fecha ORDER BY fecha ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Date fechaFila = rs.getDate("fecha");
                    long diff = (new Date(System.currentTimeMillis()).getTime() - fechaFila.getTime()) / (1000 * 60 * 60 * 24);
                    int index = 6 - (int)diff;
                    if (index >= 0 && index < 7) {
                        calorias[index] = rs.getInt("total_kcal");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al obtener calorías semanales: " + e.getMessage());
        }
        return calorias;
    }

    // 5. OBTIENE LOS MACROS TOTALES DE HOY
    public static double[] obtenerMacrosHoy(int idUsuario) {
        double[] macros = {0, 0, 0, 0}; // [Kcal, HC, Prot, Grasas]
        String sql = "SELECT SUM(kcal) as kcal, SUM(hidratos_carbono) as hc, SUM(proteinas) as prot, SUM(grasas) as fat " +
                     "FROM registro_diario WHERE id_usuario_fk = ? AND fecha = CURDATE()";

        try (Connection conn = BaseDeDatos.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    macros[0] = rs.getDouble("kcal");
                    macros[1] = rs.getDouble("hc");
                    macros[2] = rs.getDouble("prot");
                    macros[3] = rs.getDouble("fat");
                }
            }
        } catch (SQLException e) {
            System.err.println("NUTRILERN > Error al obtener macros de hoy: " + e.getMessage());
        }
        return macros;
    }
}