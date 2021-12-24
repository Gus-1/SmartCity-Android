package com.example.smartcity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.smartcity.R;
import com.example.smartcity.data.model.NetworkError;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.BoardWebService;
import com.example.smartcity.repositories.web.RetrofitConfigurationService;
import com.example.smartcity.repositories.web.dto.LoginCredentialsDto;
import com.example.smartcity.view.LoginFormState;
import com.example.smartcity.utils.InputCheck;
import com.example.smartcity.utils.errors.NoConnectionException;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private String token;

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<LoginFormState>();

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private BoardWebService webService;


    public LoginViewModel(@NonNull Application application) {
        super(application);

        this.webService = RetrofitConfigurationService.getInstance(application).getBoardWebService();
        _error.setValue(null);
    }

    public LiveData<User> getLoginResult() {
        return user;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }

    public LiveData<Integer> getStatutCode() {
        return statusCode;
    }

    public String getToken(){
        return token;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public void login(String email, String password) {
        webService.login(new LoginCredentialsDto(email, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NotNull Call<String> call, @NotNull Response<String> response) {
                if (response.isSuccessful()) {
                    JWT jwt = new JWT(response.body());
                    Claim userData = jwt.getClaim("value");

                    token = response.body();
                    _user.setValue(userData.asObject(User.class));
                    _error.setValue(null);
                } else {
                    _error.setValue(new NetworkError("Bad Credentials"));
                }
                _statusCode.setValue(response.code());
            }

            @Override
            public void onFailure(@NotNull Call<String> call, @NotNull Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? new NetworkError("error") : new NetworkError("Technical error"));
            }
        });
    }


    public void loginDataChanged(String email, String password){
        if(!InputCheck.isEmailValid(email)){
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!InputCheck.isPasswordValid(password)){
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

}
