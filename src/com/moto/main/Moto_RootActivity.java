package com.moto.main;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moto.model.NetWorkModelListener;

public class Moto_RootActivity extends Activity implements NetWorkModelListener{

	public LinearLayout navigationBar;
	private LinearLayout leftLinearLayout;
	private LinearLayout rightLinearLayout;
	private TextView navigationBarTitleTextView;
	private ImageView leftBarButton;
	private ImageView rightBarButton;
	
	public enum barButtonIconType {
	       barButtonIconType_None, 
	       barButtonIconType_Back, 
	       barButtonIconType_refresh,
	       barButtonIconType_setting
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initNavigationBar();
	}


    /**
     * 从res的layout中加载一个界面附在当前视图表面上 
     *
     *
     * @param resource 	R.layout.id
     */
	
	protected void addContentView(int resource) {
		LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams layoutParams  = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View MenuLyaout = inflater.inflate(resource, null);
		addContentView(MenuLyaout, layoutParams);
		this.navigationBar.bringToFront();
	}
	
	/**
     * 从res的layout中加载一个界面附在当前视图表面上 
     *
     *
     * @param resource 	R.layout.id
     */
	
	@SuppressLint("NewApi")
	public void ChangeScrollviewAlpha(ScrollView scrollView, LinearLayout bar) {
		if(scrollView.getScaleY() > 0)
		{
			if(scrollView.getScrollY() < 100)
			{
				bar.getBackground().setAlpha(255);
				navigationBar.setBackgroundColor(Color.rgb(72, 193, 213));
			}
			else {
				float alpha = (float) (scrollView.getScrollY() / 700.0);
				alpha = (float) ((1 - alpha) < 0.0 ? 0.0 : 1 - alpha);
				bar.getBackground().setAlpha((int) (alpha*250));
			}
		}
		else {
			bar.getBackground().setAlpha(255);
			navigationBar.setBackgroundColor(Color.rgb(72, 193, 213));
		}
	}
	

    /**
     * 从res的layout中加载一个界面附在当前视图表面上,并给导航栏赋值
     *
     *
     * @param resource 	R.layout.id
     * @param resid 	导航栏标题string id
     * @param leftType 	导航栏左按钮样式
     * @param rightType 导航栏右按钮样式
     */
	public void addContentView(int resource, int resid, barButtonIconType leftType, barButtonIconType rightType) {
		addContentView(resource);
		setNavigationBarTitle(resid);
		handleIconType(leftBarButton, leftType);
		handleIconType(rightBarButton, rightType);
	}


    /**
     * 从res的layout中加载一个界面附在当前视图表面上,并给导航栏赋值
     *
     *
     * @param resource 	R.layout.id
     * @param navigationBarTitle 	导航栏标题
     * @param leftType 	导航栏左按钮样式
     * @param rightType 导航栏右按钮样式
     */
	public void addContentView(int resource, String navigationBarTitle, barButtonIconType leftType, barButtonIconType rightType) {
		addContentView(resource);
		setNavigationBarTitle(navigationBarTitle);
		handleIconType(leftBarButton, leftType);
		handleIconType(rightBarButton, rightType);
	}
    /**
     * 
     * 进入下一个Activity
     *
     * @param extras The Bundle of extras to add to this intent, 可以为null.
     * @param cls 	The name of a class inside of the application package that will be used as the component for this Intent.
     */
	public void pushToNextActivity(Bundle extras, Class<?>cls){
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(this, cls);
		startActivity(intent);
	}

    /**
     *
     * 进入下一个Activity
     *
     *
     * @param cls 	The name of a class inside of the application package that will be used as the component for this Intent.
     */
    public void pushToNextActivity(Class<?>cls){
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }
	

    /**
     * 
     * 进入下一个Activity, 结束后带requestCode.
     *
     * @param extras The Bundle of extras to add to this intent, 可以为null.
     * @param cls 	The name of a class inside of the application package that will be used as the component for this Intent.
     * @param requestCode	If >= 0, this code will be returned in onActivityResult() when the activity exits.
     */
	public void pushToNextActivity(Bundle extras, Class<?>cls, int requestCode){
		Intent intent = new Intent();
		intent.putExtras(extras);
		intent.setClass(this, cls);
		startActivityForResult(intent, requestCode);
	}

    /**
     * 给导航栏赋一个标题
     *
     *
     * @param resid 	导航栏标题string id
     */
	public void setNavigationBarTitle(int resid) {
    	navigationBarTitleTextView.setText(resid);
	}
	

    /**
     * 给导航栏赋一个标题
     *
     *
     * @param navigationBarTitle 	导航栏标题字符串变量
     */
	public void setNavigationBarTitle(String navigationBarTitle){
		navigationBarTitleTextView.setText(navigationBarTitle);
	}
	
	@Override
	public void handleNetworkDataWithSuccess(JSONObject jsonObject)
			throws JSONException {		
		// TODO Auto-generated method stub
	}
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject)
			throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNetworkDataWithUpdate(float progress)
			throws JSONException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		
	}
	

    /**
     * 导航栏左按钮事件
     *
     *
     */
	public void leftBarButtonItemEvent() {
		finish();
	}
	

    /**
     * 导航栏右按钮事件
     *
     *
     */
	public void rightBarButtonItemEvent() {
		
	}
	
	private void handleIconType(ImageView button, barButtonIconType type) {
		switch (type) {
		case barButtonIconType_None:{
		}
		break;
		case barButtonIconType_Back:{
			button.setImageResource(R.drawable.return_up);
			leftLinearLayout.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					leftBarButtonItemEvent();
				}
			});
		}
		break;
		case barButtonIconType_refresh:{
			button.setImageResource(R.drawable.f5_broadcast);
		}
		break;
		case barButtonIconType_setting:{
			button.setImageResource(R.drawable.set);
		}
		break;
		default:
			break;
		}
		
	}

	private void initNavigationBar() {
		navigationBar = (LinearLayout)findViewById(R.id.navigationBar);
		navigationBarTitleTextView = (TextView)findViewById(R.id.live_title);
		leftLinearLayout = (LinearLayout)findViewById(R.id.left_linear_nav);
		rightLinearLayout = (LinearLayout)findViewById(R.id.right_linear_nav);
		
		
		rightLinearLayout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				rightBarButtonItemEvent();
			}
		});
		
		leftBarButton = (ImageView)findViewById(R.id.left_bar_button);
		rightBarButton = (ImageView)findViewById(R.id.right_bar_button);
	}
	

	private void addContentView(int resource, String navigationBarTitle) {
		addContentView(resource);
		setNavigationBarTitle(navigationBarTitle);
	}

	private void addContentView(int resource, int resid) {
		addContentView(resource);
		setNavigationBarTitle(resid);
	}


	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
