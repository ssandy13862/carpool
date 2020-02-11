package com.example.dec23_carpool.view.order;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.OrderInfo;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.presenter.OrderCreatePresenter;
import com.example.dec23_carpool.presenter.OrderCreatePresenter.OrderView;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.Order;

import java.io.FileNotFoundException;

public class PlanDetailActivity extends AppCompatActivity implements OrderView {

    private TextView tvMinFare, tvMaxFare, tvConfirmFare, tvPassengerMount;
    private SeekBar sbFareBar;
    private CheckBox ckIsPet, ckIsSmoke, ckIsLuggage, ckIsEat;
    private OrderCreatePresenter orderCreatePresenter;
    private ImageView ivPlanPhoto;
    private Bitmap bitmap;
    private Order order;
    private SharedPreferences sp;
    private String email, token;
    private User user;
    private TextView uploadScenePicTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_detail);

        order = (Order) getIntent().getSerializableExtra(Global.PLAN_TIME_ORDER);
        user = (User) getIntent().getSerializableExtra(Global.PLAN_TIME_USER);
        sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        email = sp.getString(Global.USER_EMAIL, "");
        token = sp.getString(Global.USER_TOKEN, "");
        if (order == null) {
            order = new Order();
        }
        init();
        findView();
    }

    private void init() {
        orderCreatePresenter = new OrderCreatePresenter
                (Global.orderRepository(), this, Global.threadExecutor());
        orderCreatePresenter.getCarFare(order);
    }

    private void findView() {
        tvMinFare = findViewById(R.id.plan_detail_tv_minFare);
        tvMaxFare = findViewById(R.id.plan_detail_tv_maxFare);
        tvConfirmFare = findViewById(R.id.plan_detail_tv_confirmFare);
        tvPassengerMount = findViewById(R.id.plan_detail_tv_passengerMount);
        sbFareBar = findViewById(R.id.plan_detail_sb_fareBar);
        ckIsPet = findViewById(R.id.plan_detail_cb_isPet);
        ckIsLuggage = findViewById(R.id.plan_detail_cb_isLuggage);
        ckIsSmoke = findViewById(R.id.plan_detail_cb_isSmoke);
        ckIsEat = findViewById(R.id.plan_detail_cb_isEat);
        ivPlanPhoto = findViewById(R.id.plan_detail_iv_planPic);
        uploadScenePicTextView = findViewById(R.id.uploadScenePicTextView);
    }

    private void setSeekBar(int min, int max) {
        int step = 1;
        sbFareBar.setMax((max - min) / step);
        tvConfirmFare.setText(String.valueOf(min));
        sbFareBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int confirmFare = min + (progress * step);
                tvConfirmFare.setText(String.valueOf(confirmFare));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void onMinusClick(View view) {
        if (!tvPassengerMount.getText().toString().equals("1")) {
            int mount = Integer.valueOf(tvPassengerMount.getText().toString());
            tvPassengerMount.setText(String.valueOf(--mount));
        }
    }

    public void onPlusClick(View view) {
        if (!tvPassengerMount.getText().toString().equals("3")) {
            int mount = Integer.valueOf(tvPassengerMount.getText().toString());
            tvPassengerMount.setText(String.valueOf(++mount));
        }
    }

    public void onDetailClick(View view) {
        //Todo 設定資料放入order
        order.setCarfare(Integer.valueOf(tvConfirmFare.getText().toString()));
        order.setMemberLimit(Integer.valueOf(tvPassengerMount.getText().toString()));
        order.setPet(ckIsPet.isChecked());
        order.setEat(ckIsEat.isChecked());
        order.setSmoke(ckIsSmoke.isChecked());
        order.setLuggage(ckIsLuggage.isChecked());
        order.setEmail(email);
        orderCreatePresenter.createOrder(order, token);
    }

    public void onUploadPictureClick(View view) {
        setCarPhoto();
    }

    // -------------------------------------------------------------------

    //詢問是否開啟相機功能
    private void setCarPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, Global.REQUEST_CODE_CAMERA);
        } else {
            uploadCarPicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Global.REQUEST_CODE_CAMERA) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                uploadCarPicture();
            }
        }
    }

    private void uploadCarPicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, Global.SELECT_CAR_PHOTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Global.SELECT_CAR_PHOTO_REQUEST_CODE) {
                assert data != null;
                Uri uri = data.getData();
                try {
                    ContentResolver contentResolver = this.getContentResolver();
                    bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                    uploadScenePicTextView.setVisibility(View.INVISIBLE);
                    ivPlanPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException err) {
                    Log.e("FileNotFoundException", err.getMessage(), err);
                }
//                    carPhoto.setText("");
            }
        }
    }


// -------------------------------------------------------------------


    @Override
    public void onOrderCreatedSuccessfully(OrderInfo orderInfo) {
        order.setId(orderInfo.getTravelInfoId());
        if (bitmap != null) {
            orderCreatePresenter.uploadPlanPicture(
                    token, email,
                    bitmap, order.getId());
        } else {
            startActivity(
                    new Intent(this, FinishActivity.class)
                            .putExtra("user", user));
        }
    }

    @Override
    public void onOrderCarfareGetSuccessfully(Order orderWithCarfare) {
        order.setCostTime(orderWithCarfare.getTime());
        tvMaxFare.setText(String.valueOf(orderWithCarfare.getMaxFare()));
        tvMinFare.setText(String.valueOf(orderWithCarfare.getMinFare()));
        setSeekBar(orderWithCarfare.getMinFare(), orderWithCarfare.getMaxFare());
    }

    @Override
    public void onOrderCreatedFailed() {
        Toast.makeText(this, getString(R.string.create_order_failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onUploadPlanPictureSuccessfully() {
        startActivity(
                new Intent(this, FinishActivity.class)
                        .putExtra("user", user));
    }

    @Override
    public void onUploadPlanPictureFaild() {
        ivPlanPhoto.setImageResource(R.drawable.ic_upload);
    }

}
