package com.example.slt_project.ui.Setting;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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
import com.example.slt_project.ui.UserManager;
import com.example.slt_project.ui.activity.LoginActivity;
import com.example.slt_project.ui.activity.MainActivity;
import com.example.slt_project.ui.activity.RegisterActivity;
import com.example.slt_project.ui.base.BaseFragment;

public class SettingFragment extends BaseFragment implements SettingContract.ISettingFragment, CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private SwitchCompat photoOrVideo;
    private SwitchCompat darkOrLight;
    private SettingPresenter presenter;
    private SeekBar fontSizeSeekBar;
    private TextView fontSizeTextView;

    private Spinner languageSpinner;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
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

        darkOrLight.setOnCheckedChangeListener(this);
        photoOrVideo.setOnCheckedChangeListener(this);
        logoutButton.setOnClickListener(this);

        sharedPreferences = getContext().getSharedPreferences("my_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();


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

//        boolean isNightModeOn = sharedPreferences.getBoolean("night_mode", false);
//        boolean isPhotoModeOn = sharedPreferences.getBoolean("photo_mode", false);

        darkOrLight.setChecked(presenter.isNightModeOn());
        photoOrVideo.setChecked(presenter.isPhotoModeOn());

        fontSizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int process, boolean b) {
                fontSizeTextView.setTextSize(process);
                fontSizeTextView.setText("Font Size = " + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, process, getResources().getDisplayMetrics()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (userManager.isLoggedIn()) {
            logoutButton.setText(R.string.logout_button_text);
        }else{
            logoutButton.setText(R.string.login_button_text);
        }

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


    @Override
    public void onClick(View view) {
        Log.d("shenme", "onClick: ");
        switch (view.getId()) {
            case R.id.log_out_button:

                if (userManager.isLoggedIn()) {
                    userManager.setLoggedIn(false);
                break;
                } else {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
        }
    }


}
