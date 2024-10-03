package com.example.herrikoprueba.Clases;

public class Socio {
    private int id;
    private String nombreCompleto;
    private String movilNumero;
    private String email;
    private String dni; // **ADDED: Campo DNI**

    // Constructor vacío necesario para Firestore
    public Socio() {
    }

    // **CHANGED: Constructor para incluir DNI**
    public Socio(int id, String nombreCompleto, String movilNumero, String email, String dni) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.movilNumero = movilNumero;
        this.email = email;
        this.dni = dni; // **ADDED: Asignación de DNI**
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getMovilNumero() {
        return movilNumero;
    }

    public void setMovilNumero(String movilNumero) {
        this.movilNumero = movilNumero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // **ADDED: Getter y Setter para DNI**
    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }
}
