package com.example.symptotrack.doctor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.symptotrack.R;
import java.util.ArrayList;
import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.VH> {

    public interface OnDoctorClick {
        void onClick(DoctorItem doc);
    }
    private final List<DoctorItem> data = new ArrayList<>();
    private final OnDoctorClick listener;

    public DoctorAdapter(OnDoctorClick listener) { this.listener = listener; }

    public void submit(List<DoctorItem> items) {
        data.clear(); data.addAll(items); notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_doctor, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        DoctorItem it = data.get(pos);
        h.tvNombre.setText(it.nombreCompleto);
        h.tvCorreo.setText(it.email);
        h.itemView.setOnClickListener(v -> listener.onClick(it));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCorreo;
        VH(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_doctor);
            tvCorreo = itemView.findViewById(R.id.tv_correo_doctor);
        }
    }
}
