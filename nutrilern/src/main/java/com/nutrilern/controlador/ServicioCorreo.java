package com.nutrilern.controlador;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ServicioCorreo {

    // Variables que se llenarán desde el archivo .properties
    private static String CORREO_ORIGEN;
    private static String CONTRASENA_APP;

    // Este bloque se ejecuta una sola vez al cargar la clase
    static {
        cargarCredenciales();
    }

    private static void cargarCredenciales() {
        Properties propiedades = new Properties();
        // Buscamos el archivo en la carpeta resources
        try (InputStream input = ServicioCorreo.class.getResourceAsStream("/correo.properties")) {

            if (input == null) {
                System.err.println("❌ CRÍTICO: No se encontró el archivo correo.properties en resources.");
                return;
            }

            // Cargamos el archivo y leemos las claves
            propiedades.load(input);
            CORREO_ORIGEN = propiedades.getProperty("correo.origen");
            CONTRASENA_APP = propiedades.getProperty("correo.contrasena");

        } catch (Exception e) {
            System.err.println("❌ Error al leer las credenciales del correo.");
            e.printStackTrace();
        }
    }

    public static boolean enviarCodigoVerificacion(String correoDestino, String codigo) {

        // Medida de seguridad: Si no se cargaron los datos, no intentamos enviar nada
        if (CORREO_ORIGEN == null || CONTRASENA_APP == null) {
            System.err.println("❌ No se puede enviar el correo: Credenciales no configuradas.");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // Le quitamos los espacios a la contraseña por si acaso
                return new PasswordAuthentication(CORREO_ORIGEN, CONTRASENA_APP.replace(" ", ""));
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(CORREO_ORIGEN));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            mensaje.setSubject("Nutrix - Tu código de verificación");

            String contenidoHtml = "<div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>" +
                    "  <h2 style='color: #228B22;'>¡Bienvenido a Nutrix!</h2>" +
                    "  <p>Estás a un solo paso de completar tu registro y empezar a dominar tu nutrición.</p>" +
                    "  <p>Tu código de seguridad de 8 dígitos es:</p>" +
                    "  <div style='background-color: #f4f4f4; padding: 15px; border-radius: 5px; display: inline-block;'>"
                    +
                    "    <span style='font-size: 28px; font-weight: bold; letter-spacing: 5px; color: #000;'>" + codigo
                    + "</span>" +
                    "  </div>" +
                    "  <p style='margin-top: 30px; font-size: 12px; color: #777;'>" +
                    "    Si no has solicitado este código, ignora este mensaje." +
                    "  </p>" +
                    "</div>";

            mensaje.setContent(contenidoHtml, "text/html; charset=utf-8");

            Transport.send(mensaje);
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}