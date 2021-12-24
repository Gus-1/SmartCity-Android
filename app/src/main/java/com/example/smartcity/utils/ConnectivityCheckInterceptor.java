package com.example.smartcity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.navigation.ActionOnlyNavDirections;

import com.example.smartcity.utils.errors.NoConnectionException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.channels.NoConnectionPendingException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityCheckInterceptor implements Interceptor{
    private Context context;

    public ConnectivityCheckInterceptor(Context context){
        this.context= context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
        if(!isOnline(context)) {
            throw new NoConnectionException();
        }

        Request.Builder builder = chain.request().newBuilder();
        return chain.proceed(builder.build());
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager == null ? null : connectivityManager.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
