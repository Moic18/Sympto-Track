package com.example.symptotrack.net.dto;

public class ShareRequest {
    public int doctor_id;
    public long patient_id;
    public String note;   // opcional
    public String fecha;  // "YYYY-MM-DD" opcional
    public ShareRequest(int doctorId, long patientId, String note, String fecha) {
        this.doctor_id = doctorId; this.patient_id = patientId; this.note = note; this.fecha = fecha;
    }
}
