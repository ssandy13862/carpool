package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.RatingRepository;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.util.ThreadExecutor;

public class OrderRatingPresenter {

    private RatingRepository ratingRepository;
    private RatingView ratingView;
    private ThreadExecutor threadExecutor;

    public OrderRatingPresenter(RatingRepository ratingRepository,
                              RatingView ratingView,
                              ThreadExecutor threadExecutor) {
        this.ratingRepository = ratingRepository;
        this.ratingView = ratingView;
        this.threadExecutor = threadExecutor;
    }

    public interface RatingView{

        void onRatingSuccessfully();
    }

    public void ratingOrder(String token, OrderInfo orderInfo){
        threadExecutor.execute(() -> {
            ratingRepository.ratingOrder(token, orderInfo);
            threadExecutor.executeUiThread(() -> {
                ratingView.onRatingSuccessfully();
            });
        });
    }

}
