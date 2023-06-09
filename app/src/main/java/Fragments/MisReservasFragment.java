package Fragments;

import static com.example.herrikoprueba.BaseDeDatos.Servicios.convertirDocumentosAReservas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.Reservas;
import com.example.herrikoprueba.Clases.ReservasAdapter;
import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.R;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class MisReservasFragment extends Fragment {
    public MisReservasFragment() {
        // Constructor público vacío requerido
    }

    ListView listaReservas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el diseño del fragmento para mostrar la vista
        View rootView = inflater.inflate(R.layout.fragment_mis_reservas, container, false);

        listaReservas = rootView.findViewById(R.id.listaReservas);

        // Actualiza las reservas
        actualizarReservas();

        return rootView;
    }


    public void actualizarReservas() {
        Servicios.obtenerReservasDesdeFirestore(getActivity(), new Servicios.FirestoreListCallbackk() {
            @Override
            public void onCallback(List<DocumentSnapshot> documentos) {
                if (documentos != null) {
                    List<Reservas> reservas = Servicios.convertirDocumentosAReservas(documentos);

                    // Crea un ReservasAdapter y lo usa para poner las reservas en la ListView
                    ReservasAdapter adapter = new ReservasAdapter(getActivity(), reservas);
                    listaReservas.setAdapter(adapter);
                } else {
                    // No se encontraron reservas
                    Toast.makeText(getActivity(), "No se encontraron reservas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            actualizarReservas();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(receiver, new IntentFilter("ReservasActualizadas"));
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(receiver);
    }

}


