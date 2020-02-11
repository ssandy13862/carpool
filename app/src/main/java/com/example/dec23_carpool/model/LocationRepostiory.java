package com.example.dec23_carpool.model;

import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.PeopleLocation;

import java.util.List;

public interface LocationRepostiory {

    void sendDriverLocation(String token, PeopleLocation peopleLocation);

    void sendPassengerLocation(String token, PeopleLocation peopleLocation);

    PeopleLocation getDriverLocation(String token, String email, int travelId);

    List<PeopleLocation> getPassengerLocation(String token, String email, int travelId);

    void finishOrder(String token, PeopleLocation peopleLocation);

}
