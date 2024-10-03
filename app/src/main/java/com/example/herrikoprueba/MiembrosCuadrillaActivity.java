package com.example.herrikoprueba;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // Import para TextView
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.herrikoprueba.Clases.Cuadrilla;
import com.example.herrikoprueba.Clases.MiembroAdapter;
import com.google.firebase.auth.FirebaseAuth; // Import para FirebaseAuth
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class MiembrosCuadrillaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MiembroAdapter adapter;
    private List<String> miembros;
    private FirebaseFirestore db;
    private String cuadrillaName;
    private Button btnAgregarMiembro;
    private TextView textNombreCuadrilla; // TextView para mostrar el nombre real de la cuadrilla

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_miembros_cuadrilla);

        // Obtener el nombre de la cuadrilla pasada desde la actividad anterior
        cuadrillaName = getIntent().getStringExtra("cuadrillaName");

        // Inicializar el TextView y establecer el nombre de la cuadrilla
        textNombreCuadrilla = findViewById(R.id.text_nombre_cuadrilla);
        if (cuadrillaName != null && !cuadrillaName.isEmpty()) {
            textNombreCuadrilla.setText(cuadrillaName);
        } else {
            textNombreCuadrilla.setText("Nombre de la Cuadrilla");
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recycler_view_miembros);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        miembros = new ArrayList<>();
        adapter = new MiembroAdapter(miembros);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        cargarMiembros();

        btnAgregarMiembro = findViewById(R.id.btn_agregar_miembro);
        btnAgregarMiembro.setOnClickListener(v -> agregarMiembro());

        // Establecer el listener para eliminar miembros
        adapter.setOnMiembroDeleteListener(miembro -> {
            confirmarEliminarMiembro(miembro);
        });
    }

    /**
     * Carga los miembros de la cuadrilla desde Firestore.
     */
    private void cargarMiembros() {
        db.collection("ListaComidas").document("Comidas").collection("cuadrillas").document(cuadrillaName)
                .collection("miembros")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        miembros.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String miembro = document.getId(); // Asumiendo que el ID del documento es el nombre del miembro
                            miembros.add(miembro);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error al cargar miembros", Toast.LENGTH_SHORT).show();
                        Log.e("MiembrosCuadrillaActivity", "Error al cargar miembros: ", task.getException());
                    }
                });
    }

    /**
     * Muestra un diálogo para agregar un nuevo miembro a la cuadrilla.
     */
    private void agregarMiembro() {
        // Mostrar un diálogo para ingresar el nombre del miembro
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Miembro");

        // Establece un EditText para que el usuario ingrese el nombre
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nuevoMiembro = input.getText().toString().trim();
            if (!nuevoMiembro.isEmpty()) {
                if (miembros.contains(nuevoMiembro)) {
                    Toast.makeText(this, "El miembro ya existe", Toast.LENGTH_SHORT).show();
                } else {
                    miembros.add(nuevoMiembro); // Añadir el miembro a la lista local
                    adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado

                    // Agregar el miembro a Firestore
                    db.collection("ListaComidas").document("Comidas").collection("cuadrillas").document(cuadrillaName)
                            .collection("miembros").document(nuevoMiembro)
                            .set(new HashMap<>()) // Crea un documento vacío para el nuevo miembro
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Miembro agregado", Toast.LENGTH_SHORT).show();
                                actualizarMemberCount(1); // Incrementar el conteo de miembros
                                actualizarTotalPersonas(); // Actualizar el total de personas en la actividad principal
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error al agregar miembro", Toast.LENGTH_SHORT).show();
                                Log.e("MiembrosCuadrillaActivity", "Error al agregar miembro: ", e);
                            });
                }
            } else {
                Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Muestra un diálogo de confirmación para eliminar un miembro.
     *
     * @param miembro Nombre del miembro a eliminar.
     */
    private void confirmarEliminarMiembro(String miembro) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar Miembro");
        builder.setMessage("¿Estás seguro de que deseas eliminar al miembro \"" + miembro + "\" de la cuadrilla \"" + cuadrillaName + "\"?");
        builder.setPositiveButton("Eliminar", (dialog, which) -> {
            eliminarMiembro(miembro);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    /**
     * Elimina un miembro específico de la cuadrilla en Firestore.
     *
     * @param miembro Nombre del miembro a eliminar.
     */
    private void eliminarMiembro(String miembro) {
        db.collection("ListaComidas").document("Comidas").collection("cuadrillas").document(cuadrillaName)
                .collection("miembros").document(miembro)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Miembro eliminado", Toast.LENGTH_SHORT).show();
                    miembros.remove(miembro); // Eliminar el miembro de la lista local
                    adapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                    actualizarMemberCount(-1); // Decrementar el conteo de miembros
                    actualizarTotalPersonas(); // Actualizar el total de personas en la actividad principal
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar miembro", Toast.LENGTH_SHORT).show();
                    Log.e("MiembrosCuadrillaActivity", "Error al eliminar miembro: ", e);
                });
    }

    /**
     * Actualiza el campo 'memberCount' en Firestore.
     *
     * @param delta Cantidad a incrementar o decrementar.
     */
    private void actualizarMemberCount(int delta) {
        db.collection("ListaComidas").document("Comidas").collection("cuadrillas").document(cuadrillaName)
                .update("memberCount", FieldValue.increment(delta))
                .addOnSuccessListener(aVoid -> {
                    // Actualización exitosa, puedes manejarlo si es necesario
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al actualizar conteo de miembros", Toast.LENGTH_SHORT).show();
                    Log.e("MiembrosCuadrillaActivity", "Error al actualizar memberCount: ", e);
                });
    }

    /**
     * Actualiza el conteo total de personas en la actividad principal.
     * Este método podría comunicarse con la actividad principal si es necesario.
     */
    private void actualizarTotalPersonas() {
        // Para simplificar, puedes optar por recargar la actividad principal
        // o implementar una comunicación entre actividades (por ejemplo, usando EventBus)
        // Aquí, simplemente llamaremos a cargarCuadrillas() en la actividad principal.
        // Para hacerlo, deberías configurar un mecanismo de comunicación.
        // Sin embargo, como estamos recargando cuadrillas en onResume(), no es necesario.
    }
}
