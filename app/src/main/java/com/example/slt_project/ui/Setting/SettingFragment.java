package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.slt_project.R;
import com.example.slt_project.ui.Mode;
import com.example.slt_project.ui.UserManager;
import com.example.slt_project.ui.activity.LoginActivity;
import com.example.slt_project.ui.activity.MainActivity;
import com.example.slt_project.ui.activity.RegisterActivity;
import com.example.slt_project.ui.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private SwitchCompat photoOrVideo;
    private SwitchCompat darkOrLight;
    private SettingPresenter presenter;
    private SeekBar fontSizeSeekBar;
    private TextView fontSizeTextView, selectLanguage;

    private Spinner languageSpinner;

    private SharedPreferences sharedPreferences;
    public Mode mode;
    private SharedPreferences.Editor editor;
//    private int textSize;
    private Button logoutButton;
    private static final String PREF_NAME = "user_pref";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    UserManager userManager;

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initViews() {
        userManager = new UserManager(getContext());


        presenter = new SettingPresenter(this);

        darkOrLight = find(R.id.light_mode);
        photoOrVideo = find(R.id.photo_mode);
        fontSizeSeekBar = find(R.id.seekBar_textsize);
        fontSizeTextView=find(R.id.text_size);
        languageSpinner=find(R.id.spinner);
        fontSizeTextView = find(R.id.text_size);
        logoutButton = find(R.id.log_out_button);
        selectLanguage = find(R.id.selectLanguage);

        darkOrLight.setOnCheckedChangeListener(this);
        photoOrVideo.setOnCheckedChangeListener(this);
        logoutButton.setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mode=new Mode(getContext());
        int textSize = sharedPreferences.getInt("text_size", 0);

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
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {

                int textSize=0;
                switch (progress) {
                    case 0:
                        textSize = 14;
                        break;
                    case 1:
                        textSize = 17;
                        break;
                    case 2:
                        textSize = 20;
                        break;
                    case 3:
                        textSize = 23;
                        break;
                    case 4:
                        textSize = 26;
                        break;
                    case 5:
                        textSize = 29;
                        break;
                }
//                textSize=sharedPreferences.getInt("text_size",14);
                fontSizeTextView.setText("文字大小="+textSize);
                updateViewTextSize(requireActivity().getWindow().getDecorView().getRootView(), textSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                // 将进度值保存到SharedPreferences中
                editor.putInt("text_size", progress);
                editor.apply();
            }
        });
        if (userManager.isLoggedIn()) {
            logoutButton.setText(R.string.logout_button_text);
        }else{
            logoutButton.setText(R.string.login_button_text);
        }

    }
    private void updateViewTextSize(View view, float textSize) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                updateViewTextSize(childView, textSize);
            }
        } else if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // 从SharedPreferences中读取字体大小设置
        int fontSize = sharedPreferences.getInt("text_size", 0);
        // 如果已经保存了设置，则更新SeekBar和文本字体大小
        if (fontSize > 0) {
            fontSizeSeekBar.setProgress(fontSize);
            fontSizeTextView.setText("文字大小=" + (14 + fontSize * 3));
            updateViewTextSize(requireActivity().getWindow().getDecorView().getRootView(), 14 + fontSize * 3);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        switch (compoundButton.getId()) {
            case R.id.light_mode:
                presenter.setNightMode(b);
                mode.setModeon(b);
                if (b) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    selectLanguage.setTextColor(getResources().getColor(R.color.white));
                    fontSizeTextView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    selectLanguage.setTextColor(getResources().getColor(R.color.black));
                    fontSizeTextView.setTextColor(getResources().getColor(R.color.black));
                }
                break;

            case R.id.photo_mode:
                presenter.setPhotoMode(b);
        }

    }


    @Override
    public void onClick(View view) {
        Log.d("shenme", "onClick: ");
        switch (view.getId()) {
            case R.id.log_out_button:

                if (userManager.isLoggedIn()) {
                    userManager.setLoggedIn(false);
                    logoutButton.setText(R.string.login_button_text);
                break;
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
        }
    }


}
