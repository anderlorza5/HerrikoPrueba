package com.example.herrikoprueba.otros;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.herrikoprueba.HomeActivity;
import com.example.herrikoprueba.R;

public class LoginViewModel extends ViewModel {

    private LoginRepository loginRepository;
    private MutableLiveData<Boolean> loginResult = new MutableLiveData<>();

    public LoginViewModel() {
        loginRepository = new LoginRepository();
    }

    public LiveData<Boolean> login(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            loginResult.setValue(false);
        } else {
            loginRepository.login(email, password, success -> loginResult.setValue(success));
        }
        return loginResult;
    }

    public static class LoginActivity extends AppCompatActivity {

        private EditText emailEditText;
        private EditText passwordEditText;
        private Button loginButton;
        private LoginViewModel loginViewModel;
        private Button volverHome;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            emailEditText = findViewById(R.id.emailEditText);
            passwordEditText = findViewById(R.id.passwordEditText);
            loginButton = findViewById(R.id.loginButton);
            volverHome = findViewById(R.id.botonVolverHome);

            loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();

                    loginViewModel.login(email, password).observe(LoginActivity.this, success -> {
                        if (success) {
                            // Inicio de sesión exitoso, navega a la siguiente actividad
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error en el inicio de sesión, por favor intente nuevamente", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });



            Button btnRegister = findViewById(R.id.btn_register);findViewById(R.id.btn_register);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            });



            volverHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            });

        }
    }
}
