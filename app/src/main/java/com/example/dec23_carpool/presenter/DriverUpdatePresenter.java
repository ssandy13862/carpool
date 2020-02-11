package com.example.dec23_carpool.presenter;


import android.graphics.Bitmap;

import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.BackendFormalErrorException;
import com.example.dec23_carpool.model.carPoolExcepton.RetrofitFormalErrorException;
import com.example.dec23_carpool.model.carPoolExcepton.UpdateFailException;
import com.example.dec23_carpool.model.userRepository.UserRepository;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.example.dec23_carpool.object.User;

public class DriverUpdatePresenter {

    private UserRepository userRepository;
    private DriverUpdateView driverUpdateView;
    private ThreadExecutor threadExecutor;

    public DriverUpdatePresenter(UserRepository userRepository,
                                 DriverUpdateView driverUpdateView,
                                 ThreadExecutor threadExecutor) {
        this.userRepository = userRepository;
        this.driverUpdateView = driverUpdateView;
        this.threadExecutor = threadExecutor;
    }

    public void update(User user) {
        threadExecutor.execute(() -> {
            try {
                userRepository.update(user);
                threadExecutor.executeUiThread(() ->
                        driverUpdateView.onDriverUpdateSuccessfully());
            } catch (UpdateFailException | ApiDoNotOpenException err) {
                threadExecutor.executeUiThread(
                        () -> driverUpdateView.onDriverUpdateFail()
                );
            } catch (RetrofitFormalErrorException err) {
                threadExecutor.executeUiThread(
                        () -> driverUpdateView.onDriverUpdateFailCauseNetFormalError());
            }
        });
    }

    public void uploadCarPics(String token, String email, Bitmap licensePic,
                              Bitmap carPic1, Bitmap carPic2, Bitmap carPic3) {
        threadExecutor.execute(() -> {
            try {
                userRepository.uploadPic(
                        token, email, licensePic,
                        carPic1, carPic2, carPic3);
                threadExecutor.executeUiThread(() ->
                        driverUpdateView.onDriverUploadPicSuccessfully());
            } catch (BackendFormalErrorException err) {
                threadExecutor.executeUiThread(() ->
                        driverUpdateView.onDriverUploadPicFailCauseFormalError());
            }
        });
    }


    public interface DriverUpdateView {

        void onDriverUploadPicSuccessfully();

        void onDriverUploadPicFailCauseFormalError();

        void onDriverUpdateSuccessfully();

        void onDriverUpdateFail();

        void onDriverUpdateFailCauseNetFormalError();

    }
}
