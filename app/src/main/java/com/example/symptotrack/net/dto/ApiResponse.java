package com.example.symptotrack.net.dto;

public class ApiResponse<T> {
    public boolean ok;
    public String error;
    public T data;
}
