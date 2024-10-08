package com.example.herrikoprueba.BaseDeDatos;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.herrikoprueba.Clases.Actividad;
import com.example.herrikoprueba.Clases.BebidaComedor;
import com.example.herrikoprueba.Clases.Reservas;
import com.example.herrikoprueba.Clases.Socio;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servicios {

    public interface ActividadCallback {
        void onCallback(Actividad actividad);
    }
    public interface FirestoreMultipleCallback {
        void onCallback(List<DocumentSnapshot> documentSnapshots);
    }
    public interface FirestoreListCallback {
        void onCallback(List<Reservas> documentos);
    }

    public interface FirestoreListCallbackk {
        void onCallback(List<DocumentSnapshot> documentos);
    }
    public interface CallbackListaReservas {
        void onCallback(List<Reservas> listaReservas);
    }

    public interface FirestoreListCallbackBebida<T> {
        void onCallback(List<T> object);
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

    public static void insertarSocio2(String nombreCompleto, String movilNumero, String email, String dni) { // **CHANGED: Añadido parámetro dni**
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtener la referencia de la colección "Socios"
        CollectionReference sociosRef = db.collection("Socios");

        // Obtener el último ID de socio y sumar 1
        sociosRef.orderBy("id", Query.Direction.DESCENDING)
                .limit(1) // **OPTIMIZATION: Limitar a 1 documento para eficiencia**
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    int ultimoId = 0; // **CHANGED: Inicializar desde 0**
                    if (!queryDocumentSnapshots.isEmpty()) {
                        Socio ultimoSocio = queryDocumentSnapshots.getDocuments().get(0).toObject(Socio.class);
                        if (ultimoSocio != null) {
                            ultimoId = ultimoSocio.getId();
                        }
                    }
                    int nuevoId = ultimoId + 1;

                    // Crear el objeto Socio con los datos proporcionados, incluyendo DNI
                    Socio nuevoSocio = new Socio(nuevoId, nombreCompleto, movilNumero, email, dni); // **CHANGED: Inclusión de DNI**

                    // Insertar el nuevo socio en la base de datos
                    sociosRef.document(String.valueOf(nuevoId)).set(nuevoSocio)
                            .addOnSuccessListener(aVoid -> {
                                // Éxito en la inserción
                                Log.d("Servicios", "Socio insertado correctamente");
                            })
                            .addOnFailureListener(e -> {
                                // Error en la inserción
                                Log.e("Servicios", "Error al insertar el socio", e);
                            });
                })
                .addOnFailureListener(e -> {
                    // Error al obtener el último ID de socio
                    Log.e("Servicios", "Error al obtener el último ID de socio", e);
                });
    }




    //este metodo inscribe en una actividad a un ususario mediante el nombre y el numero
    public static void agregarInscrito(String actividadId, String nombreCompleto, String numeroTelefono) {
        // Obtén la instancia de FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtén una referencia al documento de la colección "actividades"
        DocumentReference actividadRef = db.collection("Actividades").document(actividadId);

        actividadRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot actividadDocument = task.getResult();
                    if (actividadDocument.exists()) {
                        Long ultimoIdInscripcion = actividadDocument.getLong("ultimoIdInscripcion");

                        // Comprueba si el campo "inscritos" existe, si no existe, inicializa el último ID de inscripción a 0
                        if (ultimoIdInscripcion == null) {
                            ultimoIdInscripcion = 0L;
                        }

                        // Incrementa el último ID de inscripción
                        long nuevoIdInscripcion = ultimoIdInscripcion + 1;

                        // Crea un mapa con los nuevos datos a añadir a la subcolección
                        Map<String, Object> nuevosDatos = new HashMap<>();

                        nuevosDatos.put("idInscripcion", nuevoIdInscripcion);
                        nuevosDatos.put("nombre", nombreCompleto);
                        nuevosDatos.put("telefono", numeroTelefono);

                        // Actualiza el campo "inscritos" con los nuevos datos y el último ID de inscripción
                        actividadRef.update("inscritos", FieldValue.arrayUnion(nuevosDatos), "ultimoIdInscripcion", nuevoIdInscripcion)
                                .addOnSuccessListener(aVoid -> {
                                    // Éxito en la actualización
                                    System.out.println("Datos añadidos a la subcolección 'inscritos' correctamente");
                                })
                                .addOnFailureListener(e -> {
                                    // Error en la actualización
                                    System.out.println("Error al añadir datos a la subcolección 'inscritos': " + e.getMessage());
                                });
                    } else {
                        System.out.println("No such document");
                    }
                } else {
                    System.out.println("get failed with: " + task.getException().toString());
                    task.getException().printStackTrace();
                }
            }
        });
    }

    // recoge un documento y lo convierte en un objeto reserva
    public static Reservas crearReservaDesdeFirestore(DocumentSnapshot documentSnapshot) {
        Reservas reserva = new Reservas();
        reserva.setId(documentSnapshot.getString("id"));
        reserva.setDiaReserva(documentSnapshot.getString("diaReserva"));
        reserva.setNºComensales(documentSnapshot.getLong("nºComensales").intValue());
        reserva.setHora(documentSnapshot.getString("hora"));
        reserva.setNombreSocio(documentSnapshot.getString("nombreSocio"));
        reserva.setNumeroMovilSocio(documentSnapshot.getString("numeroMovilSocio"));

        return reserva;
    }

    //coge un objeto reserva y lo guarda en la bse de datos
    public static void guardarReservaEnFirestore(Reservas reserva) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Referencia al documento que mantiene el último valor del ID
        DocumentReference lastIdRef = db.collection("Reservas").document("meta");

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot lastIdSnap = transaction.get(lastIdRef);
                int lastId = lastIdSnap.getLong("lastId").intValue();

                // Aumenta el "id" en uno para el nuevo documento
                int newId = lastId + 1;
                reserva.setId(String.valueOf(newId));

                // Añade la reserva a la colección
                transaction.set(
                        db.collection("Reservas").document(String.valueOf(newId)),
                        reserva
                );

                // Actualiza el último valor del ID
                transaction.update(lastIdRef, "lastId", newId);

                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Transacción exitosa!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error en la transacción", e);
            }
        });
    }




    public interface FirestoreCallback {
        void onCallback(DocumentSnapshot documentSnapshot);
    }

    //metodo que recoge una reserva de la base de datos a traves del campo id
    public static void getDocumentByFieldId( String fieldId, FirestoreCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Reservas")
                .whereEqualTo("diaReserva", fieldId)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Pasamos el primer documento a la función de devolución de llamada
                            callback.onCallback(task.getResult().getDocuments().get(0));
                        } else {
                            // Si no hay resultados, pasamos null
                            callback.onCallback(null);
                        }
                    } else {
                        Log.w(TAG, "Error getting document.", task.getException());
                        // Pasamos null en caso de error
                        callback.onCallback(null);
                    }
                });
    }

    //obtener lista de Reserva con fecha

    public static void getDocumentsByFieldDate(String fecha, FirestoreListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Reservas")
                .whereEqualTo("diaReserva", fecha)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Pasamos los documentos a la función de devolución de llamada
                            callback.onCallback(task.getResult().toObjects(Reservas.class));
                        } else {
                            // Si no hay resultados, pasamos null
                            callback.onCallback(null);
                        }
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        // Pasamos null en caso de error
                        callback.onCallback(null);
                    }
                });
    }


    public static void getReservasPorFecha(String fecha, CallbackListaReservas callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Reservas")
                .whereEqualTo("diaReserva", fecha)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Reservas> listaReservas = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Reservas reserva = crearReservaDesdeFirestore(document);
                            listaReservas.add(reserva);
                        }
                        callback.onCallback(listaReservas);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        // Pasamos null en caso de error
                        callback.onCallback(null);
                    }
                });
    }



//obtener lista de reservas de un socio
public static void obtenerReservasDesdeFirestore(Context context, FirestoreListCallbackk callback) {
    // Obtener los datos de socio desde las preferencias
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    String nombreSocio = sharedPreferences.getString("nombre", "");
    String numeroMovilSocio = sharedPreferences.getString("numero", "");

    // Consultar las reservas en la base de datos
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection("Reservas")
            .whereEqualTo("nombreSocio", nombreSocio)
            .whereEqualTo("numeroMovilSocio", numeroMovilSocio)
            .get()
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (!task.getResult().isEmpty()) {
                        // Pasar los documentos a la función de devolución de llamada
                        callback.onCallback(task.getResult().getDocuments());
                    } else {
                        // Si no hay resultados, pasar null
                        callback.onCallback(null);
                    }
                } else {
                    Log.w(TAG, "Error al obtener las reservas.", task.getException());
                    // Pasar null en caso de error
                    callback.onCallback(null);
                }
            });
}

    //obtener lista de reservas de un diaReserva
    public static void obtenerReservasPorDiaDesdeFirestore(Context context, String diaReserva, FirestoreListCallbackk callback) {

        // Consultar las reservas en la base de datos
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Reservas")
                .whereEqualTo("diaReserva", diaReserva)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            // Pasar los documentos a la función de devolución de llamada
                            callback.onCallback(task.getResult().getDocuments());
                        } else {
                            // Si no hay resultados, pasar null
                            callback.onCallback(null);
                        }
                    } else {
                        Log.w(TAG, "Error al obtener las reservas.", task.getException());
                        // Pasar null en caso de error
                        callback.onCallback(null);
                    }
                });
    }


    //convierte lista de docuemtos reserva en objeto reservas
    public static List<Reservas> convertirDocumentosAReservas(List<DocumentSnapshot> documentos) {
        List<Reservas> reservas = new ArrayList<>();
        for (DocumentSnapshot documento : documentos) {
            Reservas reserva = documento.toObject(Reservas.class);
            reservas.add(reserva);
        }
        return reservas;
    }

    //recoge las bebidas de la base de datos y devuelve una lsita de articulos
    public static void getBebidasComedor(FirestoreListCallbackBebida callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("BebidaComedor")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<BebidaComedor> listaBebidasComedor = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            BebidaComedor bebidaComedor = document.toObject(BebidaComedor.class);
                            bebidaComedor.setId(document.getId());
                            listaBebidasComedor.add(bebidaComedor);
                        }
                        callback.onCallback(listaBebidasComedor);
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        callback.onCallback(null);
                    }
                });
    }



}
