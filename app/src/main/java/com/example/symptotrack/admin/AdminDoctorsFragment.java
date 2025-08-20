package com.example.symptotrack.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.ApiClient;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.*;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDoctorsFragment extends AdminTabFragment {

    private RecyclerView rv;
    private TextView emptyView;
    private MaterialSwitch swOnlyActive;
    private EditText etSearch;

    private final List<DoctorDto> full = new ArrayList<>();
    private final List<DoctorDto> filtered = new ArrayList<>();
    private AdminDoctorsAdapter adapter;
    private ApiService api;

    public AdminDoctorsFragment() { /* constructor vacío */ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_doctors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        rv           = v.findViewById(R.id.rv_doctors);
        emptyView    = v.findViewById(R.id.empty_doctors);
        swOnlyActive = v.findViewById(R.id.sw_only_active_doctors);
        etSearch     = v.findViewById(R.id.et_search_doctors);

        api = ApiClient.get().create(ApiService.class);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminDoctorsAdapter(filtered, this::toggleActive);
        rv.setAdapter(adapter);

        swOnlyActive.setOnCheckedChangeListener((buttonView, isChecked) -> load());
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(Editable s) { applyFilter(); }
        });

        load();
        ((FabHost) requireActivity()).setFabVisible(true);
    }

    private void load() {
        Integer activeParam = swOnlyActive.isChecked() ? 1 : null;
        api.adminListDoctors(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS, activeParam)
                .enqueue(new Callback<ApiResponse<List<DoctorDto>>>() {
                    @Override public void onResponse(Call<ApiResponse<List<DoctorDto>>> call,
                                                     Response<ApiResponse<List<DoctorDto>>> resp) {
                        if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                            Toast.makeText(getContext(), "Error listando doctores", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        full.clear();
                        full.addAll(resp.body().data != null ? resp.body().data : new ArrayList<>());
                        applyFilter();
                    }
                    @Override public void onFailure(Call<ApiResponse<List<DoctorDto>>> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void applyFilter() {
        String q = etSearch.getText()!=null ? etSearch.getText().toString().trim().toLowerCase() : "";
        filtered.clear();
        for (DoctorDto d : full) {
            boolean match = q.isEmpty()
                    || (d.first_name!=null && d.first_name.toLowerCase().contains(q))
                    || (d.last_name!=null && d.last_name.toLowerCase().contains(q))
                    || (d.email!=null && d.email.toLowerCase().contains(q))
                    || (d.username!=null && d.username.toLowerCase().contains(q));
            if (match) filtered.add(d);
        }
        adapter.notifyDataSetChanged();
        emptyView.setVisibility(filtered.isEmpty()? View.VISIBLE : View.GONE);
    }

    private void toggleActive(DoctorDto doc, boolean active) {
        api.adminSetDoctorStatus(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS, doc.doctor_id, new StatusRequest(active))
                .enqueue(new Callback<ApiResponse<GenericOk>>() {
                    @Override public void onResponse(Call<ApiResponse<GenericOk>> call, Response<ApiResponse<GenericOk>> resp) {
                        if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                            Toast.makeText(getContext(), "No se pudo actualizar estado", Toast.LENGTH_SHORT).show();
                            doc.is_active = !active;
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        doc.is_active = active;
                    }
                    @Override public void onFailure(Call<ApiResponse<GenericOk>> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        doc.is_active = !active;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onFabClick() {
        AdminDialogs.showCreateDoctorDialog(requireContext(), (fn, ln, email, username, pass) -> {
            api.adminCreateDoctor(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS,
                            new DoctorCreateRequest(fn, ln, email, username, pass))
                    .enqueue(new retrofit2.Callback<ApiResponse<CreatedId>>() {
                        @Override public void onResponse(Call<ApiResponse<CreatedId>> call, Response<ApiResponse<CreatedId>> resp) {
                            if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                                Toast.makeText(getContext(), "No se pudo crear doctor", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getContext(), "Doctor creado (id "+resp.body().data.id+")", Toast.LENGTH_SHORT).show();
                            load();
                        }
                        @Override public void onFailure(Call<ApiResponse<CreatedId>> call, Throwable t) {
                            Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
