package com.example.herrikoprueba;

import static android.content.ContentValues.TAG;

import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.Actividad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ActividadActivity extends AppCompatActivity {

    String idActividad;
    Actividad actividad1= new Actividad();
    Servicios servicios = new Servicios();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);






        Button botonModificar  = findViewById(R.id.modificarBoton);
        Button botonGuardar  = findViewById(R.id.guardarBoton);
        Button borrarBoton  = findViewById(R.id.borrarBoton);
        Button apuntarseBoton  = findViewById(R.id.apuntarseBoton);

        EditText titulo = findViewById(R.id.tituloInput);
        EditText descripcionInput = findViewById(R.id.descripcionInput);
        EditText fechaInput = findViewById(R.id.fechaInput);
        EditText lugarInput = findViewById(R.id.lugarInput);
        EditText horaFinalInput = findViewById(R.id.horaFinalInput);
        EditText horaInicioInput = findViewById(R.id.horaInicioInput);
        EditText precioInput = findViewById(R.id.precioInput);
        Intent intent = getIntent();
        idActividad = intent.getStringExtra("idActividad");




        //aqui va la logica de rellenar los campos  a traves del objeto actividad

        servicios.recogerActividadDB(idActividad, new Servicios.ActividadCallback() {
            @Override
            public void onCallback(Actividad actividad) {
                // Aquí puedes acceder a tu objeto actividad y actualizar tus TextViews
                actividad1=actividad;
                titulo.setText(actividad.getNombre());
                descripcionInput.setText(actividad.getDescripcion());
                fechaInput.setText(actividad.getFecha());
                lugarInput.setText(actividad.getLugar());
                horaFinalInput.setText(actividad.getHoraFinal());
                horaInicioInput.setText(actividad.getHoraInicio());

                Double precio = actividad.getPrecio();
                if (precio != null) {
                    precioInput.setText(String.valueOf(precio.doubleValue()));
                } else {
                    precioInput.setText("N/A"); // Otra indicación de que el precio es nulo
                }

                // Actualiza los demás TextViews...
            }
        });


        apuntarseBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentRef = db.collection("Actividades").document(idActividad);

                documentRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                // Verifica si el campo "inscritos" existe en el documento
                                if (documentSnapshot.contains("inscritos")) {
                                    // Si existe, obtén su valor actual y suma 1
                                    int inscritos = documentSnapshot.getLong("inscritos").intValue() + 1;

                                    // Actualiza el campo "inscritos" con el nuevo valor
                                    documentRef.update("inscritos", inscritos)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // El campo se ha actualizado correctamente
                                                    Log.d(TAG, "Campo 'inscritos' actualizado correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Ocurrió un error al actualizar el campo
                                                    Log.e(TAG, "Error al actualizar el campo 'inscritos'", e);
                                                }
                                            });
                                } else {
                                    // Si no existe, crea el campo "inscritos" con el valor inicial de 1
                                    documentRef.update("inscritos", 1)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // El campo se ha creado correctamente
                                                    Log.d(TAG, "Campo 'inscritos' creado correctamente");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Ocurrió un error al crear el campo
                                                    Log.e(TAG, "Error al crear el campo 'inscritos'", e);
                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Ocurrió un error al obtener el documento
                                Log.e(TAG, "Error al obtener el documento", e);
                            }
                        });

            }
        });

        botonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titulo.setEnabled(true);
                descripcionInput.setEnabled(true);
                fechaInput.setEnabled(true);
                lugarInput.setEnabled(true);
                horaFinalInput.setEnabled(true);
                horaInicioInput.setEnabled(true);
                precioInput.setEnabled(true);
                botonGuardar.setVisibility(View.VISIBLE);

            }
        });

        botonGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                actividad1.setNombre(titulo.getText().toString());
                actividad1.setDescripcion(descripcionInput.getText().toString());
                actividad1.setFecha(fechaInput.getText().toString());
                actividad1.setLugar(lugarInput.getText().toString());
                actividad1.setHoraFinal(horaFinalInput.getText().toString());
                actividad1.setHoraInicio(horaInicioInput.getText().toString());
                actividad1.setPrecio(parseDouble(precioInput.getText().toString()));

                servicios.modificarActividadDBConObjeto(actividad1,idActividad);

                titulo.setEnabled(false);
                descripcionInput.setEnabled(false);
                fechaInput.setEnabled(false);
                lugarInput.setEnabled(false);
                horaFinalInput.setEnabled(false);
                horaInicioInput.setEnabled(false);
                precioInput.setEnabled(false);

                botonGuardar.setVisibility(View.INVISIBLE);
                recreate();

            }
        });

        borrarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ActividadActivity.this);
                builder.setTitle("Confirmación de borrado")
                        .setMessage("¿Estás seguro de que deseas borrar esta actividad?")
                        .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // El usuario ha confirmado el borrado
                                servicios.borrarActividadDB(idActividad);
                                dialog.dismiss();
                                Intent calendarioBoton = new Intent(ActividadActivity.this, CalendarioActivity.class);
                                startActivity(calendarioBoton);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // El usuario ha cancelado el borrado
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        });


    }
}