package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setReadTimeout(8000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader( new InputStreamReader(in));
					StringBuilder resqonse = new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
						resqonse.append(line);
					}
					if(listener!=null){
						listener.onFinish(resqonse.toString());//�ص�OnFinish����
					}
					
				} catch (Exception e) {
					if(listener!=null){
						listener.onError(e);//�ص�onError
					}
				}
				finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
