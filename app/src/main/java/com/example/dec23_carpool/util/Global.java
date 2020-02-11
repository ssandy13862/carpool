package com.example.dec23_carpool.util;

import com.example.dec23_carpool.RetrofitRatingRepository;
import com.example.dec23_carpool.model.LocationRepostiory;
import com.example.dec23_carpool.model.NotifyRepostiory;
import com.example.dec23_carpool.model.RatingRepository;
import com.example.dec23_carpool.model.RetrofitLocationRepostiory;
import com.example.dec23_carpool.model.RetrofitNotifyRepostiory;
import com.example.dec23_carpool.model.googleMapConnectionRepository.GoogleMapApiConnectionRepository;
import com.example.dec23_carpool.model.googleMapConnectionRepository.GoogleMapRepository;
import com.example.dec23_carpool.model.orderRepository.OrderRepository;
import com.example.dec23_carpool.model.orderRepository.RetrofitOrderRepository;
import com.example.dec23_carpool.model.orderRepository.StubOrderRepository;
import com.example.dec23_carpool.model.userRepository.RetrofitUserRepository;
import com.example.dec23_carpool.model.userRepository.StubUserRepository;
import com.example.dec23_carpool.model.userRepository.UserRepository;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Global {

    //BD_isDriverOrNot
    public final static String BD_IS_DRIVER = "BD_IS_DRIVER";
    public final static String PLAN_TIME_ORDER = "PLAN_TIME_ORDER";
    public final static String PLAN_TIME_USER = "PLAN_TIME_USER";

    //camera parameter
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int SELECT_CAR_PHOTO_REQUEST_CODE = 2;

    //passenger driver
    public static final int PASSENGER = 1;
    public static final int DRIVER = 2;
    public static final int NOTYET = 0;
    public static final int IN_PROGRESS = 1;
    public static final int END = 2;


    //token email
    public static final String USER = "user";
    public static final String USER_TOKEN = "token";
    public static final String USER_EMAIL = "email";
    public static final String USER_NICKNAME = "nickname";
    public static final String USER_ID = "user_id";
    public static final String ORDER_ID = "order_id";


    //URL
    private final static String CARPOOL_URL = "http://35.194.149.254:8080/api/";

    //retrofit
    private static class RetrofitNetWork {

        private static Retrofit retrofit;

        private static Retrofit getRetrofitInterceptor(String url) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            return new Retrofit.Builder()
                    .baseUrl(url)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }

    public static UserRepository userRepository() {
        return new RetrofitUserRepository(Global.RetrofitNetWork
                .getRetrofitInterceptor(CARPOOL_URL));
//        return new StubUserRepository();
    }

    public static LocationRepostiory locationRepostiory() {
        return new RetrofitLocationRepostiory(Global.RetrofitNetWork
                .getRetrofitInterceptor(CARPOOL_URL));
    }

    public static NotifyRepostiory notifyRepostiory() {
        return new RetrofitNotifyRepostiory(Global.RetrofitNetWork
                .getRetrofitInterceptor(CARPOOL_URL));
//        return new StubUserRepository();
    }

    public static OrderRepository orderRepository() {
        return new RetrofitOrderRepository(RetrofitNetWork
                .getRetrofitInterceptor(CARPOOL_URL));
//        return new StubOrderRepository();
    }

    public static RatingRepository ratingRepository() {
        return new RetrofitRatingRepository(RetrofitNetWork
                .getRetrofitInterceptor(CARPOOL_URL));
//        return new StubRatingRepository();
    }

//  singletonPlace ----------------------------------------------------------------------

    private static ThreadExecutor threadExecutor;

    public static ThreadExecutor threadExecutor() {
        return threadExecutor = (threadExecutor == null) ?
                new AndroidThreadExecutor() : threadExecutor;
    }

    private static GoogleMapRepository googleMapApiRepository;

    public static GoogleMapRepository googleMapDirectionsRepository() {
        return googleMapApiRepository =
                (googleMapApiRepository == null) ?
                googleMapApiRepository = new GoogleMapApiConnectionRepository() :
                googleMapApiRepository;
    }

}

