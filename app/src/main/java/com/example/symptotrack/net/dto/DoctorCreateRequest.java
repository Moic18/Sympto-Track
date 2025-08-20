package com.example.symptotrack.net.dto;

public class DoctorCreateRequest {
    public String first_name, last_name, email, username, password;
    public DoctorCreateRequest(String fn, String ln, String em, String un, String pw) {
        this.first_name=fn; this.last_name=ln; this.email=em; this.username=un; this.password=pw;
    }
}
