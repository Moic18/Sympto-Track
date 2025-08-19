package com.example.symptotrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Inicio extends AppCompatActivity {

    private Button btn_regis_sintomas, btn_historial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inicio);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn_regis_sintomas = findViewById(R.id.btn_regis_sintomas);
        btn_historial = findViewById(R.id.btn_historial);

        btn_regis_sintomas.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Regis_sintomas.class);
            startActivity(intent);
        });

        btn_historial.setOnClickListener(v -> {
            Intent intent = new Intent(Inicio.this, Historial.class);
            startActivity(intent);
        });

    }
}