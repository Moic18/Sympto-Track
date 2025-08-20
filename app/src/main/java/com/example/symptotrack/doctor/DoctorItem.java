package com.example.symptotrack.doctor;

public class DoctorItem {
    public final int id;
    public final String nombreCompleto;
    public final String email;

    public DoctorItem(int id, String nombreCompleto, String email) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
    }
}
