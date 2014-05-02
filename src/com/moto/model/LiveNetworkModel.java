package com.moto.model;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.content.Context;

import com.loopj.android.http.RequestParams;
import com.moto.qiniu.img.Image;

public class LiveNetworkModel extends MotoNetWorkModel{

	private RequestParams avatarParam;
	private ArrayList<Image> Photo;
	private String photoName;
	public LiveNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener, cx);
		// TODO Auto-generated constructor stub
		this.route = "live";
	}
	public void writelive(RequestParams params, ArrayList<Image> photo, String photoName,String photoinfoName) 
	{
		this.avatarParam = params;
		this.Photo = photo;
		this.photoName = photoName;
		try {
			connectWithPostData(avatarParam, this.photoName, this.Photo, "writelive",photoinfoName);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writelive(RequestParams params) 
	{
		this.avatarParam = params;
		connectWithPostData(avatarParam, "writelive");
	}

	public void likelivepost(RequestParams params)
	{
		this.avatarParam = params;
		connectWithPostData(params, "likelivepost");
	}
	
	public void liveKeeppost(RequestParams params)
	{
		this.avatarParam = params;
		connectWithPostData(params, "keeplivethread");
	}

}
