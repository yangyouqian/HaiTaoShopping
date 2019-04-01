package com.bishe.haitaoshopping.personal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.bishe.haitaoshopping.Constant;
import com.bishe.haitaoshopping.MainActivity;
import com.bishe.haitaoshopping.R;
import com.bishe.haitaoshopping.Utils;
import com.bishe.haitaoshopping.component.titlebar.TitleBar;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TitleBar titleBar;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etName;
    private View divideEtName;
    private TextView tvNeedVerify;
    private TextView tvRegister;
    private TextView btnConfirm;

    private boolean mIsLoginState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Utils.setMStatusStyle(this);
        init();
    }

    private void init() {
        titleBar = findViewById(R.id.login_title_bar);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        divideEtName = findViewById(R.id.divide_et_name);
        etPassword = findViewById(R.id.et_password);
        tvNeedVerify = findViewById(R.id.tv_need_verify);
        tvRegister = findViewById(R.id.tv_register);
        btnConfirm = findViewById(R.id.btn_confirm);

        tvRegister.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        titleBar.setOnBackClickListener(new TitleBar.OnBackClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });

        changeLoginStatus(true);
    }

    private void changeLoginStatus(boolean isLogin) {
        mIsLoginState = isLogin;
        titleBar.setTitle(isLogin ? R.string.login : R.string.register);
        btnConfirm.setText(isLogin ? R.string.login : R.string.register);
        tvRegister.setVisibility(isLogin ? View.VISIBLE : View.INVISIBLE);
        etName.setVisibility(isLogin ? View.GONE : View.VISIBLE);
        divideEtName.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_register:
                changeLoginStatus(false);
                mIsLoginState = false;
                break;
            case R.id.btn_confirm:
                if (!isVerifyInput()) {
                    Utils.showToast(this, "请填写完整数据~");
                    return;
                }
                if (!mIsLoginState) {
                    //注册
                    doRegister();
                } else {
                    //登录
                    doLogin();
                }
                break;
        }
    }

    private void doRegister() {
        AVUser user = new AVUser();
        user.setUsername(getUserEmail());
        user.setEmail(getUserEmail());
        user.setPassword(getUserPassword());
        user.put(Constant.USER_NAME, getUserName());
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //注册成功,转变到登录状态
                    changeLoginStatus(true);
                    tvNeedVerify.setVisibility(View.VISIBLE);
                    tvRegister.setVisibility(View.INVISIBLE);
                    Utils.showToast(LoginActivity.this, "注册成功");
                    AVUser.logOut();//避免未验证邮箱,用户信息就已保存在本地
                } else {
                    Utils.showToast(LoginActivity.this, e.getMessage());
                }
            }
        });
    }

    private void doLogin() {
        AVUser.logInInBackground(getUserEmail(), getUserPassword(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    Utils.showToast(LoginActivity.this, "登录成功");
                    finish();
                } else {
                    Utils.showToast(LoginActivity.this, e.getMessage());
                }
            }
        });
    }

    private boolean isVerifyInput() {
        if (mIsLoginState) {
            return !TextUtils.isEmpty(getUserEmail()) && !TextUtils.isEmpty(getUserPassword());
        }
        return !TextUtils.isEmpty(getUserEmail()) && !TextUtils.isEmpty(getUserPassword()) && !TextUtils.isEmpty(getUserName());
    }

    private String getUserEmail() {
        return etEmail.getText().toString().trim();
    }

    private String getUserName() {
        return etName.getText().toString().trim();
    }

    private String getUserPassword() {
        return etPassword.getText().toString().trim();
    }
}
