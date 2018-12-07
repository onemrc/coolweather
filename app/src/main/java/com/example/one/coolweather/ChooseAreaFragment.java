package com.example.one.coolweather;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.one.coolweather.db.City;
//import com.example.one.coolweather.db.County;
import com.example.one.coolweather.db.District;
import com.example.one.coolweather.db.Province;
import com.example.one.coolweather.util.ApiUtil;
import com.example.one.coolweather.util.HttpUtil;
import com.example.one.coolweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;




/**
 * Created by one on 2018/11/28.
 * 描述：遍历省 市 数据
 */

public class ChooseAreaFragment extends Fragment {
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    public static final int LEVEL_DISTRICT =3;

    private ProgressDialog progressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView listView;

    private ArrayAdapter<String> adapter;

    private List<String> dataList = new ArrayList<>();

    /**
     * 省列表
     */
    private List<Province> provinceList;

    /**
     * 市列表
     */
    private List<City> cityList;

    /**
     * 县列表
     */
//    private List<County> countyList;

    /**
     * 区（县）列表
     */
    private List<District> districtList;

    /**
     * 选择的省份
     */
    private Province selectProvince;

    /**
     * 选择的城市
     */
    private City selectCity;

    /**
     * 当前选中的级别
     */
    private int currentLevel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area, container, false);
        titleText = view.findViewById(R.id.title_text);
        backButton = view.findViewById(R.id.back_button);
        listView = view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
//                    queryCounties();
                    queryDistrict();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel == LEVEL_DISTRICT) {
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
//                    queryProvinces();
                    queryDistrict();
                }
            }
        });

        //加载省级数据
//        DataSupport.deleteAll(City.class);
        queryProvinces();
    }

    /**
     * 查询全国所有的省，优先从数据库查，没有就去服务器上查
     */
    private void queryProvinces() {
        //头布局标题
        titleText.setText("中国");

        //隐藏返回按钮
        backButton.setVisibility(View.GONE);

        //读取数据，然后显示到界面
//        DataSupport.deleteAll(Province.class);
        provinceList = DataSupport.findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            String address = ApiUtil.cityAddress+"?key="+ApiUtil.key;
//            String address = "http://guolin.tech/api/chisna";
            queryFormServer(address, "province");
        }
    }

    /**
     * 从服务器上查询省市县数据
     *
     * @param address 地址
     * @param type    类型
     */
    private void queryFormServer(String address, final String type) {
        showProgressDialog();

        //发送请求
        HttpUtil.sendOkHttpRequest(address, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(responseText);
                } else if ("city".equals(type)) {
                    result = Utility.handleCityResponse(responseText, selectProvince.getId(),selectProvince.getProvinceName());
                }
//                else if ("county".equals(type)) {
//                    result = Utility.handleCountyResponse(responseText, selectCity.getId());
//                }
                else if ("district".equals(type)){
                    result = Utility.handleDistrictResponse(responseText,selectCity.getId(),selectCity.getCityName());
                }

                if (result) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();

                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)) {
                                queryCities();
                            }else if ("district".equals(type)){
                                queryDistrict();
                            }
//                            else if ("county".equals(type)) {
//                                queryCounties();
//                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("tag","错误信息："+e.getMessage());
                //通过runOnUtilThread()方法回到主线程处理逻辑
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载..");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }


    /**
     * 查询选择的省内所有的市，优先从数据库查，没有就去服务器上查
     */
    private void queryCities() {
        titleText.setText(selectProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);

//        DataSupport.deleteAll(City.class);

        cityList = DataSupport.where("provinceId = ?", String.valueOf(selectProvince.getId())).find(City.class);
        if (cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
//            int provinceCode = selectProvince.getId();
//            String address = "http://guolin.tech/api/china/" + provinceCode;
            String address = ApiUtil.cityAddress+"?key="+ApiUtil.key;
            queryFormServer(address, "city");
        }
    }

//    /**
//     * 查询选择的市内所有的县，优先从数据库查，没有就去服务器上查
//     */
//    private void queryCounties() {
//        titleText.setText(selectCity.getCityName());
//        backButton.setVisibility(View.VISIBLE);
//        countyList = DataSupport.where("cityid = ?", String.valueOf(selectCity.getId())).find(County.class);
//        if (countyList.size() > 0) {
//            dataList.clear();
//            for (County county : countyList) {
//                dataList.add(county.getCountyName());
//            }
//            adapter.notifyDataSetChanged();
//            listView.setSelection(0);
//            currentLevel = LEVEL_COUNTY;
//        } else {
//            int provinceCode = selectProvince.getId();
//            int cityCode = selectCity.getCityCode();
//            String address = "http://guolin.tech/api/china/" + provinceCode + "/" + cityCode;
//            queryFormServer(address, "county");
//        }
//    }


    /**
     * 查询选择的市内所有的区（县），优先从数据库查，没有就去服务器上查
     */
    private void queryDistrict() {
        titleText.setText(selectCity.getCityName());
        backButton.setVisibility(View.VISIBLE);

//        DataSupport.where("provinceId = ?", String.valueOf(selectProvince.getId())).find(City.class);
        districtList = DataSupport.where("cityId = ?", String.valueOf(selectCity.getId())).find(District.class);

        if (districtList.size() > 0) {
            dataList.clear();
            for (District district : districtList) {
                dataList.add(district.getName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_DISTRICT;
        } else {

            String address = ApiUtil.cityAddress+"?key="+ApiUtil.key;
            queryFormServer(address, "district");
        }
    }
}
