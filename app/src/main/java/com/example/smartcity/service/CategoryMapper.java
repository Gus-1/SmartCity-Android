package com.example.smartcity.service;

import android.util.Log;

import com.example.smartcity.data.model.GameCategory;
import com.example.smartcity.repositories.web.dto.GameCategoryDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    private static CategoryMapper instance = null;

    public static CategoryMapper getInstance(){
        if(instance == null){
            instance = new CategoryMapper();
        }
        return instance;
    }

    public List<GameCategory> mapToCategory (List<GameCategoryDto> gameCategoryDto){
        return gameCategoryDto == null || gameCategoryDto.isEmpty()? null:
                gameCategoryDto.stream().map(gameCategory -> new GameCategory(gameCategory.getGameCategoryId(),
                        gameCategory.getLabel(), gameCategory.getDescription())).collect(Collectors.toCollection(ArrayList::new));
    }
}