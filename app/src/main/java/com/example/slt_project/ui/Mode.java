package com.example.slt_project.ui;

import android.content.Context;
import android.content.SharedPreferences;

public class Mode {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    private static final String PREF_NAME = "user_pref";
    private static final String KEY_IS_MODE = "Mode";

    public Mode(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setModeon(boolean isModeon) {
        editor.putBoolean(KEY_IS_MODE, isModeon);
        editor.apply();
    }

    public boolean isModeon() {
        return pref.getBoolean(KEY_IS_MODE, false);
    }
}
