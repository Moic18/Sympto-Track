package com.example.symptotrack.net.dto;

public class SharedPatientDto {
    public long patient_id;
    public String first_name;
    public String last_name;
    public String email;
    public String username;

    // opcionales si tu backend los expone:
    public String last_note;   // última nota compartida
    public String last_fecha;  // "yyyy-MM-dd" de la última nota
}
