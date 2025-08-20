package com.example.symptotrack.historial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<HistItem> fullList = new ArrayList<>();
    private final List<HistItem> viewList = new ArrayList<>();

    public void submitList(List<HistItem> data) {
        fullList.clear();
        viewList.clear();
        if (data != null) {
            fullList.addAll(data);
            viewList.addAll(data);
        }
        notifyDataSetChanged();
    }

    public void filter(String query) {
        String q = query == null ? "" : query.trim().toLowerCase(Locale.getDefault());
        viewList.clear();
        if (q.isEmpty()) {
            viewList.addAll(fullList);
        } else {
            // Filtra solo los entries por nombre/nota; preserva headers necesarios
            String currentHeader = null;
            boolean headerAdded = false;
            for (int i = 0; i < fullList.size(); i++) {
                HistItem it = fullList.get(i);
                if (it.getType() == HistItem.TYPE_HEADER) {
                    currentHeader = ((HistHeader) it).label;
                    headerAdded = false;
                } else {
                    HistEntry e = (HistEntry) it;
                    if (e.nombre.toLowerCase().contains(q) ||
                            e.nota.toLowerCase().contains(q)) {
                        if (!headerAdded && currentHeader != null) {
                            viewList.add(new HistHeader(currentHeader));
                            headerAdded = true;
                        }
                        viewList.add(e);
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public void clearFilters() {
        viewList.clear();
        viewList.addAll(fullList);
        notifyDataSetChanged();
    }

    @Override public int getItemViewType(int position) {
        return viewList.get(position).getType();
    }

    @NonNull
    @Override public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == HistItem.TYPE_HEADER) {
            View v = inf.inflate(R.layout.item_historial_header_date, parent, false);
            return new HeaderVH(v);
        } else {
            View v = inf.inflate(R.layout.item_historial_entry, parent, false);
            return new EntryVH(v);
        }
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HistItem item = viewList.get(position);
        if (holder instanceof HeaderVH) {
            ((HeaderVH) holder).bind((HistHeader) item);
        } else {
            ((EntryVH) holder).bind((HistEntry) item);
        }
    }

    @Override public int getItemCount() {
        return viewList.size();
    }

    static class HeaderVH extends RecyclerView.ViewHolder {
        TextView tv;
        HeaderVH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvHeaderDate);
        }
        void bind(HistHeader h) {
            tv.setText(h.label);
        }
    }

    static class EntryVH extends RecyclerView.ViewHolder {
        MaterialCardView card;
        TextView tvNombre, tvNota, tvHora;
        Chip chipIntensidad;

        EntryVH(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card_historial);
            tvNombre = itemView.findViewById(R.id.tv_nombre_sintoma);
            tvNota = itemView.findViewById(R.id.tv_nota);
            tvHora = itemView.findViewById(R.id.tv_hora);
            chipIntensidad = itemView.findViewById(R.id.chip_intensidad);
        }

        void bind(HistEntry e) {
            tvNombre.setText(e.nombre);
            tvNota.setText(e.nota);
            tvHora.setText(e.hora);
            chipIntensidad.setText("Intensidad " + e.intensidad);

            // Opcional: color de borde por intensidad (solo UI)
            // Puedes mapear colores reales en resources si quieres.
            // Ejemplo simple:
            int stroke = 0x00000000;
            if (e.intensidad >= 7) stroke = 0x55FF0000;       // rojo suave
            else if (e.intensidad >= 4) stroke = 0x55FFA500;  // naranja suave
            else stroke = 0x5532CD32;                         // verde suave
            card.setStrokeColor(stroke);
            card.setStrokeWidth(2);
        }
    }
}
