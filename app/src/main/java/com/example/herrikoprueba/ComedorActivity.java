package com.example.herrikoprueba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;

import com.example.herrikoprueba.BaseDeDatos.ImportarSocios;
import com.example.herrikoprueba.Clases.BaseActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ComedorActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_comedor;  // Retorno el layout específico de MainActivity
    }

    private Button volverHome;
     Button crearComedor;
    EditText nombre;

    FirebaseFirestore mfFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_comedor);

        CalendarView calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Convertir la fecha a un objeto Date
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date selectedDate = calendar.getTime();

                // Formatear la fecha a un String
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String formattedDate = format.format(selectedDate);

                // Crear el Intent para iniciar la segunda actividad
                Intent intent = new Intent(ComedorActivity.this, ReservaActivity.class);
                // Pasar la fecha formateada a la segunda actividad
                intent.putExtra("selectedDate", formattedDate);
                // Iniciar la segunda actividad
                startActivity(intent);
            }
        });

    }
}