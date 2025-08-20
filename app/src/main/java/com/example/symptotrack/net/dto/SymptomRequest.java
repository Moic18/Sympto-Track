package com.example.symptotrack.net.dto;

public class SymptomRequest {
    public int user_id;
    public String symptom_name;
    public int intensity;       // 0..10
    public String entry_date;   // "yyyy-MM-dd"
    public String entry_time;   // "HH:mm:ss" (opcional: puedes enviar null)
    public String notes;

    public SymptomRequest(int user_id, String symptom_name, int intensity, String entry_date, String entry_time, String notes) {
        this.user_id = user_id;
        this.symptom_name = symptom_name;
        this.intensity = intensity;
        this.entry_date = entry_date;
        this.entry_time = entry_time;
        this.notes = notes;
    }
}
