package com.example.one.coolweather.gson;

/**
 * Created by one on 2018/12/8.
 * 描述：TODO
 */

public class Future {
    /*温度*/
    private String temperature;

    /*天气情况*/
    private String weather;

    /*风力*/
    private String wind;

    /*星期几*/
    private String week;

    /*日期*/
    private String date;

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
