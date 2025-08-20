package com.example.symptotrack.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    // ----- Escritura -----
    // Estilo antiguo (auth.SessionManager)
    public void saveLogin(String role, long id) {
        save(role, id);
    }

    // Estilo nuevo
    public void save(String role, long id) {
        sp.edit().putString("role", role).putLong("id", id).apply();
    }

    // ----- Lectura -----
    // Estilo nuevo (corto)
    public String role() { return sp.getString("role", null); }
    public long id()     { return sp.getLong("id", -1); }

    // Estilo antiguo (getters largos)
    public String getRole() { return role(); }
    public long getId()     { return id(); }

    // ----- Helpers -----
    public boolean isUser()   { return "user".equalsIgnoreCase(role()); }
    public boolean isDoctor() { return "doctor".equalsIgnoreCase(role()); }

    public void clear() { sp.edit().clear().apply(); }
}
