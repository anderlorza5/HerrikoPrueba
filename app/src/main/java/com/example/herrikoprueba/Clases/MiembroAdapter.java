package com.example.herrikoprueba.Clases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herrikoprueba.R;

import java.util.List;

public class MiembroAdapter extends RecyclerView.Adapter<MiembroAdapter.ViewHolder> {

    private List<String> miembros;
    private OnMiembroDeleteListener deleteListener;

    /**
     * Interfaz para manejar la eliminación de miembros.
     */
    public interface OnMiembroDeleteListener {
        void onMiembroDelete(String miembro);
    }

    /**
     * Constructor del adaptador.
     *
     * @param miembros Lista de nombres de miembros.
     */
    public MiembroAdapter(List<String> miembros) {
        this.miembros = miembros;
    }

    /**
     * Establece el listener para la eliminación de miembros.
     *
     * @param listener Listener que manejará la eliminación.
     */
    public void setOnMiembroDeleteListener(OnMiembroDeleteListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public MiembroAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_miembro, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MiembroAdapter.ViewHolder holder, int position) {
        String miembro = miembros.get(position);
        holder.bind(miembro, deleteListener);
    }

    @Override
    public int getItemCount() {
        return miembros.size();
    }

    /**
     * Clase ViewHolder para el adaptador de miembros.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMiembroName;
        ImageButton buttonEliminarMiembro;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMiembroName = itemView.findViewById(R.id.textViewMiembroName);
            buttonEliminarMiembro = itemView.findViewById(R.id.button_eliminar_miembro);
        }

        /**
         * Vincula los datos al ViewHolder.
         *
         * @param miembro         Nombre del miembro.
         * @param deleteListener Listener para la eliminación del miembro.
         */
        void bind(String miembro, OnMiembroDeleteListener deleteListener) {
            textViewMiembroName.setText(miembro);

            // Manejar el clic en el botón de eliminación
            buttonEliminarMiembro.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onMiembroDelete(miembro);
                }
            });
        }
    }
}
