package com.example.symptotrack.admin;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.dto.DoctorDto;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

public class AdminDoctorsAdapter extends RecyclerView.Adapter<AdminDoctorsAdapter.VH> {

    public interface OnToggleListener { void onToggle(DoctorDto user, boolean active); }

    private final List<DoctorDto> data;
    private final OnToggleListener listener;

    public AdminDoctorsAdapter(List<DoctorDto> data, OnToggleListener l) {
        this.data = data; this.listener = l;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_doctor, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        DoctorDto d = data.get(pos);
        String name = ((d.first_name!=null?d.first_name:"") + " " + (d.last_name!=null?d.last_name:"")).trim();
        h.tvName.setText(name.isEmpty() ? (d.username!=null?d.username:"(sin nombre)") : name);
        String sub = (d.username!=null?d.username:"") + (d.email!=null?(" · "+d.email):"");
        h.tvSub.setText(sub);

        h.sw.setOnCheckedChangeListener(null);
        h.sw.setChecked(d.is_active);
        h.sw.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onToggle(d, isChecked));
    }

    @Override public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView tvName, tvSub;
        MaterialSwitch sw;
        VH(@NonNull View v){
            super(v);
            avatar = v.findViewById(R.id.img_avatar);
            tvName = v.findViewById(R.id.tv_name);
            tvSub  = v.findViewById(R.id.tv_subtitle);
            sw     = v.findViewById(R.id.sw_active);
        }
    }
}
