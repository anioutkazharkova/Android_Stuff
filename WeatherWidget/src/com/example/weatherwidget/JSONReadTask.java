package com.example.weatherwidget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;


public class JSONReadTask extends AsyncTask<String, Integer, WeatherInfo> {

	WeatherInfo weather;
	private static final int KELVIN=273;
	private static final String prefix="w";
	
	Context mContext;
	RemoteViews view;
	
	public JSONReadTask(Context context,RemoteViews view)
	{
		mContext=context;
		this.view=view;
	}
	
	@Override
	protected WeatherInfo doInBackground(String... params) {
		// TODO Auto-generated method stub
		String json=params[0];
		WeatherInfo	weatherInfo=null;
		
		try {
			JSONObject topJsonObject = (JSONObject) new JSONTokener(
					json).nextValue();
			
			//Process as arrays
			JSONArray weather=topJsonObject.getJSONArray("weather");
			JSONObject weatherElement=(JSONObject)weather.get(0);
			String icon=weatherElement.getString("icon");
			String description=weatherElement.getString("description");
			
			//result+=responseObject.getString("weather");			
			JSONObject temperature=topJsonObject.getJSONObject("main");
			int temp=(int)(temperature.getDouble("temp")-KELVIN);
			int temp_min=(int)(temperature.getDouble("temp_min")-KELVIN);
			int temp_max=(int)(temperature.getDouble("temp_max")-KELVIN);
			
			JSONObject wind=topJsonObject.getJSONObject("wind");
			double wind_speed=wind.getDouble("speed");
			
			String location=topJsonObject.getString("name");
			
		weatherInfo=new WeatherInfo(icon, description, temp, temp_min, temp_max, wind_speed, location);
			
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return weatherInfo;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		view.setProgressBar(R.id.mProgressBar, 100000, values[0], false);
	}
	@Override
	protected void onPostExecute(WeatherInfo weatherInfo) {
		// TODO Auto-generated method stub
		Log.i("Test", "JSON result");
		Log.i("Test",weatherInfo.toString());
		view.setViewVisibility(R.id.mProgressBar, ProgressBar.INVISIBLE);
		view.setTextViewText(R.id.tvLocation, weatherInfo.getLocation());
		view.setTextViewText(R.id.tvTemperature, weatherInfo.getTemp().toString());
		view.setTextViewText(R.id.tvDescription, weatherInfo.getDescription());
		view.setTextViewText(R.id.tvPoint, "o");
		
		String imageName=prefix+weatherInfo.getIcon();
		view.setImageViewResource(R.id.mImageView,mContext.getResources().getIdentifier("com.example.weatherwidget:drawable/"+imageName, null, null));
		ComponentName thisWidget = new ComponentName(mContext, WeatherWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
        manager.updateAppWidget(thisWidget, view);

		
		String action="stopService";
	mContext.sendBroadcast(new Intent(action));
	}

}
