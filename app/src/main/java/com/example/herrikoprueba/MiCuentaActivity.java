package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.herrikoprueba.Clases.MiUsuarioPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import android.os.Bundle;

public class MiCuentaActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_cuenta);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // Crea el adaptador de fragments y asignalo al ViewPager
        MiUsuarioPagerAdapter pagerAdapter = new MiUsuarioPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Conecta el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }
}