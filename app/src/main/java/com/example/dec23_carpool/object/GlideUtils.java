package com.example.dec23_carpool.object;


import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.Map;

public class GlideUtils {

    private static String userphotoUrl = "http://35.194.149.254:8080/api/User/Download?";
    private static String orderPicUrl = "http://35.194.149.254:8080/api/User/DownloadTravelPic?";

    private GlideUtils() {
    }

    public static GlideUrl configUserPhotoUrl(String token, Map<String, String> queryParam) {
        StringBuilder queryUrl = new StringBuilder(userphotoUrl);
        for (String key : queryParam.keySet()) {
            String value = queryParam.get(key);
            queryUrl.append(key).append("=")
                    .append(value).append("&");
        }
        queryUrl.deleteCharAt(queryUrl.length() - 1);
        String finalUrl = queryUrl.toString();
        return new GlideUrl(finalUrl,
                new LazyHeaders.Builder()
                        .addHeader("token", token)
                        .build());
    }

    public static GlideUrl configOrderPictureUrl(Map<String, String> queryParam){
        StringBuilder queryUrl = new StringBuilder(orderPicUrl);
        for (String key : queryParam.keySet()) {
            String value = queryParam.get(key);
            queryUrl.append(key).append("=")
                    .append(value).append("&");
        }
        queryUrl.deleteCharAt(queryUrl.length() - 1);
        String finalUrl = queryUrl.toString();
        return new GlideUrl(finalUrl);
    }
}
