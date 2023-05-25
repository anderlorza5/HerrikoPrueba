package com.example.herrikoprueba;

import static com.example.herrikoprueba.Funciones.funciones.obtenerNombreCompleto;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herrikoprueba.Funciones.funciones;

public class PantallaSocio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_socio);

        Button desvincular = findViewById(R.id.desvincularBoton);
        TextView nombre = findViewById(R.id.nombreSocioOutput);

        nombre.setText(funciones.obtenerNombreCompleto(this));

        desvincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.borrarNombreCompleto(PantallaSocio.this);
               funciones.mostrarMensaje(PantallaSocio.this, "desvinculacion realizada");
                finish();
            }
        });
    }
}