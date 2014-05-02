package com.moto.model;

import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.SharedPreferences;

public class InformNetworkModel extends MotoNetWorkModel{
	
	private Context context;
	public InformNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener,cx);
		this.route = "inform";
		this.context = cx;
	}
	
	public void readPrivateMessage(String username, String timestamp) {
		RequestParams params = new RequestParams();
		SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
		String token = sharedPreferences.getString("token", "");
		params.put("token", token);
		params.put("otherusername", username);
		params.put("timestamp", timestamp);
		this.connectWithPostData(params, "readprivatemessage");
	}

	public void writePrivateMessage(String username, String message) {
		RequestParams params = new RequestParams();
		SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
		String token = sharedPreferences.getString("token", "");
		params.put("token", token);
		params.put("otherusername", username);
		params.put("message", message);
		this.connectWithPostData(params, "writeprivatemessage");
	}


}
