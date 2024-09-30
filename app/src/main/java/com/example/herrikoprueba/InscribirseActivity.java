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

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_inscribirse;  // Retorno el layout específico de MainActivity
    }

    private Button volverHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_inscribirse);
        inscribirseBoton = findViewById(R.id.inscribirseBoton);
        EditText nombre = findViewById(R.id.nombreInput3);
        EditText apellidos = findViewById(R.id.apellidosInput3);
        EditText email = findViewById(R.id.emailInput3);
        EditText numero = findViewById(R.id.numeroInput3);


        inscribirseBoton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nombre.getText().toString().isEmpty() || apellidos.getText().toString().isEmpty() || email.getText().toString().isEmpty() || numero.getText().toString().isEmpty()) {
                    mostrarMensaje("Por favor, completa todos los campos");
                } else {
                    String num = numero.getText().toString();
                    String correo = email.getText().toString();

                    if (!num.matches("^[0-9]{9}$")) {
                        mostrarMensaje("El número de teléfono debe contener exactamente 9 dígitos");
                        return;
                    }

                    // Valida el correo electrónico
                    if (!correo.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
                        mostrarMensaje("Por favor, introduce una dirección de correo electrónico válida");
                        return;
                    }

                    String nom = nombre.getText().toString();
                    String ape = apellidos.getText().toString();

                    // Llamar a la función para insertar el socio en la base de datos
                    Servicios.insertarSocio2(nom + " " + ape, num, correo);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String destinatario = correo;  // Reemplaza esto con la dirección de correo electrónico del destinatario
                            String asunto = "Inscripcion Herriko Gazteak ";
                            String texto = "te has inscrito como socio en Herriko Gazteak ONGI ETORRI!! ";

                            // Enviar el correo
                            SendMail.send(destinatario, asunto, texto);
                        }
                    }).start();
                    mostrarMensaje("te has inscrito como socio en Herriko Gazteak ONGI ETORRI");

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
                        if (mensaje== "te has inscrito como socio en Herriko Gazteak ONGI ETORRI" ){
                            finish();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}