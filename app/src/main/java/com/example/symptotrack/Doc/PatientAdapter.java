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

    public interface OnClick {
        void onClick(PatientItem item);
    }

    private final List<PatientItem> data = new ArrayList<>();
    private final OnClick listener;

    public PatientAdapter(OnClick l) { this.listener = l; }

    public void submit(List<PatientItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        PatientItem it = data.get(pos);
        h.tvNombre.setText(it.nombreCompleto != null ? it.nombreCompleto : "-");
        h.tvUltimaFecha.setText(it.ultimaFecha != null ? it.ultimaFecha : "-");
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(it);
        });
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvUltimaFecha;
        VH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_paciente);
            tvUltimaFecha = itemView.findViewById(R.id.tv_ultima_fecha);
        }
    }
}
