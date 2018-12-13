package com.example.one.coolweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.one.coolweather.gson.Future;
import com.example.one.coolweather.gson.Weather;
import com.example.one.coolweather.util.ApiUtil;
import com.example.one.coolweather.util.HttpUtil;
import com.example.one.coolweather.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by one on 2018/12/7.
 * 描述：天气信息活动类
 */

public class WeatherActivity extends AppCompatActivity {

    private ScrollView weatherLayout;

    private TextView titleView;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

//    private TextView aqiText;
//
//    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView travelText;

    private TextView dressingAdviceText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //各种控件初始化
        weatherLayout = findViewById(R.id.weather_layout);
        titleView = findViewById(R.id.title_city);
        titleUpdateTime = findViewById(R.id.title_update_time);
        degreeText = findViewById(R.id.now).findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.now).findViewById(R.id.weather_text);
        forecastLayout = findViewById(R.id.forecast_layout);
//        aqiText = findViewById(R.id.api_text);
//        pm25Text = findViewById(R.id.pm25_text);
        comfortText = findViewById(R.id.comfort_text);
//        sportText = findViewById(R.id.sport_text);
        carWashText = findViewById(R.id.car_wash_text);
        travelText = findViewById(R.id.travel_text);
        dressingAdviceText = findViewById(R.id.dressing_advice_text);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String weatherString =  preferences.getString("weather",null);
        String newDistrict =  preferences.getString("newDistrict",null);
        String oldDistrict = getIntent().getStringExtra("districtName");

        if (weatherString != null && oldDistrict.equals(newDistrict)){
            //从缓存直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);

            //显示天气
            showWeatherInfo(weather);

        }else {
            String district = getIntent().getStringExtra("districtName");
            weatherLayout.setVisibility(View.VISIBLE);

            //从服务器查询
            requestWeather(district);
        }
    }

    /**
     * 根据地区id 请求天气信息
     * @param district
     */
    public void requestWeather(final String district){
        //天气请求Url
        String weatherUrl = ApiUtil.weatherAddress+"?format=2&cityname="+district+"&key="+ApiUtil.key;

        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气请求失败",Toast.LENGTH_LONG);
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = Utility.handleWeatherResponse(responseText);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null){

                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();

                            editor.putString("weather",responseText);
                            editor.putString("newDistrict",district);
                            editor.apply();//执行

                            //显示天气
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

    /**
     * 处理并展示Weather实体数据
     * @param weather weather
     */
    private void showWeatherInfo(Weather weather){
        String districtName = weather.today.getCity();
        String updateTime = weather.sk.getTime();
        String degree = weather.today.getTemperature();

        titleView.setText(districtName);
        titleUpdateTime.setText(updateTime);
//        weatherInfoText.setText(degree);

        forecastLayout.removeAllViews();
        for (Future future:weather.futureList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);

            TextView dateText = view.findViewById(R.id.date_text);
            TextView infoText = view.findViewById(R.id.info_text);
            TextView windText = view.findViewById(R.id.wind_text);

            TextView temperatureText = view.findViewById(R.id.temperature_text);

            dateText.setText(future.getWeek());
            if (weather.today.getWeek() .equals(future.getWeek())){//今天
                dateText.setText("今天");
            }


            windText.setText(future.getWind());
            infoText.setText(future.getWeather());
            temperatureText.setText(future.getTemperature());

            forecastLayout.addView(view);
        }

        if (!weather.today.getComfort_index().equals("")){
            comfortText.setText("舒适度：" + weather.today.getComfort_index());
        }
        if (!weather.today.getDrying_index().equals("")){
            //TODO 干燥指数
        }
        carWashText.setText("洗车指数：" +weather.today.getWash_index());

        degreeText.setText(weather.sk.getTemp()+"℃");
        travelText.setText( "旅游指数：" + weather.today.getTravel_index());

        dressingAdviceText.setText("穿衣建议： " + weather.today.getDressing_advice());

        weatherInfoText.setText(weather.today.getWeather());
        weatherLayout.setVisibility(View.VISIBLE);

    }
}
