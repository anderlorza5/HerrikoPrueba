package com.example.herrikoprueba.Clases;

public class Miembro {
    private String name;

    public Miembro() {
        // Constructor vacío necesario para Firestore
    }

    public Miembro(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
