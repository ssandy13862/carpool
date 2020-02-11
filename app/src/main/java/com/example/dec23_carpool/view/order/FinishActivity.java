package com.example.dec23_carpool.view.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.view.MainActivity;
import com.example.dec23_carpool.view.fragmentAll.OrderFragment;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

    }

    public void onFinishClick(View view) {
        User user = (User) getIntent().getSerializableExtra("user");
        startActivity(new Intent(this, MainActivity.class)
                .putExtra("LOGIN_USER", user));
    }
}
