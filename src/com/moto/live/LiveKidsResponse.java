package com.moto.live;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.date.DateUtils;
import com.moto.img.ScaleImageView;
import com.moto.listview.CustomScrollView;
import com.moto.listview.NoScrollListview;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.main.R;
import com.moto.myactivity.MyActivity;
import com.moto.mymap.MyMapApplication;
import com.moto.toast.ToastClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class LiveKidsResponse extends MyActivity implements OnClickListener{
    
	private TextView title;
	private ImageView leftpage;
	private String subject;
	private String pid;
	private String photoname;
	private String message;
	private String location;
    private String dateline;
	private TextView details;
	private ScaleImageView img;
	private TextView timeTextView;
	private TextView position;
	private ImageView send;
	private EditText editText_response;
	private LinearLayout positionlayout;
	private String editString;
	private ResponseAdapter adapter;
	private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private NoScrollListview myListView;
	private boolean isRefresh = false;
	private int count = 0;
	private Handler handler;
	private CustomScrollView scrollView;
	private SharedPreferences TokenShared;
	private DisplayImageOptions Originaloptions;
	private String tokenString;
	private String readItemCommentUri = "http://damp-reef-9073.herokuapp.com/api/live/readliveitemcomment";
	private String CreateItemCommentUri = "http://damp-reef-9073.herokuapp.com/api/live/createliveitemcomment";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_kids_item_response);
		init();
		adapter = new ResponseAdapter(this, list);
		myListView.setAdapter(adapter);
		GetAsyData();
		
		scrollView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						isRefresh = true;
						count = 0;
						GetAsyData();
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onRefreshComplete();
					}
                    
				}.execute();
			}
		});
		scrollView.setOnLoadListener(new OnLoadListener() {
			public void onLoad() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(2000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						GetAsyData();
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onLoadComplete();
					}
                    
				}.execute();
			}
		});
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_WAITSUCCESS:
                        ToastClass.SetImageToast(LiveKidsResponse.this,"成功发送评论");
                        manageInput(LiveKidsResponse.this);
                        LiveKidsResponse.this.finish();
                        break;
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isRefresh = false;
                        details.setText(message);
                        if(!location.equals("null"))
                        {
                            positionlayout.setVisibility(View.VISIBLE);
                            position.setText(location);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isRefresh = false;
                        details.setText(message);
                        if(!location.equals("null"))
                        {
                            positionlayout.setVisibility(View.VISIBLE);
                            position.setText(location);
                        }
                        adapter.notifyDataSetChanged();
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_NULL:
                        isRefresh = false;
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
	private void init() {
		// TODO Auto-generated method stub
		intent = getIntent();
		subject = intent.getStringExtra("subject");
		pid = intent.getStringExtra("pid");
		photoname = intent.getStringExtra("photoname");
		location = intent.getStringExtra("location");
        dateline = intent.getStringExtra("dateline");
		Originaloptions = ImageMethod.GetOriginalOptions();
		
		positionlayout = (LinearLayout)findViewById(R.id.live_kids_item_time_response_item_comment);
		title = (TextView)findViewById(R.id.live_kids_item_response_title);
		img = (ScaleImageView)findViewById(R.id.live_kids_item_response_img);
		timeTextView = (TextView)findViewById(R.id.live_kids_item_time_response_text);
		position = (TextView)findViewById(R.id.live_kids_item_position_response_text);
		details = (TextView)findViewById(R.id.live_kids_item_response_details);
		leftpage = (ImageView)findViewById(R.id.live_kids_item_response_return);
		send = (ImageView)findViewById(R.id.live_kids_item_response_send);
		editText_response = (EditText)findViewById(R.id.live_kids_item_response_edit);
		myListView = (NoScrollListview)findViewById(R.id.live_kids_item_response_listview);
		scrollView = (CustomScrollView)findViewById(R.id.live_kids_item_response_scrollview);
		leftpage.setOnClickListener(this);
		send.setOnClickListener(this);
		
		if(!photoname.equals("null"))
		{
			String imageUrl = imgPath+photoname;
			img.setVisibility(View.VISIBLE);
			MyMapApplication.imageLoader.displayImage(imageUrl, img, Originaloptions,null);
			img.setImageHeight(80);
			img.setImageWidth(100);
		}

		timeTextView.setText(com.moto.utils.DateUtils.timestampToDeatil(dateline));
		title.setText(subject);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == leftpage)
		{
			LiveKidsResponse.this.finish();
		}
		if(v == send)
		{
			TokenShared = getSharedPreferences("usermessage", 0);
			tokenString = TokenShared.getString("token", "");
			editString = editText_response.getText().toString();
			if(tokenString.equals(""))
			{
				ToastClass.SetToast(LiveKidsResponse.this, "需要先登录才能够发送");
				setResult(304);
				LiveKidsResponse.this.finish();
			}
			else if(editString.replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(LiveKidsResponse.this,"请输入回复内容!");
			}
			else {
				SendAsyData();
			}
		}
	}
	private void SendAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("pid", pid);
		param.put("token", tokenString);
		param.put("message", editString);
		RequstClient.post(CreateItemCommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                            LiveKidsResponse.this,
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
					if (jsonObject.getString("is").equals("1")) {
						handler.obtainMessage(Constant.MSG_WAITSUCCESS)
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
			}
            
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
	
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("pid", pid);
		param.put("page", ""+count);
		
		RequstClient.post(readItemCommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                          LiveKidsResponse.this,
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
					if(isRefresh)
					{
						list.clear();
					}
                    Log.e("data",data);
                    JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
						String data_details = jsonObject
                        .getString("comment_list");
						JSONArray array = new JSONArray(data_details);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							list.add(GetMap(jsonObject2));
						}

						if (count == 0) {
                            SetLivePost(jsonObject.getJSONObject("live_post"));
							if (isRefresh)
								handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                                .sendToTarget();
							else
								handler.obtainMessage(Constant.MSG_SUCCESS)
                                .sendToTarget();
						} else {
							handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                            .sendToTarget();
						}
						count++;
                        
					} else {
						handler.obtainMessage(Constant.MSG_NULL).sendToTarget();
					}
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
	
	private HashMap<String, Object> GetMap(JSONObject jsonObject)
	{
		map = new HashMap<String, Object>();
		try {
			
			String author = jsonObject.getString("author");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String avatar = jsonObject.getString("avatar");
			map.put("avatar", avatar);
			map.put("author", author);
			map.put("message", message);
			map.put("dateline", dateline);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
		
	}

    private void SetLivePost(JSONObject jsonObject)
    {
        try {
            message = jsonObject.getString("message");
            location = jsonObject.getString("location");
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
}
