package com.example.symptotrack.net.dto;

public class UserRegisterRequest {
    public String first_name, last_name, phone, usuario_correo, password;
    public UserRegisterRequest(String fn, String ln, String ph, String uc, String pw) {
        this.first_name=fn; this.last_name=ln; this.phone=ph; this.usuario_correo=uc; this.password=pw;
    }
}
