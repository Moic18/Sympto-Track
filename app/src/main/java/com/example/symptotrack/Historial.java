package com.example.symptotrack;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.historial.HistItem;
import com.example.symptotrack.historial.HistHeader;
import com.example.symptotrack.historial.HistEntry;
import com.example.symptotrack.historial.HistorialAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class Historial extends AppCompatActivity {

    private RecyclerView rv;
    private EditText etBuscar;
    private MaterialButton btnRango, btnLimpiar;
    private HistorialAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        MaterialToolbar tb = findViewById(R.id.toolbar_historial);
        setSupportActionBar(tb);

        rv = findViewById(R.id.rv_historial);
        etBuscar = findViewById(R.id.et_buscar);
        btnRango = findViewById(R.id.btn_rango_fechas);
        btnLimpiar = findViewById(R.id.btn_limpiar_filtros);

        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistorialAdapter();
        rv.setAdapter(adapter);

        // Cargar datos de ejemplo (solo UI)
        adapter.submitList(mockData());

        // Filtro simple por texto (nombre/nota)
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) {
                adapter.filter(s.toString());
            }
        });

        // Botones (solo maquetación)
        btnRango.setOnClickListener(v -> {
            // TODO: abrir selector de rango de fechas (MaterialDateRangePicker) y filtrar
        });

        btnLimpiar.setOnClickListener(v -> {
            etBuscar.setText("");
            adapter.clearFilters();
        });
    }

    // Datos de ejemplo agrupados
    private List<HistItem> mockData() {
        List<HistItem> items = new ArrayList<>();
        // 2025-08-19
        items.add(new HistHeader("Hoy · 2025-08-19"));
        items.add(new HistEntry("Migraña", 7, "2025-08-19", "09:35",
                "Dolor en sien derecha, mejoró con descanso."));
        items.add(new HistEntry("Náusea", 4, "2025-08-19", "13:10",
                "Ligera, sin vómito. Hidratación ayudó."));

        // 2025-08-18
        items.add(new HistHeader("2025-08-18"));
        items.add(new HistEntry("Dolor abdominal", 6, "2025-08-18", "08:15",
                "Molestia al levantarse, disminuyó al comer."));
        items.add(new HistEntry("Mareos", 3, "2025-08-18", "18:40",
                "Breves al pararse rápido."));

        // 2025-08-16
        items.add(new HistHeader("2025-08-16"));
        items.add(new HistEntry("Cansancio", 5, "2025-08-16", "20:05",
                "Día largo, mejora tras dormir."));
        return items;
    }
}
