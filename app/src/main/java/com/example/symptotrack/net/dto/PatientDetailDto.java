// com/example/symptotrack/net/dto/PatientDetailDto.java
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
        public String fecha;       // "yyyy-MM-dd"
        public String note;        // texto
        public String created_at;  // opcional
    }
}
