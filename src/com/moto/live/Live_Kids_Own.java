package com.moto.live;
import org.json.JSONObject;
import com.amap.api.maps2d.MapView;
import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.listview.CustomScrollView;
import com.moto.listview.NoScrollListview;
import com.moto.main.R;
import com.moto.main.Moto_RootActivity.barButtonIconType;
import com.moto.model.CacheModel;
import com.moto.mydialog.CustomDialog;
import com.moto.toast.ToastClass;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Live_Kids_Own extends Live_Kids_User{
    
	private RelativeLayout live_kids_bottom;
	private TextView live_kids_check;
	private String checkUri = "http://damp-reef-9073.herokuapp.com/api/live/checkliving";
	private Handler mhandler;
	private boolean isContinueLive = false;
    //	private RelativeLayout.LayoutParams layoutParams;
	
	private SharedPreferences TokenShared;
	private String tokenString;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        //		addContentView(R.layout.live_kids, "我的游记", barButtonIconType.barButtonIconType_Back, barButtonIconType.barButtonIconType_None);
		init();
		mapView.removeAllViews();
		mapView = (MapView) findViewById(R.id.live_kids_map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
		SetBottomVisible();
		scrollListener();
		SetCommentListener();
		SetLeftListener();
		SetListAdapter();
		SetHandler();
		CheckAsyData();
        //
        //		leftpage.setOnClickListener(new OnClickListener() {
        //
        //			@Override
        //			public void onClick(View v) {
        //				// TODO Auto-generated method stub
        //				Live_Kids_Own.this.finish();
        //				overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
        //			}
        //		});
		mhandler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESSAGAIN:
                        isContinueLive = true;
                        if(list.size() == 0)
                        {
                            GetAsyData();
                        }
                        break;
                    case Constant.MSG_SUCCESS:
                        //					loadingProgressBar.setVisibility(View.GONE);
                        live_title.setText(subject);
                        live_kids_check.setText("续写直播");
                        live_kids_check.setEnabled(true);
                        break;
                    case Constant.MSG_FALTH:
                        live_kids_check.setEnabled(true);
                        live_kids_check.setText("写直播");
                        isContinueLive = false;
                        loadingProgressBar.setVisibility(View.GONE);
				}
				super.handleMessage(msg);
			}
			
		};
		
        //		new Thread(new Runnable() {
        //
        //			@Override
        //			public void run() {
        //				// TODO Auto-generated method stub
        //				CheckAsyData();
        //			}
        //		}).start();
		live_kids_check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isContinueLive)
				{
					intent = new Intent();
					intent.putExtra("subject", subject);
					intent.setClass(Live_Kids_Own.this, WriteLiveActivity.class);
					startActivityForResult(intent, 301);
				}
				else {
					final CustomDialog.Builder builder = new CustomDialog.Builder(Live_Kids_Own.this);
					builder.setTitle
					("创建您的直播：");
					builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							//设置你的操作事项
							if(builder.subject.getText().toString().replaceAll(" ", "").equals(""))
							{
								ToastClass.SetToast(Live_Kids_Own.this, "请输入主题!");
							}
							else {
								intent = new Intent();
								intent.putExtra("subject", builder.subject.getText().toString());
								intent.setClass(Live_Kids_Own.this, WriteLiveActivity.class);
								startActivityForResult(intent, 301);
							}
						}
					});
                    
					builder.setNegativeButton("取消",
                                              new android.content.DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    
					builder.create().show();
				}
			}
		});
	}
	
	
	@Override
	public void leftBarButtonItemEvent() {
		// TODO Auto-generated method stub
		Live_Kids_Own.this.finish();
		overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
	}
    
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
        //		SetGaoDeMap();
        
	}
	
	protected void init()
	{
		TokenShared = getSharedPreferences("usermessage", 0);
		tokenString = TokenShared.getString("token", "");
        Log.e("sdc",tokenString);
        if(tokenString.equals(""))
		{
			ToastClass.SetToast(Live_Kids_Own.this, "需要先登录才能够查看自己直播");
            //			Moto_MainActivity.radioGroup.check(R.id.main_tab_user);
		}
		
		list = CacheModel.getCacheLiveDate("kidslinkedlist"+tid,Live_Kids_Own.this);
		carList = CacheModel.getPhotoCacheLiveDate("kidslinkedlist"+tid,Live_Kids_Own.this);
		LocationList = CacheModel.getLocationCacheLiveDate("kidslinkedlist"+tid, Live_Kids_Own.this);
		WidthHeightList = CacheModel.getWidthHeightCacheLiveDate("kidslinkedlist"+tid, Live_Kids_Own.this);
		options = ImageMethod.GetOptions();
		Originaloptions = ImageMethod.GetOriginalOptions();
		text_map = (TextView)findViewById(R.id.live_kids_text_map);
		loadingProgressBar = (ProgressBar)findViewById(R.id.live_kids_loading_progressBar);
		live_title = (TextView)findViewById(R.id.live_kids_title);
		user_img = (ImageView)findViewById(R.id.live_kids_user_img);
		myListView = (NoScrollListview)findViewById(R.id.live_kids_listview);
		scrollView = (CustomScrollView)findViewById(R.id.live_kids_scrollview);
		live_kids_collect = (RelativeLayout)findViewById(R.id.live_kids_record);
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ChangeScrollviewAlpha(scrollView, navigationBar);
				return false;
			}
		});
		comment = (RelativeLayout)findViewById(R.id.live_kids_comment);
		live_kids_share = (RelativeLayout)findViewById(R.id.live_kids_share);
        //		layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);
        //		layoutParams.setMargins(0,80, 0, 45);
        //		scrollView.setLayoutParams(layoutParams);
	}
	//设置底部check控件为显示状态,并初始化控件
	private void SetBottomVisible()
	{
		live_kids_bottom = (RelativeLayout)findViewById(R.id.live_kids_bottom);
		live_kids_check = (TextView)findViewById(R.id.live_kids_check);
		live_kids_bottom.setVisibility(View.VISIBLE);
		live_kids_check.setEnabled(false);
	}
    
	//通过连接网络检查是否在继续直播
	protected void CheckAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", tokenString);
		RequstClient.post(checkUri, param, new LoadCacheResponseLoginouthandler(
                                                                                Live_Kids_Own.this,
                                                                                new LoadDatahandler(){
            
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
					if (jsonObject.getString("is").equals("1") && jsonObject.getString("status").equals("1")) {
						
						tid = jsonObject.getString("tid");
						subject = jsonObject.getString("subject");
						
						mhandler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
						.sendToTarget();
						mhandler.obtainMessage(Constant.MSG_SUCCESS)
						.sendToTarget();
					}
					else
					{
						mhandler.obtainMessage(Constant.MSG_FALTH)
						.sendToTarget();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
            
			@Override
			public void onFailure(String error, String message) {
				// TODO Auto-generated method stub
				super.onFailure(error, message);
				mhandler.obtainMessage(Constant.MSG_FALTH)
				.sendToTarget();
				ToastClass.SetToast(Live_Kids_Own.this, message);
			}
            
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
            case 303:
                Live_Kids_Own.this.finish();
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out); 
                break;
            case 304:
                setResult(304);
                Live_Kids_Own.this.finish();
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out); 
                break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			Live_Kids_Own.this.finish();
			overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out); 
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
