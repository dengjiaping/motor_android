package com.moto.live;
import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
import com.moto.listview.ProgressBarView;
import com.moto.main.Moto_MainActivity;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.CacheModel;
import com.moto.model.LiveNetworkModel;
import com.moto.mymap.MyMapApplication;
import com.moto.toast.ToastClass;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveActivity extends Moto_RootActivity{
	
    //	private TextView edit_live;
    //	private TextView continue_live;
	private LinkedList<HashMap<String, Object>> live_list = new LinkedList<HashMap<String,Object>>();
	private LinkedList<LinkedList<String>> carList = new LinkedList<LinkedList<String>>();
	private HashMap<String, Object> map;
	private LinkedList<String> like_list = new LinkedList<String>();
	private Handler handler;
	private MyAdapter adapter;
	private NoScrollListview myListView;
	private CustomScrollView scrollView;
	private int count = 0;
	private boolean isrefresh = false;
	private boolean isfirst = true;
	private String uriString = "http://damp-reef-9073.herokuapp.com/api/live/readbireflive";
    //	private ProgressBar progressBar_loading;
    //	private ImageView refresh;
	
	private DisplayImageOptions options;
	private DisplayImageOptions Originaloptions;
	private LiveNetworkModel liveNetworkModel;
	
	private SharedPreferences TokenShared;
	private String tokenString;
	private int likenum = 0;
	private int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addContentView(R.layout.live, R.string.live, barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_refresh );
		
		init();
		adapter = new MyAdapter(LiveActivity.this,LiveActivity.this, live_list);
		myListView.setAdapter(adapter);
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isrefresh = false;
                        //					progressBar_loading.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        DialogMethod.stopProgressDialog();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isrefresh = false;
                        //					progressBar_loading.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_NULL:
                        isrefresh = false;
                        //					progressBar_loading.setVisibility(View.GONE);
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_FALTH:
                        String str = (String) msg.obj;
                        ToastClass.SetToast(LiveActivity.this, str);
                        DialogMethod.stopProgressDialog();
                        break;
                    case Constant.MSG_TESTSTART:
                        DialogMethod.startProgressDialog(LiveActivity.this,"点赞中...");
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
		myListView.setOnItemClickListener(new OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
				Bundle extras = new Bundle();
				extras.putString("tid", live_list.get(arg2).get("tid").toString());
				extras.putString("subject", live_list.get(arg2).get("subject").toString());
				pushToNextActivity(extras, Live_Kids_User.class, 304);
			}
		});
	}
	
	@Override
	public void rightBarButtonItemEvent() {
		// TODO Auto-generated method stub
		super.rightBarButtonItemEvent();
		scrollView.state = scrollView.REFRESHING;
		scrollView.changeHeaderViewByState();
		isrefresh = true;
		count = 0;
		GetAsyData();
		
	}
    
	private void GetAsyData() {
		// TODO Auto-generated method stub
		RequestParams param;
		param = new RequestParams();
		param.put("page", ""+count);
		RequstClient.post(uriString, param, new LoadCacheResponseLoginouthandler(
                                                                                 LiveActivity.this,
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
					if(isrefresh || isfirst)
					{
						live_list.clear();
						carList.clear();
						like_list.clear();
						isfirst = false;
					}
					JSONObject jsonObject1 = new JSONObject(data);
					if (jsonObject1.getString("is").equals("1")) {
						String data_details = jsonObject1
                        .getString("biref_live");
						JSONArray array = new JSONArray(data_details);
						int data_num = array.length();
						for (int i = 0; i < data_num; i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							live_list.add(GetMap(jsonObject2));
						}
						
						String like_num = jsonObject1.getString("count_list");
						JSONArray num_Array = new JSONArray(like_num);
						int num = num_Array.length();
						for(int i = 0; i < num; i++)
						{
							JSONObject jsonObject = num_Array.getJSONObject(i);
							like_list.add(jsonObject.getString("like_count"));
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
				handler.obtainMessage(Constant.MSG_NULL).sendToTarget();
				ToastClass.SetToast(LiveActivity.this, message);
			}
            
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
	private void init() {
		// TODO Auto-generated method stub
		options = ImageMethod.GetOptions();
		Originaloptions = ImageMethod.GetOriginalOptions();
		
		live_list = CacheModel.getCacheLiveDate("linkedlist",LiveActivity.this);
		carList = CacheModel.getPhotoCacheLiveDate("linkedlist",LiveActivity.this);
		like_list = CacheModel.getLikeCacheLiveDate("linkedlist", LiveActivity.this);
		myListView = (NoScrollListview)findViewById(R.id.live_listview);
		scrollView = (CustomScrollView)findViewById(R.id.live_myscrollview);
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ChangeScrollviewAlpha(scrollView, navigationBar);
				return false;
			}
		});
		
		liveNetworkModel = new LiveNetworkModel(this, this);
		
		if(live_list.size() == 0)
		{
			GetAsyData();
		}
        
	}
	private HashMap<String, Object> GetMap(JSONObject jsonObject)
	{
		map = new HashMap<String, Object>();
		try {
			
			String author = jsonObject.getString("author");
			String subject = jsonObject.getString("subject");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String tid = jsonObject.getString("tid");
			String location = jsonObject.getString("location");
			String avatar = jsonObject.getString("avatar");
			map.put("author", author);
			map.put("subject", subject);
			map.put("message", message);
			map.put("dateline", dateline);
			map.put("tid", tid);
			map.put("location", location);
			map.put("avatar", avatar);
			carList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
		
	}
	
    //	内部类实现BaseAdapter  ，自定义适配器
	class MyAdapter extends BaseAdapter{
        
		private Context context;
		private LinkedList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
        //		private ImageLoader imageLoader;
		private Activity activity;
		long time = 0;
		
		
		public MyAdapter(Activity activity, Context context, LinkedList<HashMap<String, Object>> list)
		{
			this.context = context;
			this.list = list;
			this.activity = activity;
            //			imageLoader = new ImageLoader(context);
			
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
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			final ViewHolder holder;
			// TODO Auto-generated method stub
            //			if(convertView == null)
            //			{
            convertView = LayoutInflater.from(context).inflate(R.layout.live_item, null);
            holder = new ViewHolder();
            holder.user_img = (ImageView)convertView.findViewById(R.id.user_img);
            holder.user_name = (TextView)convertView.findViewById(R.id.live_username);
            holder.img = (ScaleImageView)convertView.findViewById(R.id.live_thing_img);
            holder.detail = (TextView)convertView.findViewById(R.id.live_detail_thing);
            holder.time = (TextView)convertView.findViewById(R.id.live_time_text);
            holder.live_like_people_num = (TextView)convertView.findViewById(R.id.live_like_people_num);
            holder.live_item_time = (TextView)convertView.findViewById(R.id.live_item_time);
            holder.live_item_layout = (RelativeLayout)convertView.findViewById(R.id.live_item_layout);
            holder.progressBarView = (ProgressBarView)convertView.findViewById(R.id.live_item_progress_View);
            holder.live_item_like_layout = (LinearLayout)convertView.findViewById(R.id.live_item_like_layout);
            holder.live_like_img = (ImageView)convertView.findViewById(R.id.live_like_img);
            //					convertView.setTag(holder);
            //			}
            //			else {
            //				holder = (ViewHolder) convertView.getTag();
            //			}
			map = list.get(position);
			holder.live_item_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
			if(like_list.size() > 0) {
				holder.live_like_people_num.setText(like_list.get(position));
			}
			holder.user_name.setText((CharSequence)map.get("author"));
            holder.detail.setText((CharSequence) map.get("message"));
            holder.time.setText(map.get("subject").toString());
			if(carList.get(position).size() > 0)
			{
				holder.live_item_layout.setVisibility(View.VISIBLE);
				MyMapApplication.imageLoader.displayImage(UrlUtils.imageUrl(carList.get(position).get(carList.get(position).size()-1)),  holder.img,Originaloptions,new SimpleImageLoadingListener(){
                    
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						super.onLoadingStarted(imageUri, view);
						holder.progressBarView.setProgressNotInUiThread(0);
						holder.progressBarView.setVisibility(View.VISIBLE);
					}
                    
					@Override
					public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
						// TODO Auto-generated method stub
						super.onLoadingFailed(imageUri, view, failReason);
						holder.progressBarView.setVisibility(View.GONE);
					}
                    
					@Override
					public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						holder.progressBarView.setVisibility(View.GONE);
						holder.progressBarView.setProgressNotInUiThread(100);
					}
					
				},new ImageLoadingProgressListener() {
					
					@Override
					public void onProgressUpdate(String imageUri, View view, int current,
                                                 int total) {
						// TODO Auto-generated method stub
						if((System.currentTimeMillis() - time)>1000)
						{
							time = System.currentTimeMillis();
							holder.progressBarView.setProgressNotInUiThread(Math.round(100.0f * current / total));
						}
						
					}
				});
				
				holder.img.setImageHeight(80);
				holder.img.setImageWidth(100);
			}
			if(!map.get("avatar").toString().equals("null"))
			{
				MyMapApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder.user_img,options,null);
			}
			
			holder.live_item_like_layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SendLikeMessage(position, map.get("tid").toString(), holder.live_like_people_num.getText().toString());
				}
			});
            return convertView;
		}
        
		
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		ImageView user_img;
		TextView user_name;
		ScaleImageView img;
		RelativeLayout live_item_layout;
		ProgressBarView progressBarView;
		TextView detail;
		TextView time;
		TextView live_like_people_num;
		TextView live_item_time;
		LinearLayout live_item_like_layout;
		ImageView live_like_img;
	}
	
	private void SendLikeMessage(int position, String tid, String numString){
		TokenShared = getSharedPreferences("usermessage", 0);
		tokenString = TokenShared.getString("token", "");
		if(tokenString.equals(""))
		{
			ToastClass.SetToast(LiveActivity.this, "登录之后才能够点赞哟");
		}
		else {
			likenum = Integer.parseInt(numString);
			this.position = position;
			RequestParams param;
			param = new RequestParams();
			param.put("token", tokenString);
			param.put("pid", tid);
			liveNetworkModel = new LiveNetworkModel(this, this);
			liveNetworkModel.likelivepost(param);
		}
	}
	
	
	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataWithSuccess(JSONObject);
		String status = JSONObject.getString("status");
		if(status.equals("like it!"))
		{
			like_list.set(position, ++likenum +"");
		}
		else if(status.equals("unlike it!")){
			like_list.set(position, --likenum +"");
		}
		handler.obtainMessage(Constant.MSG_SUCCESS)
		.sendToTarget();
	}
    
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject)
    throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataWithFail(jsonObject);
		// 获取一个Message对象，设置what为1
		Message msg = Message.obtain();
		msg.obj = "点赞失败";
		msg.what = Constant.MSG_FALTH;
		// 发送这个消息到消息队列中
		handler.sendMessage(msg);
	}
    
	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataGetFail(message);
		// 获取一个Message对象，设置what为1
		Message msg = Message.obtain();
		msg.obj = message;
		msg.what = Constant.MSG_FALTH;
		// 发送这个消息到消息队列中
		handler.sendMessage(msg);
	}
    
	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataStart();
		handler.obtainMessage(Constant.MSG_TESTSTART)
		.sendToTarget();
	}
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
            case 301:
                break;
                //		case 302:
                //			edit_live.setVisibility(View.GONE);
                //			continue_live.setVisibility(View.VISIBLE);
                //			break;
                //		case 303:
                //			edit_live.setVisibility(View.VISIBLE);
                //			continue_live.setVisibility(View.GONE);
                //			break;
            case 304:
                Moto_MainActivity.radioGroup.check(R.id.main_tab_user);
                break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
    
	//每次pause的时候，存一下list中的数据，方便第二次进入的时候显示
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		CacheModel.cacheLiveData("linkedlist",live_list,carList,like_list,LiveActivity.this);
	}
	
	
}
