package com.example.smartcity.data.model;

import com.example.smartcity.R;

public class NetworkError {

    private String errorMessage;

    public NetworkError(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
