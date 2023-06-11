package com.example.herrikoprueba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.herrikoprueba.Clases.FacturaBebida;
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
    private  Button limpiarBoton;



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
        limpiarBoton = findViewById(R.id.limpiarBoton);



        // Llamar a la función para obtener las bebidas de la base de datos
        obtenerBebidas();

        //para el boton de limpiar
        limpiarBoton.setOnClickListener(v -> reiniciarFactura());

        // Para el botón de pagar
        btnPagar.setOnClickListener(v -> {
            // Crear el mensaje de desglose de la factura
            StringBuilder desgloseFactura = new StringBuilder();
            for (FacturaBebida facturaBebida : bebidasFactura) {
                BebidaComedor bebida = facturaBebida.getBebida();
                desgloseFactura.append("\nBebida: ").append(bebida.getId())
                        .append("\nUnidades: ").append(facturaBebida.getUnidades())
                        .append("\nTotal: ").append(String.format(Locale.getDefault(), "%.2f", bebida.getPrecio() * facturaBebida.getUnidades()))
                        .append("\n====================\n");
            }
            desgloseFactura.append("\nTotal a pagar: ").append(totalFactura.getText());

            // Crear un AlertDialog para confirmar el pago
            new AlertDialog.Builder(TPVActivity.this)
                    .setTitle("Confirmar pago")
                    .setMessage(desgloseFactura.toString())
                    .setPositiveButton("Aceptar", (dialog, which) -> {
                        // Realizar el pago aquí
                        realizarPago();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }


    // ... otras funciones irán aquí

    private void obtenerBebidas() {
        Servicios.getBebidasComedor(new Servicios.FirestoreListCallbackBebida<BebidaComedor>() {
            @Override
            public void onCallback(List<BebidaComedor> bebidas) {
                // Limpiamos la lista de bebidas y el grid de botones antes de agregar los nuevos botones
                TPVActivity.this.bebidas.clear();
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

                    // Añadir la bebida a la lista de bebidas de la actividad
                    TPVActivity.this.bebidas.add(bebida);

                    // Añadir el botón a la cuadrícula
                    gridBebidas.addView(boton);
                }
            }
        });
    }


    List<FacturaBebida> bebidasFactura = new ArrayList<>(); // Nuevo miembro de la clase para guardar las bebidas de la factura

    private void actualizarFactura(BebidaComedor bebida) {
        // Buscar si la bebida ya está en la factura
        FacturaBebida facturaBebida = null;
        for (FacturaBebida fb : bebidasFactura) {
            if (fb.getBebida().getId().equals(bebida.getId())) {
                facturaBebida = fb;
                break;
            }
        }

        if (facturaBebida == null) {
            // La bebida no está en la factura, así que la añadimos
            facturaBebida = new FacturaBebida(bebida);
            bebidasFactura.add(facturaBebida);

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
            // La bebida ya está en la factura, así que incrementamos las unidades
            facturaBebida.incrementarUnidades();

            // Actualizar la fila existente
            TableRow filaExistente = tablaFactura.findViewWithTag(bebida.getId());
            TextView cantidad = (TextView) filaExistente.getChildAt(2);
            cantidad.setText(String.valueOf(facturaBebida.getUnidades()));

            TextView total = (TextView) filaExistente.getChildAt(3);
            double totalDouble = bebida.getPrecio() * facturaBebida.getUnidades();
            total.setText(String.format(Locale.getDefault(), "%.2f", totalDouble));

            // Actualizar el total de la factura
            this.total += bebida.getPrecio();
        }

        // Actualizar el total en la interfaz de usuario
        totalFactura.setText(String.format(Locale.getDefault(), "%.2f", this.total));
    }



    private void realizarPago() {
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
        //Actualizar el total de dinero obtenido por ventas de bebidas
        DocumentReference docRefTotal = db.collection("BebidaComedor").document("dineroTotalBebidas");
        docRefTotal.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    double antiguoTotal = document.getDouble("total");
                    docRefTotal.update("total", antiguoTotal + this.total)
                            .addOnSuccessListener(aVoid -> {
                                // Actualización exitosa
                                this.total = 0.0;
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


    private void reiniciarFactura() {
        // Reinicia la tabla de la factura
        int count = tablaFactura.getChildCount();
        List<View> rowsToDelete = new ArrayList<>();

        for (int i = 0; i < count; i++) { // Comenzar desde 1 para evitar la fila de los títulos
            View child = tablaFactura.getChildAt(i);
            if (child instanceof TableRow) rowsToDelete.add(child);
        }

        // Ahora podemos eliminar las filas
        for (View row : rowsToDelete) {
            tablaFactura.removeView(row);
        }

        // Reinicia el total de la factura en la interfaz de usuario
        totalFactura.setText("0.00");
        this.total = 0.0;

        // Reinicia la lista de bebidas de la factura
        bebidasFactura.clear();



        // Reinicia la lista de bebidas
        for (BebidaComedor bebida : bebidas) {
            bebida.setUnidadesVendidas(0);
            bebida.setTotalEuros(0);
        }
    }




}