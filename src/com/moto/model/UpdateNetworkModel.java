package com.moto.model;

import org.json.JSONObject;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;

import android.content.Context;

public class UpdateNetworkModel extends MotoNetWorkModel{

	private Context context;
	private NetWorkModelListener netWorkModelListener;
	public UpdateNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener, cx);
		// TODO Auto-generated constructor stub
		this.context = cx;
		netWorkModelListener = listener;
		update();
	}
	public void update() {
		RequstClient.get("http://damp-reef-9073.herokuapp.com/api/android", new LoadCacheResponseLoginouthandler(context, new LoadDatahandler(){
		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();

		}

		@Override
		public void onLoadCaches(String data) {
			// TODO Auto-generated method stub
			super.onLoadCaches(data);
		}

		@Override
		public void onSuccess(String data) {
			// TODO Auto-generated method stub
			super.onSuccess(data);
			try {
				JSONObject jsonObject = new JSONObject(data);
				netWorkModelListener.handleNetworkDataWithSuccess(jsonObject);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}

		@Override
		public void onFailure(String error, String message) {
			// TODO Auto-generated method stub
			super.onFailure(error, message);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		}
		}));
	}
}
