package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InscribirseActivity extends AppCompatActivity {

    private Button volverHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscribirse);

        volverHome= findViewById(R.id.botonVolverHomeInscribirse);

        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InscribirseActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}