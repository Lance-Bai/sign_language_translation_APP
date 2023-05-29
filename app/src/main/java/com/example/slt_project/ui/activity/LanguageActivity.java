package com.example.slt_project.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.slt_project.R;
import com.example.slt_project.ui.Mode;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {
    private Button zhButton, enButton, backButton;
    private Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        zhButton = findViewById(R.id.chinese);
        enButton = findViewById(R.id.english);
        backButton = findViewById(R.id.backtomain);
mode=new Mode(this);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        zhButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("chinese");
                mode.setAppLanguage("chinese");
                recreate();
            }
        });
        enButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLanguage("english");
                mode.setAppLanguage("english");
                recreate();
            }
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