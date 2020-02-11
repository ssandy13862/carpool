package com.example.dec23_carpool.view.fragmentAll;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.presenter.NotifyPresenter;
import com.example.dec23_carpool.util.CustomRecycleViewAdapterWithNotification;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.OrderDetailActivity;

import java.util.List;
import java.util.Objects;

public class NotifyFragment extends Fragment implements NotifyPresenter.NotifyView {

    private CustomRecycleViewAdapterWithNotification adapter;
    private RecyclerView recyclerView;
    private NotifyPresenter notifyPresenter;
    private TextView tvTitle;
    private ImageView ivNoNoticePic;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        notifyPresenter = new NotifyPresenter(Global.threadExecutor(), this,
                Global.notifyRepostiory());
        SharedPreferences sp = this.getActivity().getSharedPreferences(Global.USER, Context.MODE_PRIVATE);

        findView(view);
        progressBar.setVisibility(View.VISIBLE);
        notifyPresenter.readNotification(sp.getString(Global.USER_TOKEN, ""), sp.getString(Global.USER_EMAIL, ""));
    }

    private void findView(View view){
        tvTitle = view.findViewById(R.id.notification_tv_title);
        ivNoNoticePic = view.findViewById(R.id.notification_iv_pic);
        recyclerView = view.findViewById(R.id.notification_rv_list);
        progressBar = view.findViewById(R.id.notification_progressBar);
    }

    private void setRecycleView(List<Notification> notificationList) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new CustomRecycleViewAdapterWithNotification(notifyPresenter, notificationList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    //-----------------------------------------------------------------------------

    @Override
    public void onReadNotificationSuccessfully(List<Notification> notificationList) {
        progressBar.setVisibility(View.INVISIBLE);
        setRecycleView(notificationList);
        ivNoNoticePic.setVisibility(View.GONE);
        tvTitle.setVisibility(View.GONE);
    }

    @Override
    public void onReadNotificationHaveNotBeenFound() {
        progressBar.setVisibility(View.INVISIBLE);
        ivNoNoticePic.setVisibility(View.VISIBLE);
        tvTitle.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSearchOrderByIdSuccessfully(List<Order> orderList) {
        Objects.requireNonNull(getActivity())
                .startActivity
                (new Intent(getActivity(), OrderDetailActivity.class)
                .putExtra("ORDER_DETAIL", orderList.get(0)));
    }

    @Override
    public void onSearchOrderByIdFailed() {
        Toast.makeText(getActivity(), "訂單已過期", Toast.LENGTH_LONG).show();
    }
}
