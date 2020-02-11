package com.example.dec23_carpool.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.example.dec23_carpool.NotDriverActivity;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.fragmentAll.NotifyFragment;
import com.example.dec23_carpool.view.fragmentAll.HomeFragment;
import com.example.dec23_carpool.view.fragmentAll.MyOrderFragment;
import com.example.dec23_carpool.view.fragmentAll.OrderFragment;
import com.example.dec23_carpool.view.fragmentAll.UserAddressFragment;
import com.example.dec23_carpool.view.fragmentAll.UserInfoFragment;
import com.example.dec23_carpool.view.order.GoogleMapConnectionActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {


    //    private Map<Integer, Supplier<Fragment>> fragmentMap = new HashMap<>();

    private User user;
    private Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setBottomNavigationView();
    }

    private void init() {
        fragmentMap.put(R.id.navigation_home, new HomeFragment());
        fragmentMap.put(R.id.navigation_notification, new NotifyFragment());
        fragmentMap.put(R.id.navigation_order, new OrderFragment());
        fragmentMap.put(R.id.navigation_myOrder, new MyOrderFragment());
        user = (User) getIntent().getSerializableExtra("LOGIN_USER");
        if (user != null) {
            fragmentMap.put(R.layout.fragment_user_info, new UserInfoFragment(user));
            fragmentMap.put(R.layout.fragment_user_address, new UserAddressFragment());
        }
    }

    private void setBottomNavigationView() {
        BottomNavigationView bottomNav = findViewById(R.id.navigation_bar);
        bottomNav.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        bottomNav.setSelectedItemId(R.id.navigation_order);
    }

    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switchToFragment(menuItem.getItemId());
        return true;
    }

    public void switchToFragment(int fragmentId) {
        if (fragmentId == R.id.navigation_addPlan) {
            Class carPoolClass = null;
            switch (user.getIdentity1()) {
                case Global.DRIVER:
                    carPoolClass = GoogleMapConnectionActivity.class;
                    break;
                case Global.PASSENGER:
                    carPoolClass = NotDriverActivity.class;
                    break;
                default:
            }
            if (carPoolClass != null) {
                startActivity(new Intent(this,
                        carPoolClass)
                        .putExtra("LOGIN_USER", user));
            }
        } else if (fragmentMap.containsKey(fragmentId)) {
            Fragment fragment = fragmentMap.get(fragmentId);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment).commit();
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
