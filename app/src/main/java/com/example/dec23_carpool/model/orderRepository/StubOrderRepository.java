package com.example.dec23_carpool.model.orderRepository;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.example.dec23_carpool.model.carPoolExcepton.OrderNotFountException;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.OrderPastAndRecent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class StubOrderRepository implements OrderRepository {

    private List<Order> orderArrayList;
    private List<Order> orderArrayListFull;
    private List<Order> myOrderList;


    public StubOrderRepository() {
        orderArrayList = new ArrayList<>();
        orderArrayListFull = new ArrayList<>();
        myOrderList = new ArrayList<>();

        addData();
    }

    private void addData() {
        orderArrayList.add(new Order("08:00", "12:00", "2010-01-03", "台中車站", "台北車站", 180, "王小明", 1, 4.6));
        orderArrayList.add(new Order("09:00", "13:00", "2010-01-03", "台中車站", "罈子車站", 160, "王小明", 1, 4.6));
        orderArrayList.add(new Order("10:00", "14:00", "2010-01-03", "花蓮車站", "太原車站", 170, "王小明", 1, 4.6));
        orderArrayList.add(new Order("11:00", "15:00", "2010-01-03", "屏東車站", "太原車站", 150, "王小明", 1, 4.6));
        orderArrayList.add(new Order("12:00", "16:00", "2010-01-03", "桃園車站", "台北車站", 160, "王小明", 1, 4.6));
        Long time = System.currentTimeMillis();

        myOrderList.add(new Order("01:00", "16:00", "2020-01-09", "台中車站", "台北車站", 160, "王小明", 1, 4.6));
        myOrderList.add(new Order("02:00", "16:00", "2020-01-09", "桃園車站", "台北車站", 160, "王小明", 1, 4.6));
        myOrderList.add(new Order("03:00", "16:00", "2020-01-09", "花蓮車站", "台北車站", 160, "王小明", 1, 4.6));
        myOrderList.get(0).setStrDepartureDate("2020-01-09");
        myOrderList.get(1).setStrDepartureDate("2020-01-09");
        myOrderList.get(2).setStrDepartureDate("2020-01-09");

    }


    @SuppressLint("SimpleDateFormat")
    public Long stringTurnToLong(String date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Objects.requireNonNull
                (new SimpleDateFormat("yyyy-MM-dd").parse(date)));
        return calendar.getTimeInMillis();
    }

    @Override
    public OrderInfo createOrder(Order order, String token) {
        return null;
//        return new Order("08:00", "12:00",
//                "2010-01-03", "台中車站",
//                "台北車站", 180, "王小明", 1, 4.6);
    }

    public List<Order> searchOrder(String keyword, String keyword2) {

        orderArrayListFull.clear();
        //如果order的關鍵字包含輸入文字，就將此order加入過濾列表
        for (Order order : orderArrayList) {
            CharSequence constraintDepart = order.getDepartureName();
            CharSequence constraintArrive = order.getArrivePlaceName();
            String filterDepart = constraintDepart.toString().trim();
            String filterArrive = constraintArrive.toString().trim();

            if (filterDepart.contains(keyword) && filterArrive.contains(keyword2)) {
                orderArrayListFull.add(order);
            }
        }
        if (orderArrayListFull != null) {
            return orderArrayListFull;
        } else {
            throw new OrderNotFountException();
        }

    }

    @Override
    public List<Order> readOrders() {
        return orderArrayList;
    }

    @Override
    public List<Order> readOrders(String token, String email) {
        return orderArrayList;
    }

    @Override
    public OrderPastAndRecent readMyOrders(String token, String email) {
        return new OrderPastAndRecent();
    }

    @Override
    public Order getOrderCarfare(Order order) {
        order.setMaxFare(286);
        order.setMinFare(133);
        order.setTime("1:35:0");
        return order;
    }

    @Override
    public void joinOrder(String token, OrderInfo joinOrderInfo) {

    }

    @Override
    public List<Order> searchDepartureOrder(String token, String email, double departureLat, double departureLng) {
        return null;
    }

    @Override
    public List<Order> searchDepartureAndArriveOrder(String token, String email, double departureLat, double departureLng, double arriveLat, double arriveLng) {
        return null;
    }

    @Override
    public List<Order> searchArriveOrder(String token, String email, double departureLat, double departureLng) {
        return null;
    }

    @Override
    public void deleteOrder(String token, OrderInfo orderInfo) {

    }

    @Override
    public void uploadPlanPicture(String token, String email, Bitmap bitmap, int orderId) {

    }

}
