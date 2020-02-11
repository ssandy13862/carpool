package com.example.dec23_carpool.presenter;


import com.example.dec23_carpool.model.googleMapConnectionRepository.GoogleMapRepository;
import com.example.dec23_carpool.util.RoutingPath;
import com.example.dec23_carpool.util.RoutingPath.Route.Legs;
import com.example.dec23_carpool.util.RoutingPath.Route.Legs.Steps;
import com.example.dec23_carpool.util.ThreadExecutor;
import com.google.android.gms.maps.model.LatLng;

import java.util.LinkedList;
import java.util.List;

public class GoogleMapConnectPresenter {

    private MapConnectionView mapConnectionView;
    private GoogleMapRepository googleMapRepository;
    private ThreadExecutor threadExecutor;

    public GoogleMapConnectPresenter(MapConnectionView mapConnectionView,
                                     GoogleMapRepository googleMapRepository,
                                     ThreadExecutor threadExecutor) {
        this.mapConnectionView = mapConnectionView;
        this.googleMapRepository = googleMapRepository;
        this.threadExecutor = threadExecutor;
    }

    public void routing(LatLng passengerLatLng,
                        LatLng destinationAddressLatLng, String key) {
        threadExecutor.execute(() -> {
            RoutingPath routingPath = googleMapRepository
                    .routing(passengerLatLng, destinationAddressLatLng, key);

            String distance = getLegs(routingPath).getDistance().getText();
            String duration = getLegs(routingPath).getDuration().getText();
            List<LatLng> path = getPath(routingPath);
            threadExecutor.executeUiThread(() -> mapConnectionView
                    .OnRoutingSuccessfully(distance, duration, path));
        });
    }

    private Legs getLegs(RoutingPath routingPath) {
        return routingPath
                .getRoutes().get(0)
                .getLegs().get(0);
    }

    private List<LatLng> getPath(RoutingPath routingPath) {
        return getPolylinePoints(routingPath);
    }

    private List<LatLng> getPolylinePoints(RoutingPath routingPath) {
        List<LatLng> latLngList = new LinkedList<>();
        List<Steps> stepsList = routingPath
                .getRoutes()
                .get(0)
                .getLegs()
                .get(0)
                .getSteps();

        for (Steps steps : stepsList) {
            String points = steps.getPolyline().getPoints();
            List<LatLng> polylinePoints = decodePoly(points);
            latLngList.addAll(polylinePoints);
        }
        return latLngList;
    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new LinkedList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public void autoCompletelySearch(boolean isPassengerLocationSearch,
                                     String placeKeyword, LatLng placeLatLng, String key) {
        threadExecutor.execute(() -> {
            List<String> placeNameList = googleMapRepository
                    .autoCompletelySearch(placeKeyword, placeLatLng, key);
            threadExecutor.executeUiThread(() -> mapConnectionView
                    .OnAutoCompletelySearchSuccessfully(
                            isPassengerLocationSearch, placeNameList));
        });
    }

    public interface MapConnectionView {

        void OnRoutingSuccessfully(String distance, String duration, List<LatLng> path);

        void OnAutoCompletelySearchSuccessfully(boolean isPassengerLocationSearch, List<String> placeNameList);

    }
}
