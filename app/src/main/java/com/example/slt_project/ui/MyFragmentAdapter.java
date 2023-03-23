package com.example.slt_project.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.slt_project.R;

import java.util.List;
import java.util.Locale;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;

    public MyFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList==null?null:fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "S2T".toLowerCase(Locale.ROOT);
            case 1:
                return "T2S".toLowerCase(Locale.ROOT);
            case 2:
                return "setting".toLowerCase(Locale.ROOT);
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return fragmentList==null?0: fragmentList.size();
    }
}
