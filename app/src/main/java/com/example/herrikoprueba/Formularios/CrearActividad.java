package com.example.herrikoprueba.Formularios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.CalendarioActivity;


import com.example.herrikoprueba.R;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.herrikoprueba.Clases.Actividad;

public class CrearActividad extends AppCompatActivity {

    private Button volverHome;
    private Button crearActividad;
    private CheckBox CheckTicket;
    private EditText precio;
    private EditText nombre;
    private EditText lugar;
    private EditText horaInicio;
    private EditText horaFinal;
    private EditText Fecha;
    private EditText descripcion;
    private FirebaseFirestore db;
    Servicios servicios = new Servicios();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actividad);

        volverHome= findViewById(R.id.boronCancelarActividad);
        CheckTicket = findViewById(R.id.checkBoxSePaga);
        precio = findViewById(R.id.editTextPrecio);
        nombre = findViewById(R.id.editTextNombre);
        lugar = findViewById(R.id.editTextLugar);
        horaInicio = findViewById(R.id.editTextHoraInicio);
        horaFinal = findViewById(R.id.editTextHoraFinal);
        Fecha = findViewById(R.id.editTextFecha);
        descripcion = findViewById(R.id.editTextDescripcion);
        crearActividad = findViewById(R.id.botonCrearActividad);

        db = FirebaseFirestore.getInstance();

        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CrearActividad.this, CalendarioActivity.class);
                startActivity(intent);
            }
        });

        CheckTicket.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    // Si el CheckBox está marcado, habilita el EditText
                    precio.setEnabled(true);
                } else {
                    // Si el CheckBox no está marcado, deshabilita el EditText
                    precio.setEnabled(false);
                }
            }
        });

        crearActividad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreActividad = nombre.getText().toString();
                String lugarActividad = lugar.getText().toString();
                String horaInicioActividad = horaInicio.getText().toString();
                String horaFinalActividad = horaFinal.getText().toString();
                String fechaActividad = Fecha.getText().toString();
                String descripcionActividad = descripcion.getText().toString();
                boolean sePagaActividad = CheckTicket.isChecked();
                Double precioActividad = sePagaActividad ? Double.parseDouble(precio.getText().toString()) : null;

                Actividad nuevaActividad = new Actividad(nombreActividad, descripcionActividad, lugarActividad, fechaActividad, horaInicioActividad, horaFinalActividad, sePagaActividad, precioActividad);

                servicios.crearActividadDB(nombreActividad, descripcionActividad, lugarActividad, fechaActividad, horaInicioActividad, horaFinalActividad, sePagaActividad, precioActividad);
                Toast.makeText(CrearActividad.this, "Botón pulsado", Toast.LENGTH_SHORT).show();


            }
        });
    }
}