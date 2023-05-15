package com.example.slt_project.ui.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.slt_project.R;
import com.example.slt_project.ui.database.AppDataBase;
import com.example.slt_project.ui.database.UserDao;
import com.example.slt_project.ui.database.UserPO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private Button backLogin, haveAccout, signUp;
    private TextInputEditText registerEmail, registerPassword, registerPasswordConfirm;
    private UserPO userPO;
    private AppDataBase dataBase;
    Map<String, String> params = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        signUp = findViewById(R.id.register_signup_button);
//        backLogin = findViewById(R.id.register_backtoLogin);
        haveAccout = findViewById(R.id.register_account_had_button);
        registerEmail = findViewById(R.id.register_email_edittext);
        registerPassword = findViewById(R.id.register_password_edittext);
        registerPasswordConfirm = findViewById(R.id.register_password_edittext_confirm);
        signUp.setOnClickListener(this::onClick);
//        backLogin.setOnClickListener(this::onClick);
        haveAccout.setOnClickListener(this::onClick);
        userPO = new UserPO();
//        dataBase = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "mydatabase").build();
        dataBase = AppDataBase.getInstance(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_signup_button:
                String register_email = registerEmail.getText().toString();
                String register_password = registerPassword.getText().toString();
                String register_confirmPassword = registerPasswordConfirm.getText().toString();
                if (!register_email.isEmpty() && !register_password.isEmpty() && !register_confirmPassword.isEmpty()) {
                    //TODO:应该有用户名重复的提示
                    if (register_password.equals(register_confirmPassword)) {
                        params.put("username", register_email);
                        params.put("password", register_password);
                        new PostRegister(this).execute(params);
                        userPO.setUsername(register_email);
                        userPO.setPassword(register_password);
                        new InsertAsyncTask(dataBase.userDao()).execute(userPO);
                        Log.d("leo", "Insert user info: " + userPO.toString());
                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "两次密码输入不相同！", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "用户名或密码不可为空！", Toast.LENGTH_SHORT).show();

                }
                break;
//            case R.id.register_backtoLogin:
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//                break;
            case R.id.register_account_had_button:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    // 在 RegisterActivity 中创建一个异步任务用于执行数据库插入操作
    private static class InsertAsyncTask extends AsyncTask<UserPO, Void, Void> {
        private UserDao userDao;

        public InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(UserPO... users) {
            userDao.insert(users[0]); // 在后台线程中执行数据库插入操作
            return null;
        }
    }

}