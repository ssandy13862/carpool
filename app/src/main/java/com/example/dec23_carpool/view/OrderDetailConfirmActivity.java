package com.example.dec23_carpool.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.util.Global;

public class OrderDetailConfirmActivity extends AppCompatActivity {

    private TextView tvDepartureDate, tvDepartureTime, tvArriveTime, tvCostTime,tvCarfare,
            tvDeparturePlace, tvArrivePlace, tvDepartureAddress, tvArriveAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail_confirm);
        Order order = (Order)getIntent().getSerializableExtra("ORDER_DETAIL_CONFIRM");
        findView();
        setData(order);
    }

    private void findView(){
        tvDepartureDate = findViewById(R.id.order_detail_tv_departure_date);
        tvDepartureTime = findViewById(R.id.order_detail_tv_departure_time);
        tvDeparturePlace = findViewById(R.id.order_detail_tv_departure_place);
        tvArrivePlace = findViewById(R.id.order_detail_tv_arrive_place);
        tvArriveTime = findViewById(R.id.order_detail_tv_arrive_time);
        tvCostTime = findViewById(R.id.order_detail_tv_costTime);
        tvCarfare = findViewById(R.id.order_detail_tv_carfare);
        tvDepartureAddress = findViewById(R.id.order_detail_tv_departure_address);
        tvArriveAddress = findViewById(R.id.order_detail_tv_arrive_address);
    }

    private void setData(Order order){
        tvDepartureDate.setText(order.getStrDepartureDate());
        tvDepartureTime.setText(order.getStrDepartureTime());
        tvDeparturePlace.setText(order.getDepartureName());
        tvDepartureAddress.setText(order.getDepartureAddress());

        tvArriveTime.setText(order.getStrArriveTime());
        tvArrivePlace.setText(order.getArrivePlaceName());
        tvArriveAddress.setText(order.getArrivePlaceAddress());

        tvCarfare.setText(String.format("%s", "$"+order.getCarfare()));
        tvCostTime.setText("carpool "+order.getCostTime());

    }

    public void onOrderDetailConfirmClick(View view) {
        startActivity(new Intent(this, PayActivity.class));
    }
}
