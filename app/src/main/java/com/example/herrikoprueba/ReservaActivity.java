package com.example.herrikoprueba;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ReservaActivity extends AppCompatActivity {
    private String DiaReserva;
    private int numeroComensalesComida;
    private int numeroComensalesCena;
    TextView dia ;
    TextView nºcomida ;
    TextView nºcena ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);
        DiaReserva = getIntent().getStringExtra("selectedDate");
        getComensalesPorDia(DiaReserva);
        dia = findViewById(R.id.diaTexto);
        nºcomida = findViewById(R.id.numeroComidaText);
        nºcena = findViewById(R.id.NumeroCenaText);
        dia.setText(DiaReserva);
        nºcomida.setText("COMIDA: "+numeroComensalesComida+"/150");
        nºcena.setText("CENA: "+numeroComensalesCena+"/150");

    }

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

                        numeroComensalesComida = totalComensalesComida;
                        numeroComensalesCena = totalComensalesCena;

                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                    }
                });
    }

}