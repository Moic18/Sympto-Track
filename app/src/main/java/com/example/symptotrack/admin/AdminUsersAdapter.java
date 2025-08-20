package com.example.symptotrack.admin;

import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.dto.UserData;
import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.List;

public class AdminUsersAdapter extends RecyclerView.Adapter<AdminUsersAdapter.VH> {

    public interface OnToggleListener { void onToggle(UserData user, boolean active); }

    private final List<UserData> data;
    private final OnToggleListener listener;

    public AdminUsersAdapter(List<UserData> data, OnToggleListener l) {
        this.data = data; this.listener = l;
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_admin_user, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        UserData u = data.get(pos);
        String name = (u.first_name!=null?u.first_name:"") + (u.last_name!=null?(" "+u.last_name):"");
        h.tvName.setText(name.trim().isEmpty() ? (u.username!=null?u.username:"(sin nombre)") : name.trim());
        String sub = (u.email!=null?u.email:"") + (u.phone!=null?(" · "+u.phone):"");
        h.tvSub.setText(sub.trim());
        h.sw.setOnCheckedChangeListener(null);
        h.sw.setChecked(u.is_active);
        h.sw.setOnCheckedChangeListener((buttonView, isChecked) -> listener.onToggle(u, isChecked));
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
