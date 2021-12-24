package com.example.smartcity.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.JWT;
import com.example.smartcity.R;
import com.example.smartcity.data.model.Address;
import com.example.smartcity.data.model.Event;
import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.BoardWebService;
import com.example.smartcity.repositories.web.RetrofitConfigurationService;
import com.example.smartcity.repositories.web.dto.EventDto;
import com.example.smartcity.repositories.web.dto.GameCategoryDto;
import com.example.smartcity.repositories.web.dto.InscriptionDto;
import com.example.smartcity.service.CategoryMapper;
import com.example.smartcity.service.EventMapper;
import com.example.smartcity.utils.InputCheck;
import com.example.smartcity.utils.errors.NoConnectionException;
import com.example.smartcity.view.EventFormState;
import com.example.smartcity.view.RegisterFormState;

import org.jetbrains.annotations.NotNull;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyEventViewModel extends AndroidViewModel {

    private MutableLiveData<List<Event>> _eventsCreator = new MutableLiveData<>();
    private LiveData<List<Event>> eventsCreator = _eventsCreator;

    private MutableLiveData<List<Event>> _eventsJoined = new MutableLiveData<>();
    private LiveData<List<Event>> eventsJoined = _eventsJoined;

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<Event> _eventChosen = new MutableLiveData<>();
    private MutableLiveData<Event> eventChosen = _eventChosen;

    private MutableLiveData<List<GameCategory>> _gameCategories = new MutableLiveData<>();
    private MutableLiveData<List<GameCategory>> gameCategories = _gameCategories;

    private MutableLiveData<EventFormState> createEventFormState = new MutableLiveData<EventFormState>();


    private MutableLiveData<String> _error = new MutableLiveData<>();
    private LiveData<String> error = _error;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private BoardWebService webService;
    private EventMapper eventMapper;
    private CategoryMapper categoryMapper;
    private String token;

    public MyEventViewModel(Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getBoardWebService();
        this.eventMapper = EventMapper.getInstance();
        this.categoryMapper = CategoryMapper.getInstance();

        token = getApplication().getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE).getString("JSONWEBTOKEN", "");

        JWT jwt = new JWT(token);
        Claim userData = jwt.getClaim("value");
        _user.setValue(userData.asObject(User.class));

        _error.setValue(null);
    }

    public LiveData<List<Event>> getEventsCreator(){
        return eventsCreator;
    }
    public LiveData<List<Event>> getEventsJoined(){
        return eventsJoined;
    }
    public LiveData<Event> getChosenEvent(){
        return eventChosen;
    }
    public LiveData<List<GameCategory>> getGameCategories() {
        return gameCategories;
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
    public MutableLiveData<User> getUser() {
        return _user;
    }

    public void loadCreatorsEvents(){
        webService.getAllCreatorsEvent("Bearer " + token, Objects.requireNonNull(_user.getValue()).getUserid()).enqueue(new Callback<List<EventDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<EventDto>> call, @NotNull Response<List<EventDto>> response) {
                if(response.isSuccessful() && !response.body().isEmpty()){
                    _eventsCreator.setValue(new ArrayList<>(eventMapper.mapToEvent(response.body())));
                }
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<List<EventDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "No Connection" : "Technical Error");
            }
        });
    }

    public void loadGameCategories(){
        webService.getAllCategory().enqueue(new Callback<List<GameCategoryDto>>() {
            @Override
            public void onResponse(Call<List<GameCategoryDto>> call, Response<List<GameCategoryDto>> response) {
                if(response.isSuccessful() && !response.body().isEmpty()){
                    _gameCategories.setValue(new ArrayList<>(categoryMapper.mapToCategory(response.body())));
                }
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(Call<List<GameCategoryDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "Pas de connection" : "Erreur interne");
            }
        });
    }

    public void deleteEvent(Integer eventId){
        webService.deleteEvent("Bearer " + token, eventId).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "No connection" : "technical error");
            }
        });
    }

    public void quitEvent(Integer eventId){
        webService.quitEvent("Bearer " + token, new InscriptionDto(eventId)).enqueue(new Callback<Void>() {

            @Override
            public void onResponse(@NotNull Call<Void> call, @NotNull Response<Void> response) {
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "No connection" : "technical error");
            }
        });
    }

    public void updateEvent(GameCategory category, Integer eventYear, Integer eventMonth, Integer eventDay, String eventDescription, Integer nbMaxPlayer,
                         String street, Integer number, String city, Integer postCode, String country){
        String token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");

        Address address = new Address(street, number, postCode, city, country);
        GregorianCalendar gregorianEventDate = new GregorianCalendar(eventYear, eventMonth, eventDay);

        Event event = new Event(category, gregorianEventDate, eventDescription, nbMaxPlayer, address);
        webService.updateEvent("Bearer " + token, getChosenEvent().getValue().getId(),
                eventMapper.mapToEventDto(event)).enqueue(new Callback<Void>() {
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

    public void loadJoinedEvents(){
        webService.getAllJoinedEvent("Bearer " + token, Objects.requireNonNull(_user.getValue()).getUserid()).enqueue(new Callback<List<EventDto>>() {
            @Override
            public void onResponse(@NonNull Call<List<EventDto>> call, @NonNull Response<List<EventDto>> response) {
                if(response.isSuccessful() && !response.body().isEmpty()){
                    _eventsJoined.setValue(new ArrayList<>(eventMapper.mapToEvent(response.body())));
                }
                _statusCode.setValue(response.code());
                _error.setValue(null);
            }

            @Override
            public void onFailure(@NonNull Call<List<EventDto>> call, Throwable t) {
                _error.setValue(t instanceof NoConnectionException ? "No Connection" : "Technical Error");
            }
        });
    }

    public void addEventDataChanged(String description, String street, String number,
                                    String city, String postCode, String country, String nbPlayer){
        if(!InputCheck.isWordCorrect(description)){
            createEventFormState.setValue(new EventFormState(null, null, R.string.invalid_description, null, null,
                    null, null, null, null));
        } else if (!InputCheck.isWordCorrect(street)){
            createEventFormState.setValue(new EventFormState(null, null, null, R.string.invalid_street, null,
                    null, null, null, null));
        } else if (!InputCheck.isANumber(number)) {
            createEventFormState.setValue(new EventFormState(null, null, null, null, R.string.invalid_number,
                    null, null, null, null));
        } else if (!InputCheck.isWordCorrect(city)) {
            createEventFormState.setValue(new EventFormState(null, null, null, null, null,
                    R.string.invalid_city, null, null, null));
        } else if (!InputCheck.isANumber(postCode)) {
            createEventFormState.setValue(new EventFormState(null, null, null, null, null,
                    null, R.string.invalid_postCode, null, null));
        } else if (!InputCheck.isWordCorrect(country)) {
            createEventFormState.setValue(new EventFormState(null, null, null, null, null,
                    null, null, R.string.invalid_country, null));
        } else if (!InputCheck.isANumber(nbPlayer)) {
            createEventFormState.setValue(new EventFormState(null, null, null, null, null,
                    null, null,null, R.string.invalid_nbPlayer));
        } else{
            createEventFormState.setValue(new EventFormState(true));
        }
    }
}