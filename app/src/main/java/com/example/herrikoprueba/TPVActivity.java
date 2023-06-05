package com.example.herrikoprueba;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.BebidaComedor;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.BebidaComedor;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TPVActivity extends BaseActivity {
    List<BebidaComedor> bebidas = new ArrayList<>();

    private GridLayout gridBebidas;
    private TableLayout tablaFactura;
    private TextView totalFactura;
    private double total = 0.0;
    private Button btnPagar;



    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_tpvactivity; // cambia esto por el nombre de tu XML
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String diaReserva = intent.getStringExtra("diaReserva");
        String nombreSocio = intent.getStringExtra("nombreSocio");

        gridBebidas = findViewById(R.id.grid_bebidas);
        tablaFactura = findViewById(R.id.tabla_factura);
        totalFactura = findViewById(R.id.total_factura);
        btnPagar = findViewById(R.id.pagarBoton);



        // Llamar a la función para obtener las bebidas de la base de datos
        obtenerBebidas();

        // Para el botón de pagar
        btnPagar.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            for (BebidaComedor bebida : bebidas) {
                DocumentReference docRef = db.collection("BebidaComedor").document(bebida.getId());
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            double antiguoUnidadesVendidas = document.getDouble("unidadesVendidas");
                            double antiguoTotalEuros = document.getDouble("totalEuros");
                            docRef.update(
                                    "unidadesVendidas", antiguoUnidadesVendidas + bebida.getUnidadesVendidas(),
                                    "totalEuros", antiguoTotalEuros + bebida.getTotalEuros()
                            ).addOnSuccessListener(aVoid -> {
                                // Actualización exitosa
                                bebida.setUnidadesVendidas(0);
                                bebida.setTotalEuros(0);
                            }).addOnFailureListener(e -> {
                                // Error al actualizar
                            });
                        } else {
                            // El documento no existe
                        }
                    } else {
                        // Error al obtener el documento
                    }
                });
            }
        });
    }

    // ... otras funciones irán aquí

    private void obtenerBebidas() {
        Servicios.getBebidasComedor(new Servicios.FirestoreListCallbackBebida<BebidaComedor>() {
            @Override
            public void onCallback(List<BebidaComedor> bebidas) {
                // Limpiamos el grid de botones antes de agregar los nuevos botones
                gridBebidas.removeAllViews();

                for (BebidaComedor bebida : bebidas) {
                    Button boton = new Button(TPVActivity.this);
                    if (bebida.getId().equals("dineroTotalBebidas")) {
                        continue;
                    }
                    boton.setText(bebida.getId());
                    boton.setTag(bebida.getId() + "Boton");
                    boton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Actualizar las unidades vendidas y el total de euros en la bebida
                            bebida.setUnidadesVendidas(bebida.getUnidadesVendidas() + 1);
                            bebida.setTotalEuros(bebida.getTotalEuros() + bebida.getPrecio());

                            // Actualizar la factura
                            actualizarFactura(bebida);

                            // También podrías querer actualizar la representación visual de la bebida en la interfaz de usuario aquí
                        }
                    });

                    // Añadir el botón a la cuadrícula
                    gridBebidas.addView(boton);
                }
            }
        });
    }



    private void actualizarFactura(BebidaComedor bebida) {
        // Buscar si la bebida ya está en la tabla de facturación
        TableRow filaExistente = tablaFactura.findViewWithTag(bebida.getId());

        if (filaExistente == null) {
            // Crear una nueva fila
            TableRow nuevaFila = new TableRow(this);
            nuevaFila.setTag(bebida.getId());


            // Añadir los detalles de la bebida a la fila
            TextView nombre = new TextView(this);
            nombre.setText(bebida.getId());
            nombre.setPadding(20, 20, 20, 20); // añade el padding a la celda
            nuevaFila.addView(nombre);

            TextView precio = new TextView(this);
            precio.setText(String.format(Locale.getDefault(), "%.2f", bebida.getPrecio()));
            precio.setPadding(20, 20, 20, 20); // añade el padding a la celda
            nuevaFila.addView(precio);

            TextView cantidad = new TextView(this);
            cantidad.setText("1");
            cantidad.setPadding(20, 20, 20, 20); // añade el padding a la celda
            nuevaFila.addView(cantidad);

            TextView total = new TextView(this);
            total.setText(String.format(Locale.getDefault(), "%.2f", bebida.getPrecio()));
            total.setPadding(20, 20, 20, 20); // añade el padding a la celda
            nuevaFila.addView(total);

            // Añadir la fila a la tabla de facturación
            tablaFactura.addView(nuevaFila);

            // Actualizar el total de la factura
            this.total += bebida.getPrecio();
        } else {
            // Actualizar la fila existente
            TextView cantidad = (TextView) filaExistente.getChildAt(2);
            int cantidadInt = Integer.parseInt(cantidad.getText().toString()) + 1;
            cantidad.setText(String.valueOf(cantidadInt));

            TextView total = (TextView) filaExistente.getChildAt(3);
            double totalDouble = bebida.getPrecio() * cantidadInt;
            total.setText(String.format(Locale.getDefault(), "%.2f", totalDouble));

// Actualizar el total de la factura
            this.total += bebida.getPrecio();
        }

// Actualizar el total en la interfaz de usuario
        totalFactura.setText(String.format(Locale.getDefault(), "%.2f", this.total));
    }





}