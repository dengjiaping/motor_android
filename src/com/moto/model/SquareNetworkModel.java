package com.moto.model;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.RequestParams;
import com.moto.qiniu.img.Image;
import com.moto.qiniu.img.UploadImage;

import android.content.Context;
import android.os.AsyncTask;

public class SquareNetworkModel extends MotoNetWorkModel{

	private RequestParams avatarParam;
	public SquareNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener, cx);
		// TODO Auto-generated constructor stub
		this.route = "square";
	}
	
	public void CreateNewTheme(RequestParams param) 
	{
		this.avatarParam = param;
		connectWithPostData(avatarParam, "createnewtheme");
	}
	
	public void CreateNewTheme(RequestParams param,Image image) 
	{
		this.avatarParam = param;
		connectWithPostData(avatarParam, "createnewtheme");
		ArrayList<Image> arrayList = new ArrayList<Image>();
		arrayList.add(image);
		String photoWidthHeight = GetWidthHeightHash(arrayList);
		param.put("photoinfo", photoWidthHeight);
		AvatarUploadTask task = new AvatarUploadTask();
		task.execute(image);
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
			connectWithPostData(avatarParam,"createnewtheme");
		}
		}
}
