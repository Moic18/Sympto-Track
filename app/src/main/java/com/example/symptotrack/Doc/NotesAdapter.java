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

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.VH> {

    private final List<NoteItem> data = new ArrayList<>();

    public void submit(List<NoteItem> items) {
        data.clear(); data.addAll(items); notifyDataSetChanged();
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_nota, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        NoteItem it = data.get(pos);
        h.tvFecha.setText(it.fecha);
        h.tvTexto.setText(it.texto);
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvFecha, tvTexto;
        VH(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tv_fecha_nota);
            tvTexto = itemView.findViewById(R.id.tv_texto_nota);
        }
    }
}
