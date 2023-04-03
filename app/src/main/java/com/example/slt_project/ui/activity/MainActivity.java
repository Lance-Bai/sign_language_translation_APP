package com.example.slt_project.ui.activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.slt_project.R;
import com.example.slt_project.ui.MyFragmentAdapter;
import com.example.slt_project.ui.S2T.S2TFragment;
import com.example.slt_project.ui.Setting.SettingFragment;
import com.example.slt_project.ui.T2S.T2SFragment;
import com.example.slt_project.ui.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends BaseActivity   {

private List<Fragment> fragments;
    private ViewPager viewPage;
    private TabLayout tabLayout;
//    private TabItem tabItem1,tabItem2,tabItem3;
//    private BottomNavigationView bottomNavigationView;
    private MyFragmentAdapter fragmentAdapter;
    private int lastFragmentIndex =0 ;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
//        bottomNavigationView = find(R.id.navigationView);
//        bottomNavigationView.setOnItemSelectedListener(this);
        tabLayout=findViewById(R.id.tab_layout);
//        tabItem1=findViewById(R.id.tab_item_1);
//        tabItem2=findViewById(R.id.tab_item_2);
//        tabItem3=findViewById(R.id.tab_item_3);

        viewPage = find(R.id.viewPage);
       // viewPage.addOnPageChangeListener(this);
//        tabLayout.addTab(tabItem1);
        tabLayout.setupWithViewPager(viewPage);
       // tabLayout=findViewById(R.id.tab_layout);
        //   tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPage));
//        tabLayout.getTabAt(0).setText("tab 1");
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
//        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

        fragments = new ArrayList<>();
        fragments.add(new S2TFragment());
        fragments.add(new T2SFragment());
        fragments.add(new SettingFragment());

        fragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPage.setAdapter(fragmentAdapter);

        getSupportFragmentManager().beginTransaction().show(fragmentAdapter.getItem(0)).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_SLT_Project );
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
////这个是click的
//        item.setChecked(true);
//        switch (item.getItemId()){
//                case R.id.top_S2T:
//                viewPage.setCurrentItem(0);
//                break;
//            case R.id.top_T2S:
//                    viewPage.setCurrentItem(1);
//                break;
//            case R.id.top_setting:
//                    viewPage.setCurrentItem(2);
//                break;
//        }
//        return false;
//    }


//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//    }

//    @Override
//    public void onPageSelected(int position) {
//        switch (position){
//            case 0:
//                bottomNavigationView.setSelectedItemId(R.id.top_S2T);
//                break;
//            case 1:
//                bottomNavigationView.setSelectedItemId(R.id.top_T2S);
//                break;
//            case 2:
//                bottomNavigationView.setSelectedItemId(R.id.top_setting);
//                break;
//            default:
//        }
//
//    }

//    @Override
//    public void onPageScrollStateChanged(int state) {
//
//    }


}