package com.example.dec23_carpool.util;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AndroidThreadExecutor implements ThreadExecutor {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private Handler handler = new Handler();

    public void execute(Runnable runnable) {
        executorService.execute(runnable);
    }

    public void executeUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void shutDownThreadPool() {
        executorService.shutdown();
    }

}
