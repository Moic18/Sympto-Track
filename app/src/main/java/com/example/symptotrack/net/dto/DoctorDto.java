package com.example.symptotrack.net.dto;

public class DoctorDto {
    public int doctor_id;
    public String first_name;
    public String last_name;
    public String email;
    public String username;

    public String nombreCompleto() {
        return (first_name == null ? "" : first_name) + " " + (last_name == null ? "" : last_name);
    }
}
