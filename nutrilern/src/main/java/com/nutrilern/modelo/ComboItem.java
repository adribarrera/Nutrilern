package com.nutrilern.modelo;

/**
 * Guarda un ID oculto (para la BBDD) y muestra un Texto (para el usuario).
 */
public class ComboItem {
    private int id;
    private String nombre;

    public ComboItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    // Este método es la clave: es lo que el desplegable pintará en la pantalla
    @Override
    public String toString() {
        return nombre;
    }
}