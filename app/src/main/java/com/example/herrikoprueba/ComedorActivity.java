package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.herrikoprueba.BaseDeDatos.ImportarSocios;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ComedorActivity extends AppCompatActivity {

    private Button volverHome;
     Button crearComedor;
    EditText nombre;

    FirebaseFirestore mfFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comedor);

        volverHome= findViewById(R.id.botonVolverHomeComedor);
        crearComedor = findViewById(R.id.crearComedor);
        nombre = findViewById(R.id.edittextNombre);
        mfFirestore= FirebaseFirestore.getInstance();

        volverHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComedorActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
        crearComedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportarSocios.importarSocios();
            }
        });
    }

    private void crearDatos(){
        String nombree = nombre.getText().toString();

        Map<String, Object> map = new HashMap<>();
        map.put( "Nombre",  nombree);
        mfFirestore.collection("Actividades").document("Pruebaid").set(map);
    }
}