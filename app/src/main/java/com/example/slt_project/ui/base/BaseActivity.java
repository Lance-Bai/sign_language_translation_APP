package com.example.slt_project.ui.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabItem;

public abstract class BaseActivity extends AppCompatActivity {
    protected abstract int getLayoutID();

    protected abstract void initViews();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutID());
        initViews();
    }

    protected <T extends View> T find(@IdRes int id) {
        return findViewById(id);
    }


//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//public abstract boolean onNavigationItemSelected(@NonNull TabItem item);
}