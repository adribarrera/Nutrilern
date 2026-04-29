package com.nutrilern.modelo;

public class Alimento {
    private int idAlimento;
    private String nombre;
    private String marca;
    private double kcal;
    private double grasas;
    private double grasasSaturadas;
    private double hidratosCarbono;
    private double azucares;
    private double proteinas;
    private double sal;
    private int idCategoriaFk;

    // Constructor vacío por defecto
    public Alimento() {
    }

    // Constructor con todos los campos
    public Alimento(int idAlimento, String nombre, String marca, double kcal, double grasas, 
                    double grasasSaturadas, double hidratosCarbono, double azucares, 
                    double proteinas, double sal, int idCategoriaFk) {
        this.idAlimento = idAlimento;
        this.nombre = nombre;
        this.marca = marca;
        this.kcal = kcal;
        this.grasas = grasas;
        this.grasasSaturadas = grasasSaturadas;
        this.hidratosCarbono = hidratosCarbono;
        this.azucares = azucares;
        this.proteinas = proteinas;
        this.sal = sal;
        this.idCategoriaFk = idCategoriaFk;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdAlimento() { return idAlimento; }
    public void setIdAlimento(int idAlimento) { this.idAlimento = idAlimento; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public double getKcal() { return kcal; }
    public void setKcal(double kcal) { this.kcal = kcal; }

    public double getGrasas() { return grasas; }
    public void setGrasas(double grasas) { this.grasas = grasas; }

    public double getGrasasSaturadas() { return grasasSaturadas; }
    public void setGrasasSaturadas(double grasasSaturadas) { this.grasasSaturadas = grasasSaturadas; }

    public double getHidratosCarbono() { return hidratosCarbono; }
    public void setHidratosCarbono(double hidratosCarbono) { this.hidratosCarbono = hidratosCarbono; }

    public double getAzucares() { return azucares; }
    public void setAzucares(double azucares) { this.azucares = azucares; }

    public double getProteinas() { return proteinas; }
    public void setProteinas(double proteinas) { this.proteinas = proteinas; }

    public double getSal() { return sal; }
    public void setSal(double sal) { this.sal = sal; }

    public int getIdCategoriaFk() { return idCategoriaFk; }
    public void setIdCategoriaFk(int idCategoriaFk) { this.idCategoriaFk = idCategoriaFk; }

    @Override
    public String toString() {
        if (marca != null && !marca.isEmpty()) {
            return nombre + " (" + marca + ")"; // Ej: "Pollo (Hacendado)"
        }
        return nombre; // Ej: "Manzana"
    }
}