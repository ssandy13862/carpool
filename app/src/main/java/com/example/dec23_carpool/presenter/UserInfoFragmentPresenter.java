package com.example.dec23_carpool.presenter;


import com.example.dec23_carpool.model.carPoolExcepton.RetrofitFormalErrorException;
import com.example.dec23_carpool.model.carPoolExcepton.UpdateFailException;
import com.example.dec23_carpool.model.userRepository.UserRepository;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.util.ThreadExecutor;

public class UserInfoFragmentPresenter {

    private UserInfoFragmentView userInfoFragmentView;
    private UserRepository userRepository;
    private ThreadExecutor threadExecutor;

    public UserInfoFragmentPresenter(UserInfoFragmentView userInfoFragmentView,
                                     UserRepository userRepository,
                                     ThreadExecutor threadExecutor) {
        this.userInfoFragmentView = userInfoFragmentView;
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
    }

    public void modifyUserInfo(User user) {
        threadExecutor.execute(() -> {
            try {
                userRepository.update(user);
                threadExecutor.executeUiThread(() -> {
                        userInfoFragmentView.onUpdateSuccessfully(user);
                });
            } catch (UpdateFailException e) {
                threadExecutor.executeUiThread(() -> {
                    userInfoFragmentView.onUpdateFail();
                });
            } catch (RetrofitFormalErrorException e) {
                e.getMessage();
            }
        });
    }

    public interface UserInfoFragmentView {

        void onUpdateSuccessfully(User user);

        void onUpdateFail();
    }

}
