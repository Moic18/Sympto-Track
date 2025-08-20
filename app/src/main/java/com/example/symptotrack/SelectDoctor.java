package com.example.symptotrack;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.session.SessionManager;
import com.example.symptotrack.doctor.DoctorAdapter;
import com.example.symptotrack.doctor.DoctorItem;
import com.example.symptotrack.net.ApiClient;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDoctor extends AppCompatActivity {

    private RecyclerView rv;
    private DoctorAdapter adapter;
    private ApiService api;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_select_doctor);

        session = new SessionManager(this);
        api = ApiClient.get().create(ApiService.class);

        rv = findViewById(R.id.rv_doctores);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DoctorAdapter(doc -> {
            long patientId = session.getId(); // el usuario logueado
            if (!session.isUser() || patientId <= 0) {
                Toast.makeText(this, "Debes iniciar sesión como paciente", Toast.LENGTH_SHORT).show();
                return;
            }
            ShareRequest body = new ShareRequest(doc.id, patientId, "Compartido desde app", null);
            api.shareWithDoctor(body).enqueue(new Callback<ApiResponse<ShareResponse>>() {
                @Override public void onResponse(Call<ApiResponse<ShareResponse>> call, Response<ApiResponse<ShareResponse>> resp) {
                    if (resp.isSuccessful() && resp.body()!=null && resp.body().ok) {
                        Toast.makeText(SelectDoctor.this, "Compartido con " + doc.nombreCompleto, Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SelectDoctor.this, "No se pudo compartir", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(Call<ApiResponse<ShareResponse>> call, Throwable t) {
                    Toast.makeText(SelectDoctor.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
        rv.setAdapter(adapter);

        cargarDoctores();
    }

    private void cargarDoctores() {
        api.listDoctors().enqueue(new Callback<ApiResponse<List<DoctorDto>>>() {
            @Override public void onResponse(Call<ApiResponse<List<DoctorDto>>> call, Response<ApiResponse<List<DoctorDto>>> resp) {
                if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                    Toast.makeText(SelectDoctor.this, "No se pudieron cargar doctores", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<DoctorItem> ui = new ArrayList<>();
                for (DoctorDto d : resp.body().data) {
                    ui.add(new DoctorItem(d.doctor_id, d.nombreCompleto(), d.email));
                }
                adapter.submit(ui);
            }
            @Override public void onFailure(Call<ApiResponse<List<DoctorDto>>> call, Throwable t) {
                Toast.makeText(SelectDoctor.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
