package com.example.herrikoprueba;

import static android.content.ContentValues.TAG;

import static com.example.herrikoprueba.R.id.modificarBoton;
import static com.example.herrikoprueba.R.id.nombreInput;
import static java.lang.Double.parseDouble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.CaseMap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.Actividad;
import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Formularios.ValidarSocio;
import com.example.herrikoprueba.Funciones.funciones;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActividadActivity extends BaseActivity {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_actividad;  // Retorno el layout específico de MainActivity
    }


    String idActividad;
    Actividad actividad1= new Actividad();
    Servicios servicios = new Servicios();
    //Button validarBoton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_actividad);



       // validarBoton = (Button)findViewById (R.id.validarBotonMenuBarra);
        //funciones.setBotonTextoYComportamiento(this, validarBoton, PantallaSocio.class, ValidarSocio.class);


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


        funciones.esSuperUsuario(this).thenAccept(superUsuario -> {
            if (superUsuario) {
                // Hacer algo si el usuario es superUsuario
                borrarBoton.setVisibility(View.VISIBLE);
                botonModificar.setVisibility(View.VISIBLE);
            }
        });

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

        //este boton crea un registro en la actividad cuyo id se ha pasado desde el menu anterior recoge los datos del ususario ya sea en cache o lo introduza el
        apuntarseBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentRef = db.collection("Actividades").document(idActividad);

                // Obtén el nombre y número de las preferencias
                String nombreCompleto = funciones.obtenerNombreCompleto(ActividadActivity.this);
                String numeroTelefono = funciones.obtenerNumeroPreferencias(ActividadActivity.this);

                if (nombreCompleto.isEmpty() || numeroTelefono.isEmpty()) {
                    // Muestra un diálogo para recoger el nombre y número
                    // Crear un AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ActividadActivity.this);

                    // Crear un diseño personalizado para el AlertDialog
                    LayoutInflater inflater = getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_layout, null);

                    // Asignar el diseño personalizado al AlertDialog
                    builder.setView(dialogView);

                    // Obtener los EditText del diseño personalizado
                    EditText nombreInput = dialogView.findViewById(R.id.nombreInput);
                    EditText numeroInput = dialogView.findViewById(R.id.numeroInput);

                    // Configurar las acciones de los botones del AlertDialog
                    builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Obtén el nombre y número ingresados por el usuario
                                    String nombreCompleto = nombreInput.getText().toString();
                                    String numeroTelefono = numeroInput.getText().toString();

                                    // Comprobamos si existen inscritos y obtenemos el ID actual para incrementarlo
                                    documentRef.get()
                                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                    Long currentID = 1L;
                                                    if (documentSnapshot.contains("inscritos")) {
                                                        List<Map<String, Object>> inscritos = (List<Map<String, Object>>) documentSnapshot.get("inscritos");
                                                        if (!inscritos.isEmpty()) {
                                                            currentID = (Long) inscritos.get(inscritos.size() - 1).get("id") + 1;
                                                        }
                                                    }
                                                    // Preparamos los nuevos datos
                                                    Map<String, Object> nuevosDatos = new HashMap<>();
                                                    nuevosDatos.put("nombre", nombreCompleto);
                                                    nuevosDatos.put("telefono", numeroTelefono);
                                                    nuevosDatos.put("id", currentID);

                                                    // Agrega los datos a la lista de inscritos
                                                    documentRef.update("inscritos", FieldValue.arrayUnion(nuevosDatos))
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    // El campo se ha actualizado correctamente
                                                                    Log.d(TAG, "Campo 'inscritos' actualizado correctamente");
                                                                    funciones.mostrarMensaje(ActividadActivity.this, "Te has apuntado a la actividad correctamente");
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    // Ocurrió un error al actualizar el campo
                                                                    Log.e(TAG, "Error al actualizar el campo 'inscritos'", e);
                                                                }
                                                            });
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
                            })
                            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // Usuario canceló el diálogo, no se hace nada.
                                }
                            });

                    // Crear y mostrar el AlertDialog
                    AlertDialog dialog = builder.create();
                    dialog.show();


                } else {


                    // Prepara los datos del inscrito
                    Map<String, Object> nuevosDatos = new HashMap<>();
                    nuevosDatos.put("nombre", nombreCompleto);
                    nuevosDatos.put("telefono", numeroTelefono);

                    documentRef.get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    // Verifica si el campo "inscritos" existe en el documento
                                    if (documentSnapshot.contains("inscritos")) {
                                        // Si existe, añade a la persona a la actividad
                                        List<Map<String, Object>> inscritosList = (List<Map<String, Object>>) documentSnapshot.get("inscritos");
                                        int maxId = 0;
                                        if (inscritosList != null) {
                                            for (Map<String, Object> inscrito : inscritosList) {
                                                if (inscrito.containsKey("id")) {
                                                    int id = ((Number) inscrito.get("id")).intValue();
                                                    if (id > maxId) {
                                                        maxId = id;
                                                    }
                                                }
                                            }
                                        }
                                        nuevosDatos.put("id", maxId + 1);

                                        documentRef.update("inscritos", FieldValue.arrayUnion(nuevosDatos))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // El campo se ha actualizado correctamente
                                                        Log.d(TAG, "Campo 'inscritos' actualizado correctamente");
                                                        funciones.mostrarMensaje(ActividadActivity.this, "Te has apuntado a la actividad correctamente");
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
                                        // Si no existe, crea el campo "inscritos" con el valor inicial
                                        nuevosDatos.put("id", 1);

                                        documentRef.update("inscritos", Arrays.asList(nuevosDatos))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        // El campo se ha creado correctamente
                                                        Log.d(TAG, "Campo 'inscritos' creado correctamente");
                                                        funciones.mostrarMensaje(ActividadActivity.this, "Te has apuntado a la actividad correctamente");
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
                            }) .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Ocurrió un error al obtener el documento
                                    Log.e(TAG, "Error al obtener el documento", e);
                                }
                            });
                }
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
    @Override
    public void onRestart() {
        super.onRestart();
        // Código para actualizar tu actividad
        // Por ejemplo, podrías llamar a una función que actualiza la interfaz de usuario
       // funciones.setBotonTextoYComportamiento(this, validarBoton, PantallaSocio.class, ValidarSocio.class);
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
}