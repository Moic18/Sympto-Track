package com.example.symptotrack;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
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

public class Registrar extends AppCompatActivity {

    private TextInputLayout tilNombre, tilApellido, tilCelular, tilUsuarioCorreo, tilContrasena, tilConfirmarContrasena;
    private TextInputEditText etNombre, etApellido, etCelular, etUsuarioCorreo, etContrasena, etConfirmarContrasena;
    private Button btnRegistrarCuenta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registrar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bindViews();

        btnRegistrarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (validarFormulario()) {
                    Toast.makeText(Registrar.this, getString(R.string.msg_registro_valido), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindViews() {
        tilNombre = findViewById(R.id.til_nombre);
        tilApellido = findViewById(R.id.til_apellido);
        tilCelular = findViewById(R.id.til_celular);
        tilUsuarioCorreo = findViewById(R.id.til_usuario_correo);
        tilContrasena = findViewById(R.id.til_contrasena);
        tilConfirmarContrasena = findViewById(R.id.til_confirmar_contrasena);

        etNombre = findViewById(R.id.et_nombre);
        etApellido = findViewById(R.id.et_apellido);
        etCelular = findViewById(R.id.et_celular);
        etUsuarioCorreo = findViewById(R.id.et_usuario_correo);
        etContrasena = findViewById(R.id.et_contrasena);
        etConfirmarContrasena = findViewById(R.id.et_confirmar_contrasena);

        btnRegistrarCuenta = findViewById(R.id.btn_registrar_cuenta);
    }

    private boolean validarFormulario() {
        limpiarErrores();

        String nombre = getText(etNombre);
        String apellido = getText(etApellido);
        String celular = getText(etCelular);
        String usuarioCorreo = getText(etUsuarioCorreo);
        String contrasena = getText(etContrasena);
        String confirmarContrasena = getText(etConfirmarContrasena);

        boolean ok = true;

        if (nombre.isEmpty()) {
            tilNombre.setError(getString(R.string.error_nombre));
            ok = false;
        }
        if (apellido.isEmpty()) {
            tilApellido.setError(getString(R.string.error_apellido));
            ok = false;
        }

        if (celular.isEmpty()) {
            tilCelular.setError(getString(R.string.error_celular_obligatorio));
            ok = false;
        } else if (!celular.matches("^\\d{10}$")) {
            tilCelular.setError(getString(R.string.error_celular_invalido));
            ok = false;
        }

        if (usuarioCorreo.isEmpty()) {
            tilUsuarioCorreo.setError(getString(R.string.error_usuario_correo_obligatorio));
            ok = false;
        } else {
            boolean esEmail = Patterns.EMAIL_ADDRESS.matcher(usuarioCorreo).matches();
            boolean esUsuario = usuarioCorreo.matches("^[A-Za-z0-9._-]{4,}$"); // min 4
            if (!esEmail && !esUsuario) {
                tilUsuarioCorreo.setError(getString(R.string.error_usuario_correo_invalido));
                ok = false;
            }
        }

        if (contrasena.isEmpty()) {
            tilContrasena.setError(getString(R.string.error_contrasena_obligatoria));
            ok = false;
        } else if (contrasena.length() < 6) {
            tilContrasena.setError(getString(R.string.error_contrasena_corta));
            ok = false;
        }

        if (confirmarContrasena.isEmpty()) {
            tilConfirmarContrasena.setError(getString(R.string.error_confirmar_contrasena));
            ok = false;
        } else if (!TextUtils.equals(contrasena, confirmarContrasena)) {
            tilConfirmarContrasena.setError(getString(R.string.error_contrasena_no_coincide));
            ok = false;
        }

        return ok;
    }

    private void limpiarErrores() {
        tilNombre.setError(null);
        tilApellido.setError(null);
        tilCelular.setError(null);
        tilUsuarioCorreo.setError(null);
        tilContrasena.setError(null);
        tilConfirmarContrasena.setError(null);
    }

    private String getText(TextInputEditText et) {
        return et.getText() != null ? et.getText().toString().trim() : "";
    }


}