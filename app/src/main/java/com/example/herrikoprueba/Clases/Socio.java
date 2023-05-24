package com.example.herrikoprueba.Clases;

public class Socio {
    private int id;
    private String nombreCompleto;
    private String movilNumero;

    // Constructor vacío necesario para Firestore
    public Socio() {
    }

    public Socio(int id, String nombreCompleto, String movilNumero) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.movilNumero = movilNumero;
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
}
