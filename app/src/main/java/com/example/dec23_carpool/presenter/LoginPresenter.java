package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.EmailOrPasswordNotFoundException;
import com.example.dec23_carpool.object.CredentialsInfo;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.model.userRepository.UserRepository;

public class LoginPresenter {

    private UserRepository userRepository;
    private LoginView loginView;
    private ThreadExecutor threadExecutor;

    public LoginPresenter(UserRepository userRepository,
                          LoginView loginView,
                          ThreadExecutor threadExecutor) {
        this.userRepository = userRepository;
        this.loginView = loginView;
        this.threadExecutor = threadExecutor;
    }

    public interface LoginView {

        void onLoginSuccessfully(User user);

        void onLoginFailedCauseInputEmpty();

        void onLoginFailedForEmailNotFound();

        void onApiDoNotOpen();

    }

    public void login(String email, String password) {
        verifyInputEmpty(email, password);
        threadExecutor.execute(() -> {
            try {
                User user = userRepository.login(new CredentialsInfo(email, password));
                threadExecutor.executeUiThread(() ->
                        loginView.onLoginSuccessfully(user));
            } catch (EmailOrPasswordNotFoundException err) {
                threadExecutor.executeUiThread(() ->
                        loginView.onLoginFailedForEmailNotFound());
            } catch (ApiDoNotOpenException err){
                threadExecutor.executeUiThread(() ->
                        loginView.onApiDoNotOpen());
            }
        });
    }


    private void verifyInputEmpty(String email, String password) {
        if (!email.isEmpty() && !password.isEmpty()) {
            loginView.onLoginFailedCauseInputEmpty();
        }
    }

}
