package com.moto.model;

import android.content.Context;
import android.os.AsyncTask;

import com.loopj.android.http.RequestParams;
import com.moto.qiniu.img.Image;
import com.moto.qiniu.img.UploadImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MeNetworkModel extends MotoNetWorkModel{

	private RequestParams avatarParam;
	private ArrayList<Image> Photo;
	private String photoName;
	private String act;
	private String imagehash;
	public MeNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener, cx);
		// TODO Auto-generated constructor stub
		this.route = "me";
	}

    public void GetMeKeeppost(RequestParams params)
    {
        this.avatarParam = params;
        connectWithPostData(params, "readkeep");
    }
	
	public void updateProfile(RequestParams params, Image avatar, ArrayList<Image> photo, String photoName, String act) 
	{
		this.avatarParam = params;
		this.Photo = photo;
		this.photoName = photoName;
		this.act = act;
		AvatarArrayUploadTask task = new AvatarArrayUploadTask();
		task.execute(avatar);
	}
	public void updateProfile(RequestParams params, String avatarHash, ArrayList<Image> photo, String photoName, String act) 
	{
		this.avatarParam = params;
		this.Photo = photo;
		this.photoName = photoName;
		this.act = act;
		avatarParam.put("avatar", avatarHash);
		try {
			connectWithPostData(avatarParam, photoName, Photo, act);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void updateProfile(RequestParams params, Image avatar, String imageHash, String photoName, String act) 
	{
		this.avatarParam = params;
		this.imagehash = imageHash;
		this.photoName = photoName;
		this.act = act;
		AvatarUploadTask task = new AvatarUploadTask();
		task.execute(avatar);
	}
	
	public void updateProfile(RequestParams params, String avatarHash, String imageHash, String act) 
	{
		this.avatarParam = params;
		this.imagehash = imageHash;
		this.act = act;
		avatarParam.put("avatar", avatarHash);
		avatarParam.put("motophoto", imageHash);
		connectWithPostData(avatarParam, act);
	}

class AvatarArrayUploadTask extends AsyncTask<Image, Float, String>{
	private final static String qiniu_url = "http://up.qiniu.com/";
	private String avatHash;
	@Override
	protected String doInBackground(Image... params) {
		Image avatar  = params[0];
		UploadImage uploadImage = new UploadImage();

			String respose = uploadImage.post(qiniu_url,avatar);
			try {
				JSONObject jsonObject = new JSONObject(respose);
				avatHash = jsonObject.getString("hash");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return avatHash;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onProgressUpdate(Float... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		avatarParam.put("avatar", avatHash);
		try {
			connectWithPostData(avatarParam, photoName, Photo, act);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

class AvatarUploadTask extends AsyncTask<Image, Float, String>{
	private final static String qiniu_url = "http://up.qiniu.com/";
	private String avatHash;
	@Override
	protected String doInBackground(Image... params) {
		Image avatar  = params[0];
		UploadImage uploadImage = new UploadImage();

			String respose = uploadImage.post(qiniu_url,avatar);
			try {
				JSONObject jsonObject = new JSONObject(respose);
				avatHash = jsonObject.getString("hash");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return avatHash;
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected void onProgressUpdate(Float... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		avatarParam.put("avatar", avatHash);
		try {
			connectWithPostData(avatarParam, photoName, imagehash, act);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

}
