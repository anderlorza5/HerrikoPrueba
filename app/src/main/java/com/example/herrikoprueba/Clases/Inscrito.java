package com.example.herrikoprueba.Clases;

public class Inscrito {
    private String nombre;
    private String telefono;
    private Long id;

    public Inscrito(String nombre, String telefono, Long id) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public Long getId() {
        return id;
    }
}
