package Clases;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

public class Actividad {

    String id;
    String nombre;
    String descripcion;
    String lugar;
    Date fecha;
    String horaInicio;
    String horaFinal;
    Boolean sePaga;
    Double precio;

    // Constructor vacío necesario para Firestore
    public Actividad() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFinal() {
        return horaFinal;
    }

    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

    public boolean getSePaga() {
        return sePaga;
    }

    public void setSePaga(boolean sePaga) {
        this.sePaga = sePaga;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }



    public static Actividad ConvertirEnActividad(DocumentSnapshot document) {
        Actividad actividad = new Actividad();
        actividad.setId(document.getId());
        actividad.setNombre(document.getString("nombre"));
        actividad.setDescripcion(document.getString("descripcion"));
        actividad.setLugar(document.getString("lugar"));
        actividad.setFecha(document.getDate("fecha"));
        actividad.setHoraInicio(document.getString("horaInicio"));
        actividad.setHoraFinal(document.getString("horaFinal"));
        actividad.setSePaga(document.getBoolean("sePaga"));
        actividad.setPrecio(document.getDouble("precio"));
        return actividad;
    }

}
