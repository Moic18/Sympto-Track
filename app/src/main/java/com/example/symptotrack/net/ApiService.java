package com.example.symptotrack.net;

import com.example.symptotrack.net.dto.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // --- Admin: Users
    @GET("admin/users")
    Call<ApiResponse<List<UserData>>> adminListUsers(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Query("active") Integer active // null, 0, 1
    );

    @POST("admin/users")
    Call<ApiResponse<CreatedId>> adminCreateUser(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Body UserRegisterRequest body
    );

    @PATCH("admin/users/{id}/status")
    Call<ApiResponse<GenericOk>> adminSetUserStatus(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Path("id") long userId,
            @Body StatusRequest body
    );

    // --- Admin: Doctors
    @GET("admin/doctors")
    Call<ApiResponse<List<DoctorDto>>> adminListDoctors(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Query("active") Integer active
    );

    @POST("admin/doctors")
    Call<ApiResponse<CreatedId>> adminCreateDoctor(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Body DoctorCreateRequest body
    );

    @PATCH("admin/doctors/{doctor_id}/status")
    Call<ApiResponse<GenericOk>> adminSetDoctorStatus(
            @Header("X-Admin-User") String adminUser,
            @Header("X-Admin-Pass") String adminPass,
            @Path("doctor_id") int doctorId,
            @Body StatusRequest body
    );

    // --- Auth ---
    @POST("auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest body);

    // REGISTRO de usuario (texto plano)
    @POST("auth/register_user")
    Call<ApiResponse<CreatedId>> registerUser(@Body UserRegisterRequest body);

    // Compartir historial con doctor
    @POST("patients/share")
    Call<ApiResponse<ShareResponse>> shareWithDoctor(@Body ShareRequest body);

    // --- Doctors & Symptoms ---
    // Listado de doctores disponibles para que un usuario comparta su historial
    @GET("doctors")
    Call<ApiResponse<List<DoctorDto>>> listDoctors();

    // Registrar un nuevo síntoma
    @POST("symptoms")
    Call<ApiResponse<CreatedId>> createSymptom(@Body SymptomRequest body);

    // Obtener historial de síntomas de un usuario en un rango de fechas
    @GET("users/{user_id}/symptoms")
    Call<ApiResponse<List<SymptomEntry>>> listSymptoms(
            @Path("user_id") long userId,
            @Query("from") String from,
            @Query("to") String to
    );

    // Listar pacientes que compartieron con un doctor
    @GET("doctors/{doctor_id}/patients")
    Call<ApiResponse<List<PatientSummaryDto>>> listPatientsForDoctor(
            @Path("doctor_id") long doctorId
    );

    @GET("patients/{patient_id}/detail")
    Call<ApiResponse<PatientDetailDto>> patientDetail(
            @Query("doctor_id") int doctorId,
            @Path("patient_id") long patientId
    );



}
