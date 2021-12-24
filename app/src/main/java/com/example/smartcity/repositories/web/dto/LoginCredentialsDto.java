package com.example.smartcity.repositories.web.dto;

public class LoginCredentialsDto {
    private String email;
    private String password;

    public LoginCredentialsDto(String email, String password){
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
