package com.example.slt_project.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.slt_project.R;
import com.example.slt_project.ui.activity.MainActivity;

import java.util.List;
import java.util.Locale;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList;
    private List<String>titleList;
private Context contextCompat;
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
                return "Sign to Text, take a photo please";
            case 1:
                return "T2S";
            case 2:
                return "settings".toLowerCase();
            //the toLowerCase is not work!!!!
            default:
                return null;
        }
    }


    @Override
    public int getCount() {
        return fragmentList==null?0: fragmentList.size();
    }
}

