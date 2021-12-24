package com.example.smartcity.repositories.web.dto;

import com.squareup.moshi.Json;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class EventDto {
    @Json(name = "eventid")
    private Integer id;
    @Json(name = "user")
    private UserDto creator;
    @Json(name = "gamecategory")
    private GameCategoryDto gameCategory;
    @Json(name = "creationdate")
    private String creationDate;
    @Json(name = "eventdate")
    private String eventDate;
    @Json(name = "address")
    private AddressDto address;
    @Json(name = "eventdescription")
    private String eventDescription;
    @Json(name = "isverified")
    private Boolean isVerified;
    @Json(name = "nbmaxplayer")
    private Integer nbMaxPlayer;
    @Json(name = "adminmessage")
    private String adminMessage;

    public EventDto(GameCategoryDto gameCategory,String eventDate, AddressDto address, String eventDescription, Boolean isVerified, Integer nbMaxPlayer, String adminMessage) {
        this.gameCategory = gameCategory;
        this.eventDate = eventDate;
        this.address = address;
        this.eventDescription = eventDescription;
        this.isVerified = isVerified;
        this.nbMaxPlayer = nbMaxPlayer;
        this.adminMessage = adminMessage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDto getCreator() {
        return creator;
    }

    public void setCreator(UserDto creator) {
        this.creator = creator;
    }

    public GameCategoryDto getGameCategory() {
        return gameCategory;
    }

    public void setGameCategory(GameCategoryDto gameCategory) {
        this.gameCategory = gameCategory;
    }

    public GregorianCalendar getCreationDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Objects.requireNonNull(format.parse(creationDate)));

        return calendar;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public GregorianCalendar getEventDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE);

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(Objects.requireNonNull(format.parse(eventDate)));

        return calendar;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
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
