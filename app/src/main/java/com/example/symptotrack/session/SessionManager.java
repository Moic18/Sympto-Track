package com.example.symptotrack.session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private static final String PREF = "session";
    private static final String K_ROLE = "role";
    private static final String K_ID = "id";
    private static final String K_NAME = "name";

    private final SharedPreferences sp;

    public SessionManager(Context ctx) {
        sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public void save(String role, int id, String fullName) {
        sp.edit()
                .putString(K_ROLE, role)
                .putInt(K_ID, id)
                .putString(K_NAME, fullName)
                .apply();
    }

    public String role() { return sp.getString(K_ROLE, null); }
    public int id() { return sp.getInt(K_ID, -1); }
    public String name() { return sp.getString(K_NAME, null); }

    public void clear() { sp.edit().clear().apply(); }
}
