package com.example.herrikoprueba.Clases;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.MiCuentaActivity;
import com.example.herrikoprueba.R;

public abstract class BaseActivity extends AppCompatActivity {
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



       Button MiCuentaBoton = findViewById(R.id.validarBotonMenuBarra);

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
}