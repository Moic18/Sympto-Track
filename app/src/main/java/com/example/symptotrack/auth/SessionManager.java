package com.example.symptotrack.auth;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private final SharedPreferences sp;
    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences("session", Context.MODE_PRIVATE);
    }
    public void saveLogin(String role, long id) {
        sp.edit().putString("role", role).putLong("id", id).apply();
    }
    public String getRole() { return sp.getString("role", null); }
    public long getId() { return sp.getLong("id", -1); }
    public boolean isDoctor() { return "doctor".equalsIgnoreCase(getRole()); }
    public boolean isUser() { return "user".equalsIgnoreCase(getRole()); }
    public void clear() { sp.edit().clear().apply(); }
}
