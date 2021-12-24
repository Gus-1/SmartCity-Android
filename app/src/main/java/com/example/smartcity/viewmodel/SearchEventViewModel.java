package com.example.smartcity.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.smartcity.data.model.Event;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.BoardWebService;
import com.example.smartcity.repositories.web.RetrofitConfigurationService;
import com.example.smartcity.repositories.web.dto.EventDto;
import com.example.smartcity.repositories.web.dto.InscriptionDto;
import com.example.smartcity.service.EventMapper;
import com.example.smartcity.utils.errors.NoConnectionException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchEventViewModel extends AndroidViewModel {

    private MutableLiveData<List<Event>> _events = new MutableLiveData<>();
    private LiveData<List<Event>> events = _events;

    private MutableLiveData<Event> _eventChosen = new MutableLiveData<>();
    private MutableLiveData<Event> eventChosen = _eventChosen;

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<String> error = _error;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private BoardWebService webService;
    private EventMapper eventMapper;
    private String token;

    public SearchEventViewModel(Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getBoardWebService();
        this.eventMapper = EventMapper.getInstance();

        token = getApplication().getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("value");
        _user.setValue(userData.asObject(User.class));

        _error.setValue(null);
    }

    public LiveData<List<Event>> getEvents(){
        return events;
    }
    public LiveData<Event> getChosenEvent(){
        return eventChosen;
    }
    public void setChosenEvent(Event chosenEvent){
        _eventChosen.setValue(chosenEvent);
    }
    public LiveData<String> getError(){
        return error;
    }
    public LiveData<Integer> getStatusCode(){
        return statusCode;
    }

    public void loadEvents(){
        webService.getAllEvents("Bearer " + token).enqueue(new Callback<List<EventDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<EventDto>> call, @NotNull Response<List<EventDto>> response) {
                if(response.isSuccessful()){
                    _events.setValue(new ArrayList<>(eventMapper.mapToEvent(response.body())));
                }
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(Call<List<EventDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException? "No Connection" : "Technical Error");
            }
        });
    }

    public void joinEvent(Event event){

        webService.joinEvent("Bearer " + token, new InscriptionDto(event.getId())).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                if(response.isSuccessful()){

                }
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException? "No Connection" : "Technical Error");
            }
        });
    }
}