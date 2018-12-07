package com.example.one.coolweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.one.coolweather.db.City;

import com.example.one.coolweather.db.District;
import com.example.one.coolweather.db.Province;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

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
}
