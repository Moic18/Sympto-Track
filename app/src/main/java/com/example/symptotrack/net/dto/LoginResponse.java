package com.example.symptotrack.net.dto;

public class LoginResponse {
    public String role; // "user" o "doctor"
    public long id;     // user_id o doctor_id según role
    public String first_name;
    public String last_name;
}
