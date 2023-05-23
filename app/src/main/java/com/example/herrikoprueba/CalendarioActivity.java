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

        EncontrarMes("01", findViewById(R.id.botonEnero));
        EncontrarMes("02", findViewById(R.id.botonFebrero));
        EncontrarMes("03", findViewById(R.id.botonMarzo));
        EncontrarMes("04", findViewById(R.id.botonAbril));
        EncontrarMes("05", findViewById(R.id.botonMayo));
        EncontrarMes("06", findViewById(R.id.botonJunio));
        EncontrarMes("07", findViewById(R.id.botonJulio));
        EncontrarMes("08", findViewById(R.id.botonAgosto));
        EncontrarMes("09", findViewById(R.id.botonSeptiembre));
        EncontrarMes("10", findViewById(R.id.botonOctubre));
        EncontrarMes("11", findViewById(R.id.botonNoviembre));
        EncontrarMes("12", findViewById(R.id.botonDiciembre));
        highlightCurrentMonthButton();


        /*Spinner spinnerEnero = findViewById(R.id.enero);
        spinnerEnero.setPrompt("Enero");
        Spinner spinnerFebrero = findViewById(R.id.febrero);*/

        //prueba spinner

        /*ArrayAdapter<String> adapterEnero = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, array){
        @Override
        public boolean isEnabled(int position) {
            // Deshabilita el primer elemento
            return position != 0;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view = super.getDropDownView(position, convertView, parent);
            TextView tv = (TextView) view;
            if (position == 0) {
                // Establece el color del primer elemento a gris
                tv.setTextColor(Color.GRAY);
            } else {
                tv.setTextColor(Color.BLACK);
            }
            return view;
        }
    };
        adapterEnero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnero.setAdapter(adapterEnero);

        spinnerEnero.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String actividadSeleccionada = (String) parent.getItemAtPosition(position);
                Toast.makeText(CalendarioActivity.this, actividadSeleccionada, Toast.LENGTH_SHORT).show();
                // Aquí puedes hacer la lógica para mostrar la información de la actividad seleccionada
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // En caso de que no se seleccione ningún ítem
            }
        });*/


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




        /*Button button = findViewById(R.id.botonMarzo);
        String cancelar = "cancelar";
        ArrayList<String> activitiesList = new ArrayList<>();
        String[] activitiesArray = new String[]{"Actividad1", "Actividad2"};

        //activitiesList.add(String.valueOf(activitiesArray));


        activitiesList.add("Actividad1");
        activitiesList.add("Actividad2");
        activitiesList.add(cancelar);
        activitiesList = new ArrayList<>(Arrays.asList(activitiesArray));
        activitiesList.add(cancelar);
        activitiesArray = activitiesList.toArray(new String[0]);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CalendarioActivity.this);
                builder.setTitle("Selecciona una actividad")
                        .setItems(activitiesArray, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // El parámetro 'which' es el índice del ítem pulsado
                                Toast.makeText(CalendarioActivity.this, "Seleccionado: " + activitiesArray[which], Toast.LENGTH_SHORT).show();
                            }
                        });
                builder.show();
            }
        });
*/

        Button button = findViewById(R.id.botonEnero);
        String cancelar = "cancelar";

// Crear una ArrayList con las actividades
        ArrayList<String> activitiesList = new ArrayList<>();
        activitiesList.add("Actividad1");
        activitiesList.add("Actividad2");

// Añadir "cancelar" a la lista
        activitiesList.add(cancelar);

// Convertir la ArrayList a un array
        String[] activitiesArray = activitiesList.toArray(new String[0]);

       /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("Actividades")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<String> activitiesList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String fecha = document.getString("fecha");
                                        int añoActual = Calendar.getInstance().get(Calendar.YEAR);
                                        if (fecha != null && fecha.startsWith(añoActual +"/01")) { // Recoger solo las actividades de Enero de 2023
                                            activitiesList.add(document.getString("nombre"));
                                        }
                                    }
                                    activitiesList.add(cancelar); // Agregar la opción de cancelar al final
                                    String[] activitiesArray = activitiesList.toArray(new String[0]);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(CalendarioActivity.this);
                                    AlertDialog dialog = builder.setTitle("Selecciona una actividad")
                                            .setItems(activitiesArray, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialogInterface, int which) {
                                                    if (activitiesArray[which].equals(cancelar)) {
                                                        dialogInterface.dismiss();
                                                    } else {
                                                        //niciar la Activity de detalles con
                                                        //la actividad seleccionada
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

        });*/

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
    };

    private void checkIfThereAreActivitiesForMonth(String mes, Button botonMes) {
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

    private int EncontrarMes(String mes, Button botonMes) {
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


    private void highlightCurrentMonthButton() {
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
            button.setBackgroundColor(Color.RED);
            GradientDrawable border = new GradientDrawable();
           // border.setColor(Color.WHITE); // Set the color of the button
            border.setStroke(2, Color.BLACK); // Set the width and color of the border
            border.setCornerRadius(5); // Set the radius of the corners
            button.setBackground(border);

            /*if(EncontrarMes(mes,button)==1) {
                int translucentRed = Color.argb(100, 255, 0, 0); // Rojo translúcido
                button.setBackgroundColor(translucentRed);
            }*/

        }
    }


}