package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingPresenter implements SettingContract.ISettingPresenter {
    SettingFragment fragment;
    SettingPresenter(@NonNull SettingFragment fragment){
        this.fragment = fragment;

    }

    public void setVideoMode(){
        // TODO: 2023-04-04
    }

    public void setPhotoMode(){
        // TODO: 2023-04-04
    }
}
