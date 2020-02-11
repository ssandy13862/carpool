package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.carPoolExcepton.OrderFullOrAleadyJoinException;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.object.OrderInfo;

public class PayPresenter {



    private ThreadExecutor threadExecutor;
    private OrderRepository orderRepository;
    private Orderview orderview;

    public interface Orderview{

        void joinOrderSuccessfully();

        void joinOrderFailCauseOrderFullOrJoined();
    }

    public PayPresenter(ThreadExecutor threadExecutor, Orderview orderview, OrderRepository orderRepository){
        this.threadExecutor = threadExecutor;
        this.orderview = orderview;
        this.orderRepository = orderRepository;
    }

    public void joinOrder(String token, OrderInfo joinOrderInfo){
        threadExecutor.execute(() -> {
            try {
                orderRepository.joinOrder(token, joinOrderInfo);
                threadExecutor.executeUiThread(() ->
                        orderview.joinOrderSuccessfully());
            }catch (OrderFullOrAleadyJoinException err){
                threadExecutor.executeUiThread(() ->
                        orderview.joinOrderFailCauseOrderFullOrJoined());
            }
        });
    }

}
