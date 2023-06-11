package com.example.herrikoprueba.Clases;

public class FacturaBebida {
    private BebidaComedor bebida;
    private int unidades;

    public FacturaBebida(BebidaComedor bebida) {
        this.bebida = bebida;
        this.unidades = 1; // Asumiendo que inicialmente se añade una unidad de la bebida
    }

    public BebidaComedor getBebida() {
        return bebida;
    }

    public int getUnidades() {
        return unidades;
    }

    public void incrementarUnidades() {
        this.unidades++;
    }
}
