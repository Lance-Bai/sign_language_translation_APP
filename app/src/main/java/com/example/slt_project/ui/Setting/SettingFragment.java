package com.example.slt_project.ui.Setting;

import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.slt_project.R;
import com.example.slt_project.ui.base.BaseFragment;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment, CompoundButton.OnCheckedChangeListener {
    private Switch darkOrLight;
    // TODO: 2023-04-04 加一个switch， 用于选择是拍照模式或者摄像模式（默认摄像）
    private Switch photoOrVideo;
    private SettingPresenter presenter;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews() {
        presenter = new SettingPresenter(this);
        darkOrLight =find(R.id.light_mode);
        darkOrLight.setOnCheckedChangeListener(this);
        photoOrVideo = find(R.id.photo_mode);
        photoOrVideo.setOnCheckedChangeListener(this);
//        darkOrLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(b){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                   // Toast.makeText(getContext(),"Dark Mode ON",Toast.LENGTH_SHORT).show();
//                    //加了一直闪的原因
//                } else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                   // Toast.makeText(getContext(),"Dark Mode OFF",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        这块稍微调整了一下，按下面那样再加新switch可以通过添加不同的case解决，就不用每个都要单独的listener了

    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.light_mode:
                if(b){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    // Toast.makeText(getContext(),"Dark Mode ON",Toast.LENGTH_SHORT).show();
                    //加了一直闪的原因
                } else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    // Toast.makeText(getContext(),"Dark Mode OFF",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.photo_mode:
                if(b){
                    presenter.setPhotoMode();
                }else{
                    presenter.setVideoMode();
                }
        }

    }
}
