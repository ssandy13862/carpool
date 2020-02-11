package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.NotifyRepostiory;
import com.example.dec23_carpool.model.carPoolExcepton.NotificationHaveNotBeenFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.SearchOrderByIdFailedException;
import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.util.ThreadExecutor;

import java.util.List;

public class NotifyPresenter {

    private ThreadExecutor threadExecutor;
    private NotifyView notifyView;
    private NotifyRepostiory notifyRepostiory;

    public NotifyPresenter(ThreadExecutor threadExecutor, NotifyView notifyView, NotifyRepostiory notifyRepostiory){
        this.threadExecutor = threadExecutor;
        this.notifyView = notifyView;
        this.notifyRepostiory = notifyRepostiory;
    }

    public interface NotifyView{

        void onReadNotificationSuccessfully(List<Notification> notificationList);

        void onReadNotificationHaveNotBeenFound();

        void onSearchOrderByIdSuccessfully(List<Order> orderList);

        void onSearchOrderByIdFailed();


    }

    public void readNotification(String token, String email) {
        threadExecutor.execute(() -> {
            try {
                List<Notification> notificationList = notifyRepostiory.readNotiication(token, email);
                threadExecutor.executeUiThread(() ->
                        notifyView.onReadNotificationSuccessfully(notificationList));
            }catch (NotificationHaveNotBeenFoundException err){
                threadExecutor.executeUiThread(() ->
                        notifyView.onReadNotificationHaveNotBeenFound());
            }
        });
    }

    public void searchOrderById(int id){
        threadExecutor.execute(() -> {
            try {
                List<Order> orderlist =  notifyRepostiory.searchOrderById(id);
                threadExecutor.executeUiThread(() ->
                        notifyView.onSearchOrderByIdSuccessfully(orderlist));
            }catch (SearchOrderByIdFailedException err){
                threadExecutor.executeUiThread(() ->
                        notifyView.onSearchOrderByIdFailed());
            }
        });
    }




}
