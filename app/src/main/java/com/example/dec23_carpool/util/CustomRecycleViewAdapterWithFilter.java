package com.example.dec23_carpool.util;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.Order;

import java.util.ArrayList;
import java.util.List;

public class CustomRecycleViewAdapterWithFilter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable { // 實現 Filterable(過濾)

    private List<Order> orderArrayList;
    private List<Order> orderArrayListFull;

    public CustomRecycleViewAdapterWithFilter(List<Order> orderArrayList) {

        this.orderArrayList = orderArrayList;
        //創建一個複製版的orderArraylist，用來修改
        this.orderArrayListFull = new ArrayList<>(orderArrayList);

    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        public static final int ID = 0;
        private TextView tvDepartureTime;
        private TextView tvArriveTime;
        private TextView tvDeparturePlace;
        private TextView tvArrivePlace;
        private TextView tvCarfare;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            init(itemView);
        }

        private void init(View view) {
            tvDepartureTime = view.findViewById(R.id.order_tv_startTime);
            tvArriveTime = view.findViewById(R.id.order_tv_endTime);
            tvDeparturePlace = view.findViewById(R.id.order_tv_startPlace);
            tvArrivePlace = view.findViewById(R.id.order_tv_endPlace);
            tvCarfare = view.findViewById(R.id.order_tv_carfare);
        }
    }

    public void setOrderArrayList(List<Order> orderArrayList) {
        this.orderArrayList = orderArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ItemViewHolder.ID) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new ItemViewHolder(view);
        }
        return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ItemViewHolder.ID) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            Order order = orderArrayList.get(position);
            itemViewHolder.tvArrivePlace.setText(order.getArrivePlaceName());
            itemViewHolder.tvArriveTime.setText(order.getStrArriveTime());
            itemViewHolder.tvDeparturePlace.setText(order.getDepartureName());
            itemViewHolder.tvDepartureTime.setText(order.getStrDepartureTime());
            itemViewHolder.tvCarfare.setText(String.format("%d", order.getMaxFare()));
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

    // ----------------------------------

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    //創造一個filter，實作內部function
    private Filter myFilter = new Filter() {

        //製作過濾器，回傳結果
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Order> filteredList = new ArrayList<>();

            //if input is empty here, we will show all results.
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(orderArrayListFull);

                //if type something here (過濾模式)
            } else {
                //constraint: 輸入內容
                String filterPattern = constraint.toString().trim();
                //如果order的關鍵字包含輸入文字，就將此order加入過濾列表
                for (Order order : orderArrayListFull) {
                    if (order.getDepartureName().contains(filterPattern)) {
                        filteredList.add(order);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;

        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            orderArrayList.clear();
            //放入被篩選過的資料
            orderArrayList.addAll((List) results.values);
            notifyDataSetChanged();
        }

    };

}


