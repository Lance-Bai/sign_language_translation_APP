package com.example.slt_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.S2TFragment;
import com.example.slt_project.ui.Setting.SetingFragment;
import com.example.slt_project.ui.T2S.T2SFragment;
import com.example.slt_project.ui.base.BaseActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener {
    private Fragment[] fragments;
    private int lastFragmentIndex =0 ;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initViews() {
        BottomNavigationView bottomNavigationView = find(R.id.navigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        fragments = new Fragment[]{
                new S2TFragment(),
                new T2SFragment(),
                new SetingFragment()
        };
        getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragments[0]).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.Theme_SLT_Project );
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        item.setChecked(true);
        switch (item.getItemId()){
            case R.id.top_S2T:
                switchFragment(0);
                break;
            case R.id.top_T2S:
                switchFragment(1);
                break;
            case R.id.top_setting:
                switchFragment(2);
                break;
        }
        return false;
    }

    private void switchFragment(int nextIndex){
        if(nextIndex == lastFragmentIndex)return;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if(!fragments[nextIndex].isAdded()){
            fragmentTransaction.add(R.id.frameLayout, fragments[nextIndex]);
        }else{
            fragmentTransaction.show(fragments[nextIndex]);
        }
        fragmentTransaction.hide(fragments[lastFragmentIndex]).commitAllowingStateLoss();

        lastFragmentIndex = nextIndex;
    }
}