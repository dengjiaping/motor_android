package com.moto.model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import com.moto.utils.StringUtils;

import android.R.integer;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class CacheModel{
	private static SharedPreferences mshared;
	public static void savedataBase64(String fileName,String name,String data, Activity activity)
	{
		mshared = activity.getSharedPreferences(fileName, 0);
		Editor editor = mshared.edit();
		editor.putString(name, data);
		editor.commit();
	}
	
	public static void cacheLiveData(String filename, LinkedList<HashMap<String, Object>> datalist,LinkedList<LinkedList<String>> photolist,LinkedList<String> likeList,Activity activity)
	{
		List<HashMap<String, Object>> list = datalist.size() > 8 ? datalist.subList(0, 8):datalist;
		List<LinkedList<String>> 	PhotoList = photolist.size() > 8 ? photolist.subList(0, 8):photolist;
		List<String> 	likelist = likeList.size() > 8 ? likeList.subList(0, 8):likeList;
		savedataBase64(filename, "data", StringUtils.LinkedlistToBase(list), activity);
		savedataBase64(filename, "photodata", StringUtils.PhotoLinkedlistToBase(PhotoList), activity);
		savedataBase64(filename, "like", StringUtils.LikeLinkedlistToBase(likelist), activity);
	}
	
	public static void cacheLiveData(String filename, 
			LinkedList<HashMap<String, Object>> datalist,
			LinkedList<LinkedList<String>> photolist,
			LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList,
			ArrayList<HashMap<String, Object>> LocationList,
			Activity activity)
	{
		List<HashMap<String, Object>> list = datalist.size() > 8 ? datalist.subList(0, 8):datalist;
		List<LinkedList<String>> 	PhotoList = photolist.size() > 8 ? photolist.subList(0, 8):photolist;
		List<LinkedList<HashMap<String,Integer>>> WidthHeight = WidthHeightList.size() > 8 ? WidthHeightList.subList(0, 0) : WidthHeightList;
		List<HashMap<String, Object>> location = LocationList.size() > 8 ? LocationList.subList(0, 8):LocationList;
		savedataBase64(filename, "data", StringUtils.LinkedlistToBase(list), activity);
		savedataBase64(filename, "photodata", StringUtils.PhotoLinkedlistToBase(PhotoList), activity);
		savedataBase64(filename, "location", StringUtils.LocationArraylistToBase(location), activity);
		savedataBase64(filename, "widthheight", StringUtils.WidthHeightLinkedlistToBase(WidthHeight), activity);
	}
	
	public static void cacheScrollviewHeight(String filename, int height, Activity activity)
	{
		mshared = activity.getSharedPreferences(filename, 0);
		Editor editor = mshared.edit();
		editor.putInt("height", height);
		editor.commit();
	}
	public static int getHeight(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		int height = mshared.getInt("height", 0);
		return height;
	}
	public static LinkedList<HashMap<String, Object>> getCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("data", "");
		if(StringUtils.readLinkedlistFromBase(dataString) == null)
		{
			return new LinkedList<HashMap<String,Object>>();
		}
		return StringUtils.readLinkedlistFromBase(dataString);
	}
	
	public static LinkedList<LinkedList<String>> getPhotoCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("photodata", "");
		if(StringUtils.readPhotoLinkedlistFromBase(dataString) == null)
		{
			return new LinkedList<LinkedList<String>>();
		}
		return StringUtils.readPhotoLinkedlistFromBase(dataString);
	}
	public static ArrayList<HashMap<String, Object>> getLocationCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("location", "");
		if(StringUtils.readLocationLinkedlistFromBase(dataString) == null)
		{
			return new ArrayList<HashMap<String, Object>>();
		}
		return StringUtils.readLocationLinkedlistFromBase(dataString);
	}
	
	public static LinkedList<LinkedList<HashMap<String,Integer>>> getWidthHeightCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("widthheight", "");
		if(StringUtils.readWidthHeightLinkedlistFromBase(dataString) == null)
		{
			return new LinkedList<LinkedList<HashMap<String,Integer>>>();
		}
		return StringUtils.readWidthHeightLinkedlistFromBase(dataString);
	}
	
	public static LinkedList<String> getLikeCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("like", "");
		if(StringUtils.readLikeLinkedlistFromBase(dataString) == null)
		{
			return new LinkedList<String>();
		}
		return StringUtils.readLikeLinkedlistFromBase(dataString);
	}
	
//	public static LinkedList<HashMap<String, Object>> restoreLiveDataList() {
//		string
//	}
}
