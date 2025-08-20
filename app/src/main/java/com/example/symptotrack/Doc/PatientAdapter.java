package com.example.symptotrack.Doc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.symptotrack.R;
import java.util.ArrayList;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.VH> {

    public interface OnPatientClick { void onClick(PatientItem p); }

    private final List<PatientItem> data = new ArrayList<>();
    private final OnPatientClick listener;

    public PatientAdapter(OnPatientClick listener) { this.listener = listener; }

    public void submit(List<PatientItem> items) {
        data.clear(); data.addAll(items); notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paciente, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        PatientItem it = data.get(pos);
        h.tvNombre.setText(it.nombreCompleto);
        h.tvSub.setText("Último envío: " + it.ultimoEnvio);
        h.itemView.setOnClickListener(v -> listener.onClick(it));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvSub;
        VH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_paciente);
            tvSub    = itemView.findViewById(R.id.tv_subtitulo_paciente);
        }
    }
}
