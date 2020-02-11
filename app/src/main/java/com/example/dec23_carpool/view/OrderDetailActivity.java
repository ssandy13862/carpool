package com.example.dec23_carpool.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.util.Global;

import java.util.Objects;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView tvDepartureDate, tvDepartureTime, tvArriveTime, tvCostTime, tvScore,
            tvDriverName, tvDriverGender, tvCarfare, tvDeparturePlace, tvArrivePlace, tvMemberLimit, tvDepartureAddress, tvArriveAddress;
    private ImageView ivIsEat, ivIsSmoke, ivIsPet, ivIsLuggage;
    private Order order;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order = (Order)getIntent().getSerializableExtra("ORDER_DETAIL");
        sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        findView();
        setUserData(order);

    }

    private void findView(){
        tvDepartureDate = findViewById(R.id.order_detail_tv_departure_date);
        tvDepartureTime = findViewById(R.id.order_detail_tv_departure_time);
        tvDeparturePlace = findViewById(R.id.order_detail_tv_departure_place);
        tvArrivePlace = findViewById(R.id.order_detail_tv_arrive_place);
        tvArriveTime = findViewById(R.id.order_detail_tv_arrive_time);
        tvCostTime = findViewById(R.id.order_detail_tv_costTime);
        tvScore = findViewById(R.id.order_detail_tv_score);
        tvDriverName = findViewById(R.id.order_detail_tv_driver_name);
        tvDriverGender = findViewById(R.id.order_detail_tv_driver_gender);
        tvMemberLimit =findViewById(R.id.order_detail_tv_memberlimit);
        tvCarfare = findViewById(R.id.order_detail_tv_carfare);
        ivIsEat = findViewById(R.id.order_detail_iv_isEat);
        ivIsSmoke = findViewById(R.id.order_detail_iv_isSmoke);
        ivIsPet = findViewById(R.id.order_detail_iv_isPet);
        ivIsLuggage = findViewById(R.id.order_detail_iv_isLuggage);
        tvDepartureAddress = findViewById(R.id.order_detail_tv_departure_address);
        tvArriveAddress = findViewById(R.id.order_detail_tv_arrive_address);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setUserData(Order order){

        tvDepartureDate.setText(order.getStrDepartureDate());
        tvDepartureTime.setText(order.getStrDepartureTime());
        tvDeparturePlace.setText(order.getDepartureName());
        tvDepartureAddress.setText(order.getDepartureAddress());

        tvArriveTime.setText(order.getStrArriveTime());
        tvArrivePlace.setText(order.getArrivePlaceName());
        tvArriveAddress.setText(order.getArrivePlaceAddress());

        tvMemberLimit.setText(String.format("%s", order.getSeatRemained()));
        tvCarfare.setText(String.format("%s", "$"+order.getCarfare()));
        tvDriverGender.setText(String.valueOf(order.getDriver().getGender()));
        tvDriverName.setText(order.getDriver().getNickname());
        tvScore.setText(String.format("%.01f", order.getDriver().getScore()));
        tvCostTime.setText("carpool "+order.getCostTime());

        if(!order.isEat()){
            ivIsEat.setImageDrawable(getDrawable(R.drawable.ic_no_eat));
        }
        if(!order.isPet()){
            ivIsPet.setImageDrawable(getDrawable(R.drawable.ic_no_pet));
        }
        if(!order.isSmoke()){
            ivIsSmoke.setImageDrawable(getDrawable(R.drawable.ic_no_smoke));
        }
        if(!order.isLuggage()){
            ivIsLuggage.setImageDrawable(getDrawable(R.drawable.ic_no_luggage));
        }

        //儲存order id
        SharedPreferences sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        sp.edit().putInt(Global.ORDER_ID, order.getId())
                .apply();


    }

    public void onOrderDetailClick(View view) {
        switch (sp.getInt(Global.USER_ID, -1)){
            case Global.PASSENGER:
                startActivity(new Intent
                        (this, OrderDetailConfirmActivity.class)
                        .putExtra("ORDER_DETAIL_CONFIRM", order));
                break;
            case Global.DRIVER:
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailActivity.this);
                builder.setMessage("司機目前無法加入訂單 :(");
                builder.setPositiveButton("確認", (dialog, id) -> dialog.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }

    }


    public void onCancelClick(View view) {
        this.finish();
    }
}
