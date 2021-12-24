package com.example.smartcity.repositories.web.dto;

import com.squareup.moshi.Json;

public class AddressDto {
    private Integer id;
    private String street;
    private Integer number;
    private String country;
    private String city;
    @Json(name = "postalcode")
    private Integer postalCode;

    public AddressDto(String street, Integer number, String country, String city, Integer postalCode) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }
}
