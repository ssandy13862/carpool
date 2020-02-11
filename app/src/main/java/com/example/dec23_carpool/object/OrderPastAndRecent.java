package com.example.dec23_carpool.object;

import java.util.List;

public class OrderPastAndRecent {

    private String result;

    private List<Order> recent;

    private List<Order> past;

    public OrderPastAndRecent(){

    }

    public OrderPastAndRecent(List<Order> past, List<Order> recent) {
        this.recent = recent;
        this.past = past;
    }

    public List<Order> getRecent() {
        return recent;
    }

    public void setRecent(List<Order> recent) {
        this.recent = recent;
    }

    public List<Order> getPast() {
        return past;
    }

    public void setPast(List<Order> past) {
        this.past = past;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

