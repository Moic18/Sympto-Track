package com.example.symptotrack.net.dto;

public class UserData {
    public long id;
    public String first_name;
    public String last_name;
    public String phone;
    public String email;    // puede venir null si se registró con username
    public String username; // puede venir null si se registró con email
}
