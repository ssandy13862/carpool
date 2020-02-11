package com.example.dec23_carpool.view.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.Order;

import java.text.ParseException;

public class PlanTimeActivity extends AppCompatActivity {

    private TextView tvDeparturePlace;
    private DatePicker dpDepartureDate;
    private TimePicker tpDepartureTime;
    private Order order;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_time);
        findView();
        init();

    }


    private void findView() {
        tvDeparturePlace = findViewById(R.id.plan_tv_departurePlace);
        dpDepartureDate = findViewById(R.id.plan_dp_departureDate);
        tpDepartureTime = findViewById(R.id.plan_tp_departureTime);
        tpDepartureTime.setIs24HourView(true);
    }

    private void init() {
        order = (Order) getIntent().getSerializableExtra("PLAN_MAP_ORDER");
        user = (User) getIntent().getSerializableExtra("PLAN_MAP_USER");
        tvDeparturePlace.setText(getIntent().getStringExtra("DepartureName"));
    }

    private String getDateFormat(int year, int month, int day) {
        month += 1;
        return year + "-" + (month > 10 ? month : "0" + month) + "-" + (day > 10 ? day : "0" + day);
    }

    private String getTimeFormate(int hour, int minute) {
        return (hour > 10 ? hour : "0" + hour) + ":" + (minute > 10 ? minute : "0" + minute);
    }

    public void onPlanNextStepClick(View view) throws ParseException {
        int year = dpDepartureDate.getYear();
        int month = dpDepartureDate.getMonth();
        int day = dpDepartureDate.getDayOfMonth();
        int hour = tpDepartureTime.getHour();
        int minute = tpDepartureTime.getMinute();

        Log.d("PlanTimeActivity", "日期:" + year + "-" + month + "-" + day);
        Log.d("PlanTimeActivity", "時間:" + hour + ":" + minute);

        if (order == null) {
            order = new Order();
        }
//        order.setStrDepartureDate(getDateFormat(year, month, day));
//        order.setStrDepartureTime(getTimeFormate(hour, minute));
        order.setDepartureDate(getDateFormat(year, month, day), getTimeFormate(hour, minute));

        Intent intent = new Intent(this, PlanDetailActivity.class)
                .putExtra(Global.PLAN_TIME_ORDER, order)
                .putExtra(Global.PLAN_TIME_USER, user);
        startActivity(intent);
    }


}
