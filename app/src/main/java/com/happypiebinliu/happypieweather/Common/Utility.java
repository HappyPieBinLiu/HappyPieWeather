package com.happypiebinliu.happypieweather.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.happypiebinliu.happypieweather.DBUtil.HappyPieWeatherDB;
import com.happypiebinliu.happypieweather.Model.City;
import com.happypiebinliu.happypieweather.Model.County;
import com.happypiebinliu.happypieweather.Model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ope001 on 2016/10/07.
 */

public class Utility {

    /**
     *Province
     **/
    public synchronized static boolean handleProvincesResponse(HappyPieWeatherDB happyPieWeatherDB, String response){

        if (!TextUtils.isEmpty(response)){
            String[] allProvinces = response.split(ComConst.COMMA);
            if (allProvinces != null && allProvinces.length > 0){
                for (String p : allProvinces){
                    String[] array = p.split(ComConst.SPLIT_STR);
                    Province province = new Province();
                    province.setProvinceCode(array[0]);
                    province.setProvinceName(array[1]);
                    happyPieWeatherDB.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }
    /**
     *City
     **/
    public static boolean handleCitiesResponse(HappyPieWeatherDB happyPieWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(ComConst.COMMA);
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split(ComConst.SPLIT_STR);
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    happyPieWeatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }
    /**
     *County
     **/
    public static boolean handleCountiesResponse(HappyPieWeatherDB happyPieWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(ComConst.COMMA);
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split(ComConst.SPLIT_STR);
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    happyPieWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }
    public static void handleWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            // get weather info
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");

            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void saveWeatherInfo(Context context, String cityName, String weatherCode,
                                        String temp1, String temp2, String weatherDesp, String publishTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
