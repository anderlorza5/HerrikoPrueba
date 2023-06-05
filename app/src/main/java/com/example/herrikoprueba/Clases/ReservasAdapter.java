package com.example.herrikoprueba.Clases;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.herrikoprueba.R;
import com.example.herrikoprueba.TPVActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReservasAdapter extends ArrayAdapter<Reservas> {
    private Context context;
    private List<Reservas> reservas;

    public ReservasAdapter(Context context, List<Reservas> reservas) {
        super(context, R.layout.reserva_list_item, reservas);
        this.context = context;
        this.reservas = reservas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.reserva_list_item, parent, false);
        }

        Reservas reservaActual = reservas.get(position);

        TextView nombre = listItem.findViewById(R.id.reservaNombre);
        nombre.setText(reservaActual.getDiaReserva());

        Button btnEliminar = listItem.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(v -> {
            // Obtén el ID especial de la reserva
            String reservaId = reservaActual.getId();

            // Encuentra el documento por su campo id especial
            FirebaseFirestore.getInstance().collection("Reservas")
                    .whereEqualTo("id", reservaId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                // Borra cada documento que coincida con la id especial
                                FirebaseFirestore.getInstance().collection("Reservas")
                                        .document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(aVoid -> {
                                            // Si se eliminó con éxito, actualiza la ListView
                                            reservas.remove(position);
                                            notifyDataSetChanged();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Si ocurrió un error al eliminar, muestra un mensaje de error
                                            Toast.makeText(context, "Error al eliminar la reserva", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        } else {
                            // Si ocurrió un error al buscar el documento, muestra un mensaje de error
                            Toast.makeText(context, "Error al buscar la reserva", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        Button btnTPV = listItem.findViewById(R.id.btnTPV);
        btnTPV.setOnClickListener(v -> {
            Intent intent = new Intent(context, TPVActivity.class);
            // Añade los extras al Intent
            intent.putExtra("diaReserva", reservaActual.getDiaReserva());
            intent.putExtra("nombreSocio", reservaActual.getNombreSocio());
            context.startActivity(intent);
        });

        return listItem;
    }
}
