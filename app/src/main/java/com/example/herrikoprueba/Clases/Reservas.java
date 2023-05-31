package com.example.herrikoprueba.Clases;

public class Reservas {
    private String id;
    private String diaReserva;
    private int nºComensales;
    private String hora;
    private String nombreSocio;
    private String numeroMovilSocio;


    public Reservas() {
    }

    public Reservas(String id, String diaReserva, int nºComensales, String hora, String nombreSocio, String numeroMovilSocio) {
        this.id=id;
        this.diaReserva = diaReserva;
        this.nºComensales = nºComensales;
        this.hora = hora;
        this.nombreSocio = nombreSocio;
        this.numeroMovilSocio = numeroMovilSocio;
    }

    public Reservas( String diaReserva, int nºComensales, String hora, String nombreSocio, String numeroMovilSocio) {

        this.diaReserva = diaReserva;
        this.nºComensales = nºComensales;
        this.hora = hora;
        this.nombreSocio = nombreSocio;
        this.numeroMovilSocio = numeroMovilSocio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDiaReserva() {
        return diaReserva;
    }

    public void setDiaReserva(String diaReserva) {
        this.diaReserva = diaReserva;
    }

    public int getNºComensales() {
        return nºComensales;
    }

    public void setNºComensales(int nºComensales) {
        this.nºComensales = nºComensales;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getNombreSocio() {
        return nombreSocio;
    }

    public void setNombreSocio(String nombreSocio) {
        this.nombreSocio = nombreSocio;
    }

    public String getNumeroMovilSocio() {
        return numeroMovilSocio;
    }

    public void setNumeroMovilSocio(String numeroMovilSocio) {
        this.numeroMovilSocio = numeroMovilSocio;
    }


}
