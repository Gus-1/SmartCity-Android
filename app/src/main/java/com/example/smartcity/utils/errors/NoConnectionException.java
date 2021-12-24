package com.example.smartcity.utils.errors;

import android.content.Context;

import com.example.smartcity.utils.ConnectivityCheckInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;

public class NoConnectionException extends IOException {
    @Override
    public String getMessage(){
        return "No Internet Connection Exception";
    }
}
