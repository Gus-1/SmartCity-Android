package com.example.smartcity.data.model;

public class Address {
    private Integer id;
    private String street;
    private Integer number;
    private Integer postalcode;
    private String city;
    private String country;

    public Address(Integer id, String street, Integer number, Integer postalcode, String city, String country) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.postalcode = postalcode;
        this.city = city;
        this.country = country;
    }

    //Create address
    public Address(String street, Integer number, Integer postalcode, String city, String country){
        this.street = street;
        this.number = number;
        this.postalcode = postalcode;
        this.city = city;
        this.country = country;
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

    public Integer getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(Integer postalcode) {
        this.postalcode = postalcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String fullAddress() {
        return number + " " + street + ", " + postalcode + " " + city + " " +  country;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number='" + number + '\'' +
                ", postalCode='" + postalcode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
