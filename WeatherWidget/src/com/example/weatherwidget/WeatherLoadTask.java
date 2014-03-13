package com.example.weatherwidget;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherLoadTask extends AsyncTask<Double, Integer, String> {

	private static final String TAG = "HttpGetTask";

	private String URL="";
	Context mContext;
	RemoteViews view;
	
	public WeatherLoadTask(Context context, RemoteViews view)
	{
		mContext=context;
		this.view=view;
	}
	
	@Override
	protected String doInBackground(Double... params) {
		
		double lat=params[0];
		double lon=params[1];
		
		URL="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon;
		
		String data = "";
		HttpURLConnection ñonnection = null;

		try {
			ñonnection = (HttpURLConnection) new URL(URL).openConnection();

			InputStream in = new BufferedInputStream(ñonnection.getInputStream());

			data = readStream(in);

		} catch (MalformedURLException exception) {
			Log.e(TAG, "MalformedURLException");
		} catch (IOException exception) {
			Log.e(TAG, "IOException");
		} finally {
			if (null != ñonnection)
				ñonnection.disconnect();
		}
		return data;
	}

	@Override
	protected void onPostExecute(final String result) {
		Log.i("Test", "JSON connected");
	Thread thread = new Thread(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			JSONReadTask task=	(JSONReadTask) new JSONReadTask(mContext,view).execute(result);
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

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		view.setProgressBar(R.id.mProgressBar, 100000, values[0], false);
	}
	private String readStream(InputStream in) {
		BufferedReader reader = null;
		StringBuffer data = new StringBuffer("");
		try {
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = reader.readLine()) != null) {
				data.append(line);
			}
		} catch (IOException e) {
			Log.e(TAG, "IOException");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data.toString();
	}

}
