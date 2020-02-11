package com.example.dec23_carpool.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dec23_carpool.presenter.UserRegisterPresenter;
import com.example.dec23_carpool.presenter.UserRegisterPresenter.UserRegisterView;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.view.MainActivity;

public class UserRegisterActivity extends AppCompatActivity implements UserRegisterView {

    private EditText edEmail, edPassword, edPasswordCheck,
            edNickname, edPhone, edAddress;
    private TextView tvTitle, tvBirthday;
    private Spinner spGender;
    private boolean isDriver;
    private int year, month, day;
    private UserRegisterPresenter userRegisterPresenter;
    private User user;
    private CredentialsInfo credentialsInfo;

    @Override
    protected void onStart() {
        super.onStart();
        setTitle();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);
        findViews();
        init();
//        sharePreferencesLoadEmailPassword();
//        sharePreferencesSaveEmailPassword();

    }

    private void findViews() {
        edEmail = findViewById(R.id.user_ed_email);
        edPassword = findViewById(R.id.user_ed_password);
        edPasswordCheck = findViewById(R.id.user_ed_passwordCheck);
        edNickname = findViewById(R.id.user_ed_nickname);
        tvBirthday = findViewById(R.id.user_tv_birth);
        edPhone = findViewById(R.id.user_ed_phone);
        edAddress = findViewById(R.id.user_ed_address);
        spGender = findViewById(R.id.user_sp_gender);
        tvTitle = findViewById(R.id.user_tv_title);
    }

    private void init() {
        userRegisterPresenter = new UserRegisterPresenter(Global.userRepository(),
                this, Global.threadExecutor());
        isDriver = getIntent().getBooleanExtra(Global.BD_IS_DRIVER, false);
    }

    private void setTitle() {
        String userRegisterTitle = isDriver ? tvTitle.getText().toString() : "成為乘客";
        tvTitle.setText(userRegisterTitle);
    }

    public void onBirthdayTextViewClick(View view) {
        setDatePickerDialog();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    private void setDatePickerDialog() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    UserRegisterActivity.this.year = year;
                    UserRegisterActivity.this.month = month;
                    UserRegisterActivity.this.day = dayOfMonth;
                    tvBirthday.setText(setFormat(year, month + 1, dayOfMonth));

                }, year, month, day);
        dialog.show();
    }

    private String setFormat(int year, int month, int day) {
        return "生日: " + year + "-" + (month >= 10 ? month : "0" + month) + "-" + (day >= 10 ? day : "0" + day);
    }

    public int genderStringToIntType(String gender) {
        switch (gender) {
            case "男":
                return 1;
            case "女":
                return 2;
        }
        return 0;
    }

    private void sharePreferencesSaveEmailPassword() {
        SharedPreferences sp = getSharedPreferences("SP_REGISTER", MODE_PRIVATE);
        sp.edit().putString("SP_EMAIL", "123@gmail")
                .putString("SP_PASSWORD", "123")
                .putString("SP_PASSWORD_CHECK", "123")
                .putString("SP_NICKNAME", "nick")
                .putString("SP_BIRTHDAY", "1990-06-01")
                .putString("SP_PHONE", "0912345567")
                .putString("SP_ADDRESS", "地址地址")
                .apply();
    }

    private void sharePreferencesLoadEmailPassword() {
        edEmail.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_EMAIL", ""));
        edPassword.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_PASSWORD", ""));
        edPasswordCheck.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_PASSWORD_CHECK", ""));
        edNickname.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_NICKNAME", ""));
        tvBirthday.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_BIRTHDAY", ""));
        edPhone.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_PHONE", ""));
        edAddress.setText(getSharedPreferences("SP_REGISTER", MODE_PRIVATE).getString("SP_ADDRESS", ""));
    }

    private User getUserFromEditText() {
        String email = edEmail.getText().toString();
        String password = edPassword.getText().toString();
        String passwordCheck = edPasswordCheck.getText().toString();
        String nickName = edNickname.getText().toString();
        String birthday = tvBirthday.getText().toString();
        int gender = genderStringToIntType(spGender.getSelectedItem().toString());
        String phoneNumber = edPhone.getText().toString();
        String address = edAddress.getText().toString();
        boolean isPasswordOK = verifyPasswordCheck(password, passwordCheck);
        boolean isInputOK = verifyInputEmpty(email, password, nickName, birthday, phoneNumber);
        if (!isPasswordOK) return null;
        if (!isInputOK) return null;
        user = new User(email, password, nickName, birthday, gender, phoneNumber, address);
        return user;
    }

    private boolean verifyPasswordCheck(String password, String passwordCheck) {
        if (password.equals(passwordCheck)) {
            return true;
        }
        showToast(getString(R.string.password_is_different_from_check));
        return false;

    }

    private boolean verifyInputEmpty(String email, String password,
                                     String nickname, String birthday, String phoneNumber) {
        if (email.isEmpty() || password.isEmpty() ||
                nickname.isEmpty() || birthday.isEmpty() || phoneNumber.isEmpty()) {
            showToast(getString(R.string.input_can_not_be_null));
            return false;
        }
        return true;
    }

    public void onUserRegisterNextStepButtonClick(View view) {
        if (getUserFromEditText() == null) {
            return;
        }
        userRegisterPresenter.register(getUserFromEditText());
    }

    private CredentialsInfo userToCredential(User user) {
        return new CredentialsInfo(user.getEmail(), user.getPassword());
    }


    //    ----------------------------------------------------------

    @Override
    public void onUserRegisterSuccessfully() {
        //註冊後直接登入
        userRegisterPresenter.login(new CredentialsInfo(user.getEmail(), user.getPassword()));
    }

    @Override
    public void onUserAccountHasExistedError() {
        showToast("帳號已重複");
    }

    @Override
    public void onUserLoginSuccessfully(User user) {
        startActivity(
                new Intent(this,
                        (isDriver) ? DriverUpdateActivity.class : MainActivity.class)
                        .putExtra("LOGIN_USER", user));

        SharedPreferences sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        sp.edit().putString(Global.USER_EMAIL, user.getEmail())
                .putString(Global.USER_TOKEN, user.getToken())
                .apply();
    }

    @Override
    public void onUserLoginFail() {

    }


}
