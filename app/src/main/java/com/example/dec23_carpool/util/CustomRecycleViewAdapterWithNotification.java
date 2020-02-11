package com.example.dec23_carpool.util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Notification;
import com.example.dec23_carpool.presenter.NotifyPresenter;
import com.example.dec23_carpool.view.OrderDetailActivity;
import com.example.dec23_carpool.view.fragmentAll.MyOrderFragment;
import com.example.dec23_carpool.view.fragmentAll.NotifyFragment;
import com.example.dec23_carpool.view.order.PlanDetailActivity;

import java.io.Serializable;
import java.util.List;

public class CustomRecycleViewAdapterWithNotification extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable{

    private List<Notification> notificationList;
    private NotifyPresenter notifyPresenter;

    public CustomRecycleViewAdapterWithNotification(NotifyPresenter notifyPresenter, List<Notification> notificationList){
        this.notificationList = notificationList;
        this.notifyPresenter = notifyPresenter;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public static final int ID = 0;
        private TextView tvMessage;
        private TextView tvDate;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
            onClick(itemView);
        }

        private void onClick(View view){
            view.setOnClickListener(v -> {
                Notification notification = notificationList.get(getAdapterPosition());

//
//                fragment.getFragmentManager().beginTransaction()
//                        .addToBackStack(null)
//                        .replace(R.id.constarintLayout, new MyOrderFragment())
//                        .commit();

                //call api
                notifyPresenter.searchOrderById(notification.getTravelInfoId());

//                //把order傳過去
//                view.getContext().startActivity(
//                        new Intent(view.getContext(), OrderDetailActivity.class)
//                        .putExtra("NOTIFICATION_ORDER", notification.getTravelInfoId()));
            });

        }

        private void init(View view){
            tvMessage = view.findViewById(R.id.notice_tv_content);
            tvDate = view.findViewById(R.id.notice_tv_date);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ItemViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == ItemViewHolder.ID){
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Notification notification = notificationList.get(position);
            itemViewHolder.tvDate.setText(notification.getMessageTimeStamp());
            itemViewHolder.tvMessage.setText(notification.getMessage());
        }
    }

    @Override
    public int getItemViewType(int position){
        return ItemViewHolder.ID;
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}
