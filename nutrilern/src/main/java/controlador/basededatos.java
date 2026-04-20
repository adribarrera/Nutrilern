package controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos remota usando un archivo de propiedades.
 */
public class basededatos {
    
    private static Connection conexion = null;
    private static Properties props = new Properties();

    static {
        try (InputStream input = basededatos.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("NUTRILERN > Error: No se encontró el archivo db.properties");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection obtenerConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                String url = "jdbc:mysql://" + props.getProperty("db.host") + ":" + 
                             props.getProperty("db.port") + "/" + 
                             props.getProperty("db.database") + "?useSSL=true&serverTimezone=UTC";
                
                conexion = DriverManager.getConnection(url, props.getProperty("db.user"), props.getProperty("db.password"));
                System.out.println("NUTRILERN > Conexión exitosa a la base de datos.");
            }
        } catch (Exception e) {
            System.err.println("NUTRILERN > Error al conectar: " + e.getMessage());
        }
        return conexion;
    }

    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("NUTRILERN > Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
