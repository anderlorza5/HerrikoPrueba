package Fragments;

//import static androidx.core.app.ActivityCompat.Api16Impl.finishAffinity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.herrikoprueba.Funciones.funciones;
import com.example.herrikoprueba.HomeActivity;
import com.example.herrikoprueba.PantallaSocio;
import com.example.herrikoprueba.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class InicioSesionFragment extends Fragment {

    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;
    Button botonValidar;
   // Button botonSocio;
    EditText numero;
    EditText nombreInput;
    Button desvincular;
    TextView nombre;
    View rootView;
    TextView validado;

    public InicioSesionFragment() {
        // Constructor público vacío requerido
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.activity_validar_socio, container, false);

        // Initializa SharedPreferences
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Luego de inflar rootView y de inicializar sharedPreferences, inicializas tu UI dependiendo del estado actual
        String nombre = sharedPreferences.getString("nombre", null);
        String numero = sharedPreferences.getString("numero", null);

        // Comprueba si SharedPreferences contiene datos
        if (nombre != null && numero != null) {
            // Si hay datos, inicializa la UI correspondiente
            initUIDEsvincular(rootView);
        } else {
            // Si no hay datos, inicializa la UI diferente
            initUI(rootView);
        }

        return rootView;
    }

    public void refreshUI() {
        String nombre = sharedPreferences.getString("nombre", null);
        String numero = sharedPreferences.getString("numero", null);

        // Comprueba si SharedPreferences contiene datos
        if (nombre != null && numero != null) {
            // Si hay datos, inicializa la UI correspondiente
            initUIDEsvincular(rootView);
        } else {
            // Si no hay datos, inicializa la UI diferente
            initUI(rootView);
        }
    }

    public void initUI(View rootView) {
        botonValidar = rootView.findViewById(R.id.validarBoton);
        validado = rootView.findViewById(R.id.textViewValidado);

        nombreInput = rootView.findViewById(R.id.nombreInput);
        numero = rootView.findViewById(R.id.numeroInput);

        desvincular = rootView.findViewById(R.id.DEsvincularBoton);
        nombre = rootView.findViewById(R.id.NombreTexto);

        desvincular.setVisibility(View.INVISIBLE);
        nombre.setVisibility(View.INVISIBLE);
        validado.setVisibility(View.INVISIBLE);

        botonValidar.setVisibility(View.VISIBLE);
       // botonSocio.setVisibility(View.VISIBLE);
        nombreInput.setVisibility(View.VISIBLE);
        numero.setVisibility(View.VISIBLE);

        // Initializa Firestore y SharedPreferences
        db = FirebaseFirestore.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        botonValidar.setOnClickListener(v -> {
            String nombreInp = nombreInput.getText().toString();
            String numeroInput = numero.getText().toString();

            // Consulta a Firestore para ver si existe el socio
            db.collection("Socios")
                    .whereEqualTo("nombreCompleto", nombreInp)
                    .whereEqualTo("movilNumero", numeroInput)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            // El socio existe, almacena los datos en SharedPreferences
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("nombre", nombreInp);
                            editor.putString("numero", numeroInput);
                            editor.apply();
                            String primerNombre = funciones.obtenerPrimerNombre(getActivity());
                            funciones.mostrarMensajeCerrar(getActivity(),"Hola "+primerNombre+", validacion realizada con exito");

                            // Cambia el texto del botón
                            String primerNombree = funciones.obtenerPrimerNombre(getActivity());
                            //botonSocio.setText(primerNombree);

                            // Refresca la UI
                            getActivity().runOnUiThread(() -> refreshUI());
                        } else {
                            // El socio no existe o hubo un error
                            funciones.mostrarMensaje(getActivity(), "no existe ningun socio con esos datos PIYAO");
                        }
                    });

            //funciones.mostrarMensaje(getActivity(),"Cuenta validada");
            //Intent intent = new Intent("ReservasActualizadas");
           /* Intent intent = new Intent(getActivity(), HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            getActivity().finishAffinity();
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);*/
        });

    }


    public void initUIDEsvincular(View rootView){
        validado = rootView.findViewById(R.id.textViewValidado);
        botonValidar = rootView.findViewById(R.id.validarBoton);
        //botonSocio = rootView.findViewById(R.id.validarBotonMenuBarra);
        nombreInput = rootView.findViewById(R.id.nombreInput);
        numero = rootView.findViewById(R.id.numeroInput);

        desvincular = rootView.findViewById(R.id.DEsvincularBoton);
        nombre = rootView.findViewById(R.id.NombreTexto);

        botonValidar.setVisibility(View.INVISIBLE);
       // botonSocio.setVisibility(View.INVISIBLE);
        nombreInput.setVisibility(View.INVISIBLE);
        numero.setVisibility(View.INVISIBLE);

        desvincular.setVisibility(View.VISIBLE);
        nombre.setVisibility(View.VISIBLE);
        validado.setVisibility(View.VISIBLE);



        nombre.setText(funciones.obtenerNombreCompleto(getActivity()));

        desvincular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funciones.borrarNombreCompleto(getActivity());
                funciones.mostrarMensaje(getActivity(), "desvinculacion realizada");

                // Refresca la UI
                getActivity().runOnUiThread(() -> refreshUI());
                funciones.mostrarMensajeCerrar(getActivity(),"Desvinculacion realizada");
            }
            //Intent intent = new Intent("ReservasActualizadas");
            //LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
        });
    }
}

