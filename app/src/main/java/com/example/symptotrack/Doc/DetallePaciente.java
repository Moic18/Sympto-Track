package com.example.symptotrack.Doc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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

public class DetallePaciente extends AppCompatActivity {

    private TextView tvNombre, tvInfo;
    private ImageView imgLogo;
    private RecyclerView rvNotas;
    private NotesAdapter adapter;

    private ApiService api;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_detalle_paciente);

        session = new SessionManager(this);
        api = ApiClient.get().create(ApiService.class);

        tvNombre = findViewById(R.id.tv_nombre_detalle);
        tvInfo   = findViewById(R.id.tv_info_contacto);
        imgLogo  = findViewById(R.id.img_logo);
        rvNotas  = findViewById(R.id.rv_notas);

        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter();
        rvNotas.setAdapter(adapter);

        long patientId = getIntent().getLongExtra("patient_id", -1);
        if (patientId < 0) {
            Toast.makeText(this, "ID de paciente inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String patientName = getIntent().getStringExtra("patient_name");
        tvNombre.setText(patientName != null ? patientName : "Paciente");

        cargarDetalle(patientId);
    }

    private void cargarDetalle(long patientId) {
        if (!session.isDoctor()) {
            Toast.makeText(this, "Debes iniciar sesión como doctor", Toast.LENGTH_SHORT).show();
            return;
        }
        int doctorId = (int) session.getId();

        api.patientDetail(doctorId, patientId).enqueue(new Callback<ApiResponse<PatientDetailDto>>() {
            @Override public void onResponse(Call<ApiResponse<PatientDetailDto>> call, Response<ApiResponse<PatientDetailDto>> resp) {
                if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok || resp.body().data == null) {
                    Toast.makeText(DetallePaciente.this, "No se pudo cargar el detalle", Toast.LENGTH_SHORT).show();
                    return;
                }
                PatientDetailDto d = resp.body().data;
                tvNombre.setText(d.first_name + " " + d.last_name);
                tvInfo.setText(d.email + " · " + d.username);


                List<NoteItem> notes = new ArrayList<>();
                if (d.notes != null) {
                    for (PatientDetailDto.NoteDto n : d.notes) {
                        notes.add(new NoteItem(n.fecha, n.note != null ? n.note : ""));
                    }
                }
                adapter.submit(notes);
            }
            @Override public void onFailure(Call<ApiResponse<PatientDetailDto>> call, Throwable t) {
                Toast.makeText(DetallePaciente.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
