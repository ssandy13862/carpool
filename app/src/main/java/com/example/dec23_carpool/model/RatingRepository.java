package com.example.dec23_carpool.model;

import com.example.dec23_carpool.object.OrderInfo;

public interface RatingRepository {

    void ratingOrder(String token, OrderInfo orderInfo);

}
