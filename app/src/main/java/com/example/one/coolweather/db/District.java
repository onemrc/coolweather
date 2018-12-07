package com.example.one.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * Created by one on 2018/12/7.
 * 描述：区级
 */

public class District extends DataSupport {
    private int id;
    private String name;
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
