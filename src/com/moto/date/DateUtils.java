package com.moto.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

public class DateUtils {
	
	public static Date GetData(String time)
	{
		Date date = null;
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    isoFormat.setTimeZone(TimeZone.getDefault());
	    try {
			date = isoFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return date;
		
	}
	public static Date GetSimpleData(String time)
	{
		Date date = null;
		SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd");
	    isoFormat.setTimeZone(TimeZone.getDefault());
	    try {
			date = isoFormat.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return date;
		
	}
	/**
	 * 获取12.09这样格式的日期
	 */
	public static String getDateSpot(Date date)
	{
		String monthString = date.getMonth()+"";
		String dateString = date.getDate()+"";
		if(monthString.length() == 1)
		{
			monthString = "0"+monthString;
		}
		if(dateString.length() == 1)
		{
			dateString = "0"+dateString;
		}
		return monthString+"."+dateString;
		
	}
	/**
	 * 获取12月09日这样格式的日期
	 */
	public static String getDate(Date date)
	{
		String monthString = date.getMonth()+"";
		String dateString = date.getDate()+"";
		if(monthString.length() == 1)
		{
			monthString = "0"+monthString;
		}
		if(dateString.length() == 1)
		{
			dateString = "0"+dateString;
		}
		return monthString+"月"+dateString+"日";
	}
	/**
	 * 获取20:09这样格式的时间
	 */
	public static String getTime(Date date)
	{
		String hourString = date.getHours()+"";
		String minuteString = date.getMinutes()+"";
		if(hourString.length() == 1)
		{
			hourString = "0"+hourString;
		}
		if(minuteString.length() == 1)
		{
			minuteString = "0"+minuteString;
		}
		return hourString+":"+minuteString;
	}
	
	/**
	 * 获取“多久前”这样的时间，，参数位时间戳
	 */
	public static String getTimeAgo(Date date)
	{
		long start_time = date.getTime() / 1000;
		long now_time = java.lang.System.currentTimeMillis() / 1000;
		long time = now_time - start_time;
		if((time/60) >= 1)
        {
        	if((time/3600) >= 1)
        	{
        		if(((time/3600)/24) >= 1)
        		{
        			return (time/3600)/24+"天前";
        		}
        		else
        		{
        			return time/3600+"小时前";
        		}
        	}
        	else {
        		return time/60+"分钟前";
			}
        }
        else
        {
        	return time+"秒前";
        }
	}
	
	/**
	 * 获取时间间隔
	 */
	public static long getNumDate(String startTime, String endTime)
	{
		long start_time = Long.parseLong(startTime) / 1000;
		long end_time = Long.parseLong(endTime) / 1000;
		long time = end_time - start_time;
		return (time / 3600) / 24 + 1;
	}
	
	
}
