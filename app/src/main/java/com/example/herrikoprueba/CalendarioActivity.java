package com.example.herrikoprueba;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.herrikoprueba.Formularios.CrearActividad;
import com.example.herrikoprueba.Funciones.funciones;
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

public class CalendarioActivity extends AppCompatActivity {

    private Button volverHome;
    private Button abrirCrearAct;
   // CalendarView calendario;

  /* String mes="Enero";
    String prueba1="prueba1";
    String prueba2="prueba2";
    String[] array = {mes,prueba1, prueba2};*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        volverHome= findViewById(R.id.botonVolverHomeCalendario);
       abrirCrearAct= findViewById(R.id.botonAbrirCrearActividad);

        Button botonEnero = findViewById(R.id.botonEnero);
        botonEnero.setOnClickListener(SacarListaActividades);

        Button botonFebrero = findViewById(R.id.botonFebrero);
        botonFebrero.setOnClickListener(SacarListaActividades);

        Button botonMarzo = findViewById(R.id.botonMarzo);
        botonMarzo.setOnClickListener(SacarListaActividades);

        Button botonAbril = findViewById(R.id.botonAbril);
        botonAbril.setOnClickListener(SacarListaActividades);

        Button botonMayo = findViewById(R.id.botonMayo);
        botonMayo.setOnClickListener(SacarListaActividades);

        Button botonJunio = findViewById(R.id.botonJunio);
        botonJunio.setOnClickListener(SacarListaActividades);

        Button botonJulio = findViewById(R.id.botonJulio);
        botonJulio.setOnClickListener(SacarListaActividades);

        Button botonAgosto = findViewById(R.id.botonAgosto);
        botonAgosto.setOnClickListener(SacarListaActividades);

        Button botonSeptiembre = findViewById(R.id.botonSeptiembre);
        botonSeptiembre.setOnClickListener(SacarListaActividades);

        Button botonOctubre = findViewById(R.id.botonOctubre);
        botonOctubre.setOnClickListener(SacarListaActividades);

        Button botonNoviembre = findViewById(R.id.botonNoviembre);
        botonNoviembre.setOnClickListener(SacarListaActividades);

        Button botonDiciembre = findViewById(R.id.botonDiciembre);
        botonDiciembre.setOnClickListener(SacarListaActividades);


        funciones.EncontrarMes("01", findViewById(R.id.botonEnero));
        funciones.EncontrarMes("02", findViewById(R.id.botonFebrero));
        funciones.EncontrarMes("03", findViewById(R.id.botonMarzo));
        funciones.EncontrarMes("04", findViewById(R.id.botonAbril));
        funciones.EncontrarMes("05", findViewById(R.id.botonMayo));
        funciones.EncontrarMes("06", findViewById(R.id.botonJunio));
        funciones.EncontrarMes("07", findViewById(R.id.botonJulio));
        funciones.EncontrarMes("08", findViewById(R.id.botonAgosto));
        funciones.EncontrarMes("09", findViewById(R.id.botonSeptiembre));
        funciones.EncontrarMes("10", findViewById(R.id.botonOctubre));
        funciones.EncontrarMes("11", findViewById(R.id.botonNoviembre));
        funciones.EncontrarMes("12", findViewById(R.id.botonDiciembre));
        ResaltarMesActual();





        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarioActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });

        abrirCrearAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalendarioActivity.this, CrearActividad.class);
                startActivity(intent);
            }
        });




    }

    private View.OnClickListener SacarListaActividades = new View.OnClickListener() {
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
                                List<String> activitiesNames = new ArrayList<>();
                                List<String> activitiesIds = new ArrayList<>();
                                // Recoger el año actual
                                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String fecha = document.getString("fecha");
                                    if (fecha != null && fecha.startsWith(currentYear + "/" + mes)) {
                                        activitiesNames.add(document.getString("nombre"));
                                        activitiesIds.add(document.getId()); //guardamos el id de la actividad
                                    }
                                }
                                activitiesNames.add("cancelar");
                                String[] activitiesArray = activitiesNames.toArray(new String[0]);

                                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarioActivity.this);
                                AlertDialog dialog = builder.setTitle("Selecciona una actividad")
                                        .setItems(activitiesArray, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialogInterface, int which) {
                                                if (activitiesArray[which].equals("cancelar")) {
                                                    dialogInterface.dismiss();
                                                } else {
                                                    Intent intent = new Intent(CalendarioActivity.this, ActividadActivity.class);
                                                    intent.putExtra("idActividad", activitiesIds.get(which)); //pasamos el id en vez del nombre
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
    };


   /* private void checkIfThereAreActivitiesForMonth(String mes, Button botonMes) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        db.collection("Actividades")

                .whereGreaterThanOrEqualTo("fecha", currentYear + "/" + mes + "/01")
                .whereLessThan("fecha", currentYear + "/" + (Integer.parseInt(mes)+1) + "/01")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Si no hay documentos para el mes, deshabilita el botón
                            if (task.getResult().isEmpty()) {
                                botonMes.setEnabled(false);
                            }
                        } else {
                            Log.d(TAG, "Error obteniendo los documentos: ", task.getException());
                        }
                    }
                });
    }
*/



    private void ResaltarMesActual() {
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
            }*/

        }
    }


}