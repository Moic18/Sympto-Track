package com.example.symptotrack.Doc;

public class PatientItem {
    public long id;
    public String nombreCompleto;
    public String ultimaFecha;

    public PatientItem(long id, String nombreCompleto, String ultimaFecha) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.ultimaFecha = ultimaFecha;
    }
}
