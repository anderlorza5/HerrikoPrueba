package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.herrikoprueba.Formularios.ValidarSocio;

public class HomeActivity extends AppCompatActivity {
     Button loginButton;
     Button calendarioBoton;
     Button comedorBoton;
     Button inscribirse;
     Button sobreNosotros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loginButton = (Button)findViewById (R.id.botonLogin);
        calendarioBoton  = (Button)findViewById (R.id.botonCalendario);
        comedorBoton  = (Button)findViewById (R.id.botonComedor);
        inscribirse  = (Button)findViewById (R.id.botonInscribirse);
        sobreNosotros  = (Button)findViewById (R.id.botonSobreNostros);
        Button validarBoton = (Button)findViewById (R.id.validarBotonMenuBarra);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonLogin = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(botonLogin);
            }
        });
        validarBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent botonLogin = new Intent(HomeActivity.this, ValidarSocio.class);
                startActivity(botonLogin);
            }
        });

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
}