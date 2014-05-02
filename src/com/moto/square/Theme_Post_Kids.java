package com.moto.square;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.listview.CustomScrollView;
import com.moto.listview.NoScrollListview;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.main.R;
import com.moto.myactivity.MyActivity;
import com.moto.mymap.MyMapApplication;
import com.moto.toast.ToastClass;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class Theme_Post_Kids extends MyActivity{
    
	private ImageView send;
	private EditText editText;
	private NoScrollListview listview;
	private LinkedList<HashMap<String, Object>> Item_list = new LinkedList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private int count = 0;
	private KidsAdapter adapter;
	private boolean isrefresh = false;
	private String pid;
	private CustomScrollView scrollView;
	private String response_theme_message;
	private SharedPreferences TokenShared;
	private String tokenString;
	private DisplayImageOptions options;
	private Handler handler;
	private ImageView leftpage;
	
	private String readcommentUri = "http://damp-reef-9073.herokuapp.com/api/square/readcommentforpost";
	private String CreatecommentUri = "http://damp-reef-9073.herokuapp.com/api/square/createcommentforpost";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_discuss_kids_post_kids_response);
		init();
		adapter = new KidsAdapter(this, Item_list);
		listview.setAdapter(adapter);
		GetItemAsyData();
		
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_NULL:
                        isrefresh = false;
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
		
		scrollView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1500);
						} catch (Exception e) {
							e.printStackTrace();
						}
						isrefresh = true;
						count = 0;
						GetItemAsyData();
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
						GetItemAsyData();
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onLoadComplete();
					}
                    
				}.execute();
			}
		});
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				response_theme_message = editText.getText().toString();
				TokenShared = getSharedPreferences("usermessage", 0);
				tokenString = TokenShared.getString("token", "");
				if(tokenString.equals(""))
				{
					ToastClass.SetToast(Theme_Post_Kids.this, "需要先登录才能够发送");
					setResult(304);
					Theme_Post_Kids.this.finish();
				}
				else if(response_theme_message.replaceAll(" ", "").equals(""))
				{
					DialogMethod.dialogShow(Theme_Post_Kids.this,"请输入回复内容!");
				}
				else
				{
					SetAsyResponse();
				}
			}
		});
		leftpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Theme_Post_Kids.this.finish();
			}
		});
	}
    
	private void init() {
		// TODO Auto-generated method stub
		intent = getIntent();
		pid = intent.getStringExtra("pid");
		options = ImageMethod.GetOptions();
		leftpage = (ImageView)findViewById(R.id.kids_post_response_listview_return);
		send = (ImageView)findViewById(R.id.post_kids_send);
		editText = (EditText)findViewById(R.id.post_kids_response_theme_edit);
		listview = (NoScrollListview)findViewById(R.id.kids_post_response_listview);
		scrollView = (CustomScrollView)findViewById(R.id.kids_post_response_scrollview);
	}
	
	private void SetAsyResponse() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("pid", pid);
		param.put("token", tokenString);
		param.put("message", response_theme_message);
		
		RequstClient.post(CreatecommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                        Theme_Post_Kids.this,
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
						Toast.makeText(Theme_Post_Kids.this, "发送成功", Toast.LENGTH_SHORT).show();
						Theme_Post_Kids.this.finish();
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
    
	private void GetItemAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("pid", pid);
		param.put("page", ""+count);
		
		RequstClient.post(readcommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                      Theme_Post_Kids.this,
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
					if(isrefresh)
					{
						Item_list.clear();
					}
					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
						String data_details = jsonObject
                        .getString("comment_list");
						JSONArray array = new JSONArray(data_details);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							Item_list.add(GetMap(jsonObject2));
						}
						if (count == 0) {
							if (isrefresh)
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
			map.put("author", author);
			map.put("message", message);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
		
	}
	//内部类实现BaseAdapter  ，自定义适配器
	class KidsAdapter extends BaseAdapter{
        
		private Context context;
		LinkedList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
		
		public KidsAdapter(Context context, LinkedList<HashMap<String, Object>> list)
		{
			this.context = context;
			this.list = list;
		}
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
        
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
        
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
        
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolderKids holder;
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(context).inflate(R.layout.square_discuss_kids_post_kids_item, null);
			holder = new ViewHolderKids();
			holder.square_discuss_kids_post_kids_details = (TextView)convertView.findViewById(R.id.square_discuss_kids_post_kids_details);
			holder.square_discuss_kids_post_kids_img = (ImageView)convertView.findViewById(R.id.square_discuss_kids_post_kids_img);
			holder.square_discuss_kids_post_kids_username = (TextView)convertView.findViewById(R.id.square_discuss_kids_post_kids_username);
			convertView.setTag(holder);
			holder = (ViewHolderKids) convertView.getTag();
			
			map = list.get(position);	
			holder.square_discuss_kids_post_kids_details.setText((CharSequence) map.get("message"));
			holder.square_discuss_kids_post_kids_username.setText((CharSequence) map.get("author"));
			MyMapApplication.imageLoader.displayImage(imgPath,  holder.square_discuss_kids_post_kids_img,options,null);
            return convertView;
		}
        
		
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolderKids{
		TextView square_discuss_kids_post_kids_details;
		ImageView square_discuss_kids_post_kids_img;
		TextView square_discuss_kids_post_kids_username;
	}
}