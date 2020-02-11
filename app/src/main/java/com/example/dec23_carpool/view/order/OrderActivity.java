//package com.example.dec23_carpool.view.order;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.SearchView;
//import android.widget.Toast;
//
//import com.example.dec23_carpool.presenter.OrderReadPresenter;
//import com.example.dec23_carpool.R;
//import com.example.dec23_carpool.util.CustomRecycleViewAdapter;
//import com.example.dec23_carpool.util.CustomRecycleViewAdapterWithFilter;
//import com.example.dec23_carpool.util.Global;
//import com.example.dec23_carpool.object.Order;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderActivity extends AppCompatActivity implements OrderReadPresenter.OrderView {
//
//    private List<Order> orderArrayList;
//    private OrderReadPresenter orderReadPresenter;
//    private CustomRecycleViewAdapter adapter;
//    private EditText edKeywordDepart, edKeywordArrive;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_order);
//
//        init();
////      addList();
//        setRecycleView();
//        readOrders();
//        onSearchByEnterClick();
//
//    }
//
//    private void addList(){
//        orderArrayList.add(new Order("08:00", "12:00", "台中車站", "台北車站", 180, "王小明", "男", 4.6));
//        orderArrayList.add(new Order("09:00", "13:00", "台中車站", "罈子車站", 160, "王小明", "男", 4.6));
//        orderArrayList.add(new Order("10:00", "14:00", "花蓮車站", "太原車站", 170, "王小明", "男", 4.6));
//        orderArrayList.add(new Order("11:00", "15:00", "屏東車站", "太原車站", 150, "王小明", "男", 4.6));
//        orderArrayList.add(new Order("12:00", "16:00", "桃園車站", "台北車站", 160, "王小明", "男", 4.6));
//    }
//
//    private void init() {
//        edKeywordDepart = findViewById(R.id.order_et_keyword1);
//        edKeywordArrive = findViewById(R.id.order_et_keyword2);
//        orderArrayList = new ArrayList<>();
//        orderReadPresenter = new OrderReadPresenter(Global.orderRepository(),
//                this, Global.threadExecutor());
//    }
//
//    private void setRecycleView() {
//        RecyclerView recyclerView = findViewById(R.id.rv_list);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        adapter = new CustomRecycleViewAdapter(orderArrayList);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//    }
//
////    public boolean onCreateOptionsMenu(Menu menu) {
////
////        //使用inflater，以及剛創建的menu.xml
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.order_menu, menu);
////
////        //設定icon
////        MenuItem menuSearchItem = menu.findItem(R.id.order_search);
////        //設定searchView 和 searchView內文監聽
////        SearchView searchView = (SearchView) menuSearchItem.getActionView();
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////            @Override
////            public boolean onQueryTextSubmit(String query) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String newText) {
////                //recycleViewAdapter的filter去抓輸入文字
////                adapter.getFilter().filter(newText);
////                return false;
////            }
////        });
////        return true;
////    }
//
//
//
//    private void readOrders() {
//        //Todo 讀訂單
//        orderReadPresenter.readOrders();
//    }
//
//    public void onSearchByEnterClick(){
//        edKeywordDepart.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&(keyCode == KeyEvent.KEYCODE_ENTER)){
//                    orderReadPresenter.searchOrder(edKeywordDepart.getText().toString(), edKeywordArrive.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
//        edKeywordArrive.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&(keyCode == KeyEvent.KEYCODE_ENTER)){
//                    orderReadPresenter.searchOrder(edKeywordDepart.getText().toString(), edKeywordArrive.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
//    }
//
//
//    // ----------------------------------------------
//
//    @Override
//    public void onReadOrdersSuccessfully(List<Order> orders) {
//        adapter.setOrderArrayList(orders);
//        this.orderArrayList = orders;
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onSearchOrdersSuccessfully(List<Order> orders) {
//        adapter.setOrderArrayList(orders);
//        this.orderArrayList = orders;
//        adapter.notifyDataSetChanged();
//    }
//
//    @Override
//    public void onSearchFailed() {
//        Toast.makeText(this, "keyword not found", Toast.LENGTH_LONG).show();
//    }
//
//
//}
