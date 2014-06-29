package com.moto.model;

import android.content.Context;

import com.loopj.android.http.RequestParams;

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

    public void readUserproFile(String name,String token) {
        RequestParams params = new RequestParams();
        params.put("username", name);
        params.put("token", token);
        this.connectWithPostData(params, "readuserprofile");
    }

    public void readrecentpost(String name) {
        RequestParams params = new RequestParams();
        params.put("username", name);
        this.connectWithPostData(params, "readrecentpost");
    }

    public void followuser(String name,String token) {
        RequestParams params = new RequestParams();
        params.put("username", name);
        params.put("token", token);
        this.connectWithPostData(params, "followuser");
    }

    public void readfriendlist(String name) {
        RequestParams params = new RequestParams();
        params.put("username", name);
        this.connectWithPostData(params, "readfriendlist");
    }
}
