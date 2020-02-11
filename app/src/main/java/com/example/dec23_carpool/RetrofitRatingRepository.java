package com.example.dec23_carpool;

import com.example.dec23_carpool.model.RatingRepository;
import com.example.dec23_carpool.model.orderRepository.RetrofitOrderRepository;
import com.example.dec23_carpool.object.Message;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.OrderInfo;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class RetrofitRatingRepository implements RatingRepository {

    private RatingApi ratingApi;

    public RetrofitRatingRepository(Retrofit retrofit) {
        ratingApi = retrofit.create(RatingApi.class);
    }

    public interface RatingApi {

        @POST("order/settle")
        Call<Message> ratingOrder(@Header("token")String token, @Body OrderInfo orderInfo);

    }

    @Override
    public void ratingOrder(String token, OrderInfo orderInfo) {
        Call<Message> messageCall = ratingApi.ratingOrder(token, orderInfo);
        try {
            Response<Message> response = messageCall.execute();
            if(response.code() == 500){
                //throw something
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
