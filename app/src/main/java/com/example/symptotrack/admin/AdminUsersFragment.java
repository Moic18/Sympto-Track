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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.symptotrack.R;
import com.example.symptotrack.net.ApiClient;
import com.example.symptotrack.net.ApiService;
import com.example.symptotrack.net.dto.*;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminUsersFragment extends AdminTabFragment {

    private RecyclerView rv;
    private TextView emptyView;
    private MaterialSwitch swOnlyActive;
    private EditText etSearch;

    private final List<UserData> full = new ArrayList<>();
    private final List<UserData> filtered = new ArrayList<>();
    private AdminUsersAdapter adapter;
    private ApiService api;

    public AdminUsersFragment() { /* constructor vacío */ }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        super.onViewCreated(v, s);

        rv          = v.findViewById(R.id.rv_users);
        emptyView   = v.findViewById(R.id.empty_users);
        swOnlyActive= v.findViewById(R.id.sw_only_active_users);
        TextInputLayout til = v.findViewById(R.id.til_search_users);
        etSearch     = v.findViewById(R.id.et_search_users);

        api = ApiClient.get().create(ApiService.class);

        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AdminUsersAdapter(filtered, this::toggleActive);
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
        api.adminListUsers(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS, activeParam)
                .enqueue(new Callback<ApiResponse<List<UserData>>>() {
                    @Override public void onResponse(Call<ApiResponse<List<UserData>>> call,
                                                     Response<ApiResponse<List<UserData>>> resp) {
                        if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                            Toast.makeText(getContext(), "Error listando usuarios", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        full.clear();
                        full.addAll(resp.body().data != null ? resp.body().data : new ArrayList<>());
                        applyFilter();
                    }
                    @Override public void onFailure(Call<ApiResponse<List<UserData>>> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void applyFilter() {
        String q = etSearch.getText()!=null ? etSearch.getText().toString().trim().toLowerCase() : "";
        filtered.clear();
        for (UserData u : full) {
            boolean match = q.isEmpty()
                    || (u.first_name!=null && u.first_name.toLowerCase().contains(q))
                    || (u.last_name!=null && u.last_name.toLowerCase().contains(q))
                    || (u.email!=null && u.email.toLowerCase().contains(q))
                    || (u.username!=null && u.username.toLowerCase().contains(q))
                    || (u.phone!=null && u.phone.toLowerCase().contains(q));
            if (match) filtered.add(u);
        }
        adapter.notifyDataSetChanged();
        emptyView.setVisibility(filtered.isEmpty()? View.VISIBLE : View.GONE);
    }

    private void toggleActive(UserData user, boolean active) {
        api.adminSetUserStatus(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS, user.id, new StatusRequest(active))
                .enqueue(new Callback<ApiResponse<GenericOk>>() {
                    @Override public void onResponse(Call<ApiResponse<GenericOk>> call, Response<ApiResponse<GenericOk>> resp) {
                        if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                            Toast.makeText(getContext(), "No se pudo actualizar estado", Toast.LENGTH_SHORT).show();
                            user.is_active = !active;
                            adapter.notifyDataSetChanged();
                            return;
                        }
                        user.is_active = active;
                    }
                    @Override public void onFailure(Call<ApiResponse<GenericOk>> call, Throwable t) {
                        Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        user.is_active = !active;
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onFabClick() {
        AdminDialogs.showCreateUserDialog(requireContext(), (fn, ln, phone, uc, pw) -> {
            api.adminCreateUser(AdminHeaders.ADMIN_USER, AdminHeaders.ADMIN_PASS,
                            new UserRegisterRequest(fn, ln, phone, uc, pw))
                    .enqueue(new Callback<ApiResponse<CreatedId>>() {
                        @Override public void onResponse(Call<ApiResponse<CreatedId>> call, Response<ApiResponse<CreatedId>> resp) {
                            if (!resp.isSuccessful() || resp.body()==null || !resp.body().ok) {
                                Toast.makeText(getContext(), "No se pudo crear usuario", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Toast.makeText(getContext(), "Usuario creado (id "+resp.body().data.id+")", Toast.LENGTH_SHORT).show();
                            load();
                        }
                        @Override public void onFailure(Call<ApiResponse<CreatedId>> call, Throwable t) {
                            Toast.makeText(getContext(), "Fallo: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}
