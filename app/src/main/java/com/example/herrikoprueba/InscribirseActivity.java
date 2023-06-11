package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.herrikoprueba.Clases.BaseActivity;

public class InscribirseActivity extends BaseActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_inscribirse;  // Retorno el layout específico de MainActivity
    }

    private Button volverHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_inscribirse);

    }
}