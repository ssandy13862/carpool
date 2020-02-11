package com.example.dec23_carpool.model.googleMapConnectionRepository;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class GoogleFirebaseCloudMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String t) {
        FirebaseInstanceId
                .getInstance()
                .getInstanceId()
                .addOnSuccessListener(instanceIdResult -> {
                    String id = instanceIdResult.getId();
                    String token = instanceIdResult.getToken();
                });
    }

    @Override
    public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
    }
}
