package com.example.symptotrack.admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.symptotrack.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActivity extends AppCompatActivity implements FabHost {

    private ViewPager2 viewPager;
    private FloatingActionButton fab;
    private AdminPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        MaterialToolbar tb = findViewById(R.id.toolbar_admin);
        setSupportActionBar(tb);
        tb.setNavigationOnClickListener(v -> finish());

        viewPager = findViewById(R.id.view_pager);
        TabLayout tabs = findViewById(R.id.tab_layout);
        fab = findViewById(R.id.fab_add);

        adapter = new AdminPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabs, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? R.string.usuario : R.string.doctor);
        }).attach();

        fab.setOnClickListener(v -> {
            AdminTabFragment current = adapter.getCurrentFragment(viewPager.getCurrentItem());
            if (current != null) current.onFabClick();
        });
    }

    @Override
    public void setFabVisible(boolean visible) {
        fab.setVisibility(visible ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}
