package com.example.dec23_carpool.view.fragmentAll;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.presenter.OrderReadPresenter;
import com.example.dec23_carpool.util.CustomRecycleViewAdapter;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.Order;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment implements OrderReadPresenter.OrderView {

    private List<Order> orderArrayList;
    private OrderReadPresenter orderReadPresenter;
    private CustomRecycleViewAdapter adapter;
    private EditText edKeywordDepart, edKeywordArrive;
    private RecyclerView recyclerView;
    private SharedPreferences sp;
    private String token;
    private String email;
    private ProgressBar orderProgessBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
        readOrders();
        setRecycleView();
        onSearchByEnterClick();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
// user = ((MainActivity)context).getUser();
        sp = context.getSharedPreferences(Global.USER, Context.MODE_PRIVATE);
    }

    private void findViews(View view) {
        edKeywordDepart = view.findViewById(R.id.order_et_keyword1);
        edKeywordArrive = view.findViewById(R.id.order_et_keyword2);
        recyclerView = view.findViewById(R.id.rv_list);
        orderProgessBar = view.findViewById(R.id.order_progessBar);
    }

    private void init() {
        orderArrayList = new ArrayList<>();
        orderReadPresenter = new OrderReadPresenter(Global.orderRepository(),
                this, Global.threadExecutor());
        token = sp.getString(Global.USER_TOKEN, "");
        email = sp.getString(Global.USER_EMAIL, "");
    }


    private void setRecycleView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new CustomRecycleViewAdapter(this, orderArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private Address getLatLng(String place) {
        List<Address> addressList = new ArrayList<>();
        try {
            addressList.addAll(new Geocoder(getContext())
                    .getFromLocationName(place, 1));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressList.get(0);
    }

    private void readOrders() {
        orderProgessBar.setVisibility(View.VISIBLE);
        //訪客讀訂單（顯示已滿+未滿訂單）
        if (sp == null) {
            orderReadPresenter.readOrders();
        } else {
        //會員讀訂單 (顯示未滿訂單)
            orderReadPresenter.readOrders(
                    sp.getString(Global.USER_TOKEN, ""),
                    sp.getString(Global.USER_EMAIL, ""));
        }
    }

    private void onSearchByEnterClick() {
        edKeywordDepart.setOnKeyListener(this::onKey);
        edKeywordArrive.setOnKeyListener(this::onKey);
    }

    private boolean onKey(View v, int keyCode, KeyEvent event) {
        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
            if (!edKeywordDepart.getText().toString().isEmpty() && !edKeywordArrive.getText().toString().isEmpty()) {
                Address departureAddress = getLatLng(edKeywordDepart.getText().toString());
                Address arriveAddress = getLatLng(edKeywordArrive.getText().toString());
                departureAndArriveSearch(token, email, departureAddress, arriveAddress);
            } else {
                if (!edKeywordDepart.getText().toString().isEmpty()) {
                    Address departureAddress = getLatLng(edKeywordDepart.getText().toString());
                    departureSearch(token, email, departureAddress);
                }
                if (!edKeywordArrive.getText().toString().isEmpty()) {
                    Address arriveAddress = getLatLng(edKeywordArrive.getText().toString());
                    arriveSearch(token, email, arriveAddress);
                }
            }
            return true;
        } else if (edKeywordDepart.getText().toString().isEmpty() && edKeywordArrive.getText().toString().isEmpty()) {
            readOrders();
        }
        return false;
    }

    private void departureSearch(String token, String email, Address departureAddress) {
        orderReadPresenter.searchDepartureOrder
                (token, email, departureAddress.getLatitude(), departureAddress.getLongitude());
    }

    private void arriveSearch(String token, String email, Address arriveAddress) {
        orderReadPresenter.searchArriveOrder
                (token, email, arriveAddress.getLatitude(), arriveAddress.getLongitude());
    }

    private void departureAndArriveSearch(String token, String email,
                                          Address departureAddress,
                                          Address arriveAddress) {
        orderReadPresenter
                .searchDepartureAndArriveOrder(token, email,
                        departureAddress.getLatitude(),
                        departureAddress.getLongitude(),
                        arriveAddress.getLatitude(),
                        arriveAddress.getLongitude());
    }

// ----------------------------------------------

    @Override
    public void onReadOrdersSuccessfully(List<Order> orders) {
        orderProgessBar.setVisibility(View.INVISIBLE);
        adapter.setOrderArrayList(orders);
    }

    @Override
    public void onSearchOrdersSuccessfully(List<Order> orders) {
        orderProgessBar.setVisibility(View.INVISIBLE);
        adapter.setOrderArrayList(orders);
    }

    @Override
    public void onReadOrderFailCauseApiDoesNotOpen() {
        Toast.makeText(getContext(), getString(R.string.api_does_not_work), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSearchFailed() {
        Toast.makeText(getContext(), "keyword not found", Toast.LENGTH_LONG).show();
    }

}