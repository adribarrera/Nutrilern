package com.nutrilern.modelo;

public class Usuario {

    private int id;
    private String email;
    private String password;
    private String nombre;
    private String apellidos;
    private int edad;
     private double altura;
    private double pesoInicial;
    private String rol;
    private int idObjetivo;
    private String sexo;

    // Constructor completo (sin el ID que se genera en la BBDD con el
    // AUTO_INCREMENT
    public Usuario(String email, String password, String nombre, String apellidos, int edad, double altura, double pesoInicial, String rol, int idObjetivo, String sexo) {
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
        this.altura = altura;
        this.pesoInicial = pesoInicial;
        this.rol = rol;
        this.idObjetivo = idObjetivo;
        this.sexo = sexo;
    }

    // Constructor vacío
    public Usuario() {

    }
    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public double getPesoInicial() {
        return pesoInicial;
    }

    public void setPeso(double pesoInicial) {
        this.pesoInicial = pesoInicial;
    }

    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    
    public int getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }
    
    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
