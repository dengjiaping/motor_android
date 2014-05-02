package com.moto.user;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.moto.main.R;
import com.moto.myactivity.MyActivity;

public class User_browseSetting extends MyActivity implements OnClickListener{
    
	private RelativeLayout daymodel;
	private RelativeLayout nightmodel;
	private ImageView day_select;
	private ImageView night_select;
	private ImageView browse_return;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting_browse);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		daymodel = (RelativeLayout)findViewById(R.id.user_browse_daymodel);
		nightmodel = (RelativeLayout)findViewById(R.id.user_browse_nightmodel);
		day_select = (ImageView)findViewById(R.id.user_browse_dayselect);
		night_select = (ImageView)findViewById(R.id.user_browse_nightselect);
		browse_return = (ImageView)findViewById(R.id.user_browse_setting_return);
		browse_return.setOnClickListener(this);
		daymodel.setOnClickListener(this);
		nightmodel.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == daymodel)
		{
			day_select.setVisibility(View.VISIBLE);
			night_select.setVisibility(View.GONE);
		}
		if(v == nightmodel)
		{
			day_select.setVisibility(View.GONE);
			night_select.setVisibility(View.VISIBLE);
		}
		if(v == browse_return)
		{
			User_browseSetting.this.finish();
		}
	}
    
}
