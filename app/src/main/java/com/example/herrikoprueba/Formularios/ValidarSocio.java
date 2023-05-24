package com.example.herrikoprueba.Formularios;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class ValidarSocio extends AppCompatActivity {



    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validar_socio);
        Button botonValidar = findViewById(R.id.validarBoton);
        Button botonSocio = findViewById(R.id.validarBotonMenuBarra);
        EditText nombre = findViewById(R.id.nombreInput);
        EditText numero = findViewById(R.id.numeroInput);

        // Initializa Firestore y SharedPreferences
        db = FirebaseFirestore.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        botonValidar.setOnClickListener(v -> {
            String nombreInput = nombre.getText().toString();
            String numeroInput = numero.getText().toString();

            // Consulta a Firestore para ver si existe el socio
            db.collection("Socios")
                    .whereEqualTo("nombreCompleto", nombreInput)
                    .whereEqualTo("movilNumero", numeroInput)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // El socio existe, almacena los datos en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre", nombreInput);
                            editor.putString("numero", numeroInput);
                            editor.apply();

                            // Cambia el texto del botón
                            botonSocio.setText(funciones.obtenerPrimerNombre(this));
                        } else {
                            // El socio no existe o hubo un error
                            botonSocio.setText("No validado");
                        }
                    });
        });
    }
}
