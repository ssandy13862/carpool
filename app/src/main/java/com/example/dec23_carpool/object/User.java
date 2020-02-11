package com.example.dec23_carpool.object;

import androidx.annotation.NonNull;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Serializable, Cloneable {

    //會員
    private int identity1 = 1, gender;
    private String email, password, token, nickname,
    birthday, phone1, address1;

    //司機
    private String model;
    private String licenseNumber;
    private Bitmap carPic1, carPic2, carPic3, carPicLicense;

    public User(String email, String password, int identity1) {
        this.identity1 = identity1;
        this.email = email;
        this.password = password;
    }

    public User(String email, String token) {
        this.email = email;
        this.token = token;
    }

    //註冊會員用
    public User(String email, String password, String Nickname, String birthday,
                int gender, String phone1, String address1) {
        this.email = email;
        this.password = password;
        this.nickname = Nickname;
        this.birthday = birthday;
        this.gender = gender;
        this.phone1 = phone1;
        this.address1 = address1;
        setIdentity1(1);
        setModel("沒有廠牌");
        setLicenseNumber("沒有車牌號碼");
    }

    //註冊司機用
    public User(int gender, String email, String password, String Nickname, String birthday,
                String phone1, String address1, String model, String licenseNumber) {
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.nickname = Nickname;
        this.birthday = birthday;
        this.phone1 = phone1;
        this.address1 = address1;
        this.model = model;
        this.licenseNumber = licenseNumber;
        setIdentity1(2);
    }

    protected User(Parcel in) {
        identity1 = in.readInt();
        gender = in.readInt();
        email = in.readString();
        password = in.readString();
        token = in.readString();
        nickname = in.readString();
        birthday = in.readString();
        phone1 = in.readString();
        address1 = in.readString();
        model = in.readString();
        licenseNumber = in.readString();
        carPic1 = in.readParcelable(Bitmap.class.getClassLoader());
        carPic2 = in.readParcelable(Bitmap.class.getClassLoader());
        carPic3 = in.readParcelable(Bitmap.class.getClassLoader());
        carPicLicense = in.readParcelable(Bitmap.class.getClassLoader());
    }

//    public static final Creator<User> CREATOR = new Creator<User>() {
//        @Override
//        public User createFromParcel(Parcel in) {
//            return new User(in);
//        }
//
//        @Override
//        public User[] newArray(int size) {
//            return new User[size];
//        }
//    };

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getIdentity1() {
        return identity1;
    }

    public void setIdentity1(int identity1) {
        this.identity1 = identity1;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Bitmap getCarPic1() {
        return carPic1;
    }

    public void setCarPic1(Bitmap carPic) {
        this.carPic1 = carPic1;
    }

    public Bitmap getCarPic2() {
        return carPic2;
    }

    public void setCarPic2(Bitmap carPic2) {
        this.carPic2 = carPic2;
    }

    public Bitmap getCarPic3() {
        return carPic3;
    }

    public void setCarPic3(Bitmap carPic3) {
        this.carPic3 = carPic3;
    }

    public boolean isDriver() {
        return model != null || !licenseNumber.isEmpty();
    }

    public Bitmap getCarPicLicense() {
        return carPicLicense;
    }

    public void setCarPicLicense(Bitmap carPicLicense) {
        this.carPicLicense = carPicLicense;
    }

    @Override
    public String toString() {
        return "email:" + email + " password:" + password + " gender:" + gender + " nickname:" + nickname +
                " birthday" + birthday + " address:" + address1 + " phone:" + phone1 + " identity:" + identity1;
    }

    @NonNull
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

//    todo
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(identity1);
//        dest.writeInt(gender);
//        dest.writeString(email);
//        dest.writeString(password);
//        dest.writeString(token);
//        dest.writeString(nickname);
//        dest.writeString(birthday);
//        dest.writeString(phone1);
//        dest.writeString(address1);
//        dest.writeString(model);
//        dest.writeString(licenseNumber);
//        dest.writeParcelable(carPic1, flags);
//        dest.writeParcelable(carPic2, flags);
//        dest.writeParcelable(carPic3, flags);
//        dest.writeParcelable(carPicLicense, flags);
//    }
}
