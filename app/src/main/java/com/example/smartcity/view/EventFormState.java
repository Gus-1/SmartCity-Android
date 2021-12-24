package com.example.smartcity.view;

import androidx.annotation.Nullable;

public class EventFormState {
    @Nullable
    private Integer categoryIdError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer descriptionError;
    @Nullable
    private Integer nbPlayerError;
    @Nullable
    private Integer streetError;
    @Nullable
    private Integer numberError;
    @Nullable
    private Integer cityError;
    @Nullable
    private Integer postCodeError;
    @Nullable
    private Integer countryError;
    private boolean isDataValid;

    public EventFormState(@Nullable Integer categoryIdError, @Nullable Integer dateError,
                          @Nullable Integer descriptionError, @Nullable Integer streetError,
                          @Nullable Integer numberError, @Nullable Integer cityError,
                          @Nullable Integer postCodeError, @Nullable Integer countryError,
                          @Nullable Integer nbPlayerError) {
        this.categoryIdError = categoryIdError;
        this.dateError = dateError;
        this.descriptionError = descriptionError;
        this.streetError = streetError;
        this.numberError = numberError;
        this.cityError = cityError;
        this.postCodeError = postCodeError;
        this.countryError = countryError;
        this.nbPlayerError = nbPlayerError;
        this.isDataValid = false;
    }

    public EventFormState(boolean isDataValid){
        this.categoryIdError = null;
        this.dateError = null;
        this.descriptionError = null;
        this.streetError = null;
        this.numberError = null;
        this.cityError = null;
        this.postCodeError = null;
        this.countryError = null;
        this.nbPlayerError = null;
        this.isDataValid = isDataValid;
    }


    @Nullable
    public Integer getCategoryIdError() {
        return categoryIdError;
    }
    @Nullable
    public Integer getDateError() {
        return dateError;
    }
    @Nullable
    public Integer getDescriptionError() {
        return descriptionError;
    }
    @Nullable
    public Integer getNbPlayerError() {
        return nbPlayerError;
    }
    @Nullable
    public Integer getStreetError() {
        return streetError;
    }
    @Nullable
    public Integer getNumberError() {
        return numberError;
    }
    @Nullable
    public Integer getCityError() {
        return cityError;
    }
    @Nullable
    public Integer getPostCodeError() {
        return postCodeError;
    }
    @Nullable
    public Integer getCountryError() {
        return countryError;
    }
    public boolean isDataValid() {
        return isDataValid;
    }
}
