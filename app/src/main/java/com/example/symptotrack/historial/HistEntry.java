package com.example.symptotrack.historial;

public class HistEntry implements HistItem {
    public final String nombre;
    public final int intensidad; // 0..10
    public final String fecha;   // yyyy-MM-dd
    public final String hora;    // HH:mm
    public final String nota;

    public HistEntry(String nombre, int intensidad, String fecha, String hora, String nota) {
        this.nombre = nombre;
        this.intensidad = intensidad;
        this.fecha = fecha;
        this.hora = hora;
        this.nota = nota;
    }

    @Override public int getType() { return TYPE_ENTRY; }
}
