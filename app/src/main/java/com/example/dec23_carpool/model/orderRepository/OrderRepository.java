package com.example.dec23_carpool.model.orderRepository;

import android.graphics.Bitmap;

import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.OrderPastAndRecent;

import java.util.List;

public interface OrderRepository {

    //CRUD
    OrderInfo createOrder(Order order, String token);

    //訪客讀到的訂單
    List<Order> readOrders();

    //會員讀到的訂單
    List<Order> readOrders(String token, String email);

    //使用者自己的訂單
    OrderPastAndRecent readMyOrders(String token, String email);

    Order getOrderCarfare(Order order);

    void joinOrder(String token, OrderInfo joinOrderInfo);

    List<Order> searchDepartureOrder(String token, String email, double departureLat, double departureLng);

    List<Order> searchDepartureAndArriveOrder(String token, String email, double departureLat, double departureLng, double arriveLat, double arriveLng);

    List<Order> searchArriveOrder(String token, String email, double arriveLat, double arriveLng);

    void deleteOrder(String token, OrderInfo orderInfo);

    void uploadPlanPicture(String token, String email, Bitmap bitmap, int orderId);


}
