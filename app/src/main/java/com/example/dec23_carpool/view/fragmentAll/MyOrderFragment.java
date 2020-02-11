package com.example.dec23_carpool.view.fragmentAll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.OrderPastAndRecent;
import com.example.dec23_carpool.presenter.MyOrderReadPresenter;
import com.example.dec23_carpool.util.CustomRecycleViewAdapterWithSmallStyle;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.Order;
import com.necer.calendar.NCalendar;
import com.necer.painter.InnerPainter;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class MyOrderFragment extends Fragment implements MyOrderReadPresenter.MyOrderView {

    private NCalendar nCalendar;
    private TextView tvTitle1, tvTitle2;
    private ProgressBar progressBar;
    private MyOrderReadPresenter myOrderReadPresenter;
    private List<Order> orderList;
    private List<Order> seletedDateList;

    private RecyclerView recyclerView;
    private CustomRecycleViewAdapterWithSmallStyle adapter;
    private SharedPreferences sp;
    private int calendarYear, calendarMonth, calendarDay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myorder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        init();
        setnCalendar();
        sp = this.getActivity().getSharedPreferences(Global.USER, Context.MODE_PRIVATE);
        progressBar.setVisibility(View.VISIBLE);
        readMyOrders(
                sp.getString(Global.USER_TOKEN, ""),
                sp.getString(Global.USER_EMAIL, ""));
    }


    private void findView(View view) {
        nCalendar = view.findViewById(R.id.myorder_ncalendar);
        tvTitle1 = view.findViewById(R.id.myorder_tv_title1);
        tvTitle2 = view.findViewById(R.id.myorder_tv_title2);
        recyclerView = view.findViewById(R.id.myorder_rv_order);
        progressBar = view.findViewById(R.id.myorder_progress_bar);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        orderList = new ArrayList<>();
        seletedDateList = new ArrayList<>();
        myOrderReadPresenter = new MyOrderReadPresenter(Global.orderRepository(),
                this, Global.threadExecutor());
        DateTime dateTime = new DateTime();
        tvTitle1.setText(dateTime.getMonthOfYear() + "月");
        tvTitle2.setText(dateTime.getYear() + "年" +
                dateTime.getMonthOfYear() + "月" +
                dateTime.getDayOfMonth() + "日");
    }

//    private void setToolbar(){
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        setHasOptionsMenu(true);
//    }

    @SuppressLint("SetTextI18n")
    private void setnCalendar() {
        nCalendar.setOnCalendarChangedListener((baseCalendar, year, month, localDate) -> {
            calendarYear = localDate.getYear();
            calendarMonth = localDate.getMonthOfYear();
            calendarDay = localDate.getDayOfMonth();
            tvTitle1.setText(calendarMonth + "月");
            tvTitle2.setText(calendarYear + "年" + calendarMonth + "月" + calendarDay + "日");
            showOrder();
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.d("MyOrder", "onCreateOptionsMenu()");
        menu.clear();
        inflater.inflate(R.menu.myorder_menu, menu);
    }

    private void showOrder() {
        seletedDateList.clear();
        for (Order order : orderList) {
            if (getFormat(calendarYear, calendarMonth, calendarDay)
                    .equals(changeFomate(order.getStrDepartureDate()))) {
                seletedDateList.add(order);
            }
        }
        setRecyclerView(seletedDateList);
    }

    public void deleteOrderList(int id) {
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            if (order.getId() == id) {
                orderList.remove(order);
                adapter.notifyDataSetChanged();
                myOrderReadPresenter.deleteOrder
                        (sp.getString(Global.USER_TOKEN, ""),
                                new OrderInfo(sp.getString(Global.USER_EMAIL, ""), id));
                return;
            }
        }
    }

    private String getFormat(int year, int month, int day) {
        String date = year + "-" + (month >= 10 ? month : "0" + month) + "-" + (day >= 10 ? day : "0" + day);
        Log.d("Myorder", date);
        return date;
    }

    private void setRecyclerView(List<Order> orderList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new CustomRecycleViewAdapterWithSmallStyle(this, orderList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void readMyOrders(String token, String email) {
        myOrderReadPresenter.readMyOrders(token, email);
    }

    private String changeFomate(String date) {
        String[] changeDate = date.split("/");
        return changeDate[0] + "-" + changeDate[1] + "-" + changeDate[2];
    }


    //------------------------------------------------------------

    @Override
    public void onReadMyOrderSuccessfully(OrderPastAndRecent orderAll) {
        progressBar.setVisibility(View.INVISIBLE);
        if (orderAll.getPast() != null || orderAll.getRecent() != null) {
            orderList.addAll(orderAll.getRecent());
            orderList.addAll(orderAll.getPast());
            setRecyclerView(seletedDateList);
//        List<String> pointList = Arrays.asList("2020-01-03", "2020-01-04", "2020-01-05")特定格式;
            List<String> pointList = new ArrayList<>();
            for (Order order : orderList) {
                pointList.add(changeFomate(order.getStrDepartureDate()));
            }
            InnerPainter innerPainter = (InnerPainter) nCalendar.getCalendarPainter();
            innerPainter.setPointList(pointList);
            showOrder();
        }
    }

    @Override
    public void onReadMyOrderNotFound() {
        progressBar.setVisibility(View.INVISIBLE);
        setRecyclerView(seletedDateList);
        Toast.makeText(getActivity(), "orders have not been found", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDeleteMyOrderSuccessfully() {

    }

    @Override
    public void onDeleteMyOrderFailed() {

    }
}
