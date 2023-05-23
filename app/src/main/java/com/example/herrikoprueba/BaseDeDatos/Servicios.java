package com.example.herrikoprueba.BaseDeDatos;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Servicios {

    public void crearActividadDB(String nombre, String descripcion, String lugar, String fecha, String horaInicio, String horaFinal, Boolean sePaga, Double precio) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String year = fecha.substring(0, 4);

        // Crear el ID con el nombre y el año
        String id = nombre + year;

        Map<String, Object> activity = new HashMap<>();
        activity.put("id", id);
        activity.put("nombre", nombre);
        activity.put("descripcion", descripcion);
        activity.put("lugar", lugar);
        activity.put("fecha", fecha);
        activity.put("horaInicio", horaInicio);
        activity.put("horaFinal", horaFinal);
        activity.put("sePaga", sePaga);
        activity.put("precio", precio);

        db.collection("Actividades").document(id).set(activity);

    }


}
