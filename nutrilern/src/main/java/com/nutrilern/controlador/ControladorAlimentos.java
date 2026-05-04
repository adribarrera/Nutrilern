package com.nutrilern.controlador;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import com.nutrilern.modelo.CategoriaDAO;
import java.util.List;
import java.util.Map;

public class ControladorAlimentos {

    public static List<Alimento> obtenerTodos() {
        return AlimentoDAO.obtenerTodosLosAlimentos();
    }

    public static List<Alimento> obtenerTopProteinas() {
        return AlimentoDAO.obtenerTopProteinas();
    }

    public static List<Alimento> obtenerBajosEnCalorias() {
        return AlimentoDAO.obtenerBajosEnCalorias();
    }

    public static List<Alimento> buscarAlimentos(String query, int idCategoria) {
        return AlimentoDAO.obtenerAlimentosPorFiltro(query, idCategoria);
    }

    public static Map<Integer, String> obtenerCategorias() {
        return CategoriaDAO.obtenerTodasLasCategorias();
    }
}
