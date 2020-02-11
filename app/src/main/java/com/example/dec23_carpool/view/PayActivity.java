package com.example.dec23_carpool.view;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.presenter.PayPresenter;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.OrderInfo;

public class PayActivity extends AppCompatActivity implements PayPresenter.Orderview {

    private PayPresenter payPresenter;
    private EditText edEmpty1, edEmpty2, edEmpty3, edEmpty4, edDate, edverifyNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        findView();
        setTextChange();
        payPresenter = new PayPresenter(Global.threadExecutor(), this, Global.orderRepository());
    }

    private void findView(){
        edEmpty1 = findViewById(R.id.pay_ed_empty1);
        edEmpty2 = findViewById(R.id.pay_ed_empty2);
        edEmpty3 = findViewById(R.id.pay_ed_empty3);
        edEmpty4 = findViewById(R.id.pay_ed_empty4);
        edDate = findViewById(R.id.pay_ed_date);
        edverifyNum = findViewById(R.id.pay_ed_verifyNum);
    }

    public void onPayClick(View view) {
        String email = getSharedPreferences(Global.USER, MODE_PRIVATE).getString(Global.USER_EMAIL, "");
        String token = getSharedPreferences(Global.USER, MODE_PRIVATE).getString(Global.USER_TOKEN, "");
        int id = getSharedPreferences(Global.USER, MODE_PRIVATE).getInt(Global.ORDER_ID, -1);
        OrderInfo joinOrderInfo = new OrderInfo(email, id);

        payPresenter.joinOrder(token, joinOrderInfo);
    }

    private void setTextChange(){
        edEmpty1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 3){
                    edEmpty2.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edEmpty2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 3){
                    edEmpty3.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edEmpty3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 3){
                    edEmpty4.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edEmpty4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 3){
                    edDate.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edDate.addTextChangedListener(new TextWatcher() {
            @SuppressLint("SetTextI18n")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length() == 4){
                    edDate.requestFocus();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
//                if(s.length() == 2){
//                    edDate.setText(edDate.getText()+"/");
//                }
            }
        });

    }





    //---------------------------------------

    @Override
    public void joinOrderSuccessfully() {
        Toast.makeText(this, "join successful", Toast.LENGTH_LONG).show();
        startActivity(new Intent(this, PayFinishActivity.class));
    }

    @Override
    public void joinOrderFailCauseOrderFullOrJoined() {
        Toast.makeText(this, "joinOrderFailCauseOrderFullOrJoined", Toast.LENGTH_LONG).show();
    }
}
