package com.example.symptotrack.net.dto;

public class LoginRequest {
    public String identifier;
    public String password;
    public String role; // puede ser null
    public LoginRequest(String identifier, String password, String role) {
        this.identifier = identifier; this.password = password; this.role = role;
    }
}
