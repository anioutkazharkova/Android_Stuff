package com.example.weatherwidget;

public class WeatherInfo {

	private String icon;
	private String description;
	private String location;
	
	private double temp_min;
	private double temp;
	private double temp_max;
	
	double wind_speed;
	
	public WeatherInfo()
	{
		icon="";
		description="";
		location="";
		
		temp_min=0;
		temp=0;
		temp_max=0;
		
		wind_speed=0;
	}
	
	public WeatherInfo(String icon, String description,double temp_min, double temp, double temp_max,double wind_speed, String location)
	{
		this.icon=icon;
		this.description=description;
		this.location=location;
		
		this.temp=temp;
		this.temp_min=temp_min;
		this.temp_max=temp_max;
		this.wind_speed=wind_speed;
		
	}
	public Double getTemp()
	{
		return this.temp;
	}
	
	public Double getTempMin()
	{
		return this.temp_min;
	}
	
	public Double getTempMax()
	{
		return this.temp_max;
	}
	
	public Double getWindSpeed()
	{
		return this.wind_speed;
	}
	public String getIcon()
	{
		return this.icon;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	public String getLocation()
	{
		return this.location;
	}
	public String toString()
	{
		return this.location+" "+this.temp+" "+this.temp_min+" "+this.temp_max+" "+this.description+" "+this.icon;
	}
	
	
}
