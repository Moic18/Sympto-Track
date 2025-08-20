package com.example.symptotrack.Doctor;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.symptotrack.R;

public class DetallePaciente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_paciente);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvNombre = findViewById(R.id.tv_nombre);
        TextView tvApellido = findViewById(R.id.tv_apellido);
        TextView tvUsuario = findViewById(R.id.tv_usuario);
        TextView tvNotas = findViewById(R.id.tv_notas);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            tvNombre.setText(extras.getString("nombre", ""));
            tvApellido.setText(extras.getString("apellido", ""));
            tvUsuario.setText(extras.getString("username", ""));
            tvNotas.setText("Notas del paciente...");
        }
    }
}
