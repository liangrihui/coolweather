package com.coolweather.app.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.app.db.CoolWeatherDB;
import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

public class Utility {
	//解析和处理服务器返回的数据
	public synchronized static boolean handleProvincesResponse(CoolWeatherDB coolWeatherDB ,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvince = response.split(",");
			if(allProvince != null&&allProvince.length>0){
				for(String p : allProvince){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					coolWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
		
	}
	//解析和处理返回的市级城市
	public synchronized static boolean handleCityResponse(CoolWeatherDB coolWeatherdb,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCity = response.split(",");
			if(allCity!=null&&allCity.length>0){
				for(String c : allCity){
					String[] array =  c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProinceId(provinceId);
					coolWeatherdb.saveCity(city);
							
				}
				return true;
				
			}
		}
		
		return false;
		
	}
	//处理从服务器返回的县级城市数据
	public static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounty = response.split(",");
			if(allCounty!=null&&allCounty.length>0)
			for(String ct : allCounty ){
				String[] array = ct.split("\\|");
				County county = new County();
				county.setCountyCode(array[0]);
				county.setCountyName(array[1]);
				county.setCityId(cityId);
				coolWeatherDB.saveCounty(county);
			}
			return true;
		}
		return false;
	}
	//解析服务器的JSON数据，存储到本地
	public static void handleWeatherResponse(Context context,String response){
		try {
			Log.e("Utilty", "解析Json 数据");
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo= jsonObject.getJSONObject("weatherinfo");
			String cityName=weatherInfo.getString("city");
			String weatherCode=weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2= weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			savWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void savWeatherInfo(Context context,String cityName,String weatherCode,String temp1,String temp2,String weatherDesp,String publishTime){
		Log.e("Utilty", "存数据"+cityName+weatherCode+temp1+ temp2+weatherDesp+ publishTime);
	SimpleDateFormat sdf= new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
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