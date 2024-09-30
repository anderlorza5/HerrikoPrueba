package com.example.herrikoprueba.Funciones;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.herrikoprueba.Clases.CrearQR;
import com.example.herrikoprueba.Clases.SendMail;
import com.example.herrikoprueba.HomeActivity;
import com.example.herrikoprueba.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class funciones {

    /*private void ResaltarMesActual(Button) {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // Enero es 0, Febrero es 1, etc.

        String mes = "";

        Button button;
        switch (month) {
            case 0: // Enero
                button = findViewById(R.id.botonEnero);
                mes = "01";
                break;
            case 1: // Febrero
                button = findViewById(R.id.botonFebrero);
                mes = "02";
                break;
            case 2: // Marzo
                button = findViewById(R.id.botonMarzo);
                mes = "03";
                break;
            case 3: // Abril
                button = findViewById(R.id.botonAbril);
                mes = "04";
                break;
            case 4: // Mayo
                button = findViewById(R.id.botonMayo);
                mes = "05";
                break;
            case 5: // Junio
                button = findViewById(R.id.botonJunio);
                mes = "06";
                break;
            case 6: // Julio
                button = findViewById(R.id.botonJulio);
                mes = "07";
                break;
            case 7: // Agosto
                button = findViewById(R.id.botonAgosto);
                mes = "08";
                break;
            case 8: // Septiembre
                button = findViewById(R.id.botonSeptiembre);
                mes = "09";
                break;
            case 9: // Octubre
                button = findViewById(R.id.botonOctubre);
                mes = "10";
                break;
            case 10: // Noviembre
                button = findViewById(R.id.botonNoviembre);
                mes = "11";
                break;
            case 11: // Diciembre
                button = findViewById(R.id.botonDiciembre);
                mes = "12";
                break;
            default:
                button = null;
        }

        if (button != null) {

            GradientDrawable border = new GradientDrawable();
            // border.setColor(Color.WHITE); // Set the color of the button
            border.setStroke(20, Color.RED); // Set the width and color of the border
            border.setCornerRadius(7); // Set the radius of the corners
            button.setBackground(border);
            button.setBackgroundColor(Color.RED);

            /*if(EncontrarMes(mes,button)==1) {
                int translucentRed = Color.argb(100, 255, 0, 0); // Rojo translúcido
                button.setBackgroundColor(translucentRed);
            }

        }
    }*/


    public static int EncontrarMes(String mes, Button botonMes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String targetDate = currentYear + "/" + mes;
        AtomicBoolean isMonthFound = new AtomicBoolean(false);
        // AtomicBoolean hayactividad= new AtomicBoolean(false);
        AtomicInteger i = new AtomicInteger();

        db.collection("Actividades")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String fecha = document.getString("fecha");
                            if (fecha != null && fecha.startsWith(targetDate)) {
                                isMonthFound.set(true);
                                i.set(1);
                                break;

                            }
                        }
                        // Si no se encontraron documentos para el mes, deshabilita el botón
                        if (!isMonthFound.get()) {
                            botonMes.setEnabled(false);

                        }else{
                            // Creación de un objeto GradientDrawable para definir color de fondo y esquinas redondeadas
                            GradientDrawable gradientDrawable = new GradientDrawable();
                            gradientDrawable.setColor(ContextCompat.getColor(botonMes.getContext(), R.color.verdeHerriko)); // color de fondo
                            gradientDrawable.setCornerRadius(10); // radio de las esquinas redondeadas

                            // Aplicación del GradientDrawable como fondo del botón
                            botonMes.setBackground(gradientDrawable);
                        }
                    } else {
                        Log.d(TAG, "Error obteniendo los documentos: ", task.getException());
                    }
                });
        return i.get();
    }

/*    private View.OnClickListener SacarListaActividades = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Obtén la etiqueta del botón pulsado
            String mes = (String) v.getTag();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Actividades")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                List<String> activitiesList = new ArrayList<>();
                                // Recoger el año actual
                                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String fecha = document.getString("fecha");
                                    if (fecha != null && fecha.startsWith(currentYear + "/" + mes)) {
                                        activitiesList.add(document.getString("nombre"));
                                    }
                                }
                                activitiesList.add("cancelar");
                                String[] activitiesArray = activitiesList.toArray(new String[0]);

                                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarioActivity.this);
                                AlertDialog dialog = builder.setTitle("Selecciona una actividad")
                                        .setItems(activitiesArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                if (activitiesArray[which].equals("cancelar")) {
                                                    dialogInterface.dismiss();
                                                } else {
                                                    Intent intent = new Intent(CalendarioActivity.this, ActividadActivity.class);
                                                    intent.putExtra("nombreActividad", activitiesArray[which]);
                                                    startActivity(intent);
                                                }
                                            }
                                        })
                                        .create();
                                dialog.show();
                            } else {
                                Log.d(TAG, "Error obteniendo los documentos: ", task.getException());
                            }
                        }
                    });
        }
    };*/

    // Método para obtener el nombre completo de SharedPreferences
    public static String obtenerNombreCompleto(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("nombre", ""); // "" es el valor predeterminado si "nombre" no se encuentra
    }
    public static String obtenerNumero(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("numero", ""); // "" es el valor predeterminado si "nombre" no se encuentra
    }

    // Método para obtener el primer nombre de SharedPreferences
    public static String obtenerPrimerNombre(Context context) {
        String nombreCompleto = obtenerNombreCompleto(context);

        // Divide el nombreCompleto en partes, asumiendo que las partes están separadas por espacios
        String[] partes = nombreCompleto.split(" ");

        // La primera parte debería ser el primer nombre
        return partes[0];
    }

    //metodo para borrar el nombre de la cache
    public static void borrarNombreCompleto(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("nombre");
        editor.apply();
    }

    // Método para verificar si el nombre completo existe en SharedPreferences
    public static boolean existeNombreCompleto(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.contains("nombre");
    }
    public static boolean existeNumeroMovil(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.contains("numero");
    }

    //este es un metodo al que se le pasa dos pantallas y un boton, es el boton de socio que segun si estas validado o no bare una patnall o otra
    public static void setValidarBotonClick(Context context, Button button,
                                            Class<? extends Activity> activityIfNombreExists,
                                            Class<? extends Activity> activityIfNombreDoesntExist) {

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean existeNombre = existeNombreCompleto(context);
                Intent intent;
                if (existeNombre) {
                    intent = new Intent(context, activityIfNombreExists);
                } else {
                    intent = new Intent(context, activityIfNombreDoesntExist);
                }
                context.startActivity(intent);
            }
        });
    }
    //duncion que revisa si hay nombre en cache para poner un texto o otro en el boton

    public static void cambiarTextoSiNombreExiste(Context context, Button button) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains("nombre")) {
            String nombre = sharedPreferences.getString("nombre", "");
            button.setText(nombre);
        } else {
            button.setText("Validar cuenta socio");
        }
    }


    //este metodo une los dos de arriba en uno

    public static void setBotonTextoYComportamiento(Context context, Button button,
                                                    Class<? extends Activity> activityIfNombreExists,
                                                    Class<? extends Activity> activityIfNombreDoesntExist) {

        // Cambiamos el texto del botón.
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        if (sharedPreferences.contains("nombre")) {
            String nombreCompleto = sharedPreferences.getString("nombre", "");
            String primerNombre = nombreCompleto.split(" ")[0]; // Esto dividirá el nombre completo por los espacios y tomará el primer elemento, que debería ser el primer nombre.
            button.setText(primerNombre);
        } else {
            button.setText("Validar cuenta socio");
        }

        // Establecemos el comportamiento al hacer click.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean existeNombre = sharedPreferences.contains("nombre");
                Intent intent;
                if (existeNombre) {
                    intent = new Intent(context, activityIfNombreExists);
                } else {
                    intent = new Intent(context, activityIfNombreDoesntExist);
                }
                context.startActivity(intent);
            }
        });
    }

    //metodo para mostrar mensaje por pantalla
    public static void mostrarMensaje(Context context, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra el dialogo cuando el usuario pulsa "Aceptar"
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //muestra mensaje y cierra pantalla
    public static void mostrarMensajeCerrar(Activity activity, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(mensaje)
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Cierra la actividad cuando el usuario pulsa "Aceptar"
                        dialog.dismiss();
                        Intent intent = new Intent(activity, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                        activity.finishAffinity();
                        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }



    //metodo para obtener numero de las preferencias
    public static String obtenerNumeroPreferencias(Context context) {
        // Obtén una instancia de SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("numero", ""); // "" es el valor predeterminado si "nombre" no se encuentra
    }

    public static String obtenerEmailPreferencias(Context context) {
        // Obtén una instancia de SharedPreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("email", ""); // "" es el valor predeterminado si "nombre" no se encuentra
    }

    //metodo para saber si es super ususario
    public static CompletableFuture<Boolean> esSuperUsuario(Context context) {
        // Crear un CompletableFuture para manejar la devolución de la llamada
        final CompletableFuture<Boolean> future = new CompletableFuture<>();

        // Obtén la instancia de FirebaseFirestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Obtén el nombre y el número de usuario de las SharedPreferences
        String nombreCompleto = funciones.obtenerNombreCompleto(context);
        String numeroTelefono = funciones.obtenerNumeroPreferencias(context);

        // Comprueba si el nombre y el número no están vacíos
        if (!nombreCompleto.isEmpty() && !numeroTelefono.isEmpty()) {
            // Busca en la colección "socios" el documento que tiene el nombre y número de teléfono
            db.collection("Socios")
                    .whereEqualTo("nombreCompleto", nombreCompleto)
                    .whereEqualTo("movilNumero", numeroTelefono)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Comprueba si el campo "superUsuario" es true
                                if (document.getBoolean("superUsuario") != null && document.getBoolean("superUsuario")) {
                                    future.complete(true);
                                    return;
                                }
                            }
                        }
                        future.complete(false);
                    });
        } else {
            future.complete(false);
        }

        return future;
    }

    //crea un PDF
    public static ByteArrayOutputStream CrearPDF(String content) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdfDoc);

        document.add(new Paragraph(content));
        document.close();

        return outputStream;
    }

    // crear PDF con imagen
    public static ByteArrayOutputStream CrearPDFConImagen(Bitmap qrBitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(outputStream));
        Document document = new Document(pdfDoc);

        try {
            // Convertir el Bitmap a byte[]
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            // Crear ImageData con el byte[]
            ImageData imageData = ImageDataFactory.create(byteArray);

            // Agregar la ImageData al documento como un Image
            Image image = new Image(imageData);
            document.add(new Paragraph("Aquí tienes tu código QR:"));
            document.add(image);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return outputStream;
    }

    //enviar correo con pdf
    public static void enviarInscritosPorCorreo(final String inscritos) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Destinatario del correo
                    String to = "ander210194@gmail.com";  // Reemplaza con el correo al que deseas enviar

                    // Asunto del correo
                    String subject = "Lista de Inscritos";

                    // Texto del correo
                    String text = "Hola, este es un correo con la lista de inscritos en PDF.";

                    // Contenido del PDF (la lista de inscritos)
                    String pdfContent = inscritos;

                    // Genera el PDF y obtén el ByteArrayOutputStream
                    ByteArrayOutputStream pdfStream = funciones.CrearPDF(pdfContent);

                    // Envia el correo con el PDF adjunto
                    SendMail.sendConPDF(to, subject, text, pdfStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

    public static void enviarQRCodePorCorreo(final String uniqueID) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // Destinatario del correo
                    String to = "ander210194@gmail.com";  // Reemplaza con el correo al que deseas enviar

                    // Asunto del correo<
                    String subject = "Tu código QR";

                    // Texto del correo
                    String text = "Hola, este es un correo con tu código QR en PDF.";

                    // Genera el código QR como Bitmap
                    Bitmap qrBitmap = CrearQR.generateQRCode(uniqueID);

                    // Usa el Bitmap para crear un PDF
                    ByteArrayOutputStream pdfStream = funciones.CrearPDFConImagen(qrBitmap);

                    // Envia el correo con el PDF adjunto
                    SendMail.sendConPDF(to, subject, text, pdfStream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

// guardar id del QR en la DB

    public static void guardarQREnDB(String userID, String eventID, String uniqueID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Crear un nuevo documento en la colección 'tickets'
        Map<String, Object> newTicket = new HashMap<>();
        newTicket.put("userID", userID); // Suponiendo que ya tienes el userID
        newTicket.put("eventID", eventID); // Suponiendo que ya tienes el eventID
        newTicket.put("uniqueQRCode", uniqueID);
        newTicket.put("used", false); // Inicialmente, el ticket no ha sido usado

// Agregar el nuevo ticket a la colección
        db.collection("Tickets").add(newTicket)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Documento añadido con ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error al añadir el documento", e);
                    }
                });

    }

}
