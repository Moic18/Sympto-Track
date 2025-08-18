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
        // DatePicker (selección única)
        final MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(getString(R.string.sel_fecha_title))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        // abrir al tocar el campo
        View.OnClickListener open = v -> picker.show(getSupportFragmentManager(), "fecha");
        etFecha.setOnClickListener(open);
        tilFecha.setOnClickListener(open);

        // obtener la fecha seleccionada
        picker.addOnPositiveButtonClickListener(selection -> {
            // selection llega en UTC (ms). Lo pasamos a fecha local con el formato deseado.
            Calendar calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calUtc.setTimeInMillis(selection);

            Calendar calLocal = Calendar.getInstance(); // zona horaria local
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
        int intensidad = Math.round(sliderIntensidad.getValue()); // 0..10
        String fecha = getText(etFecha);
        String notas = getText(etNotas);

        boolean ok = true;
        if (nombre.isEmpty()) { tilNombre.setError(getString(R.string.error_campo_obligatorio)); ok = false; }
        if (fecha.isEmpty())  { tilFecha.setError(getString(R.string.error_campo_obligatorio));  ok = false; }

        if (!ok) return;

        // Aquí puedes enviar a tu API (Retrofit) o guardar local (Room).
        // Ejemplo rápido:
        String resumen = "Síntoma: " + nombre + " | Intensidad: " + intensidad +
                " | Fecha: " + fecha + " | Notas: " + notas;
        Toast.makeText(this, getString(R.string.msg_guardado_ok) + "\n" + resumen, Toast.LENGTH_SHORT).show();
    }

}