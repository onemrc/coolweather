package com.example.one.coolweather.gson;

/**
 * Created by one on 2018/12/7.
 * 描述：封装风力信息
 */

public class Sk {

    //当前温度
    private String temp;

    //当前风向
    private String wind_direction;

    //当前风力
    private String wind_strength;

    //当前湿度
    private String humidity;

    //更新时间
    private String time;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public void setWind_direction(String wind_direction) {
        this.wind_direction = wind_direction;
    }

    public String getWind_strength() {
        return wind_strength;
    }

    public void setWind_strength(String wind_strength) {
        this.wind_strength = wind_strength;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
