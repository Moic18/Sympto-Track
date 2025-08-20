package com.example.symptotrack.net.dto;

import java.util.List;

public class PatientDetailDto {
    public Patient patient;
    public List<Note> notes;

    public static class Patient {
        public long id;
        public String first_name;
        public String last_name;
        public String email;
        public String phone;
        public String username;
    }
    public static class Note {
        public long id;
        public String fecha;      // "YYYY-MM-DD"
        public String note;       // texto
        public String created_at; // timestamp
    }
}
