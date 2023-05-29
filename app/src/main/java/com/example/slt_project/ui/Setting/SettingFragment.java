package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.slt_project.R;
import com.example.slt_project.ui.Mode;
import com.example.slt_project.ui.UserManager;
import com.example.slt_project.ui.activity.LanguageActivity;
import com.example.slt_project.ui.activity.LoginActivity;
import com.example.slt_project.ui.activity.MainActivity;
import com.example.slt_project.ui.activity.RegisterActivity;
import com.example.slt_project.ui.base.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private SwitchCompat photoOrVideo;
    private SwitchCompat darkOrLight;
    private SettingPresenter presenter;
    private SeekBar fontSizeSeekBar;
    private TextView fontSizeTextView, selectoutputLanguage, selectAppLanguage;

    private Spinner languageSpinner, appLanguageSpinner;

    private SharedPreferences sharedPreferences;
    public Mode mode;
    private SharedPreferences.Editor editor;
    //    private int textSize;
    private Button logoutButton;
    private ImageButton arrowButton;
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
        fontSizeTextView = find(R.id.text_size);
        languageSpinner = find(R.id.spinner);
        fontSizeTextView = find(R.id.text_size);
        logoutButton = find(R.id.log_out_button);
        selectoutputLanguage = find(R.id.selectLanguage);
        selectAppLanguage = find(R.id.select_app_Language);
//        appLanguageSpinner = find(R.id.spinner_app_language);
        arrowButton=find(R.id.arrowButton);

        darkOrLight.setOnCheckedChangeListener(this);
        photoOrVideo.setOnCheckedChangeListener(this);
        logoutButton.setOnClickListener(this);
        arrowButton.setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        mode = new Mode(getContext());
//        appLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                String now = sharedPreferences.getString("applanguage", "");
//                String selectedLanguage = "";
//                switch (position) {
//                    case 0:
//                        selectedLanguage = now.equals("chinese") ? "" : "chinese";
////
//                        break;
//                    case 1:
//                        selectedLanguage = "chinese";
//                        break;
//                    case 2:
//                        selectedLanguage = "english";
//                        break;
//                }
//
//                if (!selectedLanguage.isEmpty()) {
//                    changeLanguage(selectedLanguage);
//                    editor.putString("applanguage", selectedLanguage);
//                    editor.apply();
//                    if (!selectedLanguage.equals(now)) {
////                        changeLanguage(now);
//                        editor.putString("savelanguage", selectedLanguage);
//                        editor.apply();
//                        getActivity().recreate();
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


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

                int textSize = 0;
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
                fontSizeTextView.setText(getString(R.string.setting_fontsize) + textSize);
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
        } else {
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


    private void changeLanguage(String languageCode) {
        Locale locale;
        if (languageCode.equals("chinese")) {
            locale = new Locale("zh");
        } else if (languageCode.equals("english")) {
            locale = new Locale("en");
        } else {
            locale = Locale.getDefault();
        }

        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

//    private void changeLanguage(Locale locale) {
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();
//        config.setLocale(locale);
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//    }

    @Override
    public void onResume() {
        super.onResume();
        // 从SharedPreferences中读取字体大小设置
        int fontSize = sharedPreferences.getInt("text_size", 0);
        // 如果已经保存了设置，则更新SeekBar和文本字体大小
        if (fontSize > 0) {
            fontSizeSeekBar.setProgress(fontSize);
            fontSizeTextView.setText(getString(R.string.setting_fontsize) + (14 + fontSize * 3));
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
                    selectoutputLanguage.setTextColor(getResources().getColor(R.color.white));
                    selectAppLanguage.setTextColor(getResources().getColor(R.color.white));
                    fontSizeTextView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    selectoutputLanguage.setTextColor(getResources().getColor(R.color.black));
                    selectAppLanguage.setTextColor(getResources().getColor(R.color.black));
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
            case R.id.arrowButton:
                startActivity(new Intent(getActivity(), LanguageActivity.class));
                getActivity().finish();
        }
    }


}
