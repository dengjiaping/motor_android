package com.moto.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.qiniu.img.Image;
import com.moto.qiniu.img.UploadImage;

/**
 * 网络模块请求处理的父类
 *
 */
public class MotoNetWorkModel {
	private NetWorkModelListener netWorkModelListener;
	private RequestParams param;
	private String act;
	public Context context;
	private String imageArrayName;
	private String photoinfoName;

/**
 * 网络服务请求的route路径
 *  	
 */	
	private final static String api_url = "http://motor-env-e94pufmw8k.elasticbeanstalk.com/api/";
	public String route;
	

	public MotoNetWorkModel(NetWorkModelListener listener, Context cx){
		netWorkModelListener = listener;
		context = cx;
	}

    /**
     * 发送网络服务请求，服务器响应请求is ＝1时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithSuccess(JSONObject jsonObject) handleNetworkDataWithSuccess},
	 * is＝0时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithFail(JSONObject jsonObject) handleNetworkDataWithFail}，
	 * 获得响应更新进度时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithUpdate(JSONObject jsonObject) handleNetworkDataWithUpdate}，
     *
     *
     * @param param 	发送给服务器的键值对
     * @param act 		网络服务请求的act路径
     */
	public void connectWithPostData(RequestParams param, String act){
		RequstClient.post(
				new StringBuffer().append(api_url).append(route).append("/").append(act).toString(),
				param,
				new LoadCacheResponseLoginouthandler(
				context, 
				new LoadDatahandler(){

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				try {
					netWorkModelListener.handleNetworkDataStart();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onLoadCaches(String data) {
				// TODO Auto-generated method stub
				super.onLoadCaches(data);
			}

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
			}

			@Override
			public void onSuccess(String data) {
				// TODO Auto-generated method stub
				super.onSuccess(data);
				try {
					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
						netWorkModelListener.handleNetworkDataWithSuccess(jsonObject);
					}
					else
					{
						netWorkModelListener.handleNetworkDataWithFail(jsonObject);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}

			@Override
			public void onFailure(String error, String message) {
				// TODO Auto-generated method stub
				super.onFailure(error, message);
				try {
                    Log.e("sadsd",error);
					netWorkModelListener.handleNetworkDataGetFail("网络连接超时");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
	
    /**
     * 发送带图片队列的网络服务请求，服务器响应请求is ＝1时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithSuccess(JSONObject jsonObject) handleNetworkDataWithSuccess},
	 * is＝0时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithFail(JSONObject jsonObject) handleNetworkDataWithFail}，
	 * 获得响应更新进度时调用 {@link com.moto.model.NetWorkModelListener#handleNetworkDataWithUpdate(JSONObject jsonObject) handleNetworkDataWithUpdate}，
     *
     *
     * @param param 	发送给服务器的键值对
     * @param imageArrayName 图片数组在上传时的键名
     * @param imageArray 传送到七牛的图片数组
     * @param act 		网络服务请求的act路径
     */
	public void connectWithPostData(RequestParams params,String imageArrayName, ArrayList<Image> ImageArray, String act) throws FileNotFoundException{
		this.param = params;
		this.act = act;
		this.imageArrayName = imageArrayName;
		ImageArrayUploadTask imageArrayUploadTask = new ImageArrayUploadTask();
		imageArrayUploadTask.execute(ImageArray);
		
	}
	
	public void connectWithPostData(RequestParams params,String imageArrayName, String result, String act) throws FileNotFoundException{
		this.param = params;
		this.act = act;
		this.imageArrayName = imageArrayName;
		param.put(imageArrayName, result);
		connectWithPostData(param, act);
	}
	
	public void connectWithPostData(RequestParams params,String imageArrayName, ArrayList<Image> ImageArray, String act,String photoinfoName) throws FileNotFoundException{
		this.param = params;
		this.act = act;
		this.imageArrayName = imageArrayName;
		this.photoinfoName = photoinfoName;
		
		String photoWidthHeight = GetWidthHeightHash(ImageArray);
		param.put(photoinfoName, photoWidthHeight);
		ImageArrayUploadTask imageArrayUploadTask = new ImageArrayUploadTask();
		imageArrayUploadTask.execute(ImageArray);
	}
		
	public String GetWidthHeightHash(ArrayList<Image> imageArray) {
		// TODO Auto-generated method stub
		Iterator iterator = imageArray.iterator();
		Image image;
		String hashString = "[";
		while(iterator.hasNext())
		{
			image = (Image) iterator.next();
			hashString = hashString+"{\"width\":"+image.getPhotoWidth()+",\"height\":"+image.getPhotoHeight()+"},";
		}
		hashString = hashString.substring(0,hashString.length()-1);
		hashString = hashString + "]";
		return hashString;
	}

	public class ImageArrayUploadTask extends AsyncTask<ArrayList<Image>, Float, String>{
		private final static String qiniu_url = "http://up.qiniu.com/";
		private ArrayList<String> photosHash = new ArrayList<String>();
	
		@Override
		protected String doInBackground(ArrayList<Image>... params) {
			ArrayList<Image> imageArray  = params[0];
			UploadImage uploadImage = new UploadImage();
			
			for (Iterator iterator = imageArray.iterator(); iterator.hasNext();) {
				Image image = (Image) iterator.next();
				String respose = uploadImage.post(qiniu_url,image);
				try {
					JSONObject jsonObject = new JSONObject(respose);
					photosHash.add(jsonObject.getString("hash"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			JSONArray photosJson = new JSONArray(photosHash);
			return photosJson.toString();
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
			param.put(imageArrayName, result);
		
			connectWithPostData(param, act);
		}
	}

}


