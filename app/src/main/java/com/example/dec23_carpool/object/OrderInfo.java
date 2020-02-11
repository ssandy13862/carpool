package com.example.dec23_carpool.object;

import android.graphics.Bitmap;

public class OrderInfo {

    private int travelInfoId;
    private String Email;
    private Bitmap picture;
    private int score;

    public OrderInfo(String email, int travelInfoId, int score) {
        this.travelInfoId = travelInfoId;
        Email = email;
        this.score = score;
    }

    public OrderInfo(String Email, int travelInfoId) {
        this.travelInfoId = travelInfoId;
        this.Email = Email;
    }

    public OrderInfo(String Email, Bitmap picture){
        this.Email = Email;
        this.picture = picture;
    }

    public int getTravelInfoId() {
        return travelInfoId;
    }

    public void setTravelInfoId(int travelInfoId) {
        this.travelInfoId = travelInfoId;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
