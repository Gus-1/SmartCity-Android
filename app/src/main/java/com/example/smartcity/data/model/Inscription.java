package com.example.smartcity.data.model;

public class Inscription {
    private Integer inscriptionId;
    private Event event;
    private User user;

    public Inscription(Integer inscriptionId, Event event, User user) {
        this.inscriptionId = inscriptionId;
        this.event = event;
        this.user = user;
    }

    public Integer getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(Integer inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
