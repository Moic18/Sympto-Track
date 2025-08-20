package com.example.symptotrack.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminPagerAdapter extends FragmentStateAdapter {

    private final AdminTabFragment[] fragments = new AdminTabFragment[]{
            new AdminUsersFragment(),
            new AdminDoctorsFragment()
    };

    public AdminPagerAdapter(@NonNull FragmentActivity fa) {
        super(fa);
    }

    @NonNull @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() { return fragments.length; }

    public AdminTabFragment getCurrentFragment(int position) {
        return fragments[position];
    }
}
