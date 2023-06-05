package com.example.herrikoprueba.Clases;

public class BebidaComedor {
    private String id;
    private double precio;
    private double totalEuros;
    private double unidadesVendidas;

    // Constructor por defecto
    public BebidaComedor() {}

    // Constructor con parámetros
    public BebidaComedor(String id, double precio, double totalEuros, double unidadesVendidas) {
        this.id = id;
        this.precio = precio;
        this.totalEuros = totalEuros;
        this.unidadesVendidas = unidadesVendidas;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public double getTotalEuros() {
        return totalEuros;
    }

    public void setTotalEuros(double totalEuros) {
        this.totalEuros = totalEuros;
    }

    public double getUnidadesVendidas() {
        return unidadesVendidas;
    }

    public void setUnidadesVendidas(double unidadesVendidas) {
        this.unidadesVendidas = unidadesVendidas;
    }
}
