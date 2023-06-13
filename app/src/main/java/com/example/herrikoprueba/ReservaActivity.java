package com.example.herrikoprueba;

import static com.example.herrikoprueba.BaseDeDatos.Servicios.getReservasPorFecha;
import static com.example.herrikoprueba.Funciones.funciones.obtenerNombreCompleto;
import static com.example.herrikoprueba.Funciones.funciones.obtenerNumeroPreferencias;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Clases.Reservas;
import com.example.herrikoprueba.Formularios.ValidarSocio;
import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.otros.LoginViewModel;

import java.util.List;

public class ReservaActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_reserva;  // Retorno el layout específico de MainActivity
    }


    private String DiaReserva;
    private int numeroComensalesComida=0;
    private int numeroComensalesCena=0;
    TextView dia ;
    TextView nºcomida ;
    TextView nºcena ;
    TextView nºComensales;
    TextView hora;
    Button reservarMesa;
    Button validarBoton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_reserva);
        DiaReserva = getIntent().getStringExtra("selectedDate");
        //getComensalesPorDia(DiaReserva);
        dia = findViewById(R.id.diaTexto);
        nºcomida = findViewById(R.id.numeroComidaText);
        nºcena = findViewById(R.id.NumeroCenaText);
        dia.setText(DiaReserva);
        nºComensales = findViewById(R.id.numeroComensales);
        hora = findViewById(R.id.Hora);
        reservarMesa = findViewById(R.id.reservarMesaBoton);
        validarBoton  = findViewById(R.id.validarBotonMenuBarra);

        //recoge el numero de comensales y los separa
        getReservasPorFecha(DiaReserva, callback);

       /* validarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonLogin = new Intent(ReservaActivity.this, LoginViewModel.LoginActivity.class);
                startActivity(botonLogin);
            }
        });
*/

        reservarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String horaReservaStr = hora.getText().toString().split(":")[0];
                if (horaReservaStr.isEmpty()) {
                    mostrarMensaje("La hora no puede estar vacía");
                    return;
                }

                int horaReserva2 = Integer.parseInt(horaReservaStr);

                int comensalesComidaSitio = 150 - numeroComensalesComida;
                int comensalesCenaSitio = 150 - numeroComensalesCena;

                String nComensalesStr = nºComensales.getText().toString();
                if (nComensalesStr.isEmpty()) {
                    mostrarMensaje("El número de comensales no puede estar vacío");
                    return;
                }

                int nComensales = Integer.parseInt(nComensalesStr);

                String hora1 = hora.getText().toString();
                String regexPattern = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";

                if (!hora1.matches(regexPattern)) {
                    mostrarMensaje("La hora tiene que ser en formato hh:mm");
                } else if (horaReserva2 < 19 && comensalesComidaSitio < nComensales) {
                    mostrarMensaje("No hay suficiente espacio para la comida");
                } else if (horaReserva2 >= 19 && comensalesCenaSitio < nComensales) {
                    mostrarMensaje("No hay suficiente espacio para la cena");
                } else {
                    Reservas reservas = new Reservas();
                    reservas.setDiaReserva(DiaReserva);
                    reservas.setHora(hora.getText().toString());
                    reservas.setNombreSocio(obtenerNombreCompleto(ReservaActivity.this));
                    reservas.setNumeroMovilSocio(obtenerNumeroPreferencias(ReservaActivity.this));
                    reservas.setNºComensales(nComensales);
                    Servicios.guardarReservaEnFirestore(reservas);
                    mostrarMensaje("Su reserva se ha realizado con éxito");
                }
            }
        });



/*
// Obtienes las reservas por fecha
        Servicios.getDocumentsByFieldDate(DiaReserva, new Servicios.FirestoreListCallback() {
            @Override
            public void onCallback(List<Reservas> reservasList) {
                if (reservasList != null) {
                    // Ahora tienes una lista de objetos Reservas, puedes usar el método contarComensalesPorComidaYCena
                    contarComensalesPorComidaYCena(reservasList);
                   // nºcomida.setText("COMIDA: "+ "as"+"/150");
                    // Ahora puedes utilizar las variables globales numeroComensalesComida y numeroComensalesCena
                } else {
                    // Manejar el caso cuando los documentos no se encuentran o hay un error
                  //  nºcomida.setText("COMIDA: "+ "as"+"/150");
                }
            }
        });

        nºcomida.setText("COMIDA: "+numeroComensalesComida+"/150");
        nºcena.setText("CENA: "+numeroComensalesCena+"/150");

        */


     /*   String idBuscada = "26/05/2023";





        Servicios.getDocumentByFieldId(idBuscada, new Servicios.FirestoreCallback() {
            @Override
            public void onCallback(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot != null) {
                    Reservas reserva = documentSnapshot.toObject(Reservas.class);
                    if (reserva != null) {
                        // Ahora puedes utilizar tu objeto reserva
                        // Por ejemplo, puedes actualizar tus vistas con los datos de la reserva

                         nºcomida.setText("COMIDA: "+reserva.getNºComensales()+"/150");
                         nºcena.setText("CENA: "+reserva.getNºComensales()+"/150");
                    } else {
                        // El documento no pudo ser convertido en un objeto Reservas
                        // Esto puede suceder si los campos del documento no coinciden con los de la clase Reservas
                        Log.w(TAG, "Error convirtiendo documento en Reservas.");
                    }
                } else {
                    // Manejar el caso cuando el documento no se encuentra o hay un error
                    Log.w(TAG, "Documento no encontrado.");
                }
            }
        });

*/






        /*// Aquí pon la fecha que quieres buscar
        String fechaBuscada = "26/05/2023";

        Servicios.getDocumentsByDate(fechaBuscada, new Servicios.FirestoreMultipleCallback() {
            @Override
            public void onCallback(List<DocumentSnapshot> documentSnapshots) {
                if (documentSnapshots != null) {
                    for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                        Reservas reserva = documentSnapshot.toObject(Reservas.class);
                        if (reserva != null) {


                            nºcomida.setText("COMIDA: "+reserva.getNºComensales()+"/150");
                            nºcena.setText("CENA: "+reserva.getNºComensales()+"/150");
                            // Ahora puedes utilizar tu objeto reserva
                            // Por ejemplo, puedes actualizar tus vistas con los datos de la reserva
                        } else {
                            // El documento no pudo ser convertido en un objeto Reservas
                            // Esto puede suceder si los campos del documento no coinciden con los de la clase Reservas
                            Log.w(TAG, "Error convirtiendo documento en Reservas.");
                        }
                    }
                } else {
                    // No se encontraron documentos o hubo un error
                }
            }
        });*/
    }


    public void contarComensalesPorComidaYCena(List<Reservas> reservasList) {
        for (Reservas reserva : reservasList) {
            // Extraemos la hora (HH) del string
            int horaReserva = Integer.parseInt(reserva.getHora().split(":")[0]);

            // Comparamos con las horas de corte
            if (horaReserva < 19) {
                // Si la hora de la reserva está en el rango de las horas de comida, es para la comida
                numeroComensalesComida += reserva.getNºComensales();
            } else {
                // Si la hora de la reserva no está en el rango de las horas de comida, es para la cena
                numeroComensalesCena += reserva.getNºComensales();
            }
        }
    }

        //  nºcomida.setText("COMIDA: "+numeroComensalesComida+"/150");
       // nºcena.setText("CENA: "+numeroComensalesCena+"/150");


/*
   //este metodo recoge el numero de comensales de ese dia
    public void getComensalesPorDia(String diaBuscado) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Reservas")
                .whereEqualTo("id", "1")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        int totalComensalesComida = 0;
                        int totalComensalesCena = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            int numComensales = document.getLong("nºComensales").intValue();
                            String horaReservaStr = document.getString("hora");

                            // Extraemos la hora (HH) del string
                            int horaReserva = Integer.parseInt(horaReservaStr.split(":")[0]);

                            // Comparamos con las horas de corte
                            if (horaReserva >= 7 && horaReserva <= 12 || horaReserva >= 0 && horaReserva < 2) {
                                // Si la hora de la reserva está en el rango de las horas de cena, es para la cena
                                totalComensalesCena += numComensales;
                            } else {
                                // Si la hora de la reserva no está en el rango de las horas de cena, es para la comida
                                totalComensalesComida += numComensales;
                            }
                        }

                        this.numeroComensalesComida = totalComensalesComida;
                        this.numeroComensalesCena = totalComensalesCena;

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });

    }*/


    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservaActivity.this);
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cerrar el diálogo al hacer clic en Aceptar
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

Servicios.CallbackListaReservas callback = new Servicios.CallbackListaReservas() {
    @Override
    public void onCallback(List<Reservas> listaReservas) {
        // Aquí puedes usar la lista de reservas obtenida
        if (listaReservas != null) {
            // La lista de reservas no es nula, puedes procesarla
            contarComensalesPorComidaYCena(listaReservas);
        } else {
            // La lista de reservas es nula, hubo un error
            // Maneja el error según tus necesidades
        }
        nºcomida.setText("COMIDA: "+numeroComensalesComida+"/150");
        nºcena.setText("CENA: "+numeroComensalesCena+"/150");
    }
};



    @Override
    public void onRestart() {
        super.onRestart();
        // Código para actualizar tu actividad
        // Por ejemplo, podrías llamar a una función que actualiza la interfaz de usuario
        funciones.setBotonTextoYComportamiento(this, validarBoton , PantallaSocio.class, ValidarSocio.class);
        getReservasPorFecha(DiaReserva, callback);
    }
    }



