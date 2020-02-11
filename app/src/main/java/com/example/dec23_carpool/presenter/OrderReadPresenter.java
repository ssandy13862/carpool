package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.OrderNotFountException;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.PeopleLocation;
import com.example.dec23_carpool.util.ThreadExecutor;

import java.util.List;

public class OrderReadPresenter {

    private OrderRepository orderRepository;
    private OrderView orderView;
    private ThreadExecutor threadExecutor;

    public OrderReadPresenter(OrderRepository orderRepository,
                              OrderView orderView,
                              ThreadExecutor threadExecutor) {
        this.orderRepository = orderRepository;
        this.orderView = orderView;
        this.threadExecutor = threadExecutor;
    }



    public interface OrderView {

        void onReadOrdersSuccessfully(List<Order> orders);

        void onSearchOrdersSuccessfully(List<Order> orders);

        void onReadOrderFailCauseApiDoesNotOpen();

        void onSearchFailed();
    }

    public void readOrders() {
        threadExecutor.execute(() -> {
            try {
                List<Order> orders = orderRepository.readOrders();
                threadExecutor.executeUiThread(() ->
                        orderView.onReadOrdersSuccessfully(orders));
            } catch (ApiDoNotOpenException err) {
                threadExecutor.executeUiThread(() ->
                        orderView.onReadOrderFailCauseApiDoesNotOpen());
            }
        });
    }

    public void readOrders(String token, String email) {
        threadExecutor.execute(() -> {
            try {
                List<Order> orders = orderRepository.readOrders(token, email);
                threadExecutor.executeUiThread(() ->
                        orderView.onReadOrdersSuccessfully(orders));
            } catch (ApiDoNotOpenException err) {
                threadExecutor.executeUiThread(() ->
                        orderView.onReadOrderFailCauseApiDoesNotOpen());
            }
        });
    }

    public void searchDepartureOrder(String token, String email, double departureLat, double departureLng) {
        threadExecutor.execute(() -> {
            try {
                List<Order> orders = orderRepository
                        .searchDepartureOrder(token, email, departureLat, departureLng);
                threadExecutor.executeUiThread(() ->
                        orderView.onSearchOrdersSuccessfully(orders));
            } catch (OrderNotFountException err) {
                threadExecutor.executeUiThread(() -> {
                    orderView.onSearchFailed();
                });
            }
        });
    }

    public void searchArriveOrder(String token, String email, double arriveLat, double arriveLng) {
        threadExecutor.execute(() -> {
            try {
                List<Order> orders = orderRepository
                        .searchArriveOrder(token, email, arriveLat, arriveLng);
                threadExecutor.executeUiThread(() ->
                        orderView.onSearchOrdersSuccessfully(orders));
            } catch (OrderNotFountException err) {
                threadExecutor.executeUiThread(() -> {
                    orderView.onSearchFailed();
                });
            }
        });
    }

    public void searchDepartureAndArriveOrder(String token, String email, double departureLat, double departureLng, double arriveLat, double arriveLng) {
        threadExecutor.execute(() -> {
            try {
                List<Order> orders = orderRepository.searchDepartureAndArriveOrder
                        (token, email, departureLat, departureLng, arriveLat, arriveLng);
                threadExecutor.executeUiThread(() ->
                        orderView.onSearchOrdersSuccessfully(orders));
            } catch (OrderNotFountException err) {
                threadExecutor.executeUiThread(() -> orderView.onSearchFailed());
            }
        });
    }


}
