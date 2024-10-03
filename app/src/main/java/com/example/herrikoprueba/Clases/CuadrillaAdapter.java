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

public class CuadrillaAdapter extends RecyclerView.Adapter<CuadrillaAdapter.ViewHolder> {

    private List<Cuadrilla> cuadrillas;
    private OnCuadrillaClickListener onCuadrillaClickListener;
    private OnCuadrillaDeleteListener onCuadrillaDeleteListener; // Listener para eliminación

    /**
     * Interfaz para manejar clics en cuadrillas.
     */
    public interface OnCuadrillaClickListener {
        void onCuadrillaClick(Cuadrilla cuadrilla);
    }

    /**
     * Interfaz para manejar la eliminación de cuadrillas.
     */
    public interface OnCuadrillaDeleteListener {
        void onCuadrillaDelete(Cuadrilla cuadrilla);
    }

    /**
     * Constructor del adaptador.
     *
     * @param cuadrillas Lista de cuadrillas a mostrar.
     */
    public CuadrillaAdapter(List<Cuadrilla> cuadrillas) {
        this.cuadrillas = cuadrillas;
    }

    /**
     * Establece el listener para los clics en cuadrillas.
     *
     * @param listener Listener que manejará los clics.
     */
    public void setOnCuadrillaClickListener(OnCuadrillaClickListener listener) {
        this.onCuadrillaClickListener = listener;
    }

    /**
     * Establece el listener para la eliminación de cuadrillas.
     *
     * @param listener Listener que manejará la eliminación.
     */
    public void setOnCuadrillaDeleteListener(OnCuadrillaDeleteListener listener) {
        this.onCuadrillaDeleteListener = listener;
    }

    @NonNull
    @Override
    public CuadrillaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuadrilla, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CuadrillaAdapter.ViewHolder holder, int position) {
        Cuadrilla cuadrilla = cuadrillas.get(position);
        holder.bind(cuadrilla, onCuadrillaClickListener, onCuadrillaDeleteListener);
    }

    @Override
    public int getItemCount() {
        return cuadrillas.size();
    }

    /**
     * Clase ViewHolder para el adaptador de cuadrillas.
     */
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewMiembrosCount;
        ImageButton buttonEliminar;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewCuadrillaName);
            textViewMiembrosCount = itemView.findViewById(R.id.textViewMiembrosCount);
            buttonEliminar = itemView.findViewById(R.id.button_eliminar_cuadrilla);
        }

        /**
         * Vincula los datos al ViewHolder.
         *
         * @param cuadrilla                Cuadrilla a mostrar.
         * @param clickListener            Listener para clics en la cuadrilla.
         * @param deleteListener           Listener para la eliminación de la cuadrilla.
         */
        void bind(Cuadrilla cuadrilla, OnCuadrillaClickListener clickListener, OnCuadrillaDeleteListener deleteListener) {
            textViewName.setText(cuadrilla.getName());
            textViewMiembrosCount.setText("Miembros: " + cuadrilla.getMemberCount());

            // Manejar el clic en el item para mostrar miembros
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onCuadrillaClick(cuadrilla);
                }
            });

            // Manejar el clic en el botón de eliminación
            buttonEliminar.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onCuadrillaDelete(cuadrilla);
                }
            });
        }
    }
}
