package com.example.smartcity.data.model;

public class User {
    private Integer userid;
    private String firstname;
    private String name;
    private String birthdate;
    private Boolean isadmin;
    private String email;
    private String password;
    private String photopath;

    public User(Integer userid, String firstname, String name, String birthdate,
                Boolean isadmin, String email, String photopath) {
        this.userid = userid;
        this.firstname = firstname;
        this.name = name;
        this.birthdate = birthdate;
        this.isadmin = isadmin;
        this.email = email;
        this.photopath = photopath;
    }

    //Register's constructor
    public User(String firstname, String name, String birthdate,
                String email, String photopath, String password){
        this.firstname = firstname;
        this.name = name;
        this.birthdate = birthdate;
        this.email = email;
        this.photopath = photopath;
        this.password = password;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Boolean getAdmin() {
        return isadmin;
    }

    public void setAdmin(Boolean admin) {
        isadmin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotopath() {
        return photopath;
    }

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return firstname + " " + name;
    }
}
