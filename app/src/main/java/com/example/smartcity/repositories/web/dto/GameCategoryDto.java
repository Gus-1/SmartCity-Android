package com.example.smartcity.repositories.web.dto;

import com.squareup.moshi.Json;

public class GameCategoryDto {
    @Json(name = "gamecategoryid")
    private Integer gameCategoryId;
    private String label;
    private String description;

    public GameCategoryDto(Integer gameCategoryId, String label, String description) {
        this.gameCategoryId = gameCategoryId;
        this.label = label;
        this.description = description;
    }

    public Integer getGameCategoryId() {
        return gameCategoryId;
    }

    public void setGameCategoryId(Integer gameCategory) {
        this.gameCategoryId = gameCategory;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
