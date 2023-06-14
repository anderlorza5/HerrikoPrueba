package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Clases.ImagePagerAdapter;
import com.example.herrikoprueba.Clases.SendMail;
import com.example.herrikoprueba.Formularios.ValidarSocio;
import com.example.herrikoprueba.Funciones.funciones;


    public class HomeActivity extends BaseActivity {
        @Override
        protected int getLayoutResourceId() {
            return R.layout.activity_home;  // Retorno el layout específico de MainActivity
        }


     Button calendarioBoton;
     Button comedorBoton;
     Button inscribirse;
     Button sobreNosotros;
     Button validarBoton;
     TextView comedorText;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_home);



        calendarioBoton  = (Button)findViewById (R.id.botonCalendario);
        comedorBoton  = (Button)findViewById (R.id.botonComedor);
        inscribirse  = (Button)findViewById (R.id.botonInscribirse);
        sobreNosotros  = (Button)findViewById (R.id.botonSobreNostros);
        comedorText = findViewById(R.id.comedorText);
        //validarBoton = (Button)findViewById (R.id.validarBotonMenuBarra);
        //funciones.setBotonTextoYComportamiento(this, validarBoton, MiCuentaActivity.class, MiCuentaActivity.class);
       actualizarVistas(this);

       ViewPager2 carruselFotos = findViewById(R.id.CarruselFotos);
       int[] images = {R.drawable.foto1, R.drawable.foto2, R.drawable.foto3}; // reemplaza esto con tus imágenes
       ImagePagerAdapter pagerAdapter = new ImagePagerAdapter(images);
       carruselFotos.setAdapter(pagerAdapter);




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
        //funciones.setBotonTextoYComportamiento(this, validarBoton, MiCuentaActivity.class, MiCuentaActivity.class);


    }

        public void actualizarVistas(Context context) {
            String nombre = funciones.obtenerNombreCompleto(context);
            if (nombre.equals("")) {
                // Si no hay nombre en las preferencias, poner el botón y TextView en invisibles
                comedorBoton.setVisibility(View.INVISIBLE);
                comedorText.setVisibility(View.INVISIBLE);
            } else {
                // Si hay un nombre en las preferencias, poner el botón y TextView en visibles
                comedorBoton.setVisibility(View.VISIBLE);
                comedorText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onRestart() {
            super.onRestart();

            // Actualizar vistas cuando la actividad se reinicie
            actualizarVistas(this);
        }

}