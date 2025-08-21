package com.example.symptotrack.net.dto;

public class PatientSummaryDto {
    public long patient_id;
    public String patient_fullname;   // viene así del backend
    public String last_shared_date;   // "YYYY-MM-DD"
    public int shares_count;          // cantidad de veces compartido
}
