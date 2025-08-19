package com.example.symptotrack.historial;

public class HistHeader implements HistItem {
    public final String label;
    public HistHeader(String label) { this.label = label; }
    @Override public int getType() { return TYPE_HEADER; }
}
