package com.example.dec23_carpool.presenter;

import android.graphics.Bitmap;

import com.example.dec23_carpool.model.carPoolExcepton.CreateOrderFailException;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.object.Order;

public class OrderCreatePresenter {

    private OrderRepository orderRepository;
    private ThreadExecutor threadExecutor;
    private OrderView orderView;

    public OrderCreatePresenter(OrderRepository orderRepository,
                                OrderView orderView,
                                ThreadExecutor threadExecutor) {
        this.orderView = orderView;
        this.orderRepository = orderRepository;
        this.threadExecutor = threadExecutor;
    }

    public interface OrderView {

        void onOrderCreatedSuccessfully(OrderInfo responseOrder);

        void onOrderCarfareGetSuccessfully(Order order);

        void onOrderCreatedFailed();

        void onUploadPlanPictureSuccessfully();

        void onUploadPlanPictureFaild();
    }

    public void uploadPlanPicture(String token, String email, Bitmap bitmap, int orderId) {
        threadExecutor.execute(() -> {
            try {
                orderRepository.uploadPlanPicture(token, email, bitmap, orderId);
                threadExecutor.executeUiThread(() ->
                        orderView.onUploadPlanPictureSuccessfully());
            } catch (RuntimeException err) {
                threadExecutor.executeUiThread(() ->
                        orderView.onUploadPlanPictureFaild());
            }
        });
    }


    public void createOrder(Order order, String token) {
        threadExecutor.execute(() -> {
            try {
                OrderInfo responseOrder = orderRepository.createOrder(order, token);
                threadExecutor.executeUiThread(() -> orderView.onOrderCreatedSuccessfully(responseOrder));
            } catch (CreateOrderFailException err) {
                threadExecutor.executeUiThread(() -> orderView.onOrderCreatedFailed());
            }
        });
    }

    public void getCarFare(Order order) {
        threadExecutor.execute(() -> {
            Order orderWithCarfare = orderRepository.getOrderCarfare(order);
            threadExecutor.executeUiThread(() ->
                    orderView.onOrderCarfareGetSuccessfully(orderWithCarfare));
        });
    }
}
