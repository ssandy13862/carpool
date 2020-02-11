package com.example.dec23_carpool.model;

import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.object.Order;

import java.util.List;

public interface NotifyRepostiory {

    List<Notification> readNotiication(String token, String email);

    List<Order> searchOrderById(int id);
}
