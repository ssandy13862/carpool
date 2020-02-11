package com.example.dec23_carpool.util;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;
import com.example.dec23_carpool.object.GlideUtils;
import com.example.dec23_carpool.view.OrderDetailActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class CustomRecycleViewAdapter extends Adapter<CustomRecycleViewAdapter.ItemViewHolder> implements Serializable {

    private List<Order> orderArrayList;
    private Fragment fragment;

    public CustomRecycleViewAdapter(Fragment fragment, List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        this.fragment = fragment;
    }

    class ItemViewHolder extends ViewHolder {

        private static final int ID = 0;
        private TextView tvDepartureTime;
        private TextView tvArriveTime;
        private TextView tvDeparturePlace;
        private TextView tvArrivePlace;
        private TextView tvCarfare;
        private TextView tvDate;
        private ImageView ivOrderPicture;

        private ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
            itemView.setOnClickListener(this::onOrderClick);
        }

        private void onOrderClick(View view) {
            Order order = orderArrayList.get(getAdapterPosition());
            view.getContext().startActivity(
                    new Intent(view.getContext(), OrderDetailActivity.class)
                            .putExtra("ORDER_DETAIL", order));
        }

        private void init(View view) {
            tvDepartureTime = view.findViewById(R.id.order_tv_startTime);
            tvArriveTime = view.findViewById(R.id.order_tv_endTime);
            tvDeparturePlace = view.findViewById(R.id.order_tv_startPlace);
            tvArrivePlace = view.findViewById(R.id.order_tv_endPlace);
            tvCarfare = view.findViewById(R.id.order_tv_carfare);
            tvDate = view.findViewById(R.id.order_tv_date);
            ivOrderPicture = view.findViewById(R.id.order_iv_orderPhoto);
        }
    }


    public void setOrderArrayList(List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ItemViewHolder(view);
    }

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
            setOrderPicture(holder, order);
//            itemViewHolder.tvScore.setText(String.format("%.01f", order.getDriver().getScore()));
        }
    }

    private void setOrderPicture(ItemViewHolder holder, Order order) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("PictureType", "ScenePic");
        queryMap.put("TravelInfoId", String.valueOf(order.getId()));
        GlideUrl glideUrl = GlideUtils.configOrderPictureUrl(queryMap);
        Glide.with(Objects.requireNonNull(fragment.getContext()))
                .asBitmap()
                .load(glideUrl)
                .centerCrop()
                .placeholder(R.drawable.bg_new_year)
                .error(R.drawable.bg_new_year)
                .into(holder.ivOrderPicture);
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
