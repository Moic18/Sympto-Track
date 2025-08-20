package com.example.symptotrack.admin;

import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminDialogs {

    // ----- Crear Usuario -----
    public interface OnCreateUser {
        void onCreate(String firstName, String lastName, String phone, String usuarioCorreo, String password);
    }

    public static void showCreateUserDialog(Context ctx, OnCreateUser cb) {
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * ctx.getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);

        TextInputLayout tilFn = til(ctx, "Nombre");         TextInputEditText etFn = et(tilFn, InputType.TYPE_CLASS_TEXT);
        TextInputLayout tilLn = til(ctx, "Apellido");       TextInputEditText etLn = et(tilLn, InputType.TYPE_CLASS_TEXT);
        TextInputLayout tilPh = til(ctx, "Celular");        TextInputEditText etPh = et(tilPh, InputType.TYPE_CLASS_PHONE);
        TextInputLayout tilUc = til(ctx, "Usuario o correo"); TextInputEditText etUc = et(tilUc, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        TextInputLayout tilPw = til(ctx, "Contraseña");     TextInputEditText etPw = et(tilPw, InputType.TYPE_TEXT_VARIATION_PASSWORD);

        root.addView(tilFn); root.addView(tilLn); root.addView(tilPh); root.addView(tilUc); root.addView(tilPw);
        for (int i=0;i<root.getChildCount();i++){
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) root.getChildAt(i).getLayoutParams();
            lp.topMargin = (i==0?0:pad/2);
            root.getChildAt(i).setLayoutParams(lp);
        }

        new MaterialAlertDialogBuilder(ctx)
                .setTitle("Nuevo usuario")
                .setView(root)
                .setPositiveButton("Guardar", (d, w) -> {
                    cb.onCreate(
                            safe(etFn), safe(etLn), safe(etPh), safe(etUc), safe(etPw)
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ----- Crear Doctor -----
    public interface OnCreateDoctor {
        void onCreate(String firstName, String lastName, String email, String username, String password);
    }

    public static void showCreateDoctorDialog(Context ctx, OnCreateDoctor cb) {
        LinearLayout root = new LinearLayout(ctx);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (16 * ctx.getResources().getDisplayMetrics().density);
        root.setPadding(pad, pad, pad, pad);

        TextInputLayout tilFn = til(ctx, "Nombre");         TextInputEditText etFn = et(tilFn, InputType.TYPE_CLASS_TEXT);
        TextInputLayout tilLn = til(ctx, "Apellido");       TextInputEditText etLn = et(tilLn, InputType.TYPE_CLASS_TEXT);
        TextInputLayout tilEm = til(ctx, "Correo");         TextInputEditText etEm = et(tilEm, InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        TextInputLayout tilUn = til(ctx, "Usuario");        TextInputEditText etUn = et(tilUn, InputType.TYPE_CLASS_TEXT);
        TextInputLayout tilPw = til(ctx, "Contraseña");     TextInputEditText etPw = et(tilPw, InputType.TYPE_TEXT_VARIATION_PASSWORD);

        root.addView(tilFn); root.addView(tilLn); root.addView(tilEm); root.addView(tilUn); root.addView(tilPw);
        for (int i=0;i<root.getChildCount();i++){
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) root.getChildAt(i).getLayoutParams();
            lp.topMargin = (i==0?0:pad/2);
            root.getChildAt(i).setLayoutParams(lp);
        }

        new MaterialAlertDialogBuilder(ctx)
                .setTitle("Nuevo doctor")
                .setView(root)
                .setPositiveButton("Guardar", (d, w) -> {
                    cb.onCreate(
                            safe(etFn), safe(etLn), safe(etEm), safe(etUn), safe(etPw)
                    );
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    // ---- helpers ----
    private static TextInputLayout til(Context ctx, String hint){
        TextInputLayout t = new TextInputLayout(ctx);
        t.setHint(hint);
        t.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return t;
    }

    private static TextInputEditText et(TextInputLayout til, int inputType){
        TextInputEditText e = new TextInputEditText(til.getContext());
        e.setInputType(inputType);
        til.addView(e, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return e;
    }

    private static String safe(TextInputEditText e){
        return e.getText()!=null ? e.getText().toString().trim() : "";
    }
}
