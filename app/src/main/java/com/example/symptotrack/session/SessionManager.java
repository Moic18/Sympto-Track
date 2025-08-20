package com.example.symptotrack.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences("session", Context.MODE_PRIVATE);
    }

    public void save(String role, long id) {
        sp.edit().putString("role", role).putLong("id", id).apply();
    }

    // Alias usados por tu código actual:
    public String role() { return sp.getString("role", null); }
    public long id()     { return sp.getLong("id", -1); }

    // Si prefieres getters "clásicos":
    public String getRole() { return role(); }
    public long getId()     { return id(); }

    public boolean isUser()   { return "user".equalsIgnoreCase(role()); }
    public boolean isDoctor() { return "doctor".equalsIgnoreCase(role()); }
    public void clear() { sp.edit().clear().apply(); }
}
