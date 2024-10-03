package com.example.herrikoprueba;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.herrikoprueba.BaseDeDatos.Servicios;
import com.example.herrikoprueba.Clases.BaseActivity;
import com.example.herrikoprueba.Clases.SendMail;

public class InscribirseActivity extends BaseActivity {

    Button inscribirseBoton;
    EditText nombre;
    EditText apellidos;
    EditText email;
    EditText numero;
    EditText dni; // **ADDED: Campo DNI**

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_inscribirse;  // Retorno el layout específico de MainActivity
    }

    private Button volverHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_inscribirse); // **REMOVED: Ya manejado por BaseActivity**
        inscribirseBoton = findViewById(R.id.inscribirseBoton);
        nombre = findViewById(R.id.nombreInput3);
        apellidos = findViewById(R.id.apellidosInput3);
        email = findViewById(R.id.emailInput3);
        numero = findViewById(R.id.numeroInput3);
        dni = findViewById(R.id.dniInput3); // **ADDED: Vinculación del campo DNI**

        inscribirseBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nom = nombre.getText().toString().trim();
                String ape = apellidos.getText().toString().trim();
                String correo = email.getText().toString().trim();
                String num = numero.getText().toString().trim();
                String dniInput = dni.getText().toString().trim(); // **ADDED: Obtener DNI**

                // **CHANGED: Incluir DNI en la validación de campos vacíos**
                if (nom.isEmpty() || ape.isEmpty() || correo.isEmpty() || num.isEmpty() || dniInput.isEmpty()) {
                    mostrarMensaje("Por favor, completa todos los campos");
                } else {
                    // Validaciones existentes
                    if (!num.matches("^[0-9]{9}$")) {
                        mostrarMensaje("El número de teléfono debe contener exactamente 9 dígitos");
                        return;
                    }

                    // Valida el correo electrónico
                    if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                        mostrarMensaje("Por favor, introduce una dirección de correo electrónico válida");
                        return;
                    }

                    // **ADDED: Validación del DNI**
                    if (!dniInput.matches("^[0-9]{8}[A-Za-z]$")) { // Ejemplo: 8 dígitos seguidos de una letra
                        mostrarMensaje("Por favor, introduce un DNI válido (8 números seguidos de una letra)");
                        return;
                    }

                    // **CHANGED: Llamar a la función para insertar el socio con DNI**
                    Servicios.insertarSocio2(nom + " " + ape, num, correo, dniInput);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String destinatario = correo;  // Reemplaza esto con la dirección de correo electrónico del destinatario
                            String asunto = "Inscripción Herriko Gazteak";
                            String texto = "Te has inscrito como socio en Herriko Gazteak. ¡ONGI ETORRI!";

                            // Enviar el correo
                            SendMail.send(destinatario, asunto, texto);
                        }
                    }).start();
                    mostrarMensaje("Te has inscrito como socio en Herriko Gazteak. ¡ONGI ETORRI!");
                }
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(InscribirseActivity.this);
        builder.setMessage(mensaje)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss(); // Cerrar el diálogo al hacer clic en Aceptar
                        // **CHANGED: Uso de .equals en lugar de '==' para comparar cadenas**
                        if (mensaje.equals("Te has inscrito como socio en Herriko Gazteak. ¡ONGI ETORRI!")) {
                            finish();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
