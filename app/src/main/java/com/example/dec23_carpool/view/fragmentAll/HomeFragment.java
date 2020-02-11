package com.example.dec23_carpool.view.fragmentAll;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.dec23_carpool.R;
import com.example.dec23_carpool.object.GlideUtils;
import com.example.dec23_carpool.object.User;
import com.example.dec23_carpool.presenter.HomeFragmentPresenter;
import com.example.dec23_carpool.presenter.HomeFragmentPresenter.HomeFragmentView;
import com.example.dec23_carpool.util.Global;
import com.example.dec23_carpool.view.MainActivity;
import com.example.dec23_carpool.view.login.LoginActivity;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements HomeFragmentView {

    private TextView userInfo;
    private TextView userAddress;
    private MainActivity mainActivity;
    private HomeFragmentPresenter homeFragmentPresenter;
    private TextView nickname, email, btnSignout;
    private User user;
    private SharedPreferences sp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);
        init();
    }

    private void findView(View view) {
        ivPhoto = view.findViewById(R.id.home_iv_photo);
        userInfo = view.findViewById(R.id.userInfo);
        userAddress = view.findViewById(R.id.userAddress);
        nickname = view.findViewById(R.id.home_tv_nickname);
        email = view.findViewById(R.id.home_tv_email);
        btnSignout = view.findViewById(R.id.home_tv_signout);
    }

    private void init() {
        mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            sp = mainActivity.getSharedPreferences(Global.USER, Context.MODE_PRIVATE);
        }
        user = mainActivity.getUser();
        email.setText(sp.getString(Global.USER_EMAIL, ""));
        nickname.setText(sp.getString(Global.USER_NICKNAME, ""));

        homeFragmentPresenter = new HomeFragmentPresenter(Global.userRepository(),
                this, Global.threadExecutor());
        userInfo.setOnClickListener(this::onUserInfoClick);
        userAddress.setOnClickListener(this::onUserAddressClick);
        ivPhoto.setOnClickListener(this::onPhotoClick);
        btnSignout.setOnClickListener(this::onSignoutClick);
        downloadUserPhoto();
    }

    private void onSignoutClick(View v){
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private void onUserInfoClick(View v) {
        mainActivity.switchToFragment(R.layout.fragment_user_info);
    }

    private void onUserAddressClick(View v) {
        mainActivity.switchToFragment(R.layout.fragment_user_address);
    }


    //Set car photo -----------------------------------------------------------------
    private Bitmap photoBitmap;

    private ImageView ivPhoto;

    private void onPhotoClick(View v) {
        setCarPhoto();
    }

    //詢問是否開啟相機功能
    private void setCarPhoto() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA}, Global.REQUEST_CODE_CAMERA);
        } else {
            uploadCarPicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
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
        //這裡的intent是跳轉到指定的Android內建功能，讓使用者在相片總管選照片。
        //SELECT_CAR_PHOTO_REQUEST_CODE之常數，則是為了執行onActivityResult時判斷是誰發出。
        startActivityForResult(intent, Global.SELECT_CAR_PHOTO_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //執行此函式，會將SELECT_CAR_PHOTO_REQUEST_CODE傳入requestCode
        //並自動把取得的資料內容傳入data、執行結果傳入resultCode
        if (resultCode == RESULT_OK) {
            if (requestCode == Global.SELECT_CAR_PHOTO_REQUEST_CODE) {
                //把data中相片的uri取出。
                assert data != null;
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        if (contentResolver != null) {
                            //開啟檔案路徑, 讀取檔案, 轉成inputStream(輸入串流)型態
                            new Thread(() -> Glide.get(getContext()).clearDiskCache());
                            new Thread(() -> Glide.get(getContext()).clearMemory());
                            photoBitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
                            homeFragmentPresenter
                                    .uploadPhoto(user.getToken(), user.getEmail(), photoBitmap);

//                        ImageView imageView = new ImageView(this);
//                            ivPhoto.setImageBitmap(photoBitmap);
//                        ivPhoto.setImageBitmap(getCircledBitmap(photoBitmap)); //設圓形
//                            ivPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                            ivPhoto.setBackground(ivPhoto.getDrawable());
                        }
                    } catch (FileNotFoundException err) {
                        Log.e("FileNotFoundException", err.getMessage(), err);
                    }
//                    carPhoto.setText("");
                }
            }
        }
    }

    @Override
    public void onUserUploadPhotoSuccessfully() {
        downloadUserPhoto();
    }

    private void downloadUserPhoto() {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("Email", user.getEmail());
        queryMap.put("PictureType", "Picture");
        GlideUrl glideUrl = GlideUtils.configUserPhotoUrl(user.getToken(), queryMap);
        Glide.with(this)
                .asBitmap()
                .load(glideUrl)
                .fitCenter()
                .circleCrop()
                .placeholder(R.mipmap.frog)
                .error(R.drawable.image)
                .into(ivPhoto);
    }
}
