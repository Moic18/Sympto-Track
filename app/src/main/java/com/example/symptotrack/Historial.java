package com.example.symptotrack;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.historial.HistEntry;
import com.example.symptotrack.historial.HistHeader;
import com.example.symptotrack.historial.HistItem;
import com.example.symptotrack.historial.HistorialAdapter;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.ApiResponse;
import com.example.symptotrack.net.dto.SymptomEntry;
import com.example.symptotrack.session.SessionManager;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;

import androidx.core.util.Pair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Historial conectado a la API:
 * GET /users/{userId}/symptoms?from&to
 * - Carga con loader
 * - Muestra estado vacío
 * - Filtro por texto
 * - Selector de rango de fechas
 */
public class Historial extends AppCompatActivity {

    private RecyclerView rv;
    private EditText etBuscar;
    private MaterialButton btnRango, btnLimpiar;
    private TextView emptyView;
    private ProgressBar progress;
    private HistorialAdapter adapter;

    // formatos API
    private final SimpleDateFormat dfDate   = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat dfTimeIn = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private final SimpleDateFormat dfTimeOut= new SimpleDateFormat("HH:mm",    Locale.getDefault());

    // último rango aplicado (para limpiar rápidamente)
    private String lastFrom = null, lastTo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        MaterialToolbar tb = findViewById(R.id.toolbar_historial);
        setSupportActionBar(tb);

        rv        = findViewById(R.id.rv_historial);
        etBuscar  = findViewById(R.id.et_buscar);
        btnRango  = findViewById(R.id.btn_rango_fechas);
        btnLimpiar= findViewById(R.id.btn_limpiar_filtros);
        emptyView = findViewById(R.id.empty_view);
        progress  = findViewById(R.id.progress);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter();
        rv.setAdapter(adapter);

        // Cargar (sin rango)
        cargarHistorial(null, null);

        // Filtro por texto
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
                updateEmptyStateAfterFilter();
            }
        });

        // Selector de rango de fechas
        btnRango.setOnClickListener(v -> abrirRangoFechas());

        // Limpiar filtros (texto + fechas) y recargar
        btnLimpiar.setOnClickListener(v -> {
            etBuscar.setText("");
            lastFrom = null;
            lastTo = null;
            btnRango.setText(getString(R.string.historial_btn_rango_fechas));
            adapter.clearFilters();
            cargarHistorial(null, null);
        });
    }

    // ------------ UI helpers ------------
    private void setLoading(boolean show) {
        progress.setVisibility(show ? View.VISIBLE : View.GONE);
        rv.setAlpha(show ? 0.3f : 1f);
        btnRango.setEnabled(!show);
        btnLimpiar.setEnabled(!show);
        etBuscar.setEnabled(!show);
    }

    private void showEmpty(boolean show) {
        emptyView.setVisibility(show ? View.VISIBLE : View.GONE);
        rv.setVisibility(show ? View.INVISIBLE : View.VISIBLE);
    }

    private void updateEmptyStateAfterFilter() {
        // Si después de filtrar no hay ítems, muestra vacío (pero solo si ya teníamos data cargada)
        boolean isEmpty = adapter.getItemCount() == 0;
        // Cuando filtras, no debes mostrar loader, solo vacío/no vacío:
        progress.setVisibility(View.GONE);
        showEmpty(isEmpty);
    }

    // ------------ Date Range Picker ------------
    private void abrirRangoFechas() {
        // Rango por defecto: últimos 7 días hasta hoy (UTC)
        long todayUtc = MaterialDatePicker.todayInUtcMilliseconds();
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.setTimeInMillis(todayUtc);
        utc.add(Calendar.DAY_OF_MONTH, -6);
        long aWeekAgoUtc = utc.getTimeInMillis();

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText(getString(R.string.historial_btn_rango_fechas))
                .setSelection(new Pair<>(aWeekAgoUtc, todayUtc))
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            if (selection == null) return;
            Long startUtc = selection.first;
            Long endUtc   = selection.second;
            if (startUtc == null || endUtc == null) return;

            String from = utcMsToLocalDate(startUtc);
            String to   = utcMsToLocalDate(endUtc);

            lastFrom = from;
            lastTo   = to;

            btnRango.setText(from + " – " + to);
            cargarHistorial(from, to);
        });

        picker.show(getSupportFragmentManager(), "rango_fechas");
    }

    private String utcMsToLocalDate(long utcMs) {
        Calendar calUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calUtc.setTimeInMillis(utcMs);

        Calendar calLocal = Calendar.getInstance();
        calLocal.set(
                calUtc.get(Calendar.YEAR),
                calUtc.get(Calendar.MONTH),
                calUtc.get(Calendar.DAY_OF_MONTH),
                0, 0, 0
        );
        calLocal.set(Calendar.MILLISECOND, 0);
        return dfDate.format(calLocal.getTime());
    }

    // ------------ Carga de datos ------------
    private void cargarHistorial(String from, String to) {
        SessionManager sm = new SessionManager(this);
        if (!"user".equalsIgnoreCase(sm.role()) || sm.id() <= 0) {
            Toast.makeText(this, "Inicia sesión como usuario para ver historial", Toast.LENGTH_SHORT).show();
            showEmpty(true);
            return;
        }

        setLoading(true);
        showEmpty(false);

        ApiService.api().listSymptoms(sm.id(), from, to).enqueue(new retrofit2.Callback<ApiResponse<List<SymptomEntry>>>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse<List<SymptomEntry>>> call,
                                   retrofit2.Response<ApiResponse<List<SymptomEntry>>> response) {
                setLoading(false);
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(Historial.this, "Error de red", Toast.LENGTH_SHORT).show();
                    showEmpty(true);
                    return;
                }
                ApiResponse<List<SymptomEntry>> res = response.body();
                if (!res.ok) {
                    Toast.makeText(Historial.this, res.error != null ? res.error : "No se pudo cargar", Toast.LENGTH_SHORT).show();
                    showEmpty(true);
                    return;
                }

                List<SymptomEntry> data = res.data != null ? res.data : Collections.emptyList();
                List<HistItem> items = mapearAItems(data);
                adapter.submitList(items);
                showEmpty(items.isEmpty());
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse<List<SymptomEntry>>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(Historial.this, "Fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                showEmpty(true);
            }
        });
    }

    // ------------ Mapeo a items del adapter ------------
    private List<HistItem> mapearAItems(List<SymptomEntry> apiList) {
        // ordena: fecha DESC, id DESC
        List<SymptomEntry> sorted = new ArrayList<>(apiList);
        Collections.sort(sorted, new Comparator<SymptomEntry>() {
            @Override public int compare(SymptomEntry a, SymptomEntry b) {
                int cmp = safeCompare(b.entry_date, a.entry_date);
                if (cmp != 0) return cmp;
                return Integer.compare(b.id, a.id);
            }
        });

        // agrupa por fecha
        Map<String, List<SymptomEntry>> porFecha = new LinkedHashMap<>();
        for (SymptomEntry se : sorted) {
            porFecha.computeIfAbsent(se.entry_date, k -> new ArrayList<>()).add(se);
        }

        // arma lista final
        List<HistItem> items = new ArrayList<>();
        for (Map.Entry<String, List<SymptomEntry>> e : porFecha.entrySet()) {
            String fecha = e.getKey();
            items.add(new HistHeader(formatearHeader(fecha)));

            for (SymptomEntry se : e.getValue()) {
                String horaBonita = formatearHora(se.entry_time); // "HH:mm" o ""
                items.add(new HistEntry(
                        se.symptom_name,
                        se.intensity,
                        fecha,
                        horaBonita,
                        se.notes != null ? se.notes : ""
                ));
            }
        }
        return items;
    }

    private int safeCompare(String s1, String s2) {
        if (s1 == null && s2 == null) return 0;
        if (s1 == null) return -1;
        if (s2 == null) return 1;
        return s1.compareTo(s2);
    }

    private String formatearHeader(String yyyyMMdd) {
        try {
            String hoy = dfDate.format(new java.util.Date());
            if (yyyyMMdd != null && yyyyMMdd.equals(hoy)) {
                return "Hoy · " + yyyyMMdd;
            }
        } catch (Exception ignored) {}
        return yyyyMMdd != null ? yyyyMMdd : "";
    }

    private String formatearHora(String hhmmssNullable) {
        if (hhmmssNullable == null || hhmmssNullable.isEmpty()) return "";
        try {
            return dfTimeOut.format(dfTimeIn.parse(hhmmssNullable));
        } catch (ParseException e) {
            if (hhmmssNullable.length() == 5) return hhmmssNullable; // ya venía "HH:mm"
            return "";
        }
    }
}
