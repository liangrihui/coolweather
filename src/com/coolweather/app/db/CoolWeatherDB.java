package com.coolweather.app.db;

import java.util.ArrayList;
import java.util.List;

import com.coolweather.app.model.City;
import com.coolweather.app.model.County;
import com.coolweather.app.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CoolWeatherDB {

	//���ݿ���
	public static final String DB_NAME="cool_weather";
	//���ݰ汾
	public static final int VERSION =1;
	private static CoolWeatherDB coolWeatherDB;
	private SQLiteDatabase db;
	//�����췽��˽�л�����֤ȫ��ֻ��һ��CoolWeatherDB
	private CoolWeatherDB( Context context){
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db= dbHelper.getWritableDatabase();
	}
	//��ȡCoolWeatherDB��ʵ��
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolWeatherDB==null){
			coolWeatherDB=new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	//��Province ʵ���洢�����ݿ�
	public void saveProvince(Province province){
		if(province!=null){
			ContentValues values= new ContentValues();
			values.put("provinceName", province.getProvinceName());
			values.put("provinceCode", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	//�����ݿ��ȡȫ������ʡ��Ϣ
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(province);
			} while (cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
		
	}
	//��City ʵ���洢�����ݿ�
	public void saveCity(City city){
		if(city!=null){
			ContentValues values=new ContentValues();
			values.put("cityName", city.getCityName());
			values.put("cityCede", city.getCityCode());
			values.put("provinceId", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	// �����ݿ��ȡĳʡ�����г���
	public List<City> loadCity( int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToFirst()){
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProinceId(provinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	//��County ʵ���洢���ݿ�
	public void saveCounty(County county){
		if(county!=null){
			ContentValues values=new ContentValues();
			values.put("countyName", county.getCountyName());
			values.put("countyCode", county.getCountyCode());
			values.put("cityId", county.getCityId());
			db.insert("County", null, values);
		}
	}
	//��ȡĳ���е��ؼ�����
	public List<County> loadCounty(int cityId){
		List <County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{"cityId"},null, null, null);
		if(cursor.moveToFirst()){
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	
}
