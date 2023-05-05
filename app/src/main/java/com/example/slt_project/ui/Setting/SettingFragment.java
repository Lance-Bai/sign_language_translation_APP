package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.slt_project.R;
import com.example.slt_project.ui.activity.MainActivity;
import com.example.slt_project.ui.base.BaseFragment;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment, CompoundButton.OnCheckedChangeListener{
    // TODO: 2023-04-04 加一个switch， 用于选择是拍照模式或者摄像模式（默认摄像）
    private SwitchCompat photoOrVideo;
    private SwitchCompat darkOrLight;
    private SettingPresenter presenter;
    private SeekBar fontSizeSeekBar;
    private TextView fontSizeTextView;

    private Spinner languageSpinner;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews() {
        presenter = new SettingPresenter(this);

        darkOrLight = find(R.id.light_mode);
        photoOrVideo = find(R.id.photo_mode);
        fontSizeSeekBar = find(R.id.seekBar_textsize);
        fontSizeTextView=find(R.id.text_size);
        languageSpinner=find(R.id.spinner);

        darkOrLight.setOnCheckedChangeListener(this);
        photoOrVideo.setOnCheckedChangeListener(this);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(

        ) {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String label = getMainActivity().getResources().getStringArray(R.array.language_label)[position];
                presenter.setLanguage(label);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        darkOrLight.setChecked(presenter.isNightModeOn());
        photoOrVideo.setChecked(presenter.isPhotoModeOn());

       fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
           @Override
           public void onProgressChanged(SeekBar seekBar, int process, boolean b) {
               fontSizeTextView.setTextSize(process);
               fontSizeTextView.setText("Font Size = "+ TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, process, getResources().getDisplayMetrics()));
           }

           @Override
           public void onStartTrackingTouch(SeekBar seekBar) {

           }

           @Override
           public void onStopTrackingTouch(SeekBar seekBar) {

           }
       });
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
        switch (compoundButton.getId()) {
            case R.id.light_mode:
                presenter.setNightMode(b);
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                break;

            case R.id.photo_mode:
                presenter.setPhotoMode(b);
        }

    }


}
