package com.moto.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.RequestParams;

public class SignNetWorkModel extends MotoNetWorkModel {
	
	public SignNetWorkModel(NetWorkModelListener listener, Context cx) {
		super(listener,cx);
		this.route = "user";
	}
	
	public void signUp(RequestParams params) {
		this.connectWithPostData(params, "signup");
	}
	

	public void signIn(String username, String password, String email) {
        RequestParams params = new RequestParams();
        params.put("email", email);
        params.put("username", username);
        params.put("password", password);
		this.connectWithPostData(params, "signin");
	}

	public void registerBpush(Context cx) {
		 		RequestParams params = new RequestParams();
		 		SharedPreferences  mshared;
		 		mshared = cx.getSharedPreferences("usermessage", 0);
		 		
		 		String bpush_id = mshared.getString("bpush_id", "");

		 		String channel_id = mshared.getString("channel_id", "");
		 		String username = mshared.getString("username", "");
		 		if (bpush_id.length() > 1) {
		 			params.put("bpush_id", bpush_id);
		 			params.put("devicetype", "2");
		 		}
		 		if (channel_id.length() > 1) {
		 			params.put("channel_id", channel_id);
		 		}
		 		if (username.length() > 1) {
		 			params.put("username", username);
		 		}
		 		
		 		this.connectWithPostData(params, "registerbpush");
		 	}
	
}
