package com.example.herrikoprueba.Clases;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import Fragments.InicioSesionFragment;
import Fragments.MisActividadesFragment;
import Fragments.MisReservasFragment;

public class MiUsuarioPagerAdapter extends FragmentPagerAdapter {

    private static final int NUM_PAGINAS = 3;

    public MiUsuarioPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InicioSesionFragment();
            case 1:
                return new MisReservasFragment();
            case 2:
                return new MisActividadesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGINAS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Inicio de Sesión";
            case 1:
                return "Mis Reservas";
            case 2:
                return "Mis Actividades";
            default:
                return "";
        }
    }
}
