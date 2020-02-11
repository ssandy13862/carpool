package com.example.dec23_carpool.view.order;

import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.presenter.GoogleMapConnectPresenter;
import com.example.dec23_carpool.presenter.GoogleMapConnectPresenter.MapConnectionView;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.Order;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;


import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class GoogleMapConnectionActivity extends FragmentActivity implements
        OnMapReadyCallback, MapConnectionView {

    private GoogleMap mMap;
    private SearchView departureSearch, arriveSearch;
    private AutoCompleteTextView departureAutoText, arriveAutoText;
    private LatLng departureLatLng, arriveLatLng;
    private static final int REQUEST_LOCATION = 3;
    private String mapKey;
    private Address departurePlace, arrivePlace;
    private static float CAMERA_ZOOM = 11;
    private GoogleMapConnectPresenter googleMapConnectPresenter;
    private MarkerOptions departureMarker;
    private User user;
    private boolean isSearchTextSubmit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SharedPreferences sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        user = (User) getIntent().getSerializableExtra("LOGIN_USER");
        findViews();
        getMyLocationPermission();
    }

    private void findViews() {
        departureSearch = findViewById(R.id.passengerLocationSearch);
        arriveSearch = findViewById(R.id.destinationAddressSearch);
        departureAutoText = findViewById(R.id.passengerLocationAuto);
        arriveAutoText = findViewById(R.id.destinationAddressAuto);
    }

    private void getMyLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
            init();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setMapUtils();
        setInitLocation();
        setPassengerLocation();
        setDestinationAddress();
    }

    private void setMapUtils() {
        mapKey = getString(R.string.my_map_key);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        departureSearch.setSubmitButtonEnabled(true);
        arriveSearch.setSubmitButtonEnabled(true);
    }

    private void setInitLocation() {
        FusedLocationProviderClient client = LocationServices
                .getFusedLocationProviderClient(this);
        client.getLastLocation().addOnSuccessListener(this::onSuccess);
    }

    private void onSuccess(Location location) {
        LatLng TaoYuanTrainStation = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(TaoYuanTrainStation, CAMERA_ZOOM));
    }

    private void setPassengerLocation() {
        departureSearch.setOnQueryTextListener(departureTextListener);
    }


    private OnQueryTextListener departureTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            mMap.clear();
            isSearchTextSubmit = true;
            //todo: autoSearch has bug
            departureAutoText.dismissDropDown();
            List<Address> addressList = getAddressList(query);
            if (!addressList.isEmpty()) {
                departurePlace = addressList.get(0);
                departureLatLng = configLatLng(addressList);
                departureMarker = configMarker(departureLatLng, query, false);
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(departureLatLng, CAMERA_ZOOM));
                mMap.addMarker(departureMarker);
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!autoCompletelySearchText.equals(newText)) {
                List<Address> addressList = getAddressList(newText);
                if (!addressList.isEmpty()) {
                    departureLatLng = configLatLng(addressList);
                    //todo: autoSearch has bug
                    googleMapConnectPresenter
                            .autoCompletelySearch(true, newText,
                                    departureLatLng, GoogleMapConnectionActivity.this.mapKey);
                    isSearchTextSubmit = false;
                }
            }
            return false;
        }
    };

    private OnQueryTextListener destinationTextListener = new OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            isSearchTextSubmit = true;
            //todo: autoSearch has bug
            arriveAutoText.dismissDropDown();
            List<Address> addressList = getAddressList(query);
            if (!addressList.isEmpty()) {
                arrivePlace = addressList.get(0);
                arriveLatLng = configLatLng(addressList);
                if (departureLatLng != null) {
                    if (departureMarker != null) {
                        MarkerOptions markerOptions = departureMarker;
                        mMap.clear();
                        mMap.addMarker(markerOptions);
                        MarkerOptions arriveMarker = configMarker(
                                arriveLatLng, query, true);
                        mMap.addMarker(arriveMarker);
                        googleMapConnectPresenter
                                .routing(departureLatLng, arriveLatLng,
                                        GoogleMapConnectionActivity.this.mapKey);
                    }
                }
            }
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (!autoCompletelySearchText.equals(newText)) {
                List<Address> addressList = getAddressList(newText);
                if (!addressList.isEmpty()) {
                    arriveLatLng = configLatLng(addressList);
                    //todo: autoSearch has bug
                    googleMapConnectPresenter
                            .autoCompletelySearch(false, newText,
                                    arriveLatLng, GoogleMapConnectionActivity.this.mapKey);
                    isSearchTextSubmit = false;
                }
            }
            return false;
        }
    };

    private void setDestinationAddress() {
        arriveSearch.setOnQueryTextListener(destinationTextListener);
    }

    private List<Address> getAddressList(String placeName) {
        List<Address> addressList = new LinkedList<>();
        try {
            addressList.addAll(new Geocoder(
                    GoogleMapConnectionActivity.this)
                    .getFromLocationName(placeName, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList;
    }

    private LatLng configLatLng(List<Address> addressList) {
        Address address = addressList.get(0);
        return new LatLng(
                address.getLatitude(),
                address.getLongitude());
    }

    private MarkerOptions configMarker(LatLng latLng, String title, boolean setMarkerColor) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title(title);
        if (setMarkerColor) {
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
        }
        return markerOptions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                init();
            }
        }
    }

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        googleMapConnectPresenter = new GoogleMapConnectPresenter(this,
                Global.googleMapDirectionsRepository(),
                Global.threadExecutor());
    }

    public void onMapNextStepClick(View view) {

        String departureName = departureSearch.getQuery().toString();
        String arriveName = arriveSearch.getQuery().toString();
        if (order != null && !departureName.isEmpty() && !arriveName.isEmpty()) {

            order.setDepartureName(departurePlace.getFeatureName());
            order.setArrivePlaceName(arrivePlace.getFeatureName());

            startActivity(
                    new Intent(this, PlanTimeActivity.class)
                            .putExtra("PLAN_MAP_USER", user)
                            .putExtra("PLAN_MAP_ORDER", order)
                            .putExtra("DepartureName",departureName));
        } else {
            Toast.makeText(this, "data can't be null", Toast.LENGTH_LONG).show();
        }
    }

// ----------------------------------------------------------------------------

    private Order order;

    @Override
    public void OnRoutingSuccessfully(String distance,
                                      String costTime,
                                      List<LatLng> path) {
        mMap.addPolyline(new PolylineOptions()
                .addAll(path)
                .width(13)
                .color(Color.rgb(105, 152, 242))
                .geodesic(true));
        order = new Order(departureLatLng.latitude, departureLatLng.longitude,
                arriveLatLng.latitude, arriveLatLng.longitude, distance, costTime);
        order.setDepartureAddress(departurePlace.getAddressLine(0));
        order.setArrivePlaceAddress(arrivePlace.getAddressLine(0));
    }

    private String autoCompletelySearchText = "";

    @Override
    public void OnAutoCompletelySearchSuccessfully(boolean isDepartureSearch,
                                                   List<String> placeNameList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                placeNameList);
        if (!isSearchTextSubmit) {
            if (isDepartureSearch) {
                setAutoSearchAdatper(departureAutoText, arrayAdapter,
                        this::onPassengerSearchItemClick);

            } else {
                setAutoSearchAdatper(arriveAutoText, arrayAdapter,
                        this::onDestinationSearchItemClick);
            }
        }
    }

    private void onPassengerSearchItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
        departureAutoText.dismissDropDown();
        autoCompletelySearchText = departureAutoText
                .getAdapter()
                .getItem(position)
                .toString();
        departureSearch.setQuery(autoCompletelySearchText, true);
    }

    private void onDestinationSearchItemClick(AdapterView<?> parent, View view,
                                              int position, long id) {
        arriveAutoText.dismissDropDown();
        autoCompletelySearchText = arriveAutoText
                .getAdapter()
                .getItem(position)
                .toString();
        arriveSearch.setQuery(autoCompletelySearchText, true);
    }

    private void setAutoSearchAdatper(AutoCompleteTextView autoCompleteTextView,
                                      ArrayAdapter arrayAdapter,
                                      OnItemClickListener onItemClickListener) {
        autoCompleteTextView.setAdapter(arrayAdapter);
        autoCompleteTextView.showDropDown();
        autoCompleteTextView.setOnItemClickListener(onItemClickListener);
    }

}