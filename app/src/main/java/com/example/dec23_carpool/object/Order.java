package com.example.dec23_carpool.object;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressLint("SimpleDateFormat")
public class Order implements Serializable {

    private int id;

    private String event;
    private String email;
    private String departureName;
    private String arrivePlaceName;

    private String departureDate;
    private String arriveDate;

    private String departureAddress;
    private String arrivePlaceAddress;

    private String strDepartureTime;
    private String strDepartureDate;
    private String strArriveTime;
    private String strArriveDate;

    private String time;
    private String costTime;
    private String distance;

    private int maxFare;
    private int minFare;
    private int carfare;
    private int memberLimit;
    private int seatRemained;
    private int travelState;

    private double departureLat;
    private double departureLng;
    private double arrivePlaceLat;
    private double arrivePlaceLng;

    //    private List<LatLng> latLngList; // can't serializable
    private boolean isPet;
    private boolean isSmoke;
    private boolean isLuggage;
    private boolean isEat;
    private Driver driver;

    public static class Driver implements Serializable {

        private String nickname;
        private int gender;
        private String phone1;
        private double score;

        public Driver() {
            gender = 2;
        }

        public Driver(String nickname, int gender, String phone1, double score) {
            this.nickname = nickname;
            this.gender = gender;
            this.phone1 = phone1;
            this.score = score;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getGender() {
            if (gender == 1) {
                return "女";
            } else {
                return "男";
            }
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

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    public Order() {
    }

    public Order(double departureLat, double departureLng,
                 double arrivePlaceLat, double arrivePlaceLng,
                 String distance, String costTime) {
        this.departureLat = departureLat;
        this.departureLng = departureLng;
        this.arrivePlaceLat = arrivePlaceLat;
        this.arrivePlaceLng = arrivePlaceLng;
        this.distance = distance;
        this.costTime = costTime;
    }

    public Order(String strDepartureTime, String strArriveTime,
                 String strDepartureDate, String departureName,
                 String arrivePlaceName, int carfare, String name,
                 int gender, double score) {
        this.strDepartureDate = strDepartureDate;
        this.departureName = departureName;
        this.strDepartureTime = strDepartureTime;
        this.arrivePlaceName = arrivePlaceName;
        this.strArriveTime = strArriveTime;
        this.carfare = carfare;
        driver = new Driver(name, gender, "0911", score);
    }

    private String turnToDateAndTime(long second) {
        return turnToDate(second) + "T" + turnToTime(second) + ":00";
    }

    private String turnToDate(long second) {
        Date date = new Date(second * 1000);
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }

    private String turnToTime(long second) {
        Date date = new Date(second * 1000);
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public String getDepartureDate() {
        return turnToDateAndTime(Long.valueOf(departureDate));
    }

    public long getLongDepartureDate() {
//        long date = (Long.valueOf(departureDate));
        long date = (Long.valueOf(departureDate) - TimeUnit.HOURS.toSeconds(8));
        return date;
    }

    public String getStrDepartureDate() {
        long date = Long.valueOf(departureDate) - TimeUnit.HOURS.toSeconds(8);
        return strDepartureDate = (strDepartureDate == null) ?
                turnToDate(date) : strDepartureDate;
    }

    public String getStrDepartureTime() {
        long date = (Long.valueOf(departureDate) - TimeUnit.HOURS.toMillis(8));
        return strDepartureTime = (strDepartureTime == null) ?
                turnToTime(date) : strDepartureTime;
    }

    public String getStrArriveTime() {
        long date = (Long.valueOf(arriveDate) - TimeUnit.HOURS.toMillis(8));
        return strArriveTime = (strArriveTime == null) ?
                turnToTime(date) : strArriveTime;
    }

    public String getCostTime() {
        long seconds = Long.valueOf(arriveDate) - Long.valueOf(departureDate);
        long totalMinutes = seconds / 60;
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        costTime = String.format("%02d:%02d", hours, minutes);
        return costTime;
    }

    public void setDepartureDate(String date, String time) {
        this.departureDate = date + "T" + time + ":00";
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public String getArriveTime() {
//        return strArriveTime;
//    }

//    public String getDepartureTime() {
//        return strDepartureTime;
//    }

    public String getDepartureName() {
        return departureName;
    }

    public void setDepartureName(String departureName) {
        this.departureName = departureName;
    }

    public String getArrivePlaceName() {
        return arrivePlaceName;
    }

    public void setArrivePlaceName(String arrivePlaceName) {
        this.arrivePlaceName = arrivePlaceName;
    }

    public int getMaxFare() {
        return maxFare;
    }

    public void setMaxFare(int maxFare) {
        this.maxFare = maxFare;
    }


    public int getMemberLimit() {
        return memberLimit;
    }

    public void setMemberLimit(int memberLimit) {
        this.memberLimit = memberLimit;
    }

    public boolean isPet() {
        return isPet;
    }

    public void setPet(boolean pet) {
        isPet = pet;
    }

    public boolean isSmoke() {
        return isSmoke;
    }

    public void setSmoke(boolean smoke) {
        isSmoke = smoke;
    }

    public boolean isLuggage() {
        return isLuggage;
    }

    public void setLuggage(boolean luggage) {
        isLuggage = luggage;
    }

    public boolean isEat() {
        return isEat;
    }

    public void setEat(boolean eat) {
        isEat = eat;
    }

    public double getDepartureLat() {
        return departureLat;
    }

    public void setDepartureLat(double departureLat) {
        this.departureLat = departureLat;
    }

    public double getDepartureLng() {
        return departureLng;
    }

    public void setDepartureLng(double departureLng) {
        this.departureLng = departureLng;
    }

    public double getArrivePlaceLat() {
        return arrivePlaceLat;
    }

    public void setArrivePlaceLat(double arrivePlaceLat) {
        this.arrivePlaceLat = arrivePlaceLat;
    }

    public double getArrivePlaceLng() {
        return arrivePlaceLng;
    }

    public void setArrivePlaceLng(double arrivePlaceLng) {
        this.arrivePlaceLng = arrivePlaceLng;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }


    public void setStrDepartureTime(String strDepartureTime) {
        this.strDepartureTime = strDepartureTime;
    }


    public void setStrDepartureDate(String strDepartureDate) {
        this.strDepartureDate = strDepartureDate;
    }


    public void setStrArriveTime(String strArriveTime) {
        this.strArriveTime = strArriveTime;
    }

    public String getStrArriveDate() {
        if (arriveDate != null) {
            return turnToDate(Long.valueOf(arriveDate));
        } else {
            return strArriveDate;
        }
    }

    public void setStrArriveDate(String strArriveDate) {
        this.strArriveDate = strArriveDate;
    }


    public void setCostTime(String costTime) {
        this.costTime = costTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getMinFare() {
        return minFare;
    }

    public int getCarfare() {
        return carfare;
    }

    public void setCarfare(int carfare) {
        this.carfare = carfare;
    }

    public void setMinFare(int minFare) {
        this.minFare = minFare;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeatRemained() {
        return seatRemained;
    }

    public void setSeatRemained(int seatRemained) {
        this.seatRemained = seatRemained;
    }

    public String getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(String departureAddress) {
        this.departureAddress = departureAddress;
    }

    public String getArrivePlaceAddress() {
        return arrivePlaceAddress;
    }

    public void setArrivePlaceAddress(String arrivePlaceAddress) {
        this.arrivePlaceAddress = arrivePlaceAddress;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getTravelState() {
        return travelState;
    }

    public void setTravelState(int travelState) {
        this.travelState = travelState;
    }
}
