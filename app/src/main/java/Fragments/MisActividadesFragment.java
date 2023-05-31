package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.herrikoprueba.R;


public class MisActividadesFragment extends Fragment {

    public MisActividadesFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento para mostrar la vista
        View rootView = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);

        // Configurar la lógica y los elementos de interfaz de usuario del fragmento aquí

        return rootView;
    }
}