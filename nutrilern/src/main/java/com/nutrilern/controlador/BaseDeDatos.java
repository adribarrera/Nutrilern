package com.nutrilern.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Clase para gestionar la conexión a la base de datos remota usando un archivo
 * de propiedades.
 */
public class BaseDeDatos {

    private static Properties props = new Properties();

    static {
        try (InputStream input = BaseDeDatos.class.getClassLoader().getResourceAsStream("db.properties")) {
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
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://" + props.getProperty("db.host") + ":" +
                    props.getProperty("db.port") + "/" +
                    props.getProperty("db.database") + "?useSSL=true&serverTimezone=UTC";

            conn = DriverManager.getConnection(url, props.getProperty("db.user"),
                    props.getProperty("db.password"));
            // Eliminamos el sysout de éxito porque spammearía la consola cada vez que se
            // hace una query
        } catch (Exception e) {
            System.err.println("NUTRILERN > Error al conectar: " + e.getMessage());
        }
        return conn;
    }
}
