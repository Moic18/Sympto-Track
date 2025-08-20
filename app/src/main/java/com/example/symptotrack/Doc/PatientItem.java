package com.example.symptotrack.Doc;

public class PatientItem {
    public final int id;
    public final String nombreCompleto;
    public final String ultimoEnvio; // fecha texto

    public PatientItem(int id, String nombreCompleto, String ultimoEnvio) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.ultimoEnvio = ultimoEnvio;
    }
}