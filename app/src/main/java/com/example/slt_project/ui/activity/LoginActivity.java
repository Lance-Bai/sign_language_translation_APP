package com.example.slt_project.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.slt_project.R;
import com.example.slt_project.ui.S2T.PostData;
import com.example.slt_project.ui.SendAble;
import com.example.slt_project.ui.UserManager;
import com.example.slt_project.ui.base.BaseActivity;
import com.example.slt_project.ui.database.AppDataBase;
import com.example.slt_project.ui.database.UserDao;
import com.example.slt_project.ui.database.UserPO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private Button skipButton, loginButton, registerButton;
    private TextInputEditText email, password;
    AppDataBase db;
    UserDao userDao;
    UserManager userManager;
    Map<String, String> params = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userManager = new UserManager(this);

        setContentView(R.layout.activity_login);
        skipButton = findViewById(R.id.skip_button);
        loginButton = findViewById(R.id.login_button);
        email = findViewById(R.id.email_edittext);
        password = findViewById(R.id.password_edittext);
        registerButton = findViewById(R.id.login_register_button);

        registerButton.setOnClickListener(this::onClick);
        skipButton.setOnClickListener(this::onClick);
        db = AppDataBase.getInstance(this);
//        db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "mydatabase").build();
        userDao = db.userDao();
        loginButton.setOnClickListener(this);
    }
    class NotTranslateSend implements SendAble {
    @Override
    public void translateTo(String s) {
    }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_register_button:
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.skip_button:
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.login_button:
                String username_info = email.getText().toString();
                String password_info = password.getText().toString();

                if (!username_info.isEmpty() && !password_info.isEmpty()) {
                    params.put("username", username_info);
                    params.put("password", password_info);
                    new PostLogin(this, userManager).execute(params);
                    new QueryAsyncTask(userDao, new OnUsernameLoadedListener() {
                        @Override
                        public void onUsernameLoaded(String username) {
                            // 在这里处理查询结果，例如判断用户名是否正确
                            if (username.equals(username_info)) {
                                // 用户名正确，执行相应操作
                                Toast.makeText(LoginActivity.this, "用户名正确！", Toast.LENGTH_SHORT).show();

                            } else {
                                // 用户名错误，执行相应操作
                                Toast.makeText(LoginActivity.this, "用户名错误！", Toast.LENGTH_SHORT).show();

                            }
                        }
                        @Override
                        public void onUserPasswordLoaded(String password) {
                            // 在这里处理查询结果，例如判断密码是否正确
                            if (password.equals(password_info)) {
                                // 密码正确，执行相应操作
//                                Toast.makeText(LoginActivity.this, "密码正确！", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                userManager.setLoggedIn(true);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                startActivity(intent);
//                                finish();
                            } else {
                                // 密码错误，执行相应操作
                                Toast.makeText(LoginActivity.this, "密码错误！", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }).execute(email.getText().toString(), password.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private static class QueryAsyncTask extends AsyncTask<String, Void, UserPO> {

        private UserDao mUserDao;
        private OnUsernameLoadedListener mListener;

        QueryAsyncTask(UserDao userDao, OnUsernameLoadedListener listener) {
            mUserDao = userDao;
            mListener = listener;
        }

        @Override
        protected UserPO doInBackground(String... strings) {
            return mUserDao.findUser(strings[0], strings[1]);

        }

        @Override
        protected void onPostExecute(UserPO userPO) {
            // 处理查询结果
            if (userPO != null) {
                String username = userPO.getUsername();
                String password=userPO.getPassword();
                // 将用户名返回到调用方，可以通过接口回调、广播、LiveData 等方式进行
                // 示例：通过接口回调将用户名返回到调用方
                if (mListener != null) {
                    mListener.onUsernameLoaded(username);
                    mListener.onUserPasswordLoaded(password);

                }
            }
        }
    }

    public interface OnUsernameLoadedListener {
        void onUsernameLoaded(String username);
        void onUserPasswordLoaded(String password);
    }

}