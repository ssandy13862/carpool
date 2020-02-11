//package com.example.dec23_carpool.view.fragmentAll;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.app.ActivityCompat;
//import androidx.fragment.app.Fragment;
//
//import com.example.dec23_carpool.R;
//import com.example.dec23_carpool.presenter.GoogleMapConnectPresenter;
//import com.example.dec23_carpool.util.Global;
//import com.example.dec23_carpool.object.Order;
//import com.example.dec23_carpool.view.order.GoogleMapConnectionActivity;
//import com.example.dec23_carpool.view.order.PlanTimeActivity;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import java.io.IOException;
//import java.util.List;
//
//public class AddPlanFragment extends Fragment implements OnMapReadyCallback, GoogleMapConnectPresenter.MapConnectionView{
//
//    private GoogleMap mMap;
//    private SearchView departureSearch, arriveSearch;
//    private AutoCompleteTextView departureAutoText, arriveAutoText;
//    private LatLng departureLatLng, arriveLatLng;
//    private static final int REQUEST_LOCATION = 3;
//    private String mapKey;
//    private Address departurePlace, arrivePlace;
//    private static float CAMERA_ZOOM = 11;
//    private GoogleMapConnectPresenter googleMapConnectPresenter;
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_add_plan, container, false);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState){
//        super.onViewCreated(view, savedInstanceState);
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        findViews(view);
//        getMyLocationPermission();
//    }
//
//    private void findViews(View view) {
//        departureSearch = view.findViewById(R.id.passengerLocationSearch);
//        arriveSearch = view.findViewById(R.id.destinationAddressSearch);
//        departureAutoText = view.findViewById(R.id.passengerLocationAuto);
//        arriveAutoText = view.findViewById(R.id.destinationAddressAuto);
//    }
//
//    private void getMyLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(getActivity(),
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(getActivity(),
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_LOCATION);
//        } else {
//            init();
//        }
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        setMapUtils();
//        setInitLocation();
//        setPassengerLocation();
//        setDestinationAddress();
//    }
//
//    private void setMapUtils() {
//        mapKey = getString(R.string.my_map_key);
//        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setZoomControlsEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(true);
//        departureSearch.setSubmitButtonEnabled(true);
//        arriveSearch.setSubmitButtonEnabled(true);
//    }
//
//    private void setInitLocation() {
//        LatLng TaoYuanTrainStation = new LatLng(24.9891959, 121.3112828);
//        mMap.animateCamera(CameraUpdateFactory
//                .newLatLngZoom(TaoYuanTrainStation, CAMERA_ZOOM));
//    }
//
//    private void setPassengerLocation() {
//
//        departureSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            List<Address> addressList;
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                try {
//                    mMap.clear();
//                    addressList = new Geocoder(
//                            getActivity())
//                            .getFromLocationName(query, 1);
//                    if (!addressList.isEmpty()) {
//                        departurePlace = addressList.get(0);
//                        departureLatLng = new LatLng(departurePlace.getLatitude(),
//                                departurePlace.getLongitude());
//                        mMap.addMarker(new MarkerOptions()
//                                .position(departureLatLng)
//                                .title(query));
//
//                        mMap.animateCamera(CameraUpdateFactory
//                                .newLatLngZoom(departureLatLng, CAMERA_ZOOM));
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                try {
//                    if (!autoCompletelySearchText.equals(newText)) {
//                        addressList = new Geocoder(
//                                getActivity())
//                                .getFromLocationName(newText, 1);
//                        if (!addressList.isEmpty()) {
//                            departurePlace = addressList.get(0);
//                            departureLatLng = new LatLng(departurePlace.getLatitude(),
//                                    departurePlace.getLongitude());
//                            googleMapConnectPresenter.autoCompletelySearch(
//                                    true, newText,
//                                    departureLatLng,
//                                    mapKey);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        });
//    }
//
//    private void setDestinationAddress() {
//        arriveSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            List<Address> addressList;
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                //LatLng就是Latitude 與Longitude，也就是座標的經緯度。
//                try {
//                    addressList = new Geocoder(
//                            getActivity())
//                            .getFromLocationName(query, 1);
//                    if (!addressList.isEmpty()) {
//                        arrivePlace = addressList.get(0);
//                        arriveLatLng = new LatLng(arrivePlace.getLatitude(),
//                                arrivePlace.getLongitude());
//                        if (departureLatLng != null) {
//                            googleMapConnectPresenter.routing(departureLatLng,
//                                    arriveLatLng,
//                                    mapKey);
//
//                            mMap.addMarker(new MarkerOptions()
//                                    .position(arriveLatLng)
//                                    .title(query)
//                                    .icon(BitmapDescriptorFactory
//                                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
//                        }
//                    }
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                try {
//                    if (!autoCompletelySearchText.equals(newText)) {
//                        addressList = new Geocoder(
//                               getActivity())
//                                .getFromLocationName(newText, 1);
//                        if (!addressList.isEmpty()) {
//                            arrivePlace = addressList.get(0);
//                            arriveLatLng = new LatLng(arrivePlace.getLatitude(),
//                                    arrivePlace.getLongitude());
//                            googleMapConnectPresenter.autoCompletelySearch(
//                                    false, newText,
//                                    arriveLatLng,
//                                    mapKey);
//                        }
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                return false;
//            }
//        });
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_LOCATION) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                init();
//            }
//        }
//    }
//
//    private void init() {
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
//                .findFragmentById(R.id.google_map);
//        mapFragment.getMapAsync(this);
//        googleMapConnectPresenter = new GoogleMapConnectPresenter(this,
//                Global.googleMapDirectionsRepository(),
//                Global.threadExecutor());
//    }
//
//    public void onMapNextStepClick(View view) {
//
//        //todo 開始 抵達 polyline
//        // 公里數 車程花費時間
//
//        if(order != null || departureSearch.getQuery().toString().isEmpty() || arriveSearch.getQuery().toString().isEmpty()){
//
//            order.setDepartureName(departurePlace.getFeatureName());
//            order.setArrivePlaceName(arrivePlace.getFeatureName());
//            startActivity(new Intent(getActivity(), PlanTimeActivity.class)
//                    .putExtra("PLAN_MAP_ORDER", order));
//        }else {
//            Toast.makeText(getActivity(), "data can't be null", Toast.LENGTH_LONG).show();
//        }
//    }
//
//
//
////    ----------------------------------------------------------------------------
//
//    private Order order;
//
//    @Override
//    public void OnRoutingSuccessfully(List<LatLng> path) {
//        //當重新查詢時清空
//        mMap.clear();
//        LatLng latLng1 = path.get(0);
//        LatLng latLng2 = path.get(1);
//
//        mMap.addPolyline(new PolylineOptions()
//                .addAll(path)
//                .width(13)
//                .color(Color.rgb(105, 152, 242))
//                .geodesic(true));
//
//        order = new Order(latLng1.latitude, latLng1.longitude, latLng2.latitude, latLng2.longitude);
//
//    }
//
//    private String autoCompletelySearchText = "";
//
//    @Override
//    public void OnAutoCompletelySearchSuccessfully(boolean isPassengerLocationSearch,
//                                                   List<String> placeNameList) {
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1,
//                placeNameList);
//        if (isPassengerLocationSearch) {
//            departureAutoText.setAdapter(arrayAdapter);
//            departureAutoText.showDropDown();
//            departureAutoText.setOnItemClickListener((parent, view, position, id) -> {
//                departureAutoText.dismissDropDown();
//                autoCompletelySearchText = departureAutoText
//                        .getAdapter()
//                        .getItem(position)
//                        .toString();
//                departureSearch.setQuery(autoCompletelySearchText, true);
//            });
//
//        } else {
//            arriveAutoText.setAdapter(arrayAdapter);
//            arriveAutoText.showDropDown();
//            arriveAutoText.setOnItemClickListener((parent, view, position, id) -> {
//                arriveAutoText.dismissDropDown();
//                autoCompletelySearchText = arriveAutoText
//                        .getAdapter()
//                        .getItem(position)
//                        .toString();
//                arriveSearch.setQuery(autoCompletelySearchText, true);
//            });
//        }
//    }
//}
