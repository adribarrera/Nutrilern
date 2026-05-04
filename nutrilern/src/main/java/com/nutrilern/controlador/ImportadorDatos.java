package com.nutrilern.controlador;

import com.nutrilern.modelo.Alimento;
import com.nutrilern.modelo.AlimentoDAO;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Utilidad para importar datos desde archivos CSV a la base de datos.
 */
public class ImportadorDatos {

    /**
     * Importa alimentos desde el recurso CSV predeterminado.
     * @return El número de registros importados con éxito.
     */
    public static int importarAlimentosDesdeCSV() {
        int contador = 0;
        String rutaCsv = "/data/datos_alimentos.csv";

        try (InputStream is = ImportadorDatos.class.getResourceAsStream(rutaCsv);
             BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            if (is == null) {
                System.err.println("NUTRILERN > No se pudo encontrar el archivo: " + rutaCsv);
                return 0;
            }

            String linea;
            boolean primeraLinea = true;

            while ((linea = br.readLine()) != null) {
                // Saltar la cabecera
                if (primeraLinea) {
                    primeraLinea = false;
                    continue;
                }

                String[] campos = linea.split(",");
                if (campos.length >= 10) {
                    try {
                        Alimento al = new Alimento();
                        al.setNombre(campos[0].trim());
                        al.setMarca(campos[1].trim());
                        al.setKcal(Double.parseDouble(campos[2].trim()));
                        al.setGrasas(Double.parseDouble(campos[3].trim()));
                        al.setGrasasSaturadas(Double.parseDouble(campos[4].trim()));
                        al.setHidratosCarbono(Double.parseDouble(campos[5].trim()));
                        al.setAzucares(Double.parseDouble(campos[6].trim()));
                        al.setProteinas(Double.parseDouble(campos[7].trim()));
                        al.setSal(Double.parseDouble(campos[8].trim()));
                        al.setIdCategoriaFk(Integer.parseInt(campos[9].trim()));

                        // Comprobar si ya existe para evitar duplicados
                        boolean existe = AlimentoDAO.buscarAlimentos(al.getNombre()).stream()
                                .anyMatch(a -> a.getNombre().equalsIgnoreCase(al.getNombre()) 
                                            && (a.getMarca() == null || a.getMarca().equalsIgnoreCase(al.getMarca())));

                        if (!existe && AlimentoDAO.crearAlimentoGlobal(al)) {
                            contador++;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("NUTRILERN > Error de formato en línea: " + linea);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("NUTRILERN > Error durante la importación: " + e.getMessage());
        }

        return contador;
    }
}
