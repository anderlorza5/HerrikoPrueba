package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import Formularios.CrearActividad;

public class CalendarioActivity extends AppCompatActivity {

    private Button volverHome;
    private Button abrirCrearAct;
    CalendarView calendario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);
        volverHome= findViewById(R.id.botonVolverHomeCalendario);
       abrirCrearAct= findViewById(R.id.botonAbrirCrearActividad);

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

        /*calendario = (CalendarView) findViewById(R.id.calendario);
        calendario.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Aquí puedes obtener la fecha seleccionada
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                // Aquí puedes buscar en tu base de datos si hay alguna actividad para esa fecha
                // Si hay una actividad, puedes abrir la nueva actividad pasando la fecha o cualquier otra información necesaria

                Intent intent = new Intent(CalendarioActivity.this, ActividadActivity.class);
                intent.putExtra("selectedDate", selectedDate);
                startActivity(intent);
            }
        });*/
    }
}