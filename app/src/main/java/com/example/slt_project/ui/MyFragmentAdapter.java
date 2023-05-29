package com.example.slt_project.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.slt_project.R;

import java.util.List;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> fragmentList;
    private final Context contextCompat;
    public MyFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> fragmentList, Context contextCompat) {
        super(fm);
        this.fragmentList = fragmentList;
        this.contextCompat=contextCompat;
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
//                return ();
                return contextCompat.getResources().getString(R.string.tab1_left);

            case 1:
                return contextCompat.getResources().getString(R.string.tab2_center);
            case 2:
                return contextCompat.getResources().getString(R.string.tab3_right);
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

