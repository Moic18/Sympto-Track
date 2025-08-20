package com.example.symptotrack.Doc;

public class NoteItem {
    public final String fecha; // yyyy-MM-dd
    public final String texto;

    public NoteItem(String fecha, String texto) {
        this.fecha = fecha;
        this.texto = texto;
    }
}
