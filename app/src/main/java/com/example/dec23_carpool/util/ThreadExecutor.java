package com.example.dec23_carpool.util;

public interface ThreadExecutor {

    void execute(Runnable runnable);

    void executeUiThread(Runnable runnable);

}
