package com.example.dec23_carpool.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.presenter.LoginPresenter;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.MainActivity;
import com.example.dec23_carpool.view.register.IntroductionActivity;

public class LoginActivity extends AppCompatActivity implements LoginPresenter.LoginView {

    private EditText edEmail;
    private EditText edPassword;
    private LoginPresenter loginPresenter;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();
        init();
        sharePreferencesSaveEmailPassword();
        sharePreferencesLoadEmailPassword();
    }

    private void findViews() {
        edEmail = findViewById(R.id.login_et_email);
        edPassword = findViewById(R.id.login_et_password);
    }

    private void init() {
        loginPresenter = new LoginPresenter(Global.userRepository(),
                this, Global.threadExecutor());
        sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
    }

    private void sharePreferencesSaveEmailPassword() {
        String email = "test090@gmail.com";
        String password = "password";
        SharedPreferences sp = getSharedPreferences("SP_EMAIL_PASSWORD", MODE_PRIVATE);
        sp.edit().putString("SP_EMAIL", email)
                .putString("SP_PASSWORD", password)
                .apply();
    }

    private void sharePreferencesLoadEmailPassword() {
        edEmail.setText(getSharedPreferences("SP_EMAIL_PASSWORD", MODE_PRIVATE).getString("SP_EMAIL", ""));
        edPassword.setText(getSharedPreferences("SP_EMAIL_PASSWORD", MODE_PRIVATE).getString("SP_PASSWORD", ""));
    }

    public void onForgetPasswordButtonClick(View view) {

    }

    public void onMemberSignInButtonClick(View view) {
        String email = this.edEmail.getText().toString();
        String password = this.edPassword.getText().toString();
        loginPresenter.login(email, password);
    }

    public void onVisitorSignInButtonClick(View view) {
        sp.edit().putString(Global.USER_EMAIL, null)
                .putString(Global.USER_TOKEN, null)
                .apply();
        startActivity(new Intent(this, MainActivity.class));
    }

    public void onMemberSignUpButtonClick(View view) {
        startActivity(new Intent(this, IntroductionActivity.class));
    }


    //   ---------------------------------------------------------------------------------

    @Override
    public void onLoginSuccessfully(User user) {
        startActivity(new Intent(this, MainActivity.class)
                .putExtra("LOGIN_USER", user));
        sp.edit().putString(Global.USER_NICKNAME, user.getNickname())
                 .putString(Global.USER_EMAIL, user.getEmail())
                 .putString(Global.USER_TOKEN, user.getToken())
                 .putInt(Global.USER_ID, user.getIdentity1())
                 .apply();


        }

    @Override
    public void onLoginFailedCauseInputEmpty() {
//        showToast(getString(R.string.input_can_not_be_null));
    }

    @Override
    public void onLoginFailedForEmailNotFound() {
        showToast(getString(R.string.account_or_password_is_wroung));
    }

    @Override
    public void onApiDoNotOpen() {
        showToast(getString(R.string.api_does_not_work));
    }


    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
