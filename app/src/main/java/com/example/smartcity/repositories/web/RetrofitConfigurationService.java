package com.example.smartcity.repositories.web;

import android.annotation.SuppressLint;
import android.content.Context;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import java.security.cert.CertificateException;

import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.example.smartcity.utils.ConnectivityCheckInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class RetrofitConfigurationService {
    private static final String URL = "http://10.0.2.2:3001";
    //private static final String URL = "http://10.10.0.153:3001";   //Ip comité tablette
    //private static final String URL = "http://172.1.3.167:3001";   //Ip wifi Henallux

    private Retrofit retrofitClient;
    private OkHttpClient client;

    private static BoardWebService BoardWebService;

    static{
        BoardWebService = null;
    }

    private RetrofitConfigurationService(Context context){
        client = getUnsafeOkHttpClient().addInterceptor(new ConnectivityCheckInterceptor(context)).build();

        Moshi moshiConverter = new Moshi.Builder().add(new KotlinJsonAdapterFactory()).build();

        this.retrofitClient = new Retrofit.Builder().client(client).addConverterFactory(MoshiConverterFactory.create(moshiConverter)).baseUrl(URL).build();
    }


    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try{
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            //Voluntary ignored
                        }
                        @SuppressLint("TrustAllX509TrustManager")
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            //Voluntary ignored
                        }
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static RetrofitConfigurationService getInstance(Context context){
        return new RetrofitConfigurationService(context);
    }

    public BoardWebService getBoardWebService(){
        if(BoardWebService == null){
            BoardWebService = retrofitClient.create(BoardWebService.class);
        }
        return BoardWebService;
    }
}
