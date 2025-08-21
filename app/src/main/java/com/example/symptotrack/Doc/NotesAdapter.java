package com.example.symptotrack.Doc;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.dto.PatientDetailDto;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.VH> {

    private final List<PatientDetailDto.Note> data = new ArrayList<>();

    public void submit(@Nullable List<PatientDetailDto.Note> notes) {
        data.clear();
        if (notes != null) data.addAll(notes);
        notifyDataSetChanged();
    }

    @Override public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_note, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(VH h, int pos) {
        PatientDetailDto.Note n = data.get(pos);
        h.tvFecha.setText(n.fecha != null ? n.fecha : "-");
        h.tvNote.setText(n.note != null ? n.note : "-");
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvFecha, tvNote;
        VH(View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvNote  = itemView.findViewById(R.id.tv_note);
        }
    }
}
