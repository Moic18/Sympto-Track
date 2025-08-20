package com.example.symptotrack.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.symptotrack.Doc.ListaPacientes;
import com.example.symptotrack.R;

public class Vista_doctor extends AppCompatActivity {

    private Button btnVerPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vista_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnVerPacientes = findViewById(R.id.btn_ver_pacientes);
        btnVerPacientes.setOnClickListener(v -> {
            startActivity(new Intent(Vista_doctor.this, ListaPacientes.class));
        });
    }
}