package com.example.symptotrack.net.dto;

public class SymptomEntry {
    public int id;
    public int user_id;
    public String symptom_name;
    public int intensity;
    public String entry_date;
    public String entry_time; // puede venir null
    public String notes;
    public String created_at;
}
