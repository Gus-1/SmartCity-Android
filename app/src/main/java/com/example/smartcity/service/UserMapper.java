package com.example.smartcity.service;

import com.example.smartcity.data.model.User;
import com.example.smartcity.repositories.web.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    private static UserMapper instance = null;

    public static  UserMapper getInstance(){
        if(instance == null){
            instance = new UserMapper();
        }
        return instance;
    }

    public UserDto mapToUserDto(User user){
        return new UserDto(user.getFirstname(), user.getName(), user.getBirthdate(), user.getPassword(),
                user.getEmail(), user.getPhotopath());
    }
}
