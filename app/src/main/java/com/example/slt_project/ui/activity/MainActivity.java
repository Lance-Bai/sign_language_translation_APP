package com.example.slt_project.ui.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

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
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends BaseActivity {

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

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        requestPermissions(REQUIRED_PERMISSIONS, 2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            TabLayout tabLayout = findViewById(R.id.tab_layout);
            ViewPager viewPage = find(R.id.viewPage);

            tabLayout.setupWithViewPager(viewPage);
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            mode = new Mode(this);
            if (mode.whatlan() != null) {
                if (mode.whatlan().equals("english")) {
                    changeLanguage("english");
                } else if (mode.whatlan().equals("chinese")) {
                    changeLanguage("chinese");
                }
            }

            List<Fragment> fragments = new ArrayList<>();
            fragments.add(new S2TFragment());
            fragments.add(new T2SFragment());
            fragments.add(new SettingFragment());

            MyFragmentAdapter fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments, this);
            viewPage.setAdapter(fragmentAdapter);

            Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.s2t_tab_icon_warm);
            Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.t2s_tab_icon_warm);
            Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.setting_cool);


            if (mode.isModeon()) {
                tabLayout.setBackgroundResource(R.color.grey_DarkGrey);
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
                            tab.setIcon(R.drawable.t2s_tab_icon_warm);
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

    }

    private void changeLanguage(String languageCode) {
        Locale locale;
        if (languageCode.equals("chinese")) {
            locale = new Locale("zh");
        } else if (languageCode.equals("english")) {
            locale = new Locale("en");
        } else {
            locale = Locale.getDefault();
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}