package com.example.dec23_carpool.model.googleMapConnectionRepository;

import com.example.dec23_carpool.util.RoutingPath;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface GoogleMapRepository {

    RoutingPath routing(LatLng passengerLatLng,
                        LatLng destinationAddressLatLng,
                        String key);

    List<String> autoCompletelySearch(String placeKeyword,
                                      LatLng placeLatLng, String key);
}
