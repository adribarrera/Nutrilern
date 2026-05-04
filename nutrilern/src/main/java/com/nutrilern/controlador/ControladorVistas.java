package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.PesoDAO;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * CONTROLADOR DE VISTAS (MVC)
 * --------------------------
 * Este controlador actúa como el "cerebro" que prepara los datos para las pantallas.
 * Su objetivo es que las clases de la Vista (Swing) no tengan que saber NADA de la 
 * base de datos ni de cálculos complejos.
 */
public class ControladorVistas {

    /**
     * Prepara los datos para el Dashboard (Menú Principal).
     * @param usuario El usuario que tiene la sesión iniciada.
     * @return Un mapa con: macros consumidos, macros objetivo e IMC.
     */
    public static Map<String, Object> obtenerDatosMenuPrincipal(Usuario usuario) {
        Map<String, Object> datos = new HashMap<>();
        
        // 1. Consultamos cuánto ha comido el usuario hoy (Kcal, HC, Prot, Grasas)
        double[] macrosHoy = AlimentoDAO.obtenerMacrosHoy(usuario.getId());
        datos.put("macrosHoy", macrosHoy);

        // 2. Calculamos cuáles deberían ser sus objetivos según su perfil físico
        double[] macrosObjetivo = CalculadoraNutricional.calcularMacros(usuario);
        datos.put("macrosObjetivo", macrosObjetivo);

        // 3. Calculamos su IMC (Índice de Masa Corporal) actual
        double imc = CalculadoraNutricional.calcularIMC(usuario.getPesoInicial(), usuario.getAltura());
        datos.put("imc", imc);
        datos.put("imcClasificacion", CalculadoraNutricional.getClasificacionIMC(imc));

        return datos;
    }

    /**
     * Prepara los datos para el Panel de Evolución (Gráficas y Calendario).
     * @param usuario Usuario actual.
     * @param fecha La fecha seleccionada en el calendario para ver el detalle de macros.
     * @return Datos para la gráfica de barras (semana), circular (día) y lineal (peso).
     */
    public static Map<String, Object> obtenerDatosEvolucion(Usuario usuario, java.time.LocalDate fecha) {
        Map<String, Object> datos = new HashMap<>();

        // 1. Obtiene las calorías totales de los últimos 7 días para la gráfica de barras
        int[] calSemana = AlimentoDAO.obtenerCaloriasUltimos7Dias(usuario.getId());
        datos.put("caloriasSemana", calSemana);

        // 2. Obtiene los macros de un día concreto para la gráfica circular (Donut/Pie)
        double[] macrosRaw = AlimentoDAO.obtenerMacrosPorDia(usuario.getId(), fecha);
        
        // Si el total de calorías es 0, significa que no registró nada ese día
        if (macrosRaw[0] == 0) {
            datos.put("macrosHoyPorcentaje", new int[0]); // Mandamos array vacío
        } else {
            // Convertimos los gramos a porcentajes para que la gráfica circular sea legible
            int[] macrosPorcentaje = calcularPorcentajesMacros(macrosRaw);
            datos.put("macrosHoyPorcentaje", macrosPorcentaje);
        }

        // 3. Obtiene los últimos 10 pesajes del usuario para la evolución lineal
        List<Object[]> historialPe = PesoDAO.obtenerHistorialPesos(usuario.getId(), 10);
        datos.put("historialPeso", historialPe);

        return datos;
    }

    /**
     * Método interno para transformar gramos de macros en porcentajes relativos.
     * Útil para gráficas circulares.
     */
    private static int[] calcularPorcentajesMacros(double[] macros) {
        int[] porcentajes = new int[]{33, 33, 34}; // Default
        double total = macros[1] + macros[2] + macros[3];
        if (total > 0) {
            porcentajes[0] = (int) ((macros[1] / total) * 100); // HC
            porcentajes[1] = (int) ((macros[2] / total) * 100); // Prot
            porcentajes[2] = 100 - porcentajes[0] - porcentajes[1]; // Fat
        }
        return porcentajes;
    }

    // --- LÓGICA DE ADMINISTRACIÓN ---

    public static List<Usuario> listarUsuariosAdmin() {
        return com.nutrilern.modelo.UsuarioDAO.obtenerTodosLosUsuarios();
    }

    public static boolean eliminarUsuarioAdmin(int id) {
        return com.nutrilern.modelo.UsuarioDAO.eliminarUsuario(id);
    }

    public static boolean guardarUsuarioAdmin(Usuario u, String password, boolean esNuevo) {
        if (esNuevo) {
            u.setPassword(password); // registrarUsuario ya hashea internamente
            return com.nutrilern.modelo.UsuarioDAO.registrarUsuario(u);
        } else {
            // Primero actualizamos todos los datos del usuario
            boolean ok = com.nutrilern.modelo.UsuarioDAO.actualizarUsuarioCompleto(u);
            
            // Si el admin escribió una contraseña nueva, la hasheamos y guardamos
            if (ok && password != null && !password.isEmpty()) {
                String hashNuevo = GestorSeguridad.hashearPassword(password);
                ok = com.nutrilern.modelo.UsuarioDAO.actualizarPassword(u.getId(), hashNuevo);
            }
            return ok;
        }
    }
}
