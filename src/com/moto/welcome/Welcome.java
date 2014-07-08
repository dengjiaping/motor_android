package com.moto.welcome;

import android.content.Intent;
import android.os.Bundle;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.moto.main.Moto_MainActivity;
import com.moto.myactivity.MyActivity;

public class Welcome extends MyActivity{
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		intent = new Intent(Welcome.this, UpdateServise.class);
		startService(intent);
		
		PushManager.startWork(getApplicationContext(),
                              PushConstants.LOGIN_TYPE_API_KEY,
                              Utils.getMetaValue(this, "api_key"));
		intent = new Intent();
		intent.setClass(Welcome.this, Moto_MainActivity.class);
		startActivity(intent);
		finish();
	}
	
}
