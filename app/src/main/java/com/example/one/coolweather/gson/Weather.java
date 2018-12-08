package com.example.one.coolweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by one on 2018/12/7.
 * 描述：封装天气各个实体类
 */

public class Weather {

    public Sk sk;


    public Today today;


    @SerializedName("future")
    public List<Future> futureList;


}
