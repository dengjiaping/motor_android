package com.moto.model;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.moto.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheModel{
	private static SharedPreferences mshared;
	public static void savedataBase64(String fileName,String name,String data, Activity activity)
	{
		mshared = activity.getSharedPreferences(fileName, 0);
		Editor editor = mshared.edit();
		editor.putString(name, data);
		editor.commit();
	}
	
	public static void cacheLiveData(String filename, ArrayList<HashMap<String, Object>> datalist,ArrayList<ArrayList<String>> photolist, Activity activity)
	{
		List<HashMap<String, Object>> list = datalist.size() > 8 ? datalist.subList(0, 8):datalist;
		List<ArrayList<String>> 	PhotoList = photolist.size() > 8 ? photolist.subList(0, 8):photolist;
//		List<String> 	likelist = likeList.size() > 8 ? likeList.subList(0, 8):likeList;
//        List<String> 	commentlist = comment_list.size() > 8 ? comment_list.subList(0, 8):likeList;
		savedataBase64(filename, "data", StringUtils.LinkedlistToBase(list), activity);
		savedataBase64(filename, "photodata", StringUtils.PhotoLinkedlistToBase(PhotoList), activity);
//		savedataBase64(filename, "like", StringUtils.LikeLinkedlistToBase(likelist), activity);
//        savedataBase64(filename,"comment",StringUtils.CommentLinkedlistToBase(commentlist),activity);
	}
	
	public static void cacheLiveData(String filename,
                                     ArrayList<HashMap<String, Object>> datalist,
                                     ArrayList<ArrayList<String>> photolist,
                                     ArrayList<ArrayList<HashMap<String,Integer>>> WidthHeightList,
			ArrayList<HashMap<String, Object>> LocationList,
			Activity activity)
	{
		List<HashMap<String, Object>> list = datalist.size() > 8 ? datalist.subList(0, 8):datalist;
		List<ArrayList<String>> 	PhotoList = photolist.size() > 8 ? photolist.subList(0, 8):photolist;
		List<ArrayList<HashMap<String,Integer>>> WidthHeight = WidthHeightList.size() > 8 ? WidthHeightList.subList(0, 0) : WidthHeightList;
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
	public static ArrayList<HashMap<String, Object>> getCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("data", "");
		if(StringUtils.readLinkedlistFromBase(dataString) == null)
		{
			return new ArrayList<HashMap<String,Object>>();
		}
		return StringUtils.readLinkedlistFromBase(dataString);
	}
	
	public static ArrayList<ArrayList<String>> getPhotoCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("photodata", "");
		if(StringUtils.readPhotoLinkedlistFromBase(dataString) == null)
		{
			return new ArrayList<ArrayList<String>>();
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
	
	public static ArrayList<ArrayList<HashMap<String,Integer>>> getWidthHeightCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("widthheight", "");
		if(StringUtils.readWidthHeightLinkedlistFromBase(dataString) == null)
		{
			return new ArrayList<ArrayList<HashMap<String,Integer>>>();
		}
		return StringUtils.readWidthHeightLinkedlistFromBase(dataString);
	}
	
	public static ArrayList<String> getLikeCacheLiveDate(String filename,Activity activity){
		mshared = activity.getSharedPreferences(filename, 0);
		String dataString = mshared.getString("like", "");
		if(StringUtils.readLikeLinkedlistFromBase(dataString) == null)
		{
			return new ArrayList<String>();
		}
		return StringUtils.readLikeLinkedlistFromBase(dataString);
	}

    public static ArrayList<String> getCommentCacheLiveDate(String filename,Activity activity){
        mshared = activity.getSharedPreferences(filename, 0);
        String dataString = mshared.getString("comment", "");
        if(StringUtils.readLikeLinkedlistFromBase(dataString) == null)
        {
            return new ArrayList<String>();
        }
        return StringUtils.readLikeLinkedlistFromBase(dataString);
    }
	
//	public static LinkedList<HashMap<String, Object>> restoreLiveDataList() {
//		string
//	}
}
