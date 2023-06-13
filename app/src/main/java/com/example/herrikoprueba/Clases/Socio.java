package com.example.herrikoprueba.Clases;

public class Socio {
    private int id;
    private String nombreCompleto;
    private String movilNumero;
    private String email;

    // Constructor vacío necesario para Firestore
    public Socio() {
    }

    public Socio(int id, String nombreCompleto, String movilNumero, String email) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.movilNumero = movilNumero;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMovilNumero() {
        return movilNumero;
    }

    public void setMovilNumero(String movilNumero) {
        this.movilNumero = movilNumero;
    }
}
