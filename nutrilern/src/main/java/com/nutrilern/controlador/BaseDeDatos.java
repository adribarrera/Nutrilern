package com.nutrilern.controlador;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Gestión de la conexión a la base de datos MySQL.
 */
public class BaseDeDatos {

    private static Properties props = new Properties();

    static {
        // Cargar configuración desde db.properties
        try (InputStream input = BaseDeDatos.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.err.println("NUTRILERN > Archivo db.properties no encontrado.");
            } else {
                props.load(input);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Obtiene una conexión activa con el servidor.
     */
    public static Connection obtenerConexion() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://" + props.getProperty("db.host") + ":" +
                    props.getProperty("db.port") + "/" +
                    props.getProperty("db.database") + "?useSSL=true&serverTimezone=UTC";

            conn = DriverManager.getConnection(url, props.getProperty("db.user"),
                    props.getProperty("db.password"));

        } catch (Exception e) {
            System.err.println("NUTRILERN > Error de conexión: " + e.getMessage());
        }
        return conn;
    }
}
