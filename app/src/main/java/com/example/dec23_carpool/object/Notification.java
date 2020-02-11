package com.example.dec23_carpool.object;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class Notification {

    private int travelInfoId;
    private String message;
    private String messageTimeStamp;

    public int getTravelInfoId() {
        return travelInfoId;
    }

    public void setTravelInfoId(int travelInfoId) {
        this.travelInfoId = travelInfoId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String turnToDate(Long second){
        Date date = new Date(second * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }

    public String turnToHourMin(Long second){
        long twentyFourHours = 24 * 60 * 60;
        long oneHour = 60 * 60;
        long threeDay = twentyFourHours * 3;
        long timeDifference = System.currentTimeMillis() / 1000 - second;
        long showTime;
        if(timeDifference < twentyFourHours){
            if(timeDifference < oneHour){
                showTime = timeDifference / 60;
                return showTime + " 分前";
            }
            showTime = timeDifference / 60 / 60;
            return showTime + " 小時前";
        }else {
            if(timeDifference > threeDay){
                return turnToDate(second);
            }
            showTime = timeDifference / 60 / 60 / 24;
            return showTime + " 天前";
        }
    }

    public String getMessageTimeStamp() {
//        String date = turnToDate(Long.valueOf(messageTimeStamp));
        String date = turnToHourMin(Long.valueOf(messageTimeStamp));
        return date;
    }

    public void setMessageTimeStamp(String messageTimeStamp) {
        this.messageTimeStamp = messageTimeStamp;
    }
}
