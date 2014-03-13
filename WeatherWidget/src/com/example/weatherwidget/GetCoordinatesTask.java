package com.example.weatherwidget;




import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;


public class GetCoordinatesTask extends AsyncTask<Void, Integer, Location> {

	private double Lat=0;
	private double Lon=0;
	private LocationManager locManager;
	private boolean isConnected=false;
	
	
	private Context mContext;
	private RemoteViews view;
	public GetCoordinatesTask(Context context,RemoteViews view)
	{
		mContext=context;
		this.view=view;
	}

	LocationListener listener=new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			Location loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			onLocationChanged(loc);
			Log.i("Weather","Network provider is enabled");
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			Log.i("Weather","Network provider is disabled");
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			if (location!=null)
			{
				Lat=location.getLatitude();
				Lon=location.getLongitude();
				Log.i("Weather", Lat+" "+Lon);
			}
			Log.i("Weather","Location changed");
		}
	};
	protected void onPreExecute() {
		
		view.setViewVisibility(R.id.mProgressBar,ProgressBar.VISIBLE);
		String provider=LocationManager.NETWORK_PROVIDER;
		
		locManager=(LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		if (locManager.isProviderEnabled(provider))
		{
		locManager.requestLocationUpdates(provider, 0, 0, listener);
		
		Log.i("Weather","Requested");
		listener.onProviderEnabled(provider);
		}
		else
		{
					
			isConnected=true;
			
			
			Log.i("Weather","Unabled");
			listener.onProviderDisabled(provider);
		}
	};

	@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			
		view.setProgressBar(R.id.mProgressBar, 100000, values[0], false);
		}
		@Override
		protected Location doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Location loc=null;
			if (locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			{
			 loc=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			listener.onLocationChanged(loc);
			}
			return loc;
			
		}
		@Override
			protected void onPostExecute(Location loc) {
				// TODO Auto-generated method stub
				if (loc!=null)
				{
					Lat=loc.getLatitude();
					Lon=loc.getLongitude();
					Log.i("Weather", Lat+" "+Lon);
				}
				locManager.removeUpdates(listener);
				if (isConnected)
				{
					//locClient.disconnect();
					isConnected=false;
				}
				Log.i("Weather", Lat+" "+Lon);
				
				Thread thread=new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						WeatherLoadTask task=	(WeatherLoadTask) new WeatherLoadTask(mContext,view).execute(Lat,Lon);
					try {
						task.get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					}
				});
				thread.start();
			
			}

}
