package com.example.dec23_carpool.model.orderRepository;

import android.graphics.Bitmap;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.CreateOrderFailException;
import com.example.dec23_carpool.model.carPoolExcepton.MediaNotSupportException;
import com.example.dec23_carpool.model.carPoolExcepton.MyOrderNotFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.OrderFullOrAleadyJoinException;
import com.example.dec23_carpool.model.carPoolExcepton.ReadOrderFailException;
import com.example.dec23_carpool.model.carPoolExcepton.ServiceErrorException;
import com.example.dec23_carpool.object.Message;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.OrderPastAndRecent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class RetrofitOrderRepository implements OrderRepository {

    private OrderApi orderApi;

    public RetrofitOrderRepository(Retrofit retrofit) {
        orderApi = retrofit.create(OrderApi.class);
    }

    public interface OrderApi {

        @GET("order/search")
// @Headers("Content-Type:application/json")
        Call<List<Order>> readOrder();

        @GET("order/search")
        Call<List<Order>> searchDepartureOrder(@Header("token") String token, @Query("email") String email, @Query("departureLat") double lat, @Query("departureLng") double lng);

        @GET("order/search")
        Call<List<Order>> searchArriveOrder(@Header("token") String token, @Query("email") String email, @Query("arrivePlaceLat") double lat, @Query("arrivePlaceLng") double lng);

        @GET("order/search")
        Call<List<Order>> searchDepartureAndArriveOrder(@Header("token") String token, @Query("email") String email, @Query("departureLat") double departureLat, @Query("departureLng") double departureLng, @Query("arrivePlaceLat") double arrivePlaceLat, @Query("arrivePlaceLng") double arrivePlaceLng);

        @GET("order/search")
        Call<List<Order>> readOrder(@Header("token") String token, @Query("email") String email);

        @GET("order/gettravellist")
        Call<OrderPastAndRecent> readMyOrder(@Header("token") String token, @Query("email") String email);

        @POST("googleMapService/getTravelinfo")
        Call<Order> getOrderCarfare(@Body Order order);

        @POST("order/joinTravel")
        Call<Message> joinOrder(@Header("token") String token, @Body OrderInfo orderInfo);

        // @DELETE("order/delete")
        @HTTP(method = "DELETE", path = "order/delete", hasBody = true)
        Call<Message> deleteOrder(@Header("token") String token, @Body OrderInfo orderInfo);

        @POST("order/add")
        Call<OrderInfo> addOrder(@Header("token") String token, @Body Order order);

        @Multipart
        @POST("user/upload/scenePic")
        Call<Message> uplaodPlanPicture(@Header("token") String token,
                                        @Part MultipartBody.Part email,
                                        @Part MultipartBody.Part file,
                                        @Part MultipartBody.Part orderId);

        @Multipart
        @POST("order/addTravel")
        Call<Message> uplaodPlan(@Header("token") String token,
                                 @Part MultipartBody.Part email,
                                 @Part MultipartBody.Part image,
                                 @Body Order order);
    }

//-------------------------------------

    private MultipartBody.Part convertBitmapToMultiPart(String keyName, String fileName, Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
        return MultipartBody.Part.createFormData(keyName, fileName, body);
    }

    @Override
    public void uploadPlanPicture(String token, String email, Bitmap bitmap, int orderId) {
        Call<Message> messageCall = orderApi.uplaodPlanPicture(token,
                MultipartBody.Part.createFormData("email", email),
                convertBitmapToMultiPart("ScenePic", "planPicture.jpg", bitmap),
                MultipartBody.Part.createFormData("TravelInfoId", String.valueOf(orderId)));
        try {
            Response<Message> response = messageCall.execute();
            int responseCode = response.code();
            if (responseCode == 555) {
                //throw something
            }
            if (responseCode == 500) {
                throw new RuntimeException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteOrder(String token, OrderInfo orderInfo) {
        Call<Message> messageCall = orderApi.deleteOrder(token, orderInfo);
        try {
            Response<Message> response = messageCall.execute();
            if (response.code() == 404) {
                //throw something
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderPastAndRecent readMyOrders(String token, String email) {
        Call<OrderPastAndRecent> ordercall = orderApi.readMyOrder(token, email);
        try {
            Response<OrderPastAndRecent> response = ordercall.execute();
            OrderPastAndRecent order = response.body();
            if (response.code() == 201 && order != null) {
                if (order.getResult() == null) {
                    return order;
                }
                if (order.getResult().equals("No Email")) {
                    throw new MyOrderNotFoundException();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void joinOrder(String token, OrderInfo joinOrderInfo) {
        Call<Message> orderCall = orderApi.joinOrder(token, joinOrderInfo);
        try {
            Response<Message> response = orderCall.execute();
            if (response.code() == 201 &&
                    response.body().getResult().equals("This travel is full or already join")) {
                throw new OrderFullOrAleadyJoinException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public OrderInfo createOrder(Order order, String token) {
        Call<OrderInfo> responseCall = orderApi.addOrder(token, order);
        OrderInfo orderInfo = null;
        try {
            Response<OrderInfo> response = responseCall.execute();
            if (response.code() == 204  || response.code() == 400) {
                throw new CreateOrderFailException();
            }
            if (response.code() == 500) {
                throw new ServiceErrorException();
            }
            orderInfo = response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return orderInfo;
    }

    @Override
    public Order getOrderCarfare(Order order) {
        Call<Order> orderCall = orderApi.getOrderCarfare(order);
        try {
            Response<Order> response = orderCall.execute();
            return response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> readOrders() {
        Call<List<Order>> orderCall = orderApi.readOrder();
        try {
            Response<List<Order>> response = orderCall.execute();
            if (response.code() == 409) {
                throw new ReadOrderFailException();
            }
            if (response.code() == 415) {
                throw new MediaNotSupportException();
            }
            if (response.code() == 503) {
                throw new ApiDoNotOpenException();
            }
            return response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> readOrders(String token, String email) {
        Call<List<Order>> orderCall = orderApi.readOrder(token, email);
        List<Order> orderList = new LinkedList<>();
        try {
            Response<List<Order>> response = orderCall.execute();
            if (response.code() == 409) {
                throw new ReadOrderFailException();
            }
            if (response.code() == 415) {
                throw new MediaNotSupportException();
            }
            if (response.code() == 503) {
                throw new ApiDoNotOpenException();
            }
            orderList = response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return orderList;
    }

    public List<Order> searchDepartureOrder(String token, String email, double departureLat, double departureLng) {
        Call<List<Order>> orderCall = orderApi.searchDepartureOrder(token, email, departureLat, departureLng);
        try {
            Response<List<Order>> response = orderCall.execute();
            return response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> searchDepartureAndArriveOrder(String token, String email, double departureLat, double departureLng, double arriveLat, double arriveLng) {
        Call<List<Order>> orderCall = orderApi.searchDepartureAndArriveOrder(token, email, departureLat, departureLng, arriveLat, arriveLng);
        try {
            Response<List<Order>> response = orderCall.execute();
            return response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> searchArriveOrder(String token, String email, double lat, double lng) {
        Call<List<Order>> orderCall = orderApi.searchArriveOrder(token, email, lat, lng);
        try {
            Response<List<Order>> response = orderCall.execute();
            return response.body();
        } catch (IOException err) {
            err.printStackTrace();
        }
        return null;
    }

}