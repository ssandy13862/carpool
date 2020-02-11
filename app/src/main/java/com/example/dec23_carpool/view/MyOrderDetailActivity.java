package com.example.dec23_carpool.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.PeopleLocation;
import com.example.dec23_carpool.presenter.MyOrderDetailPresenter;
import com.example.dec23_carpool.util.Global;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MyOrderDetailActivity extends AppCompatActivity implements OnMapReadyCallback, MyOrderDetailPresenter.MyOrderDetailView {

    private TextView tvDepartureDate, tvDepartureTime, tvArriveTime, tvCostTime, tvScore,
            tvDriverName, tvDriverGender, tvCarfare, tvDeparturePlace, tvArrivePlace, tvDepartureAddress, tvArriveAddress;
    private ImageView ivIsEat, ivIsSmoke, ivIsPet, ivIsLuggage, ivLocation, ivDriverPhoto;
    private Order myOrder;
    private ConstraintLayout driverLayout;
    private SharedPreferences sp;
    private String token, email;
    private int userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder_detail);

        myOrder = (Order) getIntent().getSerializableExtra("MYORDER_DETAIL");
        sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        userId = sp.getInt(Global.USER_ID, -1);
        token = sp.getString(Global.USER_TOKEN, "");
        email = sp.getString(Global.USER_EMAIL, "");

        findView();
        setUserData(myOrder);
        setTravelState();
        myOrderDetailPresenter = new MyOrderDetailPresenter
                (Global.threadExecutor(), MyOrderDetailActivity.this,
                        Global.locationRepostiory());

        switch (userId) {
            case Global.PASSENGER:
                setDataForPassenger(myOrder);
                break;
            case Global.DRIVER:
                break;
        }
        dialog = setMapDialog();
    }

    private void findView() {
        tvDepartureDate = findViewById(R.id.myorder_detail_tv_departure_date);
        tvDepartureTime = findViewById(R.id.myorder_detail_tv_departure_time);
        tvDeparturePlace = findViewById(R.id.myorder_detail_tv_departure_place);
        tvArrivePlace = findViewById(R.id.myorder_detail_tv_arrive_place);
        tvArriveTime = findViewById(R.id.myorder_detail_tv_arrive_time);
        tvCostTime = findViewById(R.id.myorder_detail_tv_costTime);
        tvScore = findViewById(R.id.myorder_detail_tv_score);
        tvDriverName = findViewById(R.id.myorder_detail_tv_driver_name);
        tvDriverGender = findViewById(R.id.myorder_detail_tv_driver_gender);
        tvCarfare = findViewById(R.id.myorder_detail_tv_carfare);
        tvDepartureAddress = findViewById(R.id.myorder_detail_tv_departure_address);
        tvArriveAddress = findViewById(R.id.myorder_detail_tv_arrive_address);
        ivIsEat = findViewById(R.id.myorder_detail_iv_isEat);
        ivIsSmoke = findViewById(R.id.myorder_detail_iv_isSmoke);
        ivIsPet = findViewById(R.id.myorder_detail_iv_isPet);
        ivIsLuggage = findViewById(R.id.myorder_detail_iv_isLuggage);
        ivLocation = findViewById(R.id.mymyorder_detail_iv_location);
        ivDriverPhoto = findViewById(R.id.myOrder_detail_driverPhoto);
        driverLayout = findViewById(R.id.myorder_layout2);
    }

    private void setTravelState() {
        switch (myOrder.getTravelState()) {
            case Global.NOTYET:
            case Global.END:
                ivLocation.setImageDrawable(getDrawable(R.drawable.ic_grey_marker));
                break;
            case Global.IN_PROGRESS:
                break;
        }
    }

    private void setDataForPassenger(Order order) {
        tvDriverGender.setText(String.valueOf(order.getDriver().getGender()));
        tvDriverName.setText(order.getDriver().getNickname());
        tvScore.setText(String.format("%.01f", order.getDriver().getScore()));
        driverLayout.setVisibility(View.GONE);
//        todo Email needs driver's email from backend
//        Map<String, String> queryMap = new HashMap<>();
//        queryMap.put("email", order.getEmail());
//        queryMap.put("pictureType", "Picture");
//        GlideUrl glideUrl = GlideUtils.configUserPhotoUrl(token, queryMap);
//        Glide.with(this)
//                .asBitmap()
//                .load(glideUrl)
//                .circleCrop()
//                .placeholder(R.drawable.people)
//                .error(R.drawable.people)
//                .into(ivDriverPhoto);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void setUserData(Order order) {

        tvDepartureDate.setText(order.getStrDepartureDate());
        tvDepartureTime.setText(order.getStrDepartureTime());
        tvDeparturePlace.setText(order.getDepartureName());
        tvDepartureAddress.setText(order.getDepartureAddress());

        tvArriveTime.setText(order.getStrArriveTime());
        tvArrivePlace.setText(order.getArrivePlaceName());
        tvArriveAddress.setText(order.getArrivePlaceAddress());

        tvCostTime.setText("carpool " + order.getCostTime());
        tvCarfare.setText(String.format("%s", "$" + order.getCarfare()));

        if (!order.isEat()) {
            ivIsEat.setImageDrawable(getDrawable(R.drawable.ic_no_eat));
        }
        if (!order.isPet()) {
            ivIsPet.setImageDrawable(getDrawable(R.drawable.ic_no_pet));
        }
        if (!order.isSmoke()) {
            ivIsSmoke.setImageDrawable(getDrawable(R.drawable.ic_no_smoke));
        }
        if (!order.isLuggage()) {
            ivIsLuggage.setImageDrawable(getDrawable(R.drawable.ic_no_luggage));
        }
    }

    public void onCancelClick(View view) {
        this.finish();
    }

// show google map ---------------------------------------------------------------

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;
    private static float CAMERA_ZOOM = 11;
    private LocationManager myLocationManager;
    private GoogleMap gpsMap;
    private Dialog dialog;
    private TextView tvCounter;
    private LatLng myLocationLatLng;
    private MyOrderDetailPresenter myOrderDetailPresenter;
    private Button btnEndOrderConFirm;
    private Button btnEndOrderConFirmGrey;
    private static final int REQUEST_LOCATION = 3;


    public void onMarkerClick(View view) {
        if (myOrder.getTravelState() == Global.IN_PROGRESS || myOrder.getTravelState() == Global.NOTYET) {
            myOrderDetailPresenter.setLocationThreadStart();
            getCurrentLocation();
            dialog.show();
            setCounter();
            getLocation();
        }
    }

    private Dialog setMapDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_map_and_counter, null);
        final Dialog contentDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(true)
                .create();
        findView(dialogView);
        getMyLocationPermission();
        return contentDialog;
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

    private void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.gps_map);
        mapFragment.getMapAsync(this);
    }

    private void findView(View dialogView) {
        tvCounter = dialogView.findViewById(R.id.dialog_counter);
        btnEndOrderConFirm = dialogView.findViewById(R.id.dialog_confirm_end_order);
        btnEndOrderConFirmGrey = dialogView.findViewById(R.id.dialog_confirm_end_order_grey);
    }

    private LocationListener myLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                myLocationLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                setInitLocation();
                sendLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    private void getCurrentLocation() {
        boolean isGPSEnabled = myLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = myLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (isNetworkEnabled) {
            myLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, myLocationListener);
        }
        if (isGPSEnabled) {
            myLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, myLocationListener);
        }
    }

    private void drawMarker(double lat, double lng, Drawable iconDrawable) {
        assert iconDrawable != null;
        if (gpsMap != null) {
            LatLng gps = new LatLng(lat, lng);
            gpsMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .title("Current Position")
// .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    .icon((getMarkerIconFromDrawable(iconDrawable))));

        }
    }

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setMapUtils();
        mapInit();
    }

    private void mapInit() {
        myLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void setMapUtils() {
        gpsMap.setMyLocationEnabled(true);
        gpsMap.getUiSettings().setMyLocationButtonEnabled(true);
        gpsMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void setInitLocation() {
        if (myLocationLatLng != null) {
            gpsMap.animateCamera(CameraUpdateFactory
                    .newLatLngZoom(myLocationLatLng, CAMERA_ZOOM));
        }
    }

    public void onCancelInGpsViewClick(View view) {
        dialog.dismiss();
        dialog.setOnDismissListener(dialog -> myOrderDetailPresenter.setLocationThreadStop());
        myOrderDetailPresenter.setLocationThreadStop();
    }

//in 10 minutes to the appointed time ---------------------------------------------------------------

    private void sendLocation(Location location) {
        long time = myOrder.getLongDepartureDate() * 1000;
        int id = myOrder.getId();
        PeopleLocation passengerLocation = new PeopleLocation(email, location.getLatitude(), location.getLongitude());
        PeopleLocation driverLocation = new PeopleLocation(email, id, location.getLatitude(), location.getLongitude());
        switch (userId) {
            case Global.PASSENGER:
                myOrderDetailPresenter.sendPassengerLocation(token, passengerLocation);
                break;
            case Global.DRIVER:
                myOrderDetailPresenter.sendDriverLocation(token, driverLocation);
                break;
        }
    }

    private void getLocation() {
        int id = myOrder.getId();
        //
        long departureTime = myOrder.getLongDepartureDate() * 1000;
        long timeNow = System.currentTimeMillis();
        boolean getLocationTimingCorrectly;
//        do {
//            // 訂單倒數10分鐘條件式為false , 才可得到對方座標
//            // 未測試
//            getLocationTimingCorrectly = (departureTime - timeNow) >= 600000;
//        } while (getLocationTimingCorrectly);
//        //
        switch (userId) {
            case Global.PASSENGER:
                myOrderDetailPresenter.getDriverLocation(token, email, id);
                break;
            case Global.DRIVER:
                myOrderDetailPresenter.getPassengerLocation(token, email, id);
                break;
        }
    }

//timer counter ---------------------------------------------------------

    private void setCounter() {
        myOrderDetailPresenter.setCounter(userId, myOrder.getLongDepartureDate());
    }

    public void onConfirmEndOrderClick(View view) {
        switch (userId) {
            case Global.DRIVER:
                myOrderDetailPresenter.finishOrder(token, new PeopleLocation(email, myOrder.getId(), myLocationLatLng.latitude, myLocationLatLng.longitude));
                startActivity(new Intent(this, OrderFinishActivity.class));
                break;
            case Global.PASSENGER:
                //跳轉完成旅程頁面+評價
                startActivity(new Intent(this, OrderFinishActivity.class));
                break;
        }
    }

//--------------------------------------------------------------------------------------------

    @Override
    public void onSendDriverLocationSuccessfully() {

    }

    @Override
    public void onSendDriverLocationFailed() {

    }

    @Override
    public void onSendPassengerLocationSuccessfully() {

    }

    @Override
    public void onGetDriverLocationSuccessfully(PeopleLocation peopleLocation) {
        gpsMap.clear();
        drawMarker(peopleLocation.getDriverLat(),
                peopleLocation.getDriverLng(),
                getDrawable(R.drawable.ic_car));
        getLocationAfterFewSeconds(3);
    }

    @Override
    public void onGetPassengerLocationSuccessfully(List<PeopleLocation> peopleLocationList) {
        gpsMap.clear();
        for (PeopleLocation peopleLocation : peopleLocationList) {
            drawMarker(peopleLocation.getLat(),
                    peopleLocation.getLng(),
                    getDrawable(R.drawable.ic_passenger));
        }
        getLocationAfterFewSeconds(3);
    }

    private void getLocationAfterFewSeconds(int seconds) {
        // 延遲N秒 輪詢
        Global.threadExecutor().execute(() -> {
            try {
                Thread.sleep(seconds * 1000);
                getLocation();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onGetDriverLocationFailed() {
        gpsMap.clear();
    }

    @Override
    public void onGetPassengerLocationFailed() {
        gpsMap.clear();
    }

    @Override
    public void onConfirmOrderSuccessfully() {
        if (userId == Global.PASSENGER) {
            btnEndOrderConFirmGrey.setVisibility(View.GONE);
            btnEndOrderConFirm.setVisibility(View.VISIBLE);
            btnEndOrderConFirm.setClickable(true);
//            btnEndOrderConFirm.setBackground(getDrawable(R.drawable.radius_green_button));
            btnEndOrderConFirm.setOnClickListener(this::onConfirmEndOrderClick);
        }
    }

    @Override
    public void onDriverConfirmButtomClickable() {
        btnEndOrderConFirmGrey.setVisibility(View.GONE);
        btnEndOrderConFirm.setVisibility(View.VISIBLE);
//        btnEndOrderConFirm.setBackground(getDrawable(R.drawable.radius_green_button));
//        btnEndOrderConFirm.setBackgroundResource(R.drawable.radius_green_button);
        btnEndOrderConFirm.setClickable(true);
        btnEndOrderConFirm.setOnClickListener(this::onConfirmEndOrderClick);
    }

    @Override
    public void onTimeCounter(String time) {
        tvCounter.setText(time);
    }


}

