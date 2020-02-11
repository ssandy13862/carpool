package com.example.dec23_carpool.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.presenter.OrderRatingPresenter;
import com.example.dec23_carpool.util.Global;

public class OrderFinishActivity extends AppCompatActivity implements OrderRatingPresenter.RatingView {


    private TextView tvHint;
    private RatingBar ratingBar;
    private SharedPreferences sp;
    private int userId, travelId, score;
    private String token, email;
    private OrderRatingPresenter orderRatingPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finish);

        sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        userId = sp.getInt(Global.USER_ID, -1);
        token = sp.getString(Global.USER_TOKEN, "");
        email = sp.getString(Global.USER_EMAIL, "");
        travelId = getSharedPreferences(Global.USER, MODE_PRIVATE).getInt(Global.ORDER_ID, -1);


        orderRatingPresenter = new OrderRatingPresenter(Global.ratingRepository(), this, Global.threadExecutor());
        findView();
        showView();
    }

    private void findView(){
        tvHint = findViewById(R.id.order_finish_tv_hint);
        ratingBar = findViewById(R.id.order_finish_rating);
    }

    private void showView(){
        switch (userId){
            case Global.DRIVER:
                ratingBar.setVisibility(View.GONE);
                break;
            case Global.PASSENGER:
                tvHint.setText("點點手指\n幫辛苦開車的人按個評分");
                ratingBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onCompleteClick(View view) {
        switch (userId){
            case Global.DRIVER:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case Global.PASSENGER:
                score = ratingBar.getNumStars();
                orderRatingPresenter.ratingOrder(token, new OrderInfo(email, travelId, score));
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }

//------------------------------------------------------------------------------

    @Override
    public void onRatingSuccessfully() {
        Toast.makeText(this, "rating successfully", Toast.LENGTH_LONG).show();
    }
}
