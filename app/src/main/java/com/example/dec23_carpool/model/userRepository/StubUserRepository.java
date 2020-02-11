package com.example.dec23_carpool.model.userRepository;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.dec23_carpool.model.carPoolExcepton.UpdateFailException;
import com.example.dec23_carpool.model.carPoolExcepton.EmailOrPasswordNotFoundException;
import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.User;
import java.util.ArrayList;
import java.util.List;

public class StubUserRepository implements UserRepository {

    List<User> userList;

    public StubUserRepository(){
        userList = new ArrayList<>();
        userList.add(new User("123@gmail", "123", "云",
                "2019-12-31", 2, "0912345678", "你家"));
        userList.add(new User("test090@gmail.com", "password", "云",
                "2019-12-31", 2, "0912345678", "你家"));
    }

    @Override
    public User login(CredentialsInfo credentialsInfo) {
        userList.add(new User(credentialsInfo.getEmail(), credentialsInfo.getPassword()));
        for(int i = 0; i < userList.size(); i++){
            User user = userList.get(i);
            if (credentialsInfo.getEmail().equals(user.getEmail()) &&
                    credentialsInfo.getPassword().equals(user.getPassword())) {
                 return user;
            }
        }
        throw new EmailOrPasswordNotFoundException();
    }

    @Override
    public User readUserInfo(CredentialsInfo credentialsInfo) {
        return null;
    }

    @Override
    public void uploadPic(String token, String email, Bitmap licensePic,
                          Bitmap carPic1, Bitmap carPic2, Bitmap carPic3) {

    }

    @Override
    public void uploadUserPhoto(String token, String email, Bitmap bitmap) {

    }


    @Override
    public void register(User user) {
        for(int i = 0; i < userList.size(); i++){
            if(userList.get(i).getEmail().equals(user.getEmail())){
                throw new UpdateFailException();
            }
        }
        userList.add(user);
        for(int i = 0; i < userList.size(); i++){
            System.out.println("成功加入:"+userList.get(i).getEmail());
        }
        System.out.println(userList.size());
        user.setIdentity1(userList.size());
    }

    @Override
    public void update(User user) {
        for(int i = 0; i < userList.size(); i++){
            String email = userList.get(i).getEmail();
            if(email.equals(user.getEmail())){
                userList.get(i).setModel(user.getModel());
                userList.get(i).setLicenseNumber(user.getLicenseNumber());
                if(user.getCarPic1() != null){
                    userList.get(i).setCarPic1(user.getCarPic1());
                }
                Log.d("StubUser", "user: "+userList.get(i));
                Log.d("StubUser", "model: "+userList.get(i).getModel());
                Log.d("StubUser", "Lincense: "+userList.get(i).getLicenseNumber());
                Log.d("StubUser", "carPic: "+userList.get(i).getCarPic1());
            }
        }
    }



}
