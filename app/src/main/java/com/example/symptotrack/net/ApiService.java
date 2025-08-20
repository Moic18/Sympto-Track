package com.example.symptotrack.net;

import com.example.symptotrack.net.dto.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @POST("auth/login")
    Call<ApiResponse<LoginData>> login(@Body LoginRequest body);

    @POST("auth/register_user")
    Call<ApiResponse<UserData>> registerUser(@Body RegisterUserRequest body);

    @POST("symptoms")
    Call<ApiResponse<CreatedId>> createSymptom(@Body SymptomRequest body);

    @GET("users/{userId}/symptoms")
    Call<ApiResponse<List<SymptomEntry>>> listSymptoms(
            @Path("userId") int userId,
            @Query("from") String from,
            @Query("to") String to
    );

    // helper
    static ApiService api() { return ApiClient.get().create(ApiService.class); }
}
