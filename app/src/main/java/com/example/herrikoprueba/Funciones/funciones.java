package com.example.herrikoprueba.Funciones;

import static android.content.ContentValues.TAG;



import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.herrikoprueba.ActividadActivity;
import com.example.herrikoprueba.CalendarioActivity;
import com.example.herrikoprueba.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
}
