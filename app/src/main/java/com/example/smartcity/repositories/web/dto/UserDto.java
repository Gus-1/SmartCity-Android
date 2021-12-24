package com.example.smartcity.repositories.web.dto;

import com.squareup.moshi.Json;

public class UserDto {
    @Json(name = "userid")
    private Integer userId;
    @Json(name = "firstname")
    private String firstName;
    private String lastname;
    private String birthdate;
    @Json(name = "isadmin")
    private Boolean isAdmin;
    private String password;
    private String email;
    @Json(name = "photopath")
    private String photoPath;

    public UserDto(String firstName, String lastname, String birthdate,
                   String password, String email, String photoPath) {
        this.firstName = firstName;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.password = password;
        this.email = email;
        this.photoPath = photoPath;
    }

    public UserDto(Integer id, String firstName, String lastname, String birthdate, String password,
                   String email, String photoPath, Boolean isAdmin){
        this(firstName, lastname, birthdate, password, email, photoPath);
        this.userId = id;
        this.isAdmin = isAdmin;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
