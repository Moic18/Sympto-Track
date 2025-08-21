package com.example.symptotrack.Doc;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.ApiClient;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.ApiResponse;
import com.example.symptotrack.net.dto.PatientDetailDto;
import com.example.symptotrack.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetallePaciente extends AppCompatActivity {

    private ImageView imgLogo;
    private TextView tvNombre, tvInfoContacto;
    private RecyclerView rvNotas;
    private NotesAdapter notesAdapter;

    private ApiService api;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_paciente);

        // Bind views (IDs del XML que me pasaste)
        imgLogo        = findViewById(R.id.img_logo);
        tvNombre       = findViewById(R.id.tv_nombre_detalle);
        tvInfoContacto = findViewById(R.id.tv_info_contacto);
        rvNotas        = findViewById(R.id.rv_notas);

        // Init
        session = new SessionManager(this);
        api     = ApiClient.get().create(ApiService.class);

        // Recycler para notas
        rvNotas.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter();
        rvNotas.setAdapter(notesAdapter);

        // Extras del intent
        long patientId   = getIntent().getLongExtra("patient_id", -1);
        String titleName = getIntent().getStringExtra("patient_name");
        if (titleName != null && !titleName.isEmpty()) {
            setTitle(titleName);
        }

        if (!session.isDoctor()) {
            Toast.makeText(this, "Debes iniciar sesión como doctor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        int doctorId = (int) session.getId();
        if (doctorId <= 0 || patientId <= 0) {
            Toast.makeText(this, "IDs inválidos. Regresa e inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cargarDetalle(doctorId, patientId);
    }

    private void cargarDetalle(int doctorId, long patientId) {
        api.patientDetail(doctorId, patientId)
                .enqueue(new Callback<ApiResponse<PatientDetailDto>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<PatientDetailDto>> call,
                                           Response<ApiResponse<PatientDetailDto>> resp) {
                        if (!resp.isSuccessful() || resp.body() == null) {
                            Toast.makeText(DetallePaciente.this, "Error de red", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ApiResponse<PatientDetailDto> body = resp.body();
                        if (!body.ok || body.data == null) {
                            String msg = body.error != null ? body.error : "No se pudo cargar el detalle";
                            Toast.makeText(DetallePaciente.this, msg, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        bindData(body.data);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<PatientDetailDto>> call, Throwable t) {
                        Toast.makeText(DetallePaciente.this, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bindData(PatientDetailDto dto) {
        // Datos personales
        if (dto.patient != null) {
            String nombre = ((dto.patient.first_name != null ? dto.patient.first_name : "") + " " +
                    (dto.patient.last_name  != null ? dto.patient.last_name  : "")).trim();
            tvNombre.setText(nombre.isEmpty() ? "-" : nombre);

            String email = dto.patient.email != null ? dto.patient.email : "-";
            String phone = dto.patient.phone != null ? dto.patient.phone : "-";
            tvInfoContacto.setText(email + " · " + phone);
        } else {
            tvNombre.setText("-");
            tvInfoContacto.setText("-");
        }

        // Notas (Recycler)
        notesAdapter.submit(dto.notes);
    }
}
