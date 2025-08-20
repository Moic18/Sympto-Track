package com.example.symptotrack;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.*;
import com.example.symptotrack.session.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Regis_sintomas extends AppCompatActivity {

    private TextInputLayout tilNombre, tilFecha, tilNotas;
    private TextInputEditText etNombre, etFecha, etNotas;
    private Slider sliderIntensidad;
    private Button btnGuardar;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_regis_sintomas);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();

        // NEW: preseleccionar fecha de hoy en el campo
        Calendar hoy = Calendar.getInstance();
        etFecha.setText(df.format(hoy.getTime()));

        setupFechaPicker();
        btnGuardar.setOnClickListener(v -> guardar());
    }

    private void bindViews() {
        tilNombre = findViewById(R.id.til_nombre);
        tilFecha  = findViewById(R.id.til_fecha);
        tilNotas  = findViewById(R.id.til_notas);

        etNombre = findViewById(R.id.et_nombre);
        etFecha  = findViewById(R.id.et_fecha);
        etNotas  = findViewById(R.id.et_notas);

        sliderIntensidad = findViewById(R.id.slider_intensidad);
        btnGuardar       = findViewById(R.id.btn_guardar);
    }

    private void setupFechaPicker() {
        // DatePicker (selección única), selección inicial hoy en UTC
        final MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.sel_fecha_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // abrir al tocar el campo o el layout
        View.OnClickListener open = v -> picker.show(getSupportFragmentManager(), "fecha");
        etFecha.setOnClickListener(open);
        tilFecha.setOnClickListener(open);

        // obtener la fecha seleccionada
        picker.addOnPositiveButtonClickListener(selection -> {
            // selection llega en UTC (ms). Convertimos a fecha local y formateamos.
            Calendar calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calUtc.setTimeInMillis(selection);

            Calendar calLocal = Calendar.getInstance();
            calLocal.set(
                    calUtc.get(Calendar.YEAR),
                    calUtc.get(Calendar.MONTH),
                    calUtc.get(Calendar.DAY_OF_MONTH)
            );

            etFecha.setText(df.format(calLocal.getTime()));
            tilFecha.setError(null);
        });
    }

    private void limpiarErrores() {
        tilNombre.setError(null);
        tilFecha.setError(null);
        tilNotas.setError(null);
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }

    private void guardar() {
        limpiarErrores();

        String nombre = getText(etNombre);
        int intensidad = Math.round(sliderIntensidad.getValue());
        String fecha = getText(etFecha); // "yyyy-MM-dd" si usas df así
        String notas = getText(etNotas);

        boolean ok = true;
        if (nombre.isEmpty()) { tilNombre.setError(getString(R.string.error_campo_obligatorio)); ok = false; }
        if (fecha.isEmpty())  { tilFecha.setError(getString(R.string.error_campo_obligatorio));  ok = false; }
        if (!ok) return;

        // user_id de la sesión
        SessionManager sm = new SessionManager(this);
        if (!"user".equalsIgnoreCase(sm.role()) || sm.id() <= 0) {
            Toast.makeText(this, "Inicia sesión como usuario para registrar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Hora opcional: si no tienes, manda null
        SymptomRequest body = new SymptomRequest(
                sm.id(), nombre, intensidad, fecha, null, notas
        );

        btnGuardar.setEnabled(false);

        ApiService.api().createSymptom(body).enqueue(new Callback<ApiResponse<CreatedId>>() {
            @Override
            public void onResponse(Call<ApiResponse<CreatedId>> call, Response<ApiResponse<CreatedId>> response) {
                btnGuardar.setEnabled(true);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(Regis_sintomas.this, "Error de red", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiResponse<CreatedId> res = response.body();
                if (!res.ok) {
                    Toast.makeText(Regis_sintomas.this, res.error != null ? res.error : "No se guardó", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(Regis_sintomas.this, "Síntoma guardado (id " + res.data.id + ")", Toast.LENGTH_SHORT).show();
                // Opcional: limpiar campos
                etNombre.setText("");
                etNotas.setText("");
                sliderIntensidad.setValue(0f);
            }

            @Override
            public void onFailure(Call<ApiResponse<CreatedId>> call, Throwable t) {
                btnGuardar.setEnabled(true);
                Toast.makeText(Regis_sintomas.this, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
