package com.example.symptotrack.net.dto;

public class RegisterUserRequest {
    public String first_name;
    public String last_name;
    public String phone;
    public String usuario_correo; // email o username
    public String password;

    public RegisterUserRequest(String first_name, String last_name, String phone, String usuario_correo, String password) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone = phone;
        this.usuario_correo = usuario_correo;
        this.password = password;
    }
}
