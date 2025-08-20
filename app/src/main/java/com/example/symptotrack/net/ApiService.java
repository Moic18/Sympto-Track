package com.example.symptotrack.net;

import com.example.symptotrack.net.dto.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // -------- AUTH --------
    @POST("auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body LoginRequest body);

    @POST("auth/register_user")
    Call<ApiResponse<UserData>> registerUser(@Body UserRegisterRequest body);

    // -------- DOCTORES --------
    @GET("doctors")
    Call<ApiResponse<List<DoctorDto>>> listDoctors();

    // -------- COMPARTIR --------
    @POST("patients/share")
    Call<ApiResponse<ShareResponse>> shareWithDoctor(@Body ShareRequest body);

    @GET("doctors/{doctor_id}/patients")
    Call<ApiResponse<List<PatientSummaryDto>>> listPatientsForDoctor(@Path("doctor_id") int doctorId);

    @GET("doctors/{doctor_id}/patients/{patient_id}")
    Call<ApiResponse<PatientDetailDto>> patientDetail(@Path("doctor_id") int doctorId, @Path("patient_id") long patientId);

    // -------- SÍNTOMAS --------
    // POST /symptoms (crear)
    @POST("symptoms")
    Call<ApiResponse<CreatedId>> createSymptom(@Body SymptomRequest body);

    // GET /users/{user_id}/symptoms?from&to (listar)
    @GET("users/{user_id}/symptoms")
    Call<ApiResponse<List<SymptomEntry>>> listSymptoms(@Path("user_id") long userId,
                                                       @Query("from") String from,
                                                       @Query("to") String to);
}
