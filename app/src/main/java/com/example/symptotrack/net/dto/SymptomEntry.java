package com.example.symptotrack.net.dto;

public class SymptomEntry {
    public int id;
    public long user_id;
    public String symptom_name;
    public int intensity;          // 0..10
    public String entry_date;      // "YYYY-MM-DD"
    public String entry_time;      // "HH:mm:ss" o null
    public String notes;           // puede ser null
    public String created_at;      // timestamp
}
