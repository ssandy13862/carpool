package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.carPoolExcepton.MyOrderNotFoundException;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.OrderPastAndRecent;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.object.Order;

import java.util.List;

public class MyOrderReadPresenter {

    private MyOrderView myOrderView;
    private ThreadExecutor threadExecutor;
    private OrderRepository orderRepository;

    public MyOrderReadPresenter(OrderRepository orderRepository,
                                MyOrderView myOrderView,
                                ThreadExecutor threadExecutor){
        this.myOrderView = myOrderView;
        this.threadExecutor = threadExecutor;
        this.orderRepository = orderRepository;

    }

    public void readMyOrders(String token, String email){
        threadExecutor.execute(() -> {
            try {
                OrderPastAndRecent orders = orderRepository.readMyOrders(token, email);
                threadExecutor.executeUiThread(() ->
                        myOrderView.onReadMyOrderSuccessfully(orders));
            }catch (MyOrderNotFoundException err){
                threadExecutor.executeUiThread(() ->
                        myOrderView.onReadMyOrderNotFound());
            }
        });
    }

    public void deleteOrder(String token, OrderInfo orderInfo){
        threadExecutor.execute(() -> {
                orderRepository.deleteOrder(token, orderInfo);
                threadExecutor.executeUiThread(() ->
                        myOrderView.onDeleteMyOrderSuccessfully());
        });
    }

    public interface MyOrderView{

        void onReadMyOrderSuccessfully(OrderPastAndRecent orderAll);

        void onReadMyOrderNotFound();

        void onDeleteMyOrderSuccessfully();

        void onDeleteMyOrderFailed();
    }


}
