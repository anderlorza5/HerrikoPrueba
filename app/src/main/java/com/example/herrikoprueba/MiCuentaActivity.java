package com.example.herrikoprueba;

import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Clases.MiUsuarioPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MiCuentaActivity extends BaseActivity  {
    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_mi_cuenta;  // Retorno el layout específico de MainActivity
    }
    private ViewPager viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_mi_cuenta);

        viewPager = findViewById(R.id.CarruselFotos);
        tabLayout = findViewById(R.id.tabLayout);

        // Crea el adaptador de fragments y asignalo al ViewPager
        MiUsuarioPagerAdapter pagerAdapter = new MiUsuarioPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        // Conecta el ViewPager con el TabLayout
        tabLayout.setupWithViewPager(viewPager);
    }
}