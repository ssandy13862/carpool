package com.example.dec23_carpool.model.userRepository;

import android.graphics.Bitmap;

import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.User;

public interface UserRepository {

//    void takeToken(String email, String password);

    void register(User user);

    void update(User user);

    User login(CredentialsInfo credentialsInfo);

    User readUserInfo(CredentialsInfo credentialsInfo);

    void uploadPic(String token, String email, Bitmap licensePic,
                   Bitmap carPic1, Bitmap carPic2, Bitmap carPic3);

    void uploadUserPhoto(String token, String email, Bitmap bitmap);
}
