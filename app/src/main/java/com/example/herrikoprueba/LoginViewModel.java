package com.example.herrikoprueba;



import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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
}
