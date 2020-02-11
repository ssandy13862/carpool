package com.example.dec23_carpool.presenter;

import android.graphics.Bitmap;

import com.example.dec23_carpool.model.carPoolExcepton.BackendFormalErrorException;
import com.example.dec23_carpool.model.userRepository.UserRepository;
import com.example.dec23_carpool.util.ThreadExecutor;

public class HomeFragmentPresenter {

    private UserRepository userRepository;
    private HomeFragmentView homeFragmentView;
    private ThreadExecutor threadExecutor;

    public HomeFragmentPresenter(UserRepository userRepository,
                                 HomeFragmentView homeFragmentView,
                                 ThreadExecutor threadExecutor) {
        this.userRepository = userRepository;
        this.homeFragmentView = homeFragmentView;
        this.threadExecutor = threadExecutor;
    }

    public void uploadPhoto(String token, String email, Bitmap userPhoto) {
        threadExecutor.execute(() -> {
            try {
                userRepository.uploadUserPhoto(token, email, userPhoto);
                threadExecutor.executeUiThread(() ->
                        homeFragmentView.onUserUploadPhotoSuccessfully());
            } catch (BackendFormalErrorException err) {
            }
        });
    }

    public interface HomeFragmentView {
        void onUserUploadPhotoSuccessfully();
    }
}
