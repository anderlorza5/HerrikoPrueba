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
import com.google.firebase.firestore.DocumentSnapshot;

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
    Button verReservas;


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
        verReservas = findViewById(R.id.mostrarReservasBoton);

        //recoge el numero de comensales y los separa
        getReservasPorFecha(DiaReserva, callback);



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



        verReservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Servicios.obtenerReservasPorDiaDesdeFirestore(ReservaActivity.this, DiaReserva, new Servicios.FirestoreListCallbackk() {
                    @Override
                    public void onCallback(List<DocumentSnapshot> documentSnapshots) {
                        if (documentSnapshots != null) {
                            // Convertir los documentos a objetos de Reservas
                            List<Reservas> reservasList = Servicios.convertirDocumentosAReservas(documentSnapshots);

                            // Crear un StringBuilder para formatear los datos
                            StringBuilder stringBuilder = new StringBuilder();

                            // Iterar sobre la lista de reservas para formatear los datos
                            for (Reservas reserva : reservasList) {
                                stringBuilder.append("Número: ")
                                        .append(reserva.getNumeroMovilSocio())
                                        .append("\nNombre: ")
                                        .append(reserva.getNombreSocio())
                                        .append("\nHora: ")
                                        .append(reserva.getHora())
                                        .append("\n**********\n");
                            }

                            // Mostrar el AlertDialog con los datos formateados
                            mostrarMensaje(stringBuilder.toString());
                        } else {
                            // Mostrar un mensaje de error si no hay reservas
                            mostrarMensaje("No hay reservas para este día");
                        }
                    }
                });
            }
        });



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

    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservaActivity.this);
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cerrar el diálogo al hacer clic en Aceptar
                        if (mensaje.equals("Su reserva se ha realizado con éxito")){
                            finish();
                        }
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



