package com.example.dec23_carpool.model.googleMapConnectionRepository;

import com.example.dec23_carpool.model.carPoolExcepton.PlacesSuggestionNotFoundException;
import com.example.dec23_carpool.model.carPoolExcepton.RoutingPathNotFoundException;
import com.example.dec23_carpool.util.PlacesAutoCompleteAddress;
import com.example.dec23_carpool.util.PlacesAutoCompleteAddress.Predictions;
import com.example.dec23_carpool.util.RoutingPath;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class GoogleMapApiConnectionRepository implements GoogleMapRepository {

    private GoogleMapDirectionsAPI googleMapDirectionsAPI;

    private GoogleMapPlacesAPI googleMapPlacesAPI;


    public GoogleMapApiConnectionRepository() {
        String directionsBaseURL = "https://maps.googleapis.com/maps/api/directions/";
        String placesAutoCompletelyBaseURL = "https://maps.googleapis.com/maps/api/place/autocomplete/";
        googleMapDirectionsAPI = new Retrofit.Builder()
                .baseUrl(directionsBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(GoogleMapDirectionsAPI.class);
        googleMapPlacesAPI = new Retrofit.Builder()
                .baseUrl(placesAutoCompletelyBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(GoogleMapPlacesAPI.class);
    }

    private interface GoogleMapDirectionsAPI {
        @GET("json")
        Call<RoutingPath>
        routing(@Query("origin") String passengerLatLng,
                @Query("destination") String destinationAddressLatLng,
                @Query("key") String key,
                @Query("sensor") String sensor,
                @Query("mode") String mode);
    }

    private interface GoogleMapPlacesAPI {
        @GET("json")
        Call<PlacesAutoCompleteAddress>
        placesAutoComplete(@Query("input") String keyword,
                           @Query("types") String types,
                           @Query(("location")) String placeLatLng,
                           @Query("radius") int radiusMeters,
                           @Query("strictbounds") String strictbounds,
                           @Query("key") String key);

    }

    @Override
    public RoutingPath routing(LatLng passengerLatLng,
                               LatLng destinationAddressLatLng,
                               String key) {
        Call<RoutingPath> routingPathCall = googleMapDirectionsAPI
                .routing(getStringLatLng(passengerLatLng),
                        getStringLatLng(destinationAddressLatLng),
                        key, "false", "driving");
        RoutingPath routingPath = null;
        try {
            Response<RoutingPath> routingPathResponse = routingPathCall.execute();
            routingPath = routingPathResponse.body();
            if (routingPath == null) {
                throw new RoutingPathNotFoundException();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return routingPath;
    }

    @Override
    public List<String> autoCompletelySearch(String placeKeyword, LatLng placeLatLng, String key) {
        Call<PlacesAutoCompleteAddress> placesAutoCompleteAddressCall = googleMapPlacesAPI
                .placesAutoComplete(placeKeyword,
                        "establishment", getStringLatLng(placeLatLng),
                        300, "strictbounds", key);
        List<String> placeNameList = new LinkedList<>();
        try {
            Response<PlacesAutoCompleteAddress> placesAutoCompleteAddressResponse =
                    placesAutoCompleteAddressCall.execute();
            PlacesAutoCompleteAddress placesAutoCompleteAddress =
                    placesAutoCompleteAddressResponse.body();
            if (placesAutoCompleteAddress == null) {
                throw new PlacesSuggestionNotFoundException();
            }
            for (Predictions prediction : placesAutoCompleteAddress.getPredictions()) {
                String placeName = prediction.getStructured_formatting().getMain_text();
                placeNameList.add(placeName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return placeNameList;
    }

    private String getStringLatLng(LatLng latLng) {
        return latLng.latitude + "," + latLng.longitude;
    }


}
