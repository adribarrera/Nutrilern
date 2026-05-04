package com.nutrilern.controlador;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import java.util.List;
import java.sql.Date;

/**
 * Gestiona la lógica relacionada con el registro de comidas y cálculos nutricionales en vivo.
 */
public class ControladorComidas {

    /**
     * Calcula los macros proporcionales a los gramos introducidos.
     * @param a Alimento base (con valores cada 100g)
     * @param gr Gramos consumidos
     * @return Array de doubles con [kcal, prot, hc, gras, sat, azu, sal]
     */
    public static double[] calcularMacrosProporcionales(Alimento a, double gr) {
        double factor = gr / 100.0;
        return new double[]{
            a.getKcal() * factor,
            a.getProteinas() * factor,
            a.getHidratosCarbono() * factor,
            a.getGrasas() * factor,
            a.getGrasasSaturadas() * factor,
            a.getAzucares() * factor,
            a.getSal() * factor
        };
    }

    /**
     * Registra una fila en el diario.
     */
    public static boolean registrarComida(int idUser, int idAl, double gr, double[] macros) {
        return AlimentoDAO.registrarFilaDiario(
            idUser, idAl, gr, "General", new Date(System.currentTimeMillis()),
            macros[0], macros[1], macros[2], macros[3], macros[4], macros[5], macros[6]
        );
    }
    
    /**
     * Obtiene los registros de hoy para un usuario.
     */
    public static List<Object[]> obtenerRegistrosHoy(int idUser) {
        return AlimentoDAO.obtenerRegistroDiarioHoy(idUser);
    }
}
