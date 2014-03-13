package com.example.weatherwidget;


import java.util.concurrent.ExecutionException;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;


public class WeatherUpdateService extends Service {

	public boolean isNetworkEnabled=false;
	Context mContext;
	RemoteViews remoteView;
	
	public WeatherUpdateService()
	{
		
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Log.i("Test", "Service started");
		mContext=getApplicationContext();
		 AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
		 int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		 for (int widgetId : allWidgetIds) {
		 remoteView=new RemoteViews(getApplicationContext().getPackageName(),R.layout.weather_layout);
		
		 if (GetIsNetworkEnabled())
			{
			 SetVisibleViews();
			 
				// TODO Auto-generated method stub
				GetCoordinatesTask task =(GetCoordinatesTask) new GetCoordinatesTask(mContext,remoteView).execute();
				try {
					task.get();
					appWidgetManager.updateAppWidget(widgetId, remoteView);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			else
			{
				SetInvisibleViews();
				
			}
		 
		 appWidgetManager.updateAppWidget(widgetId, remoteView);
		 }
		
		return super.onStartCommand(intent, flags, startId);
	}
	public void SetInvisibleViews()
	{
		remoteView.setViewVisibility(R.id.tvAttention,TextView.VISIBLE);
		remoteView.setViewVisibility(R.id.tvTemperature,TextView.INVISIBLE);
		remoteView.setViewVisibility(R.id.tvDescription,TextView.INVISIBLE);
		remoteView.setViewVisibility(R.id.tvPoint,TextView.INVISIBLE);
		remoteView.setViewVisibility(R.id.tvLocation,TextView.INVISIBLE);
		remoteView.setViewVisibility(R.id.mImageView, ImageView.INVISIBLE);
		
	}
	public void SetVisibleViews()
	{
		remoteView.setViewVisibility(R.id.tvAttention,TextView.INVISIBLE);
		remoteView.setViewVisibility(R.id.tvTemperature,TextView.VISIBLE);
		remoteView.setViewVisibility(R.id.tvDescription,TextView.VISIBLE);
		remoteView.setViewVisibility(R.id.tvPoint,TextView.VISIBLE);
		remoteView.setViewVisibility(R.id.tvLocation,TextView.VISIBLE);
		remoteView.setViewVisibility(R.id.mImageView, ImageView.VISIBLE);		
		
	}

	public boolean GetIsNetworkEnabled()
	{
		ConnectivityManager cm=(ConnectivityManager)getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo=cm.getActiveNetworkInfo();
		if (netInfo!=null)
		{
			if (netInfo.isConnected())
			{
				isNetworkEnabled=true;
			}
			
		}
		else
		{
			isNetworkEnabled=false;
		}
		return isNetworkEnabled;
	}
}
