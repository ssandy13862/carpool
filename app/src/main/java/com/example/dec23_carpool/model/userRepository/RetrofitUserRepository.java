package com.example.dec23_carpool.model.userRepository;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.BackendFormalErrorException;
import com.example.dec23_carpool.model.carPoolExcepton.EmailOrPasswordNotFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.RegisterFailException;
import com.example.dec23_carpool.model.carPoolExcepton.RetrofitFormalErrorException;
import com.example.dec23_carpool.model.carPoolExcepton.UpdateFailException;
import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.object.Message;
import com.example.dec23_carpool.object.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public class RetrofitUserRepository implements UserRepository {

    private UserApi userApi;

    public RetrofitUserRepository(Retrofit retrofit) {
        userApi = retrofit.create(UserApi.class);
    }

    public interface UserApi {
        @Multipart
        @POST("user/upload/picture")
        Call<Message> uploadUserPhoto(@Header("token") String token,
                                      @Part MultipartBody.Part email,
                                      @Part MultipartBody.Part file);

        @Multipart
        @POST("user/upload/car")
        Call<Message> uploadCarPic(@Header("token") String token,
                                   @Part MultipartBody.Part email,
                                   @Part MultipartBody.Part licensePic,
                                   @Part MultipartBody.Part carPic1,
                                   @Part MultipartBody.Part carPic2,
                                   @Part MultipartBody.Part carPic3);

        @POST("user/register")
        Call<User> post(@Body User user);

        @POST("user/gettoken")
        Call<User> login(@Body CredentialsInfo credentialsInfo);

        @PUT("user/update")
        Call<User> update(@Header("token") String token, @Body User user);

    }

    @Override
    public void update(User user) {
        Call<User> userCall = userApi.update(user.getToken(), user);
        try {
            Response<User> response = userCall.execute();
            if (response.code() == 409) {
                throw new UpdateFailException();
            }
            if (response.code() == 400) {
                throw new RetrofitFormalErrorException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public User login(CredentialsInfo credentialsInfo) {
        Call<User> userCall = userApi.login(credentialsInfo);
        try {
            Response<User> response = userCall.execute();
            if (response.code() == 409) {
                throw new EmailOrPasswordNotFoundException();
            }
            if (response.code() == 503) {
                throw new ApiDoNotOpenException();
            }
            return response.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public User readUserInfo(CredentialsInfo credentialsInfo) {
        return null;
    }

    @Override
    public void uploadUserPhoto(String token, String email, Bitmap bitmap) {
        Call<Message> userCall = userApi.uploadUserPhoto(token,
                MultipartBody.Part.
                        createFormData("email", email),
                convertBitmapToMultiPart(bitmap, "picture", "userPhoto.jpg"));
        try {
            //只能執行一次
            Response<Message> response = userCall.execute();
            if (response.code() == 500) {
                throw new BackendFormalErrorException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MultipartBody.Part convertBitmapToMultiPart(Bitmap bitmap,
                                                        String keyName,
                                                        String fileName) {
        if (bitmap == null) {
            return null;
        } else {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            RequestBody body = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
            return MultipartBody.Part.createFormData(keyName, fileName, body);
        }
    }

    public void uploadPic(String token, String email, Bitmap licensePic,
                          Bitmap carPic1, Bitmap carPic2, Bitmap carPic3) {
        Call<Message> userCall = userApi.uploadCarPic(
                token, MultipartBody.Part.createFormData("email", email),
                convertBitmapToMultiPart(licensePic, "licensePic", "licensePic.jpg"),
                convertBitmapToMultiPart(carPic1, "carPic1", "carPic1.jpg"),
                convertBitmapToMultiPart(carPic2, "carPic2", "carPic2.jpg"),
                convertBitmapToMultiPart(carPic3, "carPic3", "carPic3.jpg"));
        try {
            //只能執行一次
            Response<Message> response = userCall.execute();
            if (response.code() == 500) {
                throw new BackendFormalErrorException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register(User user) {
        Call<User> userCall = userApi.post(user);
        try {
            //只能執行一次
            Response<User> response = userCall.execute();
            if (response.code() == 409) {
                throw new RegisterFailException();
            }
            if (response.code() == 400) {
                throw new RetrofitFormalErrorException();
            }
            if (response.code() == 503) {
                throw new ApiDoNotOpenException();
            }
            Log.d("RetrofitUserRepository", response.message());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
