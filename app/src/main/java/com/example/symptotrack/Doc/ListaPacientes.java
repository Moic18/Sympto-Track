package com.example.symptotrack.Doc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.session.SessionManager;
import com.example.symptotrack.net.ApiClient;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.*;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListaPacientes extends AppCompatActivity {

    private RecyclerView rv;
    private PatientAdapter adapter;
    private ApiService api;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_lista_pacientes);

        session = new SessionManager(this);
        api = ApiClient.get().create(ApiService.class);

        rv = findViewById(R.id.rv_pacientes);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PatientAdapter(p -> {
            Intent i = new Intent(ListaPacientes.this, DetallePaciente.class);
            i.putExtra("patient_id", p.id);
            i.putExtra("patient_name", p.nombreCompleto);
            startActivity(i);
        });
        rv.setAdapter(adapter);

        cargarPacientes();
    }

    private void cargarPacientes() {
        if (!session.isDoctor()) {
            Toast.makeText(this, "Debes iniciar sesión como doctor", Toast.LENGTH_SHORT).show();
            return;
        }
        int doctorId = (int) session.getId();
        api.listPatientsForDoctor(doctorId).enqueue(new Callback<ApiResponse<List<PatientSummaryDto>>>() {
            @Override public void onResponse(Call<ApiResponse<List<PatientSummaryDto>>> call, Response<ApiResponse<List<PatientSummaryDto>>> resp) {
                if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                    Toast.makeText(ListaPacientes.this, "No se pudieron cargar pacientes", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<PatientItem> ui = new ArrayList<>();
                for (PatientSummaryDto s : resp.body().data) {
                    ui.add(new PatientItem(s.patient_id, s.patient_fullname, s.last_shared_date));
                }
                adapter.submit(ui);
            }
            @Override public void onFailure(Call<ApiResponse<List<PatientSummaryDto>>> call, Throwable t) {
                Toast.makeText(ListaPacientes.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
