package com.example.one.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.one.coolweather.db.City;

import com.example.one.coolweather.db.District;
import com.example.one.coolweather.db.Province;
import com.example.one.coolweather.gson.Weather;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by one on 2018/11/28.
 * 描述：解析数据，并存到数据库中
 */

public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     *
     * @param response
     * @return
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
//                Province del = new Province();
//                del.delete();

                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");

                String provinceName = "";
                int provinceId = 0;
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject result = jsonArray.getJSONObject(i);
                    if (!provinceName .equals(result.getString("province"))) {
                        provinceName = result.getString("province");
                        Province province = new Province();
                        province.setId(++provinceId);
                        province.setProvinceName(provinceName);
                        province.save();
                    }
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     *
     * @param response
     * @param provinceId
     * @return
     */
    public static boolean handleCityResponse(String response, int provinceId,String provinceName) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject json = new JSONObject(response);
                JSONArray resultArray = json.getJSONArray("result");

                String cityName = "";
                int cityId = 0;



                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject result = resultArray.getJSONObject(i);
                    if (!cityName.equals(result.getString("city")) && provinceName.equals(result.getString("province"))) {
                        cityName = result.getString("city");
                        City city = new City();
                        city.setId(++cityId);
                        city.setProvinceId(provinceId);
                        city.setCityName(cityName);
                        city.save();
                    }
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

//    /**
//     * 解析和处理服务器返回的县级数据
//     * @param response
//     * @param cityId
//     * @return
//     */
//    public static boolean handleCountyResponse(String response,int cityId){
//        if (!TextUtils.isEmpty(response)){
//            try{
//                JSONArray allCities = new JSONArray(response);
//                for (int i=0;i<allCities.length();i++){
//                    JSONObject countyObject = allCities.getJSONObject(i);
//                    County county = new County();
//                    county.setCountyName(countyObject.getString("name"));
//                    county.setWeatherId(countyObject.getString("weather_id"));
//                    county.setCityId(cityId);
//                    county.save();
//                }
//                return true;
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的区级数据
     *
     * @param response 相应
     * @param cityId   城市
     * @return
     */
    public static boolean handleDistrictResponse(String response, int cityId,String cityName) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject json = new JSONObject(response);
                JSONArray resultArray = json.getJSONArray("result");
                for (int i = 0; i < resultArray.length(); i++) {
                    JSONObject result = resultArray.getJSONObject(i);

                    if (cityName.equals(result.getString("city"))){
                        District district = new District();
                        district.setId(Integer.valueOf(result.getString("id")));
                        district.setName(result.getString("district"));//district
                        district.setCityId(cityId);
                        district.save();
                    }
                }
                return true;
            } catch (Exception e) {
                Log.d("tag","报错：handleDistrictResponse");
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析JSON数据 成 Weather实体类
     * @param response 相应信息
     * @return Weather
     */
    public static Weather handleWeatherResponse(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONObject result = jsonObject.getJSONObject("result");
//            JSONArray jsonArray = jsonObject.getJSONObject("result").getJSONArray("future");
            String weatherContent = result.toString();




            //TODO Debug
            Weather weather = new Gson().fromJson(weatherContent,Weather.class);


            return weather;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
