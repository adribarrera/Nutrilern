package com.nutrilern.controlador;

import com.nutrilern.modelo.Usuario;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.PesoDAO;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Controlador centralizado para separar la lógica de negocio y acceso a datos
 * de las interfaces gráficas (Vista).
 */
public class ControladorVistas {

    /**
     * Obtiene todos los datos necesarios para el resumen del menú principal.
     */
    public static Map<String, Object> obtenerDatosMenuPrincipal(Usuario usuario) {
        Map<String, Object> datos = new HashMap<>();
        
        // 1. Macros consumidos hoy
        double[] macrosHoy = AlimentoDAO.obtenerMacrosHoy(usuario.getId());
        datos.put("macrosHoy", macrosHoy);

        // 2. Objetivos nutricionales
        double[] macrosObjetivo = CalculadoraNutricional.calcularMacros(usuario);
        datos.put("macrosObjetivo", macrosObjetivo);

        // 3. IMC y Clasificación
        double imc = CalculadoraNutricional.calcularIMC(usuario.getPesoInicial(), usuario.getAltura());
        datos.put("imc", imc);
        datos.put("imcClasificacion", CalculadoraNutricional.getClasificacionIMC(imc));

        return datos;
    }

    /**
     * Obtiene los datos necesarios para las gráficas del panel de evolución,
     * filtrando los macros por una fecha específica.
     */
    public static Map<String, Object> obtenerDatosEvolucion(Usuario usuario, java.time.LocalDate fecha) {
        Map<String, Object> datos = new HashMap<>();

        // 1. Calorías última semana
        int[] calSemana = AlimentoDAO.obtenerCaloriasUltimos7Dias(usuario.getId());
        datos.put("caloriasSemana", calSemana);

        // 2. Distribución de macros del día solicitado
        double[] macrosRaw = AlimentoDAO.obtenerMacrosPorDia(usuario.getId(), fecha);
        
        // Si no hay calorías consumidas, mandamos array vacío para indicar "Sin registros"
        if (macrosRaw[0] == 0) {
            datos.put("macrosHoyPorcentaje", new int[0]);
        } else {
            int[] macrosPorcentaje = calcularPorcentajesMacros(macrosRaw);
            datos.put("macrosHoyPorcentaje", macrosPorcentaje);
        }

        // 3. Historial de peso
        List<Object[]> historialPe = PesoDAO.obtenerHistorialPesos(usuario.getId(), 10);
        datos.put("historialPeso", historialPe);

        return datos;
    }

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
}
