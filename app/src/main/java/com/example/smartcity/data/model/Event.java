package com.example.smartcity.data.model;

import java.util.GregorianCalendar;

public class Event {
    private Integer id;
    private User creator;
    private GameCategory gameCategory;
    private GregorianCalendar creationDate;
    private GregorianCalendar eventDate;
    private Address address;
    private String eventDescription;
    private Boolean isVerified;
    private Integer nbMaxPlayer;
    private String adminMessage;

    public Event(Integer id, User creator, GameCategory gameCategory, GregorianCalendar creationDate, GregorianCalendar eventDate, Address address, String eventDescription, Boolean isVerified, Integer nbMaxPlayer, String adminMessage) {
        this.id = id;
        this.creator = creator;
        this.gameCategory = gameCategory;
        this.creationDate = creationDate;
        this.eventDate = eventDate;
        this.address = address;
        this.eventDescription = eventDescription;
        this.isVerified = isVerified;
        this.nbMaxPlayer = nbMaxPlayer;
        this.adminMessage = adminMessage;
    }

    //Create event
    public Event(GameCategory gameCategory, GregorianCalendar eventDate, String eventDescription, Integer nbMaxPlayer, Address address) {
        this.gameCategory = gameCategory;
        this.eventDate = eventDate;
        this.address = address;
        this.eventDescription = eventDescription;
        this.nbMaxPlayer = nbMaxPlayer;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public GameCategory getGameCategory() {
        return gameCategory;
    }

    public void setGameCategoryId(GameCategory gameCategory) {
        this.gameCategory = gameCategory;
    }

    public GregorianCalendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(GregorianCalendar creationDate) {
        this.creationDate = creationDate;
    }

    public GregorianCalendar getEventDate() {
        return eventDate;
    }

    public void setEventDate(GregorianCalendar eventDate) {
        this.eventDate = eventDate;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public Integer getNbMaxPlayer() {
        return nbMaxPlayer;
    }

    public void setNbMaxPlayer(Integer nbMaxPlayer) {
        this.nbMaxPlayer = nbMaxPlayer;
    }

    public String getAdminMessage() {
        return adminMessage;
    }

    public void setAdminMessage(String adminMessage) {
        this.adminMessage = adminMessage;
    }
}
