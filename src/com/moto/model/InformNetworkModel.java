package com.moto.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.RequestParams;

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

    public void readReply() {
        RequestParams params = new RequestParams();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
        String token = sharedPreferences.getString("token", "");
        params.put("token", token);
        this.connectWithPostData(params, "readreply");
    }

    public void readAt() {
        RequestParams params = new RequestParams();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
        String token = sharedPreferences.getString("token", "");
        params.put("token", token);
        this.connectWithPostData(params, "readat");
    }

    public void readConversation() {
        RequestParams params = new RequestParams();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
        String token = sharedPreferences.getString("token", "");
        params.put("token", token);
        this.connectWithPostData(params, "readconversation");
    }

    public void deleteconversation(String ucid) {
        RequestParams params = new RequestParams();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("usermessage", 0);
        String token = sharedPreferences.getString("token", "");
        params.put("token", token);
        params.put("ucid",ucid);
        this.connectWithPostData(params, "deleteconversation");
    }

}
