package com.nutrilern.modelo;

public class Alimento {
    private int id;
    private String nombre;
    private int kcal;
    private int idCategoria;

    public Alimento() {
        this.kcal = 0;
        this.idCategoria = 30020; // Sin categoría por defecto
    }

    public Alimento(String nombre, int kcal, int idCategoria) {
        this.nombre = nombre;
        this.kcal = kcal;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getKcal() { return kcal; }
    public void setKcal(int kcal) { this.kcal = kcal; }

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
}
