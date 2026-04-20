package bbdd_test;

import controlador.basededatos;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("=== NUTRILERN: Test de Conexión ===");

        // Intentamos obtener la conexión
        Connection conn = basededatos.obtenerConexion();

        if (conn != null) {
            System.out.println("✅ ¡ÉXITO! El programa ha podido conectar con el servidor de TiDB Cloud.");
            basededatos.cerrarConexion();
        } else {
            System.err.println("❌ ERROR: No se ha podido establecer la conexión.");
            System.err.println("Por favor, revisa que los datos en src/main/resources/db.properties sean correctos.");
        }
    }
}
