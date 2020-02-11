package com.example.dec23_carpool.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.util.CustomRecycleViewAdapterWithSmallStyle.ItemViewHolder;
import com.example.dec23_carpool.view.MyOrderDetailActivity;
import com.example.dec23_carpool.view.fragmentAll.MyOrderFragment;

import java.util.List;

public class
CustomRecycleViewAdapterWithSmallStyle extends Adapter<ItemViewHolder> {

    private List<Order> orderArrayList;
    private Activity activity;
    private MyOrderFragment fragment;
    private SharedPreferences sp;

    public CustomRecycleViewAdapterWithSmallStyle(Fragment fragment, List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.fragment = (MyOrderFragment) fragment;
        this.activity = fragment.getActivity();
        if (activity != null) {
            sp = activity.getSharedPreferences(Global.USER, Context.MODE_PRIVATE);
        }

    }

    class ItemViewHolder extends ViewHolder {

        private static final int ID = 0;
        private TextView tvDepartureTime;
        private TextView tvArriveTime;
        private TextView tvDeparturePlace;
        private TextView tvArrivePlace;
        private TextView tvCarfare;
        private TextView tvDate;
        private ImageView ivDelete;
        private TextView tvIdentity;
        private TextView tvEvent;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
            itemView.setOnClickListener(this::onItemViewClick);
        }

        private void onItemViewClick(View view) {
            if (getAdapterPosition() != -1) {
                Order order = orderArrayList.get(getAdapterPosition());
                view.getContext().startActivity(
                        new Intent(view.getContext(), MyOrderDetailActivity.class)
                                .putExtra("MYORDER_DETAIL", order));
            }
        }

        private void init(View view) {
            tvDate = view.findViewById(R.id.myorder_tv_date);
            tvDepartureTime = view.findViewById(R.id.myorder_tv_startTime);
            tvArriveTime = view.findViewById(R.id.myorder_tv_endTime);
            tvDeparturePlace = view.findViewById(R.id.myorder_tv_startPlace);
            tvArrivePlace = view.findViewById(R.id.myorder_tv_endPlace);
            tvCarfare = view.findViewById(R.id.myorder_tv_carfare);
            ivDelete = view.findViewById(R.id.myorder_iv_delete);
            tvIdentity = view.findViewById(R.id.myorder_tv_identity);
            tvEvent = view.findViewById(R.id.myorder_tv_event);
            ivDelete.setOnClickListener(this::onDeleteImageViewClick);
        }

        private void onDeleteImageViewClick(View view) {
            Builder dialog = setDialog();
            dialog.create().show();
        }

        private Builder setDialog() {

            return new Builder(activity)
                    .setMessage("是否刪除此行程?")
                    .setNegativeButton("是", (dialog12, which) -> {
                        //開始判斷
                        long newTime = System.currentTimeMillis();
                        long threeDay = 259200000;
                        Order order = orderArrayList.get(getLayoutPosition());
                        if (order.getLongDepartureDate() * 1000 - newTime < threeDay) {
                            //回傳不可刪除此訂單
                            new Builder(activity)
                                    .setMessage("在預定時間三天內退出訂單，\n只能退款7成約定金額，是否取消?")
                                    .setNegativeButton("是", (dialog13, which12) -> {
                                        new Builder(activity)
                                                .setMessage("已進入退款程序")
                                                .setNegativeButton("是", (dialog15, which13) -> {
                                                    //刪除訂單
                                                    deleteOrder();
                                                    dialog15.dismiss();
                                                }).show();
                                    })
                                    .setPositiveButton("否", (dialog1, which1)
                                            -> dialog1.dismiss())
                                    .show();
                            return;
                        }
                        deleteOrder();
                        dialog12.dismiss();
                    })
                    .setPositiveButton("否", (dialog14, which) ->
                            dialog14.dismiss());
        }

        private void deleteOrder() {
            //必須先存下index和id，否則在二次傳輸的時候因為第一筆已刪除，造成錯誤
            int index = getLayoutPosition();
            if (index != -1) {
                Order order = orderArrayList.get(index);
                orderArrayList.remove(order);
                notifyDataSetChanged();
                //刪除Fragment訂單, 刪除後端訂單
                fragment.deleteOrderList(order.getId());
            }
        }
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_small_order_for_constaint, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (getItemViewType(position) == ItemViewHolder.ID) {
            Order order = orderArrayList.get(position);
            holder.tvDate.setText(order.getStrDepartureDate());
            holder.tvArrivePlace.setText(order.getArrivePlaceName());
            holder.tvArriveTime.setText(order.getStrArriveTime());
            holder.tvDeparturePlace.setText(order.getDepartureName());
            holder.tvDepartureTime.setText(order.getStrDepartureTime());
            holder.tvCarfare.setText(String.format("%s", "$" + order.getCarfare()));

            int identity = sp.getInt(Global.USER_ID, -1);
            switch (identity) {
                case 1:
                    holder.tvIdentity.setText("我是乘客");
                    break;
                case 2:
                    holder.tvIdentity.setText("我是司機");
                    break;
            }

            switch (order.getTravelState()) {
                case 0:
                    holder.tvEvent.setText("未開始");
                    holder.tvEvent.setTextColor(activity.getColor(R.color.white));
                    holder.tvEvent.setBackground(activity.getDrawable(R.drawable.radius_green_button));
                    break;
                case 1:
                    holder.tvEvent.setText("進行中");
                    holder.tvEvent.setTextColor(activity.getColor(R.color.white));
                    holder.tvEvent.setBackground(activity.getDrawable(R.drawable.radius_orange_button));
                    break;
                case 2:
                    holder.tvEvent.setText("已結束");
                    holder.tvEvent.setTextColor(activity.getColor(R.color.white));
                    holder.tvEvent.setBackground(activity.getDrawable(R.drawable.radius_grey_button));
                    break;
            }

        }

    }

    @Override
    public int getItemViewType(int position) {
        return ItemViewHolder.ID;
    }

    @Override
    public int getItemCount() {
        return orderArrayList.size();

    }
}
