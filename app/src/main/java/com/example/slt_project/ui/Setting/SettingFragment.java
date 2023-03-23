package com.example.slt_project.ui.Setting;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.slt_project.R;
import com.example.slt_project.ui.base.BaseFragment;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment {
private Switch switch1;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews() {
         switch1=find(R.id.light_mode);
switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
           // Toast.makeText(getContext(),"Dark Mode ON",Toast.LENGTH_SHORT).show();
            //加了一直闪的原因
        } else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
           // Toast.makeText(getContext(),"Dark Mode OFF",Toast.LENGTH_SHORT).show();
        }
    }
});

    }


}
