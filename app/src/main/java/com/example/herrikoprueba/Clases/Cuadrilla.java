package com.example.herrikoprueba.Clases;

public class Cuadrilla {
    private String name;
    private int memberCount; // Campo para el conteo de miembros

    public Cuadrilla() {
        // Constructor vacío necesario para Firestore
    }

    public Cuadrilla(String name) {
        this.name = name;
        this.memberCount = 0; // Inicializar el conteo de miembros a 0
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
}
