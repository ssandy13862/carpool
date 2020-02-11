package com.example.dec23_carpool.model;

import com.example.dec23_carpool.model.carPoolExcepton.NotificationHaveNotBeenFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.SearchOrderByIdFailedException;
import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.object.Order;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class RetrofitNotifyRepostiory implements NotifyRepostiory{

    private  NotifyApi notifyApi;

    public RetrofitNotifyRepostiory(Retrofit retrofit){
        this.notifyApi = retrofit.create(NotifyApi.class);
    }

    public interface NotifyApi{

        @GET("order/search")
        Call<List<Order>> searchOrderById(@Query("travelInfoId") int id);

        @GET("user/message")
        Call<List<Notification>> readNotification(@Header("token") String token, @Query("email") String email);

    }

    @Override
    public List<Notification> readNotiication(String token, String email) {
        Call<List<Notification>> notificationCall = notifyApi.readNotification(token, email);
        try {
            Response<List<Notification>> response = notificationCall.execute();
            if(response.code() == 201){
                assert response.body() != null;
                if(response.body().size() == 0) {
                    throw new NotificationHaveNotBeenFoundException();
                }
                return response.body();
            }
            if(response.code() == 500){
                throw new NotificationHaveNotBeenFoundException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public List<Order> searchOrderById(int id) {
        Call<List<Order>> orderCall = notifyApi.searchOrderById(id);
        try {
            Response <List<Order>> response = orderCall.execute();
            if(response.code() == 201){
                assert response.body() != null;
                if(response.body().size() == 0){
                    throw new SearchOrderByIdFailedException();
                }
                return response.body();
            }
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }


}
