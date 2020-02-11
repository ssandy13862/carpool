package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.carPoolExcepton.EmailOrPasswordNotFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.RegisterFailException;
import com.example.dec23_carpool.model.userRepository.UserRepository;
import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.object.User;

public class UserRegisterPresenter {

    private UserRegisterView userRegisterView;
    private UserRepository userRepository;
    private ThreadExecutor threadExecutor;

    public UserRegisterPresenter(UserRepository userRepository,
                                 UserRegisterView userRegisterView,
                                 ThreadExecutor threadExecutor) {
        this.userRegisterView = userRegisterView;
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
    }

    public void register(User user) {

        threadExecutor.execute(() -> {
            try {
                userRepository.register(user);
                threadExecutor.executeUiThread(
                        () -> userRegisterView.onUserRegisterSuccessfully());
            } catch (RegisterFailException err) {
                threadExecutor.executeUiThread(
                        () -> userRegisterView.onUserAccountHasExistedError());
            }
        });


    }

    public void login(CredentialsInfo credentialsInfo){
        /*
        運算或網路部分，要讓他非同步(防止畫面停止不動)，
        所以用回呼函數的方式在得到資料之後同步。讚讚讚
         */
        threadExecutor.execute(() -> {
            try {
                User user = userRepository.login(credentialsInfo);
                threadExecutor.executeUiThread(() ->
                        userRegisterView.onUserLoginSuccessfully(user));
            } catch (EmailOrPasswordNotFoundException err) {
                threadExecutor.executeUiThread(
                        () -> userRegisterView.onUserLoginFail());
            }
        });
    }

    public interface UserRegisterView {

        void onUserRegisterSuccessfully();

        void onUserAccountHasExistedError();

        void onUserLoginSuccessfully(User user);

        void onUserLoginFail();

    }


}
