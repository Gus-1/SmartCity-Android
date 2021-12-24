package com.example.smartcity.repositories.web.dto;

public class InscriptionDto {
    private Integer inscriptionId;
    private Integer eventId;
    private Integer userId;

    public InscriptionDto(Integer eventId) {
        this.eventId = eventId;
    }

    public InscriptionDto(Integer inscriptionId, Integer eventId, Integer userId) {
        this.inscriptionId = inscriptionId;
        this.eventId = eventId;
        this.userId = userId;
    }

    public Integer getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Integer inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public Integer getEvent() {
        return eventId;
    }

    public void setEvent(Integer eventId) {
        this.eventId = eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
