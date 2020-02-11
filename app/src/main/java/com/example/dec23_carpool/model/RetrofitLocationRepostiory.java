package com.example.dec23_carpool.model;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.PeopleDoNotExistException;
import com.example.dec23_carpool.model.carPoolExcepton.SendDriverUnsupportedMediaTypeException;
import com.example.dec23_carpool.object.Message;
import com.example.dec23_carpool.object.PeopleLocation;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class RetrofitLocationRepostiory implements LocationRepostiory{

    private LocationApi locationApi;

    public RetrofitLocationRepostiory(Retrofit retrofit){
        this.locationApi = retrofit.create(LocationApi.class);
    }

    public interface LocationApi{

        @POST("order/updateDriverLocation")
        Call<Message> sendDriverLocation(@Header("token") String token, @Body PeopleLocation peopleLocation);

        @POST("order/updateCustomerLocation")
        Call<Message> sendPassengerLocation(@Header("token") String token, @Body PeopleLocation peopleLocation);

        @GET("order/getDriverLocation")
        Call<PeopleLocation> getDriverLocation(@Header("token") String token, @Query("email") String email, @Query("travelInfoId") int id);

        @GET("order/getCustomerLocation")
        Call<List<PeopleLocation>> getPassengerLocation(@Header("token") String token, @Query("email") String email, @Query("travelInfoId") int id);

        @POST("order/end")
        Call<Message> finishOrder(@Header("token")String token, @Body PeopleLocation peopleLocation);

    }

    @Override
    public void sendDriverLocation(String token, PeopleLocation peopleLocation) {
        Call<Message> messageCall = locationApi.sendDriverLocation(token, peopleLocation);
        try {
            Response<Message> response = messageCall.execute();
            if(response.code() == 415){
                throw new SendDriverUnsupportedMediaTypeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendPassengerLocation(String token, PeopleLocation peopleLocation) {
        Call<Message> messageCall = locationApi.sendPassengerLocation(token, peopleLocation);
        try {
            Response<Message> response = messageCall.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PeopleLocation getDriverLocation(String token, String email, int travelId) {
        Call<PeopleLocation> messageCall = locationApi.getDriverLocation(token, email, travelId);
        Response<PeopleLocation> response = null;
        try {
            response = messageCall.execute();
            if(response.code() == 404){
                throw new PeopleDoNotExistException();
            }
            if(response.code() == 503){
                throw new ApiDoNotOpenException();
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    @Override
    public List<PeopleLocation> getPassengerLocation(String token, String email, int travelId) {
        Call<List<PeopleLocation>> messageCall = locationApi.getPassengerLocation(token, email, travelId);
        try {
            Response<List<PeopleLocation>> response = messageCall.execute();
            if(response.code() == 404){
                throw new PeopleDoNotExistException();
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void finishOrder(String token, PeopleLocation peopleLocation) {
        Call<Message> messageCall = locationApi.finishOrder(token, peopleLocation);
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
