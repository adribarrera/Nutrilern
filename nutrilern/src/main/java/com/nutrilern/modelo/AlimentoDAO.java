package com.nutrilern.modelo;

import com.nutrilern.controlador.BaseDeDatos;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Gestión de datos para alimentos y registros diarios.
 */
public class AlimentoDAO {

    /**
     * Obtiene la lista completa de alimentos.
     */
    public static List<Alimento> obtenerTodosLosAlimentos() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento ORDER BY nombre ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extraerAlimentoDelResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar alimentos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Filtra alimentos por texto y categoría.
     */
    public static List<Alimento> obtenerAlimentosPorFiltro(String query, int idCategoria) {
        List<Alimento> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM alimento WHERE 1=1");
        if (query != null && !query.isEmpty())
            sql.append(" AND nombre LIKE ?");
        if (idCategoria > 0)
            sql.append(" AND id_categoria_fk = ?");
        sql.append(" ORDER BY nombre ASC");

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

            int paramIdx = 1;
            if (query != null && !query.isEmpty())
                pstmt.setString(paramIdx++, "%" + query + "%");
            if (idCategoria > 0)
                pstmt.setInt(paramIdx++, idCategoria);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerAlimentoDelResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al filtrar alimentos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Registra un nuevo alimento global.
     */
    public static boolean crearAlimentoGlobal(Alimento nuevoAlimento) {
        String sql = "INSERT INTO alimento (nombre, marca, kcal, grasas, grasas_saturadas, hidratos_carbono, azucares, proteinas, sal, id_categoria_fk) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
            System.err.println("Error al crear alimento: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra una ingesta en el diario del usuario.
     */
    public static boolean registrarFilaDiario(int idUsuario, int idAlimento, double gramos, String tipoComida,
            Date fecha, double kcal, double prot, double hc, double grasas, double sat, double azucar, double sal) {

        String sql = "INSERT INTO registro_diario " +
                "(id_usuario_fk, id_alimento_fk, cantidad_gramos, tipo_comida, fecha, kcal, proteinas, hidratos_carbono, grasas, grasas_saturadas, azucares, sal) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idAlimento);
            pstmt.setDouble(3, gramos);
            pstmt.setString(4, tipoComida != null ? tipoComida : "General");
            pstmt.setDate(5, fecha);
            pstmt.setDouble(6, kcal);
            pstmt.setDouble(7, prot);
            pstmt.setDouble(8, hc);
            pstmt.setDouble(9, grasas);
            pstmt.setDouble(10, sat);
            pstmt.setDouble(11, azucar);
            pstmt.setDouble(12, sal);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar comida: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene calorías diarias de los últimos 7 días.
     */
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
                    long diff = (new java.util.Date().getTime() - fechaFila.getTime()) / (1000 * 60 * 60 * 24);
                    int index = 6 - (int) diff;
                    if (index >= 0 && index < 7) {
                        calorias[index] = rs.getInt("total_kcal");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener histórico semanal: " + e.getMessage());
        }
        return calorias;
    }

    /**
     * Obtiene macros acumulados por fecha.
     */
    public static double[] obtenerMacrosPorDia(int idUsuario, java.time.LocalDate fecha) {
        double[] macros = { 0, 0, 0, 0 }; 
        String sql = "SELECT SUM(kcal) as kcal, SUM(hidratos_carbono) as hc, SUM(proteinas) as prot, SUM(grasas) as fat "
                + "FROM registro_diario WHERE id_usuario_fk = ? AND fecha = ?";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            pstmt.setDate(2, java.sql.Date.valueOf(fecha));

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    macros[0] = rs.getDouble("kcal");
                    macros[1] = rs.getDouble("hc");
                    macros[2] = rs.getDouble("prot");
                    macros[3] = rs.getDouble("fat");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener macros diarios: " + e.getMessage());
        }
        return macros;
    }

    /**
     * Obtiene macros del día actual.
     */
    public static double[] obtenerMacrosHoy(int idUsuario) {
        return obtenerMacrosPorDia(idUsuario, java.time.LocalDate.now());
    }

    /**
     * Obtiene alimentos con mayor contenido proteico.
     */
    public static List<Alimento> obtenerTopProteinas() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento ORDER BY proteinas DESC LIMIT 50";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extraerAlimentoDelResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking proteínas: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene alimentos con menor densidad calórica.
     */
    public static List<Alimento> obtenerBajosEnCalorias() {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento WHERE kcal > 0 ORDER BY kcal ASC LIMIT 50";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(extraerAlimentoDelResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ranking calorías: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Búsqueda por coincidencia en nombre o marca.
     */
    public static List<Alimento> buscarAlimentos(String textoBusqueda) {
        List<Alimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM alimento WHERE nombre LIKE ? OR marca LIKE ? ORDER BY nombre ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String parametro = "%" + textoBusqueda + "%";
            pstmt.setString(1, parametro);
            pstmt.setString(2, parametro);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(extraerAlimentoDelResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en búsqueda de alimentos: " + e.getMessage());
        }
        return lista;
    }

    private static Alimento extraerAlimentoDelResultSet(ResultSet rs) throws SQLException {
        return new Alimento(
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
                rs.getInt("id_categoria_fk"));
    }

    /**
     * Obtiene las entradas del registro diario para el día actual.
     */
    public static List<Object[]> obtenerRegistroDiarioHoy(int idUsuario) {
        List<Object[]> registros = new ArrayList<>();
        String sql = "SELECT r.*, a.nombre, a.id_categoria_fk " +
                "FROM registro_diario r " +
                "JOIN alimento a ON r.id_alimento_fk = a.id_alimento " +
                "WHERE r.id_usuario_fk = ? AND r.fecha = CURDATE() ORDER BY r.id_registro ASC";

        try (Connection conn = BaseDeDatos.obtenerConexion();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Alimento al = new Alimento();
                    al.setIdAlimento(rs.getInt("id_alimento_fk"));
                    al.setNombre(rs.getString("nombre"));
                    al.setIdCategoriaFk(rs.getInt("id_categoria_fk"));

                    registros.add(new Object[] {
                            al,
                            al.getIdCategoriaFk(),
                            rs.getDouble("cantidad_gramos"),
                            rs.getDouble("kcal"),
                            rs.getDouble("proteinas"),
                            rs.getDouble("hidratos_carbono"),
                            rs.getDouble("grasas"),
                            rs.getDouble("grasas_saturadas"),
                            rs.getDouble("azucares"),
                            rs.getDouble("sal"),
                            true
                    });
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al recuperar registro hoy: " + e.getMessage());
        }
        return registros;
    }
}