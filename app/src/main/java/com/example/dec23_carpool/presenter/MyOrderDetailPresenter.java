package com.example.dec23_carpool.presenter;

import com.example.dec23_carpool.model.LocationRepostiory;
import com.example.dec23_carpool.model.carPoolExcepton.ApiDoNotOpenException;
import com.example.dec23_carpool.model.carPoolExcepton.PeopleDoNotExistException;
import com.example.dec23_carpool.model.carPoolExcepton.SendDriverUnsupportedMediaTypeException;
import com.example.dec23_carpool.object.PeopleLocation;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.util.ThreadExecutor;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MyOrderDetailPresenter {

    private ThreadExecutor threadExecutor;
    private MyOrderDetailView myOrderDetailView;
    private LocationRepostiory locationRepostiory;
    private boolean isOpen;

    public MyOrderDetailPresenter(ThreadExecutor threadExecutor,
                                  MyOrderDetailView myOrderDetailView,
                                  LocationRepostiory locationRepostiory) {
        this.threadExecutor = threadExecutor;
        this.myOrderDetailView = myOrderDetailView;
        this.locationRepostiory = locationRepostiory;
        this.isOpen = false;
    }


    public interface MyOrderDetailView {

        void onSendDriverLocationSuccessfully();

        void onSendDriverLocationFailed();

        void onSendPassengerLocationSuccessfully();

        void onGetDriverLocationSuccessfully(PeopleLocation peopleLocation);

        void onGetPassengerLocationSuccessfully(List<PeopleLocation> peopleLocationList);

        void onGetDriverLocationFailed();

        void onGetPassengerLocationFailed();

        void onConfirmOrderSuccessfully();

        void onDriverConfirmButtomClickable();

        void onTimeCounter(String time);

    }

    public void sendDriverLocation(String token, PeopleLocation peopleLocation) {
        threadExecutor.execute(() -> {
//          while (time - timeNow >= 0 && isOpen) {
//                if(time - timeNow <= 600000) {
            try {
                locationRepostiory.sendDriverLocation(token, peopleLocation);
                threadExecutor.executeUiThread(() ->
                        myOrderDetailView.onSendDriverLocationSuccessfully());
            } catch (SendDriverUnsupportedMediaTypeException err) {
                threadExecutor.executeUiThread(() ->
                        myOrderDetailView.onSendDriverLocationFailed());
            }
        });
    }

    public void sendPassengerLocation(String token, PeopleLocation peopleLocation) {
        threadExecutor.execute(() -> {
            locationRepostiory.sendPassengerLocation(token, peopleLocation);
            threadExecutor.executeUiThread(() ->
                    myOrderDetailView.onSendPassengerLocationSuccessfully());
        });
    }

    public void getDriverLocation(String token, String email, int travelId) {
        threadExecutor.execute(() -> {
            try {
                PeopleLocation peopleLocation = locationRepostiory
                        .getDriverLocation(token, email, travelId);
                threadExecutor.executeUiThread(() -> myOrderDetailView
                        .onGetDriverLocationSuccessfully(peopleLocation));
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (PeopleDoNotExistException err) {
                threadExecutor.executeUiThread(() ->
                        myOrderDetailView.onGetDriverLocationFailed());
            }
        });
    }

    public void getPassengerLocation(String token, String email, int travelId) {
        threadExecutor.execute(() -> {
            while (isOpen) {
                try {
                    List<PeopleLocation> peopleLocationList =
                            locationRepostiory.getPassengerLocation(token, email, travelId);
                    threadExecutor.executeUiThread(() -> myOrderDetailView
                            .onGetPassengerLocationSuccessfully(peopleLocationList));
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (PeopleDoNotExistException | ApiDoNotOpenException err) {
                    threadExecutor.executeUiThread(() ->
                            myOrderDetailView.onGetPassengerLocationFailed());
                }
            }
        });
    }

    public void finishOrder(String token, PeopleLocation peopleLocation) {
        threadExecutor.execute(() -> {
            locationRepostiory.finishOrder(token, peopleLocation);
            threadExecutor.executeUiThread(() -> myOrderDetailView.onConfirmOrderSuccessfully());
        });
    }

    public void setLocationThreadStop() {
        isOpen = false;
    }

    public void setLocationThreadStart() {
        isOpen = true;
    }

    public void setCounter(int userId, long orderDepartureSecond) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long twelveHours = 12 * 60 * 60;
                long now = System.currentTimeMillis() / 1000;
                long totalSeconds = orderDepartureSecond - now;
                long totalMinutes = totalSeconds / 60;
                long minutes = totalMinutes % 60;
                long sceonds = totalSeconds % 60;
                String time = (minutes < 10 ? "0" + minutes : minutes) + ":" + (sceonds < 10 ? "0" + sceonds : sceonds);
                threadExecutor.executeUiThread(() -> {
                    if (0 <= totalSeconds && totalSeconds < twelveHours) {
                        myOrderDetailView.onTimeCounter(time);
                        if (totalSeconds == 0) {
                            myOrderDetailView.onTimeCounter(time);
                            setLocationThreadStop();
                            timer.cancel();
                            //一旦跑到有thread.uithread的函式，就不會再執行此函式之下的function。
                            setEndOrder(userId);
                        }
                    }
                    if (totalSeconds < 0) {
                        myOrderDetailView.onTimeCounter("00:00");
                        setLocationThreadStop();
                        timer.cancel();
                        //一旦跑到有thread.uithread的函式，就不會再執行此函式之下的function。
                        setEndOrder(userId);
                    }
                    if (totalSeconds > twelveHours) {
                        timer.cancel();
                    }
                });
            }
        }, 0, 1000);
    }

    private void setEndOrder(int userId) {
        if (userId == Global.DRIVER) {
            threadExecutor.executeUiThread(
                    () -> myOrderDetailView.onDriverConfirmButtomClickable());
        }
        if (userId == Global.PASSENGER) {
            threadExecutor.executeUiThread(
                    () -> myOrderDetailView.onDriverConfirmButtomClickable());
        }
    }

}
