package com.example.dec23_carpool.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dec23_carpool.R;

public class PayFinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_finish);
    }

    public void onCompleteClick(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
