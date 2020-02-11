package com.example.dec23_carpool.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.register.UserRegisterActivity;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

    }

    public void onDriverButtonClick(View view) {
        jumpTo(UserRegisterActivity.class, true);
    }

    public void onPassengerButtonClick(View view) {
        jumpTo(UserRegisterActivity.class, false);
    }

    private void jumpTo(Class<? extends AppCompatActivity> activity, boolean isDriver) {
        startActivity(new Intent(this, activity)
                .putExtra(Global.BD_IS_DRIVER, isDriver));
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
