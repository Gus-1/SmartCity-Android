package com.example.smartcity.view;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer firstNameError;
    @Nullable
    private Integer lastNameError;
    @Nullable
    private Integer dateError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer passwordError;
    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer firstNameError, @Nullable Integer lastNameError,
                             @Nullable Integer dateError, @Nullable Integer emailError,
                             @Nullable Integer passwordError) {
        this.firstNameError = firstNameError;
        this.lastNameError = lastNameError;
        this.dateError = dateError;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid){
        this.emailError = null;
        this.passwordError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getEmailError(){
        return emailError;
    }
    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }
    @Nullable
    public Integer getFirstNameError() {
        return firstNameError;
    }
    @Nullable
    public Integer getLastNameError() {
        return lastNameError;
    }
    @Nullable
    Integer getDateError() {
        return dateError;
    }

    public boolean isDataValid(){
        return isDataValid;
    }
}
