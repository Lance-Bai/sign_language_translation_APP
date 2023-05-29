package com.example.slt_project.ui.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slt_project.R;
import com.example.slt_project.ui.Mode;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Button zhButton = findViewById(R.id.chinese);
        Button enButton = findViewById(R.id.english);
        Button backButton = findViewById(R.id.backtomain);
mode=new Mode(this);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        });
        zhButton.setOnClickListener(view -> {
            changeLanguage("chinese");
            mode.setAppLanguage("chinese");
            recreate();
        });
        enButton.setOnClickListener(view -> {
            changeLanguage("english");
            mode.setAppLanguage("english");
            recreate();
        });
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

}