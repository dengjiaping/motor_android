package com.moto.live;

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

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.listview.CustomScrollView;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.main.R;
import com.moto.myactivity.MyActivity;
import com.moto.toast.ToastClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class Live_kidsAllResponse extends MyActivity{
    
	private NoScrollListview myListview;
	private CustomScrollView scrollView;
	private ImageView sendView;
	private EditText editmessage;
	private String tid;
	private ImageView leftpage;
	private ResponseAdapter adapter;
	private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private boolean isRefresh = false;
	private int count = 0;
	private String editString;
	private SharedPreferences TokenShared;
	private String tokenString;
	private Handler handler;
	private String readCommentUri = path+"api/live/readlivecomment";
	private String CreateCommentUri = path+"api/live/createlivecomment";
	@Override	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.live_kids_item_allresponse);
		init();
		adapter = new ResponseAdapter(this, list);
		myListview.setAdapter(adapter);
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
		sendView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				editString = editmessage.getText().toString();
				TokenShared = getSharedPreferences("usermessage", 0);
				tokenString = TokenShared.getString("token", "");
				if(tokenString.equals(""))
				{
					ToastClass.SetToast(Live_kidsAllResponse.this, "需要先登录才能够发送");
					setResult(304);
					Live_kidsAllResponse.this.finish();
				}
				else if(editString.replaceAll(" ", "").equals(""))
				{
					ToastClass.SetToast(Live_kidsAllResponse.this, "信息不能够为空");
				}
				else {
					SendAsyData();
				}
			}
		});
		
		leftpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Live_kidsAllResponse.this.finish();
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
                        manageInput(Live_kidsAllResponse.this);
                        ToastClass.SetImageToast(Live_kidsAllResponse.this,"成功发送评论");
                        Live_kidsAllResponse.this.finish();
                        break;
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isRefresh = false;
                        adapter.notifyDataSetChanged();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isRefresh = false;
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
		tid = intent.getStringExtra("tid");
		myListview = (NoScrollListview)findViewById(R.id.live_kids_item_allresponse_listview);
		scrollView = (CustomScrollView)findViewById(R.id.live_kids_item_allresponse_scrollview);
		sendView = (ImageView)findViewById(R.id.live_kids_item_allresponse_send);
		editmessage = (EditText)findViewById(R.id.live_kids_item_allresponse_edit);
		leftpage = (ImageView)findViewById(R.id.live_kids_item_allresponse_return);
	}
	
	private void SendAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("tid", tid);
		param.put("token", tokenString);
		param.put("message", editString);
		RequstClient.post(CreateCommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                        Live_kidsAllResponse.this,
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
		param.put("tid", tid);
		param.put("page", ""+count);
		
		RequstClient.post(readCommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                      Live_kidsAllResponse.this,
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
					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
                        Log.e("asds",data);
                        String data_details = jsonObject
                        .getString("comment_list");
						JSONArray array = new JSONArray(data_details);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							list.add(GetMap(jsonObject2));
						}
						if (count == 0) {
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
}
