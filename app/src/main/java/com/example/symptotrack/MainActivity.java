package com.example.symptotrack;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.example.symptotrack.Doctor.Vista_doctor;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText txt_usuario, txt_contrasena;
    private TextInputLayout ly_usuario, ly_contrasena;
    private Button btn_ingresar, btn_registrarse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txt_usuario     = findViewById(R.id.txt_usuario);
        txt_contrasena  = findViewById(R.id.txt_contrasena);
        ly_usuario      = findViewById(R.id.ly_usuario);
        ly_contrasena   = findViewById(R.id.ly_contrasena);
        btn_ingresar    = findViewById(R.id.btn_ingresar);
        btn_registrarse = findViewById(R.id.btn_registrarse);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validarUsuario();
            }
        });

        btn_registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registrar.class);
                startActivity(intent);
            }
        });

    }

    private void validarUsuario() {
        String usuario = txt_usuario.getText() != null
                ? txt_usuario.getText().toString().trim()
                : "";

        if (usuario.isEmpty()) {
            ly_usuario.setError("Por favor ingrese un usuario");
        } else {
            ly_usuario.setError(null);
        }

        String contrasena = txt_contrasena.getText() != null
                ? txt_contrasena.getText().toString().trim()
                : "";
        if (contrasena.isEmpty()) {
            ly_contrasena.setError("La contraseña es obligatoria");
        } else {
            ly_contrasena.setError(null);
        }

        if(!usuario.isEmpty() && !contrasena.isEmpty() ){
            iniciarSesion();
        }

    }

    private void iniciarSesion() {
        String usuario = txt_usuario.getText() != null ? txt_usuario.getText().toString().trim() : "";
        String contrasena = txt_contrasena.getText() != null ? txt_contrasena.getText().toString().trim() : "";

        if ("prueba".equals(usuario) && "0000".equals(contrasena)) {
            Intent intent = new Intent(MainActivity.this, Inicio.class);
            startActivity(intent);
        } else if ("doctor".equals(usuario) && "0000".equals(contrasena)) {
            Intent intent = new Intent(MainActivity.this, Vista_doctor.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }


}