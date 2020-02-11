package com.example.dec23_carpool.view.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dec23_carpool.presenter.DriverUpdatePresenter;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.view.MainActivity;
import com.example.dec23_carpool.view.order.GoogleMapConnectionActivity;

import java.io.FileNotFoundException;

import static com.example.dec23_carpool.presenter.DriverUpdatePresenter.*;

public class DriverUpdateActivity extends AppCompatActivity implements DriverUpdateView {

    private Spinner spModel;

    private EditText edLicenseNumber;
    private DriverUpdatePresenter driverUpdatePresenter;
    private User user;
    //camera
    private ImageView ivCarPhoto1, ivCarPhoto2, ivCarPhoto3, tvLicnsePhoto;
    private Bitmap carBitmap1 = null, carBitmap2 = null, carBitmap3 = null, carBitmap4 = null;
    private int uploadingCarPhotoNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        findViews();
        init();
        getUserIntentExtra();
        sharePreferencesSaveEmailPassword();
        sharePreferencesLoadEmailPassword();
    }

    private void getUserIntentExtra() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("LOGIN_USER");
    }

    private void findViews() {
        edLicenseNumber = findViewById(R.id.driver_ed_licenseNumber);
        spModel = findViewById(R.id.driver_sp_model);
        ivCarPhoto1 = findViewById(R.id.driver_iv_photo1);
        ivCarPhoto2 = findViewById(R.id.driver_iv_photo2);
        ivCarPhoto3 = findViewById(R.id.driver_iv_photo3);
        tvLicnsePhoto = findViewById(R.id.driver_tv_licensePhoto);
    }

    private void init() {
        uploadingCarPhotoNumber = 0;
        driverUpdatePresenter = new DriverUpdatePresenter(Global.userRepository(),
                this,
                Global.threadExecutor());
    }

    public void onDriverRegisterNextStepButtonClick(View view) {
        String model = spModel.getSelectedItem().toString();
        String licenseNumber = edLicenseNumber.getText().toString();

//        Drawable drawable = ivCarPhoto1.getBackground();
        if (!licenseNumber.isEmpty() || !model.equals("選選選選廠牌")) {
            user.setModel(model);
            user.setLicenseNumber(licenseNumber);
            if (carBitmap1 != null) {
//
//                user.setCarPic1(carBitmap1);
//                user.setCarPicLicense(carBitmap4);
//                driverUpdatePresenter.uploadCarPics(
//                        user.getToken(), user.getEmail(), carBitmap4,
//                        carBitmap1, carBitmap2, carBitmap3);
            }
            user.setIdentity1(2);
            driverUpdatePresenter.update(user);
            //跳轉dialog:司機註冊成功!!!詢問是否要進行行程規劃?
            //是:繼續規劃
            //否:進入列表
        } else {
            showToast(getString(R.string.input_can_not_be_null));
        }

    }

    private void sharePreferencesSaveEmailPassword() {
        SharedPreferences sp = getSharedPreferences("SP_REGISTER_DRIVER", MODE_PRIVATE);
        sp.edit().putString("SP_LICENSE", "MIT_6666")
                .apply();
    }

    private void sharePreferencesLoadEmailPassword() {
        edLicenseNumber.setText(getSharedPreferences("SP_REGISTER_DRIVER", MODE_PRIVATE).getString("SP_LICENSE", ""));
    }


    private void turnToOtherPageAccordingToUserOption() {
        Intent intent = new Intent()
                .putExtra("LOGIN_USER", user);
        new AlertDialog.Builder(this).
                setTitle("規劃行程").
                setMessage("是否開始規劃行程 ?").
                setNegativeButton("是", (dialog, which) -> {
                    intent.setClass(DriverUpdateActivity.this, GoogleMapConnectionActivity.class);
                    DriverUpdateActivity.this.startActivity(intent);
                }).
                setPositiveButton("否", (dialog, which) -> {
                    intent.setClass(DriverUpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                }).show();
    }

    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

//Set car photo -----------------------------------------------------------------

    public void onCarPhotoUpload1Click(View view) {
        uploadingCarPhotoNumber = 1;
        setCarPhoto();
    }

    public void onCarPhotoUpload2Click(View view) {
        uploadingCarPhotoNumber = 2;
        setCarPhoto();
    }

    public void onCarPhotoUpload3Click(View view) {
        uploadingCarPhotoNumber = 3;
        setCarPhoto();
    }

    public void onCarLicenseUploadClick(View view) {
        uploadingCarPhotoNumber = 4;
        setCarPhoto();
    }

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
                    switch (uploadingCarPhotoNumber) {
                        case 1:
                            carBitmap1 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                            ivCarPhoto1.setImageBitmap(carBitmap1);
                            break;
                        case 2:
                            carBitmap2 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                            ivCarPhoto2.setImageBitmap(carBitmap2);
                            break;
                        case 3:
                            carBitmap3 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                            ivCarPhoto3.setImageBitmap(carBitmap3);
                            break;
                        case 4:
                            carBitmap4 = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                            tvLicnsePhoto.setImageBitmap(carBitmap4);
                            break;
                    }
                    //imageView.setImageBitmap(getCircledBitmap(carBitmap1)) //設圓形
//                        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                        carPhoto.setBackground(imageView.getDrawable());
                } catch (FileNotFoundException err) {
                    Log.e("FileNotFoundException", err.getMessage(), err);
                }
//                    carPhoto.setText("");
            }
        }
    }

//  ------------------------------------------------------------------------------------

    @Override
    public void onDriverUploadPicSuccessfully() {

    }

    @Override
    public void onDriverUploadPicFailCauseFormalError() {
        showToast("DriverUploadPicFailCauseFormalError");
    }

    @Override
    public void onDriverUpdateSuccessfully() {
        SharedPreferences sp = getSharedPreferences(Global.USER, MODE_PRIVATE);
        sp.edit().putString(Global.USER_EMAIL, user.getEmail())
                .putString(Global.USER_TOKEN, user.getToken())
                .apply();
        turnToOtherPageAccordingToUserOption();
    }

    @Override
    public void onDriverUpdateFail() {
        showToast("DriverUpdateFail");
    }

    @Override
    public void onDriverUpdateFailCauseNetFormalError() {
        showToast("FailCauseNetFormalError");
    }


}
