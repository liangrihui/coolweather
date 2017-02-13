package com.coolweather.app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coolweather.app.R;
import com.coolweather.app.util.HttpCallbackListener;
import com.coolweather.app.util.HttpUtil;
import com.coolweather.app.util.Utility;
public class WeatherActivity extends Activity implements OnClickListener{
	private LinearLayout weatherInfoLayout;
	//城市
	private TextView cityNameText;
	//时间
	private TextView publishText;
	//天气信息
	private TextView weatherDespText;
	//气温1
	private TextView temp1Text;
	//气温2
	private TextView temp2Text;
	//当前日期
	private TextView currentDateText;
	//切换城市
	private Button switchCity;
	//更新天气按钮
	private Button refreshWeather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView)findViewById(R.id.publish_text);
		weatherDespText =(TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDateText = (TextView)findViewById(R.id.current_date); 
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		String countyCode = getIntent().getStringExtra("county_code");
		
		Log.e("weatherA",countyCode);
		if(!TextUtils.isEmpty(countyCode)){
			publishText.setText("同步中......");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather();
		}
	}
	
	//查询县级代号所对应的天气
	private void queryWeatherCode(String countyCode){
		Log.e("weatherA","查询县级天气代码："+countyCode);
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	private void queryWeatherInfo(String weatherCode){
		Log.e("weatherA","查询县级天气内容：weatherCode="+weatherCode);
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	//查询天气代号对应的天气
	private void queryFromServer(final String address,final String type) {
		Log.e("weatherA","地址："+address+"类型："+type);
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public void onFinish(final String response) {
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						String[] array = response.split("\\|");
						if(array != null&&array.length==2){
							String weatherCode = array[1];
							Log.e("weatherA","天气代码地址："+weatherCode);
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					Log.e("weatherA","地址："+address+"类型："+type);
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showWeather();
						}
					});
				}
				
					
			}
			
			@Override
			public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("同步失败");
					}
				});
			}
		});
	}
//从sharedPreferences文件中存储天气信息
	private void showWeather() {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Log.e("weatherA","读起本地信息"+prefs.getString("city_name", "")+prefs.getString("temp1", "")
				+prefs.getString("temp2", "")+prefs.getString("weather_desp", "")+prefs.getString("current_date", ""));
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
	}

@Override
public void onClick(View arg0) {
	switch(arg0.getId()){
	case R.id.switch_city:
		Intent intent = new Intent(this,ChooseAreaActivity.class);
		intent.putExtra("from_weather_activity", true);
		startActivity(intent);
		finish();
		break;
	case R.id.refresh_weather:
		publishText.setText("同步中....");
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String weatherCode = prefs.getString("weather_code", "");
		if(!TextUtils.isEmpty(weatherCode)){
			queryWeatherInfo(weatherCode);
		}
		break;
	default:
		break;
	}
}
}
