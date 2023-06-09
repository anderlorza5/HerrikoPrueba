package com.example.herrikoprueba.otros;



import com.google.firebase.auth.FirebaseAuth;

public class LoginRepository {

    private FirebaseAuth firebaseAuth;

    public LoginRepository() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void login(String email, String password, LoginCallback callback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onLoginResult(true);
                    } else {
                        callback.onLoginResult(false);
                    }
                });
    }

    public interface LoginCallback {
        void onLoginResult(boolean success);
    }
}
