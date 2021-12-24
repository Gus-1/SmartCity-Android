package com.example.smartcity.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.smartcity.R;
import com.example.smartcity.data.model.Address;
import com.example.smartcity.data.model.Event;
import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.BoardWebService;
import com.example.smartcity.repositories.web.RetrofitConfigurationService;
import com.example.smartcity.repositories.web.dto.GameCategoryDto;
import com.example.smartcity.view.EventFormState;
import com.example.smartcity.utils.InputCheck;
import com.example.smartcity.utils.errors.NoConnectionException;
import com.example.smartcity.service.EventMapper;
import com.example.smartcity.service.CategoryMapper;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEventViewModel extends AndroidViewModel {

    private MutableLiveData<EventFormState> createEventFormState = new MutableLiveData<EventFormState>();

    private MutableLiveData<User> _user = new MutableLiveData<>();
    private LiveData<User> user = _user;

    private MutableLiveData<List<GameCategory>> _gameCategory = new MutableLiveData<>();
    private LiveData<List<GameCategory>> gameCategory = _gameCategory;

    private MutableLiveData<String> _error = new MutableLiveData<String>();
    private LiveData<String> error = _error;

    private MutableLiveData<Integer> _statusCode = new MutableLiveData<>();
    private LiveData<Integer> statusCode = _statusCode;

    private MutableLiveData<HashMap<String , String>> _inputErrors = new MutableLiveData<>();
    private LiveData<HashMap<String, String>> inputErrors = _inputErrors;

    private BoardWebService webService;
    private EventMapper eventMapper;
    private CategoryMapper categoryMapper;

    public CreateEventViewModel(@NonNull Application application) {
        super(application);
        this.webService = RetrofitConfigurationService.getInstance(getApplication()).getBoardWebService();
        this.eventMapper = EventMapper.getInstance();
        this.categoryMapper = CategoryMapper.getInstance();
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

    public LiveData<List<GameCategory>> getGameCategory() {
        return gameCategory;
    }

    public MutableLiveData<EventFormState> getCreateEventFormState() {
        return createEventFormState;
    }

    public void addEvent(GameCategory category, Integer eventYear, Integer eventMonth, Integer eventDay, String eventDescription, Integer nbMaxPlayer,
                         String street, Integer number, String city, Integer postCode, String country){

        String token = getApplication()
                .getSharedPreferences("JSONWEBTOKEN", Context.MODE_PRIVATE)
                .getString("JSONWEBTOKEN", "");



        Address address = new Address(street, number, postCode, city, country);
        GregorianCalendar gregorianEventDate = new GregorianCalendar(eventYear, eventMonth, eventDay);

        Event event = new Event(category, gregorianEventDate, eventDescription, nbMaxPlayer, address);
        webService.insertEvent("Bearer " + token,eventMapper.mapToEventDto(event)).enqueue(new Callback<Void>() {
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

    public void loadGameCategory(){
        webService.getAllCategory().enqueue(new Callback<List<GameCategoryDto>>() {
            @Override
            public void onResponse(Call<List<GameCategoryDto>> call, Response<List<GameCategoryDto>> response) {
                if(response.isSuccessful() && !response.body().isEmpty()){
                    _gameCategory.setValue(new ArrayList<>(categoryMapper.mapToCategory(response.body())));
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
