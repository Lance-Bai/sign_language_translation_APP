package com.example.slt_project.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.slt_project.R;
import com.example.slt_project.ui.MyFragmentAdapter;
import com.example.slt_project.ui.S2T.S2TFragment;
import com.example.slt_project.ui.Setting.SetingFragment;
import com.example.slt_project.ui.T2S.T2SFragment;
import com.example.slt_project.ui.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener, ViewPager.OnPageChangeListener {
    private List<Fragment> fragments;
    private ViewPager viewPage;
    private BottomNavigationView bottomNavigationView;
    private MyFragmentAdapter fragmentAdapter;
    private int lastFragmentIndex =0 ;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        bottomNavigationView = find(R.id.navigationView);
        bottomNavigationView.setOnItemSelectedListener(this);

        viewPage = find(R.id.viewPage);
        viewPage.addOnPageChangeListener(this);


        fragments = new ArrayList<>();
        fragments.add(new S2TFragment());
        fragments.add(new T2SFragment());
        fragments.add(new SetingFragment());

        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPage.setAdapter(fragmentAdapter);

        getSupportFragmentManager().beginTransaction().show(fragmentAdapter.getItem(0)).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SLT_Project );
//        Button button_1 = (Button) findViewById(R.id.button_1);
//        button_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.top_S2T:
                viewPage.setCurrentItem(0);
                break;
            case R.id.top_T2S:
                viewPage.setCurrentItem(1);
                break;
            case R.id.top_setting:
                viewPage.setCurrentItem(2);
                break;
        }
        return false;
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                bottomNavigationView.setSelectedItemId(R.id.top_S2T);
                break;
            case 1:
                bottomNavigationView.setSelectedItemId(R.id.top_T2S);
                break;
            case 2:
                bottomNavigationView.setSelectedItemId(R.id.top_setting);
                break;
            default:
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}