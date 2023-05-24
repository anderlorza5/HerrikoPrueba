package com.example.herrikoprueba.Clases;

import java.util.Calendar;

public class Constantes {

    //public static String getAñoActual;
    String añoActual = Calendar.getInstance().get(Calendar.YEAR)+"";
    String mesActual = Calendar.getInstance().get(Calendar.MONTH)+"";
    boolean logeado= false;

    public  String getAñoActual() {
        return añoActual;
    }

    public boolean isLogeado() {
        return logeado;
    }

    public void setLogeado(boolean logeado) {
        this.logeado = logeado;
    }
}
