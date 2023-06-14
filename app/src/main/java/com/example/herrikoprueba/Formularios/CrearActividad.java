package com.example.herrikoprueba.Formularios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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


import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.InscribirseActivity;
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

                // Finalmente, si quieres que se cierre la actividad actual, llamas a finish()
                finish();
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

                if (nombreActividad.equals("") ){
                    funciones.mostrarMensaje(CrearActividad.this,"debes rellenar el nombre");
                    return;
                }
                if (fechaActividad.equals("") ){
                    funciones.mostrarMensaje(CrearActividad.this,"debes rellenar la fecha");
                    return;
                }
                if (!fechaActividad.matches("^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])$")) {
                    funciones.mostrarMensaje(CrearActividad.this,"La fecha debe estar en el formato yyyy/mm/dd");
                    return;
                }
                servicios.crearActividadDB(nombreActividad, descripcionActividad, lugarActividad, fechaActividad, horaInicioActividad, horaFinalActividad, sePagaActividad, precioActividad);
                mostrarMensaje( "actividad creada");
               // Toast.makeText(CrearActividad.this, "Botón pulsado", Toast.LENGTH_SHORT).show();


                // Finalmente, si quieres que se cierre la actividad actual, llamas a finish()


            }
        });
    }

    @Override
    public void onBackPressed() {
        // Primero realizas cualquier tarea que necesites hacer antes de navegar hacia atrás

        // Luego lanzas la actividad que desees
        Intent intent = new Intent(this, CalendarioActivity.class);
        startActivity(intent);

        // Finalmente, si quieres que se cierre la actividad actual, llamas a finish()
        finish();
    }

    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CrearActividad.this);
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cerrar el diálogo al hacer clic en Aceptar
                        finish();
                        Intent intent = new Intent(CrearActividad.this, CalendarioActivity.class);
                        startActivity(intent);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}