package com.example.smartcity.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity.R;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.BoardWebService;
import com.example.smartcity.repositories.web.RetrofitConfigurationService;
import com.example.smartcity.service.UserMapper;
import com.example.smartcity.view.RegisterFormState;
import com.example.smartcity.utils.InputCheck;
import com.example.smartcity.utils.errors.NoConnectionException;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends AndroidViewModel {

    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<RegisterFormState>();


    private MutableLiveData<String> _error = new MutableLiveData<String>();
    private LiveData<String> error = _error;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private BoardWebService webService;
    private UserMapper userMapper;

    public RegisterViewModel(@NotNull Application application){
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getBoardWebService();
        this.userMapper = UserMapper.getInstance();
        _error.setValue(null);
    }

    public LiveData<HashMap<String, String>> getInputErrors() {
        return inputErrors;
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Integer> getStatusCode() {
        return statusCode;
    }

    public MutableLiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public void addUser(String firstName, String lastName, String email, String password, String birthDate){
        String photoPath = "C:/photo/1";   //Default value when created with Android

        User user = new User(firstName, lastName, birthDate, email, photoPath, password);
        webService.insertUser(userMapper.mapToUserDto(user)).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "Pas de connection" : "Erreur interne");
            }
        });
    }

    public void registerDataChanged(String firstName, String lastName, String birthDate,
                                    String email, String password){
        if(!InputCheck.isWordCorrect(firstName)){
            registerFormState.setValue(new RegisterFormState(R.string.invalid_firstName, null,null,null,null));
        } else if (!InputCheck.isWordCorrect(lastName)){
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_lastName,null,null,null));
        } else if (!InputCheck.isEmailValid(email)){
            registerFormState.setValue(new RegisterFormState(null, null,null,R.string.invalid_email,null));
        } else if (!InputCheck.isPasswordValid(password)){
            registerFormState.setValue(new RegisterFormState(null, null,null,null,R.string.invalid_password));
        } else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }
}
