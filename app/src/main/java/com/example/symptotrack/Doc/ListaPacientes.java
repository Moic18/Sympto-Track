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
        long doctorId = session.getId();
        if (doctorId <= 0) {
            Toast.makeText(this, "ID de doctor inválido. Vuelva a iniciar sesión.", Toast.LENGTH_SHORT).show();
            return;
        }
        api.listPatientsForDoctor(doctorId)
                .enqueue(new retrofit2.Callback<ApiResponse<java.util.List<PatientSummaryDto>>>() {
                    @Override
                    public void onResponse(retrofit2.Call<ApiResponse<java.util.List<PatientSummaryDto>>> call,
                                           retrofit2.Response<ApiResponse<java.util.List<PatientSummaryDto>>> resp) {
                        if (!resp.isSuccessful() || resp.body() == null || !resp.body().ok || resp.body().data == null) {
                            String msg = resp.body() != null && resp.body().error != null
                                    ? resp.body().error
                                    : "No se pudo cargar los pacientes";
                            Toast.makeText(ListaPacientes.this, msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        java.util.List<PatientSummaryDto> lista = resp.body().data;
                        java.util.List<PatientItem> items = new ArrayList<>();
                        for (PatientSummaryDto dto : lista) {
                            String nombre = ((dto.first_name != null ? dto.first_name : "") + " " +
                                    (dto.last_name != null ? dto.last_name : "")).trim();
                            String fecha = dto.last_fecha != null ? dto.last_fecha : "";
                            items.add(new PatientItem(dto.patient_id, nombre, fecha));
                        }
                        adapter.submit(items);
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ApiResponse<java.util.List<PatientSummaryDto>>> call, Throwable t) {
                        Toast.makeText(ListaPacientes.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
