package com.example.smartcity.repositories.web;

import com.example.smartcity.repositories.web.dto.EventDto;
import com.example.smartcity.repositories.web.dto.GameCategoryDto;
import com.example.smartcity.repositories.web.dto.InscriptionDto;
import com.example.smartcity.repositories.web.dto.LoginCredentialsDto;
import com.example.smartcity.repositories.web.dto.UserDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.DELETE;
import retrofit2.http.Path;

public interface BoardWebService {
    @GET("/event/")
    Call<List<EventDto>> getAllEvents(@Header(value="Authorization") String token);

    @GET("/event/user/{id}")
    Call<List<EventDto>> getAllCreatorsEvent(@Header(value="Authorization") String token, @Path("id") Integer id);

    @GET("/event/joined/{id}")
    Call<List<EventDto>> getAllJoinedEvent(@Header(value="Authorization") String token, @Path("id") Integer id);

    @DELETE("/event/{id}")
    Call<Void> deleteEvent(@Header(value="Authorization") String token, @Path("id") Integer id);

    @POST("/event/")
    Call<Void> insertEvent(@Header(value="Authorization") String token, @Body EventDto event);

    @POST("/user/login")
    Call<String> login(@Body LoginCredentialsDto loginCredentials);

    @POST("/user/")
    Call<Void> insertUser(@Body UserDto user);

    @POST("/inscription/")
    Call<Void> joinEvent(@Header(value="Authorization") String token, @Body InscriptionDto inscription);

    @POST("/inscription/unlink/")
    Call<Void> quitEvent(@Header(value="Authorization") String token, @Body InscriptionDto inscriptionDto);

    @GET("/gameCategory/")
    Call<List<GameCategoryDto>> getAllCategory();

    @PATCH("/event/{id}")
    Call<Void> updateEvent(@Header(value="Authorization") String token, @Path("id") Integer id, @Body EventDto eventDto);
}
