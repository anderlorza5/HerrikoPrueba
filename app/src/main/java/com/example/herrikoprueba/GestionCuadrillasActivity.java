package com.example.herrikoprueba;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herrikoprueba.Clases.Cuadrilla;
import com.example.herrikoprueba.Clases.CuadrillaAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class GestionCuadrillasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CuadrillaAdapter adapter;
    private List<Cuadrilla> cuadrillas;
    private FirebaseFirestore db;
    private Button btnAgregarCuadrilla;
    private TextView textTotalPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_cuadrillas);

        // Inicialización de vistas
        recyclerView = findViewById(R.id.recycler_view_cuadrillas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cuadrillas = new ArrayList<>();
        adapter = new CuadrillaAdapter(cuadrillas);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        textTotalPersonas = findViewById(R.id.text_total_personas);

        btnAgregarCuadrilla = findViewById(R.id.btn_agregar_cuadrilla);
        btnAgregarCuadrilla.setOnClickListener(v -> agregarCuadrilla());

        // Manejar clic en cuadrilla para cargar miembros
        adapter.setOnCuadrillaClickListener(cuadrilla -> {
            // Abrir la actividad de miembros pasando el nombre de la cuadrilla
            abrirActividadMiembros(cuadrilla.getName());
        });

        // Manejar eliminación de cuadrilla
        adapter.setOnCuadrillaDeleteListener(cuadrilla -> {
            confirmarEliminarCuadrilla(cuadrilla);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCuadrillas(); // Recargar cuadrillas cada vez que la actividad se reanude
    }

    /**
     * Abre la actividad de miembros pasando el nombre de la cuadrilla.
     *
     * @param cuadrillaName Nombre de la cuadrilla.
     */
    private void abrirActividadMiembros(String cuadrillaName) {
        Intent intent = new Intent(this, MiembrosCuadrillaActivity.class);
        intent.putExtra("cuadrillaName", cuadrillaName); // PASAR EL NOMBRE DE LA CUADRILLA
        startActivity(intent);
    }

    /**
     * Carga las cuadrillas desde Firestore y actualiza el RecyclerView.
     */
    private void cargarCuadrillas() {
        // Limpia la lista antes de cargar las cuadrillas desde Firestore
        cuadrillas.clear();

        db.collection("ListaComidas").document("Comidas").collection("cuadrillas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Cuadrilla> cuadrillasCargadas = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cuadrilla cuadrilla = document.toObject(Cuadrilla.class);
                            cuadrillasCargadas.add(cuadrilla);
                        }
                        cuadrillas.addAll(cuadrillasCargadas);
                        adapter.notifyDataSetChanged();
                        actualizarTotalPersonas(); // Actualiza el total de personas después de cargar
                    } else {
                        Toast.makeText(this, "Error al cargar cuadrillas", Toast.LENGTH_SHORT).show();
                        Log.e("GestionCuadrillas", "Error al cargar cuadrillas: ", task.getException());
                    }
                });
    }

    /**
     * Muestra un diálogo para agregar una nueva cuadrilla.
     */
    private void agregarCuadrilla() {
        // Mostrar un diálogo para ingresar el nombre de la cuadrilla
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Cuadrilla");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nuevoNombre = input.getText().toString().trim();
            if (!nuevoNombre.isEmpty()) {
                Cuadrilla nuevaCuadrilla = new Cuadrilla(nuevoNombre);
                // Añadir ownerUid si es necesario, dependiendo de tus reglas de Firestore
                db.collection("ListaComidas").document("Comidas").collection("cuadrillas")
                        .document(nuevoNombre)
                        .set(nuevaCuadrilla)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Cuadrilla agregada", Toast.LENGTH_SHORT).show();
                            cargarCuadrillas(); // Recargar cuadrillas después de agregar
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Error al agregar cuadrilla", Toast.LENGTH_SHORT).show();
                            Log.e("GestionCuadrillas", "Error al agregar cuadrilla: ", e);
                        });
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Actualiza el conteo total de personas en todas las cuadrillas.
     */
    private void actualizarTotalPersonas() {
        AtomicInteger totalPersonas = new AtomicInteger();
        AtomicInteger cuadrillasProcesadas = new AtomicInteger(0);

        if (cuadrillas.isEmpty()) {
            textTotalPersonas.setText("Total Personas: 0");
            return;
        }

        for (Cuadrilla cuadrilla : cuadrillas) {
            db.collection("ListaComidas").document("Comidas").collection("cuadrillas")
                    .document(cuadrilla.getName())
                    .collection("miembros")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            int count = task.getResult().size();
                            cuadrilla.setMemberCount(count); // Establecer el conteo de miembros en la cuadrilla
                            totalPersonas.addAndGet(count);
                        } else {
                            Log.e("GestionCuadrillas", "Error al obtener miembros para cuadrilla: " + cuadrilla.getName(), task.getException());
                        }
                        // Incrementar el contador de cuadrillas procesadas
                        if (cuadrillasProcesadas.incrementAndGet() == cuadrillas.size()) {
                            adapter.notifyDataSetChanged(); // Notificar cambios en el adaptador
                            textTotalPersonas.setText("Total Personas: " + totalPersonas.get());
                        }
                    });
        }
    }

    /**
     * Muestra un diálogo de confirmación para eliminar una cuadrilla.
     *
     * @param cuadrilla Cuadrilla a eliminar.
     */
    private void confirmarEliminarCuadrilla(Cuadrilla cuadrilla) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Cuadrilla");
        builder.setMessage("¿Estás seguro de que deseas eliminar la cuadrilla \"" + cuadrilla.getName() + "\"? Esta acción también eliminará todos sus miembros.");
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            eliminarCuadrilla(cuadrilla);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Elimina una cuadrilla y todos sus miembros de Firestore.
     *
     * @param cuadrilla Cuadrilla a eliminar.
     */
    private void eliminarCuadrilla(Cuadrilla cuadrilla) {
        // Primero, eliminar todos los miembros de la cuadrilla
        db.collection("ListaComidas")
                .document("Comidas")
                .collection("cuadrillas")
                .document(cuadrilla.getName())
                .collection("miembros")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = db.batch();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            batch.delete(document.getReference());
                        }

                        // Ejecutar el batch de eliminación de miembros
                        batch.commit()
                                .addOnSuccessListener(aVoid -> {
                                    // Luego, eliminar la cuadrilla
                                    db.collection("ListaComidas")
                                            .document("Comidas")
                                            .collection("cuadrillas")
                                            .document(cuadrilla.getName())
                                            .delete()
                                            .addOnSuccessListener(aVoid1 -> {
                                                Toast.makeText(this, "Cuadrilla eliminada", Toast.LENGTH_SHORT).show();
                                                cargarCuadrillas(); // Recargar la lista de cuadrillas
                                                actualizarTotalPersonas(); // Actualizar el total de personas
                                            })
                                            .addOnFailureListener(e -> {
                                                Toast.makeText(this, "Error al eliminar cuadrilla", Toast.LENGTH_SHORT).show();
                                                Log.e("GestionCuadrillas", "Error al eliminar cuadrilla: ", e);
                                            });
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Error al eliminar miembros", Toast.LENGTH_SHORT).show();
                                    Log.e("GestionCuadrillas", "Error al eliminar miembros: ", e);
                                });
                    } else {
                        Toast.makeText(this, "Error al obtener miembros para eliminar", Toast.LENGTH_SHORT).show();
                        Log.e("GestionCuadrillas", "Error al obtener miembros para eliminar: ", task.getException());
                    }
                });
    }
}
