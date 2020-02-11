package com.example.dec23_carpool.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.dec23_carpool.R;

import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

    }

    public void onTestClick(View view) {
        setCounter();
    }

    private int second1, minute1;
    private long ordertime = 1580101800;



    private void setTimerCounter(){

        Timer timer = new Timer();
        second1 = 60;
        minute1 = 10;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                minute1 -= 1;
                if(minute1 == -1){
                    timer.cancel();
                }
            }
        }, 0, 60000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                second1 -= 1;
                Log.d("myorder", (minute1 < 10 ? "0" + minute1 : minute1) + ":" + (second1 < 10 ? "0" + second1 : second1));
                if(second1 == 0){
                    second1 = 60;
                }
            }
        }, 0, 1000);
    }

    private void setCounter(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long totalSeconds = ordertime - System.currentTimeMillis() / 1000;
                long totalMinutes = totalSeconds / 60;
                long minutes = totalMinutes % 60;
                long sceonds = totalSeconds % 60;
                Log.d("myorder", (minutes < 10 ? "0" + minutes : minutes) + ":" + (sceonds < 10 ? "0" + sceonds : sceonds));
                if(totalSeconds == 0){
                    timer.cancel();
                }
            }
        }, 0, 1000);
    }


}
