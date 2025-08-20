package com.example.symptotrack.net.dto;

import java.util.List;

public class PatientDetailDto {
    public long id;
    public String first_name;
    public String last_name;
    public String email;
    public String username;

    public List<NoteDto> notes;

    public static class NoteDto {
        public long id;
        public String note;
        public String fecha;
    }
}
