package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingPresenter implements SettingContract.ISettingPresenter {
    SettingFragment fragment;
    private SharedPreferences sharedPreferences;

    SettingPresenter(@NonNull SettingFragment fragment){
        this.fragment = fragment;
        sharedPreferences = fragment.getContext().getSharedPreferences("my_prefs", MODE_PRIVATE);
    }

    public void setNightMode(Boolean b){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("night_mode", b);
        editor.commit();
    }

    public void setPhotoMode(Boolean b){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("photo_mode", b);
        editor.commit();
    }


    public boolean isNightModeOn(){
        return sharedPreferences.getBoolean("night_mode", false);
    }

    public boolean isPhotoModeOn(){
        return sharedPreferences.getBoolean("photo_mode",false);
    }
}
