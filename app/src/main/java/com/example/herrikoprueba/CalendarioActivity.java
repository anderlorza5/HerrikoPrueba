package com.example.herrikoprueba;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Formularios.CrearActividad;
import com.example.herrikoprueba.Funciones.funciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarioActivity extends BaseActivity {
    private Button abrirCrearAct;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Map<Button, String> monthButtonMap = new HashMap<>();
    private static final String CANCEL_OPTION = "Cancelar";

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_calendario;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        abrirCrearAct = findViewById(R.id.botonAbrirCrearActividad);

        // Configurar los botones para cada mes
        setMonthButtons();
        // Configurar el botón para crear una nueva actividad
        configureCrearActividadButton();
        // Resaltar el mes actual
        resaltarMesActual();

        // Verificar si el usuario es un super usuario y mostrar el botón de crear actividad si es el caso
        funciones.esSuperUsuario(this).thenAccept(superUsuario -> {
            if (superUsuario) {
                abrirCrearAct.setVisibility(View.VISIBLE);
            }
        });
    }

    // Método para configurar los botones de cada mes
    private void setMonthButtons() {
        int[] monthButtons = {
                R.id.botonEnero, R.id.botonFebrero, R.id.botonMarzo, R.id.botonAbril,
                R.id.botonMayo, R.id.botonJunio, R.id.botonJulio, R.id.botonAgosto,
                R.id.botonSeptiembre, R.id.botonOctubre, R.id.botonNoviembre, R.id.botonDiciembre
        };
        // Asignar a cada botón el mes correspondiente usando un mapa
        for (int i = 0; i < monthButtons.length; i++) {
            Button button = findViewById(monthButtons[i]);
            String month = String.format("%02d", i + 1);
            monthButtonMap.put(button, month);
            button.setOnClickListener(sacarListaActividades); // Configurar el listener para mostrar las actividades del mes
            funciones.EncontrarMes(month, button); // Resaltar el mes si tiene actividades
        }
    }

    // Configurar el botón para abrir la actividad de crear una nueva actividad
    private void configureCrearActividadButton() {
        abrirCrearAct.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarioActivity.this, CrearActividad.class);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error al iniciar CrearActividad: ", e);
            }
        });
    }

    // Listener para mostrar la lista de actividades de un mes seleccionado
    private final View.OnClickListener sacarListaActividades = v -> {
        String mes = monthButtonMap.get(v); // Obtener el mes del botón presionado desde el mapa
        int currentYear = Calendar.getInstance().get(Calendar.YEAR); // Obtener el año actual
        db.collection("Actividades")
                .whereGreaterThanOrEqualTo("fecha", currentYear + "/" + mes + "/01")
                .whereLessThan("fecha", currentYear + "/" + mes + "/32")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> activitiesNames = new ArrayList<>();
                        List<String> activitiesIds = new ArrayList<>();
                        // Filtrar actividades del mes actual
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String fecha = document.getString("fecha");
                            if (fecha != null && fecha.startsWith(currentYear + "/" + mes)) {
                                activitiesNames.add(document.getString("nombre"));
                                activitiesIds.add(document.getId());
                            }
                        }
                        activitiesNames.add(CANCEL_OPTION); // Agregar la opción de cancelar
                        showActivityDialog(activitiesNames, activitiesIds); // Mostrar el diálogo con la lista de actividades
                    } else {
                        Log.d(TAG, "Error obteniendo los documentos: ", task.getException());
                    }
                });
    };

    // Mostrar un diálogo para que el usuario seleccione una actividad
    private void showActivityDialog(List<String> activitiesNames, List<String> activitiesIds) {
        String[] activitiesArray = activitiesNames.toArray(new String[0]);
        new AlertDialog.Builder(CalendarioActivity.this)
                .setTitle("Selecciona una actividad")
                .setItems(activitiesArray, (dialogInterface, which) -> {
                    if (activitiesArray[which].equals(CANCEL_OPTION)) {
                        dialogInterface.dismiss(); // Cerrar el diálogo si se elige cancelar
                    } else {
                        Intent intent = new Intent(CalendarioActivity.this, ActividadActivity.class);
                        intent.putExtra("idActividad", activitiesIds.get(which)); // Pasar el ID de la actividad seleccionada
                        try {
                            startActivity(intent);
                            finish();
                        } catch (Exception e) {
                            Log.e(TAG, "Error al iniciar ActividadActivity: ", e);
                        }
                    }
                })
                .create()
                .show();
    }

    // Resaltar el botón del mes actual
    private void resaltarMesActual() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH); // Enero es 0, Febrero es 1, etc.
        int[] monthButtons = {
                R.id.botonEnero, R.id.botonFebrero, R.id.botonMarzo, R.id.botonAbril,
                R.id.botonMayo, R.id.botonJunio, R.id.botonJulio, R.id.botonAgosto,
                R.id.botonSeptiembre, R.id.botonOctubre, R.id.botonNoviembre, R.id.botonDiciembre
        };

        // Resaltar el botón del mes actual cambiando el fondo y agregando un borde
        if (month >= 0 && month < monthButtons.length) {
            Button button = findViewById(monthButtons[month]);
            GradientDrawable border = new GradientDrawable();
            border.setStroke(20, Color.RED); // Borde rojo
            border.setCornerRadius(7); // Bordes redondeados
            button.setBackground(border);
            button.setBackgroundColor(Color.RED); // Fondo rojo
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        // Código para actualizar tu actividad
        // Por ejemplo, podrías llamar a una función que actualiza la interfaz de usuario
        //funciones.setBotonTextoYComportamiento(this, validarBoton, PantallaSocio.class, ValidarSocio.class);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //recreate();
        //SacarListaActividades();

    }

}