package com.example.herrikoprueba.Clases;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.HomeActivity;
import com.example.herrikoprueba.MiCuentaActivity;
import com.example.herrikoprueba.R;

public abstract class BaseActivity extends AppCompatActivity {
    Button MiCuentaBoton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.base_activity_layout, null);

        // Inflamos la barra de menú
        View menuBar = getLayoutInflater().inflate(R.layout.barra_menu2, mainLayout, false);
        mainLayout.addView(menuBar, 0);  // Añade la barra de menú en la primera posición

        // Inflamos el contenido de la actividad específica
        View activityContent = getLayoutInflater().inflate(getLayoutResourceId(), mainLayout, false);
        mainLayout.addView(activityContent, 1);  // Añade el contenido de la actividad después de la barra de menú
        setContentView(mainLayout);



        MiCuentaBoton = findViewById(R.id.validarBotonMenuBarra);
        ImageButton botonHome = findViewById(R.id.homeBoton);

        botonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }
        });

        funciones.setBotonTextoYComportamiento(this, MiCuentaBoton, MiCuentaActivity.class, MiCuentaActivity.class);
        /*homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });*/
    }

    protected abstract int getLayoutResourceId();

    public void onRestart() {
        super.onRestart();
        funciones.setBotonTextoYComportamiento(this, MiCuentaBoton, MiCuentaActivity.class, MiCuentaActivity.class);
        // Código para actualizar tu actividad
        // Por ejemplo, podrías llamar a una función que actualiza la interfaz de usuario
        //funciones.setBotonTextoYComportamiento(this, validarBoton, PantallaSocio.class, ValidarSocio.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        funciones.setBotonTextoYComportamiento(this, MiCuentaBoton, MiCuentaActivity.class, MiCuentaActivity.class);

        //SacarListaActividades();

    }
}