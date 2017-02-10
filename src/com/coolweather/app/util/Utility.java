package com.coolweather.app.util;

import android.text.TextUtils;

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
	
	
}
