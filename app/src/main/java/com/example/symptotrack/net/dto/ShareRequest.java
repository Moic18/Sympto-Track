package com.example.symptotrack.net.dto;

public class ShareRequest {
    public long doctor_id;
    public long patient_id;
    public String note;   // opcional
    public String fecha;  // "yyyy-MM-dd" opcional

    public ShareRequest(long doctor_id, long patient_id, String note, String fecha) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.note = note;
        this.fecha = fecha;
    }
}
