package com.example.herrikoprueba.BaseDeDatos;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.herrikoprueba.ActividadActivity;
import com.example.herrikoprueba.Clases.Actividad;
import com.example.herrikoprueba.Clases.Constantes;
import com.example.herrikoprueba.Clases.Socio;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Servicios {

    public interface ActividadCallback {
        void onCallback(Actividad actividad);
    }

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

    public void crearActividadDBConObjeto(Actividad actividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Crear el ID con el nombre y el año
        String year = actividad.getFecha().substring(0, 4);
        String id = actividad.getNombre() +year;
        actividad.setId(id);

        // Crear un HashMap para almacenar los datos de la actividad
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("id", actividad.getId());
        activityMap.put("nombre", actividad.getNombre());
        activityMap.put("descripcion", actividad.getDescripcion());
        activityMap.put("lugar", actividad.getLugar());
        activityMap.put("fecha", actividad.getFecha());
        activityMap.put("horaInicio", actividad.getHoraInicio());
        activityMap.put("horaFinal", actividad.getHoraFinal());
        activityMap.put("sePaga", actividad.getSePaga());
        activityMap.put("precio", actividad.getPrecio());

        // Agregar el documento a la base de datos
        db.collection("Actividades").document(id).set(activityMap);
    }

    public void modificarActividadDBConObjeto(Actividad actividad,String idActividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener el ID de la actividad
        idActividad = actividad.getId();

        // Crear un HashMap para almacenar los datos de la actividad
        Map<String, Object> activityMap = new HashMap<>();
        activityMap.put("nombre", actividad.getNombre());
        activityMap.put("descripcion", actividad.getDescripcion());
        activityMap.put("lugar", actividad.getLugar());
        activityMap.put("fecha", actividad.getFecha());
        activityMap.put("horaInicio", actividad.getHoraInicio());
        activityMap.put("horaFinal", actividad.getHoraFinal());
        activityMap.put("sePaga", actividad.getSePaga());
        activityMap.put("precio", actividad.getPrecio());

        // Actualizar el documento en la base de datos
        db.collection("Actividades").document(idActividad).update(activityMap);

    }


    public void recogerActividadDB(String idActividad, final ActividadCallback actividadCallback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Actividades").document(idActividad)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Actividad actividad = Actividad.ConvertirEnActividad(documentSnapshot);
                            // Utilizar el callback para devolver la actividad
                            actividadCallback.onCallback(actividad);
                        } else {
                            Log.d("ActividadActivity", "No existe un documento con ID: " + idActividad);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ActividadActivity", "Error obteniendo el documento: ", e);
                    }
                });
    }


    public void borrarActividadDB(String idActividad) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Actividades").document(idActividad)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // El documento se ha borrado exitosamente
                        Log.d(TAG, "Documento borrado con éxito");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Se produjo un error al intentar borrar el documento
                        Log.d(TAG, "Error al borrar el documento: " + e);
                    }
                });
    }

    public static void insertarSocio(String nombreCompleto, String movilNumero) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener la referencia de la colección "Socios"
        CollectionReference sociosRef = db.collection("Socios");

        // Obtener el último ID de socio y sumar 1
        sociosRef.orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int ultimoId = 2;
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Socio ultimoSocio = document.toObject(Socio.class);
                        ultimoId = ultimoSocio.getId();
                        break; // Salir del bucle después de obtener el primer documento
                    }
                    int nuevoId = ultimoId + 1;

                    // Crear el objeto Socio con los datos proporcionados
                    Socio nuevoSocio = new Socio(nuevoId, nombreCompleto, movilNumero);

                    // Insertar el nuevo socio en la base de datos
                    sociosRef.document(String.valueOf(nuevoId)).set(nuevoSocio)
                            .addOnSuccessListener(aVoid -> {
                                // Éxito en la inserción
                                Log.d(TAG, "Socio insertado correctamente");
                            })
                            .addOnFailureListener(e -> {
                                // Error en la inserción
                                Log.e(TAG, "Error al insertar el socio", e);
                            });
                })
                .addOnFailureListener(e -> {
                    // Error al obtener el último ID de socio
                    Log.e(TAG, "Error al obtener el último ID de socio", e);
                });
    }



}
