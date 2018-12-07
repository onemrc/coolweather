package com.example.one.coolweather.util;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by one on 2018/11/28.
 * 描述：网络请求工具类
 */

public class HttpUtil {

    /**
     * 发起HTTP请求
     * @param address
     * @param callback
     */
    public static void sendOkHttpRequest(String address,Callback callback){
        OkHttpClient client = new OkHttpClient();

        client.newBuilder().connectTimeout(10, TimeUnit.MINUTES).readTimeout(20,TimeUnit.MINUTES).build();

        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

}
