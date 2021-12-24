package com.example.smartcity.data.model;

import androidx.annotation.NonNull;

public class GameCategory {
    private Integer gameCategoryId;
    private String label;
    private String description;

    public GameCategory(Integer gameCategoryId, String label, String description) {
        this.gameCategoryId = gameCategoryId;
        this.label = label;
        this.description = description;
    }

    public Integer getGameCategoryId() {
        return gameCategoryId;
    }

    public void setGameCategoryId(Integer gameCategoryId) {
        this.gameCategoryId = gameCategoryId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCategory that = (GameCategory) o;

        if (!getGameCategoryId().equals(that.getGameCategoryId())) return false;
        if (!getLabel().equals(that.getLabel())) return false;
        return getDescription().equals(that.getDescription());
    }

    @Override
    public int hashCode() {
        int result = getGameCategoryId().hashCode();
        result = 31 * result + getLabel().hashCode();
        result = 31 * result + getDescription().hashCode();
        return result;
    }

    @NonNull
    public String toString() {
        return getLabel();
    }
}
