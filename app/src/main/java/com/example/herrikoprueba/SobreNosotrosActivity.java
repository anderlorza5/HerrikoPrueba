package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SobreNosotrosActivity extends AppCompatActivity {

    private Button volverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sobre_nosotros);

        volverHome= findViewById(R.id.botonVolverHomeNosotros);
        Button validarBoton = (Button)findViewById (R.id.validarBotonMenuBarra);


        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SobreNosotrosActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}