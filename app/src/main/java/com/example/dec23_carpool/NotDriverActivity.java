package com.example.dec23_carpool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.view.MainActivity;

public class NotDriverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_driver);
    }

    public void onBackClick(View view) {
        startActivity(new Intent(this, MainActivity.class)
                .putExtra("LOGIN_USER",
                        getIntent().getSerializableExtra("LOGIN_USER")));
    }
}
