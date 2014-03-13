package com.example.weatherwidget;






import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class WeatherWidget extends AppWidgetProvider {
	
	
	
	 ComponentName thisWidget;
	 Intent serviceIntent;
	 AlarmManager alarmManager;
	 
@Override
public void onUpdate(Context context, AppWidgetManager appWidgetManager,
		int[] appWidgetIds) {
	// TODO Auto-generated method stub
	
	Log.i("Test","Update widget");
	thisWidget = new ComponentName(context,WeatherWidget.class);
	
	Calendar calendar=Calendar.getInstance();
	alarmManager=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	
	 serviceIntent=new Intent(context, WeatherUpdateService.class);
	serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
	
	PendingIntent pendIntent=PendingIntent.getService(context, 0, serviceIntent,0);
	alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1*60*1000, pendIntent); 
	
}


@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
	
	Log.i("Test", intent.getAction());
	if (intent.getAction().equals("stopService"))
	{
		context.stopService(new Intent(context, WeatherUpdateService.class));
		
	}
		super.onReceive(context, intent);
	}
}
