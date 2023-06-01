package Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.herrikoprueba.R;


public class MisReservasFragment extends Fragment {

    public MisReservasFragment() {
        // Constructor público vacío requerido
    }

    ListView listView ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento para mostrar la vista
        View rootView = inflater.inflate(R.layout.fragment_mis_reservas, container, false);

        // Configurar la lógica y los elementos de interfaz de usuario del fragmento aquí

        return rootView;
    }
}