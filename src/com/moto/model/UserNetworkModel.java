package com.moto.model;

import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.SharedPreferences;

public class UserNetworkModel extends MotoNetWorkModel {

	public UserNetworkModel(NetWorkModelListener listener, Context cx) {
		super(listener, cx);
		this.route = "user";
	}
	
	public void searchUser(String name) {
 		RequestParams params = new RequestParams();
 		params.put("username", name);
 		this.connectWithPostData(params, "searchuser");
	}
}
