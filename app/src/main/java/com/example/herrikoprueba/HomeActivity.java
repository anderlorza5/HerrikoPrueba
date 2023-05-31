package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.herrikoprueba.Formularios.ValidarSocio;
import com.example.herrikoprueba.Funciones.funciones;

public class HomeActivity extends AppCompatActivity {
     Button loginButton;
     Button calendarioBoton;
     Button comedorBoton;
     Button inscribirse;
     Button sobreNosotros;
    Button validarBoton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        loginButton = (Button)findViewById (R.id.botonLogin);
        calendarioBoton  = (Button)findViewById (R.id.botonCalendario);
        comedorBoton  = (Button)findViewById (R.id.botonComedor);
        inscribirse  = (Button)findViewById (R.id.botonInscribirse);
        sobreNosotros  = (Button)findViewById (R.id.botonSobreNostros);
        validarBoton = (Button)findViewById (R.id.validarBotonMenuBarra);
        funciones.setBotonTextoYComportamiento(this, validarBoton, MiCuentaActivity.class, MiCuentaActivity.class);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonLogin = new Intent(HomeActivity.this, MiCuentaActivity.class);
                startActivity(botonLogin);
            }
        });

        //funciones.setValidarBotonClick(this, validarBoton, PantallaSocio.class, ValidarSocio.class);


        calendarioBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent calendarioBoton = new Intent(HomeActivity.this, CalendarioActivity.class);
                startActivity(calendarioBoton);
            }
        });

        comedorBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comedorioBoton = new Intent(HomeActivity.this, ComedorActivity.class);
                startActivity(comedorioBoton);
            }
        });
        inscribirse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comedorioBoton = new Intent(HomeActivity.this, InscribirseActivity.class);
                startActivity(comedorioBoton);
            }
        });
        sobreNosotros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comedorioBoton = new Intent(HomeActivity.this, SobreNosotrosActivity.class);
                startActivity(comedorioBoton);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        funciones.setBotonTextoYComportamiento(this, validarBoton, MiCuentaActivity.class, MiCuentaActivity.class);


    }

}