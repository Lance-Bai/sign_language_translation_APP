package com.example.slt_project.ui.activity;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.slt_project.R;
import com.example.slt_project.ui.Mode;
import com.example.slt_project.ui.MyFragmentAdapter;
import com.example.slt_project.ui.S2T.S2TFragment;
import com.example.slt_project.ui.Setting.SettingFragment;
import com.example.slt_project.ui.T2S.T2SFragment;
import com.example.slt_project.ui.base.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {

    private List<Fragment> fragments;
    private ViewPager viewPage;
    private TabLayout tabLayout;
    Mode mode;
    private final String[] REQUIRED_PERMISSIONS = new String[]{
            "android.permission.CAMERA",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET",
            "android.permission.FOREGROUND_SERVICE",
            "android.permission.ACCESS_NETWORK_STATE",
            "android.permission.ACCESS_WIFI_STATE"

    };
    //    private TabItem tabItem1,tabItem2,tabItem3;
//    private BottomNavigationView bottomNavigationView;
    private MyFragmentAdapter fragmentAdapter;
    private int lastFragmentIndex = 0;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        requestPermissions(REQUIRED_PERMISSIONS, 0);
//        bottomNavigationView = find(R.id.navigationView);
//        bottomNavigationView.setOnItemSelectedListener(this);
        tabLayout = findViewById(R.id.tab_layout);
        viewPage = find(R.id.viewPage);

        tabLayout.setupWithViewPager(viewPage);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);


        fragments = new ArrayList<>();
        fragments.add(new S2TFragment());
        fragments.add(new T2SFragment());
        fragments.add(new SettingFragment());
        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPage.setAdapter(fragmentAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.s2t_tab_icon_warm);
        tabLayout.getTabAt(1).setIcon(R.drawable.t2s_tab_icon1_cool);
        tabLayout.getTabAt(2).setIcon(R.drawable.setting_cool);

        mode = new Mode(this);
        if (mode.isModeon()) {
            tabLayout.setBackgroundResource(R.color.grey_DarkGrey);
//            DrawableCompat.setTint(drawable, Color.WHITE);
        } else {
            tabLayout.setBackgroundResource(R.color.grey_300);
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 修改选中的 Tab 的图标
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.s2t_tab_icon_warm);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.t2s_tab_icon_warm);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.setting_warm);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 恢复未选中的 Tab 的图标
                switch (tab.getPosition()) {
                    case 0:
                        tab.setIcon(R.drawable.s2t_tab_icon_warm);
                        break;
                    case 1:
                        tab.setIcon(R.drawable.t2s_tab_icon1_cool);
                        break;
                    case 2:
                        tab.setIcon(R.drawable.setting_cool);
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        getSupportFragmentManager().beginTransaction().show(fragmentAdapter.getItem(0)).commit();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}