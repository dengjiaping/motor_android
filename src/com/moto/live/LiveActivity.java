package com.moto.live;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.img.ScaleImageView;
import com.moto.listview.CustomListView;
import com.moto.listview.ProgressBarView;
import com.moto.main.Moto_MainActivity;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.CacheModel;
import com.moto.model.LiveNetworkModel;
import com.moto.toast.ToastClass;
import com.moto.utils.DateUtils;
import com.moto.utils.StringUtils;
import com.moto.utils.SystemSoundPlayer;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LiveActivity extends Moto_RootActivity{
	
    //	private TextView edit_live;
    //	private TextView continue_live;
	private ArrayList<HashMap<String, Object>> live_list = new ArrayList<HashMap<String,Object>>();
	private ArrayList<ArrayList<String>> carList = new ArrayList<ArrayList<String>>();
	private HashMap<String, Object> map;
//	private ArrayList<String> like_list = new ArrayList<String>();
//    private ArrayList<String> comment_list = new ArrayList<String>();
	private Handler handler;
	private MyAdapter adapter;
	private CustomListView myListView;
//	private CustomScrollView scrollView;
	private boolean isrefresh = false;
    private boolean isload = false;
	private String uriString = path+"api/live/readbireflive";
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
		super.onCreate(savedInstanceState);
		addContentView(R.layout.live, R.string.live, barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_refresh);
        init();
		adapter = new MyAdapter(LiveActivity.this);
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
                        isload = false;
                        //					progressBar_loading.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
//                        DialogMethod.stopProgressDialog();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:

                        isrefresh = false;
                        isload = false;
                        //					progressBar_loading.setVisibility(View.GONE);

                        myListView.onLoadMoreComplete();
                        myListView.onRefreshComplete();
//                        scrollView.onRefreshComplete();
//                        scrollView.onLoadComplete();

                        SystemSoundPlayer.getInstance(LiveActivity.this).playSendSound();
                        adapter.notifyDataSetChanged();
                        break;
                    case Constant.MSG_NULL:
                        isrefresh = false;
                        isload = false;
                        //					progressBar_loading.setVisibility(View.GONE);
                        myListView.onLoadMoreComplete();
                        myListView.onRefreshComplete();
//                        scrollView.onRefreshComplete();
//                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_FALTH:
                        isrefresh = false;
                        isload = false;
                        String str = (String) msg.obj;
                        ToastClass.SetToast(LiveActivity.this, str);
//                        DialogMethod.stopProgressDialog();
                        break;
                    case Constant.MSG_TESTSTART:
                        isrefresh = false;
                        isload = false;
                        DialogMethod.startProgressDialog(LiveActivity.this,"点赞中...");
                        break;
 				}
				super.handleMessage(msg);
			}
			
		};

//        //必须在这里面添加head或者foot
//        scrollView.addHeadFootView();
//
        myListView.setOnRefreshListener(new CustomListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isrefresh = true;
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
        myListView.setOnLoadListener(new CustomListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isload = true;
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
				extras.putString("tid", live_list.get(arg2-1).get("tid").toString());
				extras.putString("subject", live_list.get(arg2-1).get("subject").toString());
				pushToNextActivity(extras, Live_Kids_User.class, 304);
			}
		});
	}
	
	@Override
	public void rightBarButtonItemEvent() {
		// TODO Auto-generated method stub
		super.rightBarButtonItemEvent();
        myListView.mHeadState = myListView.REFRESHING;
        myListView.changeHeadViewByState();
		isrefresh = true;
		GetAsyData();


	}
    
	private void GetAsyData() {
		// TODO Auto-generated method stub
		RequestParams param;
		param = new RequestParams();
        if(isload)
        {
            if(live_list.size() == 0)
            {
                param.put("timestamp", DateUtils.getUTCCurrentTimestamp());
            }
            else
            {
                String time = live_list.get(live_list.size()-1).get("dateline").toString();
                param.put("timestamp", DateUtils.getUTCTimestamp(time));
            }

        }
        else
        {
            param.put("timestamp", DateUtils.getUTCCurrentTimestamp());

        }

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
					if(isrefresh)
					{
						live_list = new ArrayList<HashMap<String, Object>>();
						carList = new ArrayList<ArrayList<String>>();
//						like_list.clear();
//                        comment_list.clear();
//						isfirst = false;
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
						
//						String like_num = jsonObject1.getString("count_list");
//						JSONArray num_Array = new JSONArray(like_num);
//						int num = num_Array.length();
//						for(int i = 0; i < num; i++)
//						{
//							JSONObject jsonObject = num_Array.getJSONObject(i);
//							like_list.add(jsonObject.getString("like_count"));
//						}
//
//                        String comment_details = jsonObject1.getString("comment_list");
//                        JSONArray comment_Array = new JSONArray(comment_details);
//                        int comment_num = comment_Array.length();
//                        for(int i = 0; i < comment_num; i++)
//                        {
//                            JSONObject jsonObject = comment_Array.getJSONObject(i);
//                            comment_list.add(jsonObject.getString("comment_count"));
//                        }

						if (!isload) {
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
//		like_list = CacheModel.getLikeCacheLiveDate("linkedlist", LiveActivity.this);
//        comment_list = CacheModel.getCommentCacheLiveDate("linkedlist",LiveActivity.this);
		myListView = (CustomListView)findViewById(R.id.live_listview);

		myListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ChangeListviewAlpha(myListView, navigationBar);
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
            String status = jsonObject.getString("status");
            String like_count = jsonObject.getString("like_count");
            String comment_count = jsonObject.getString("comment_count");
			map.put("author", author);
			map.put("subject", subject);
			map.put("message", message);
			map.put("dateline", dateline);
            map.put("status",status);
			map.put("tid", tid);
			map.put("location", location);
			map.put("avatar", avatar);
            map.put("like_count",like_count);
            map.put("comment_count",comment_count);
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
		private HashMap<String, Object> map;
        //		private ImageLoader imageLoader;
		long time = 0;

        //定义两个int常量标记不同的Item视图
        public static final int HAVE_PHOTO_ITEM = 0;

        public static final int NO_PHOTO_ITEM = 1;



        public MyAdapter(Context context)
		{
			this.context = context;
            //			imageLoader = new ImageLoader(context);
			
		}

        @Override
        public int getItemViewType(int position) {
            if(carList.get(position).size() > 0)
            {
                return HAVE_PHOTO_ITEM;
            }
            else
            {
                return NO_PHOTO_ITEM;
            }
        }

        @Override
        public int getViewTypeCount() {
            //包含有两个视图，所以返回值为2
            return 2;
        }

        @Override
		public int getCount() {
			// TODO Auto-generated method stub
			return live_list.size();
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
			final ViewHolder holder1;
            final ViewHolder holder2;
            int type = getItemViewType(position);
			// TODO Auto-generated method stub
            switch (type)
            {
                case NO_PHOTO_ITEM:
                    if(convertView == null)
                    {
                        convertView = LayoutInflater.from(context).inflate(R.layout.live_item_nophoto, null);
                        holder1 = new ViewHolder();
                        holder1.user_img = (ImageView) convertView.findViewById(R.id.user_img);
                        holder1.user_name = (EmojiconTextView) convertView.findViewById(R.id.live_username);
                        holder1.detail = (EmojiconTextView) convertView.findViewById(R.id.live_detail_thing);
                        holder1.time = (EmojiconTextView) convertView.findViewById(R.id.live_time_text);
                        holder1.live_like_people_num = (TextView) convertView.findViewById(R.id.live_like_people_num);
                        holder1.live_item_time = (TextView) convertView.findViewById(R.id.live_item_time);
                        holder1.live_item_like_layout = (RelativeLayout) convertView.findViewById(R.id.live_item_like_layout);
                        holder1.live_like_img = (ImageView) convertView.findViewById(R.id.live_like_img);

                        holder1.live_comment_num = (TextView) convertView.findViewById(R.id.live_comment_num);
                        holder1.live_item_status_img = (ImageView) convertView.findViewById(R.id.live_item_status_img);
                        convertView.setTag(holder1);
                    }
                    else
                    {
                        holder1 = (ViewHolder) convertView.getTag();
                    }

                    map = live_list.get(position);
                    holder1.live_item_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
                    holder1.live_like_people_num.setText(map.get("like_count").toString());
                    holder1.live_comment_num.setText(map.get("comment_count").toString());

                    holder1.user_name.setText((CharSequence)map.get("author"));
                    holder1.detail.setText((CharSequence) map.get("message"));
                    holder1.time.setText(map.get("subject").toString());
                    //判断结束或者正在直播
                    if(map.get("status").equals("0"))
                    {
                        holder1.live_item_status_img.setBackgroundResource(R.drawable.over);
                    }
                    if(!map.get("avatar").toString().equals("null"))
                    {
                        MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder1.user_img,options,null);
                    }

//                    holder1.live_item_like_layout.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            SendLikeMessage(position, map.get("tid").toString(), holder1.live_like_people_num.getText().toString());
//                        }
//                    });
                    break;
                case HAVE_PHOTO_ITEM:
                    if(convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.live_item_havephoto, null);
                        holder2 = new ViewHolder();
                        holder2.user_img = (ImageView) convertView.findViewById(R.id.user_img);
                        holder2.user_name = (EmojiconTextView) convertView.findViewById(R.id.live_username);
                        holder2.img = (ScaleImageView) convertView.findViewById(R.id.live_thing_img);
                        holder2.detail = (EmojiconTextView) convertView.findViewById(R.id.live_detail_thing);
                        holder2.time = (EmojiconTextView) convertView.findViewById(R.id.live_time_text);
                        holder2.live_like_people_num = (TextView) convertView.findViewById(R.id.live_like_people_num);
                        holder2.live_item_time = (TextView) convertView.findViewById(R.id.live_item_time);

                        holder2.progressBarView = (ProgressBarView) convertView.findViewById(R.id.live_item_progress_View);
                        holder2.live_item_like_layout = (RelativeLayout) convertView.findViewById(R.id.live_item_like_layout);
                        holder2.live_like_img = (ImageView) convertView.findViewById(R.id.live_like_img);

                        holder2.live_comment_num = (TextView) convertView.findViewById(R.id.live_comment_num);
                        holder2.live_item_status_img = (ImageView) convertView.findViewById(R.id.live_item_status_img);
                        convertView.setTag(holder2);
                    }
                    else
                    {
                        holder2 = (ViewHolder) convertView.getTag();
                    }
                    map = live_list.get(position);
                    holder2.live_item_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
                    holder2.live_like_people_num.setText(map.get("like_count").toString());
                    holder2.live_comment_num.setText(map.get("comment_count").toString());

                    holder2.user_name.setText((CharSequence)map.get("author"));
                    holder2.detail.setText((CharSequence) map.get("message"));
                    holder2.time.setText(map.get("subject").toString());
                    //判断结束或者正在直播
                    if(map.get("status").equals("0"))
                    {
                        holder2.live_item_status_img.setBackgroundResource(R.drawable.over);
                    }
                    MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl_avatar(carList.get(position).get(0), 480),  holder2.img,Originaloptions,new SimpleImageLoadingListener(){

                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
                            super.onLoadingStarted(imageUri, view);
                            holder2.progressBarView.setVisibility(View.VISIBLE);
                            holder2.progressBarView.setProgressNotInUiThread(0);

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // TODO Auto-generated method stub
                            super.onLoadingFailed(imageUri, view, failReason);
                            holder2.progressBarView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            holder2.progressBarView.setProgressNotInUiThread(100);
                            holder2.progressBarView.setVisibility(View.GONE);

                        }

                    },new ImageLoadingProgressListener() {

                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current,
                                                     int total) {
                            // TODO Auto-generated method stub
                            if((System.currentTimeMillis() - time)>1000)
                            {
                                time = System.currentTimeMillis();
                                holder2.progressBarView.setProgressNotInUiThread(Math.round(100.0f * current / total));
                            }

                        }
                    });

                    holder2.img.setImageHeight(80);
                    holder2.img.setImageWidth(100);
//                    }
                    if(!map.get("avatar").toString().equals("null"))
                    {
                        MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder2.user_img,options,null);
                    }

//                    holder2.live_item_like_layout.setOnClickListener(new OnClickListener() {
//
//                        @Override
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            SendLikeMessage(position, map.get("tid").toString(), holder2.live_like_people_num.getText().toString());
//                        }
//                    });
                    break;
            }

            return convertView;
		}
        
		
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		ImageView user_img;
        EmojiconTextView user_name;
		ScaleImageView img;
		ProgressBarView progressBarView;
        EmojiconTextView detail;
        EmojiconTextView time;
		TextView live_like_people_num;
		TextView live_item_time;
		RelativeLayout live_item_like_layout;
		ImageView live_like_img;

        TextView live_comment_num;
        ImageView live_item_status_img;
	}
	
//	private void SendLikeMessage(int position, String tid, String numString){
//		TokenShared = getSharedPreferences("usermessage", 0);
//		tokenString = TokenShared.getString("token", "");
//		if(tokenString.equals(""))
//		{
//			ToastClass.SetToast(LiveActivity.this, "登录之后才能够点赞哟");
//		}
//		else {
//			likenum = Integer.parseInt(numString);
//			this.position = position;
//			RequestParams param;
//			param = new RequestParams();
//			param.put("token", tokenString);
//			param.put("pid", tid);
//			liveNetworkModel = new LiveNetworkModel(this, this);
//			liveNetworkModel.likelivepost(param);
//		}
//	}
//
//
//	@Override
//	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
//    throws JSONException {
//		// TODO Auto-generated method stub
//		super.handleNetworkDataWithSuccess(JSONObject);
//		String status = JSONObject.getString("status");
//		if(status.equals("like it!"))
//		{
//			like_list.set(position, ++likenum +"");
//		}
//		else if(status.equals("unlike it!")){
//			like_list.set(position, --likenum +"");
//		}
//		handler.obtainMessage(Constant.MSG_SUCCESS)
//		.sendToTarget();
//	}
//
//	@Override
//	public void handleNetworkDataWithFail(JSONObject jsonObject)
//    throws JSONException {
//		// TODO Auto-generated method stub
//		super.handleNetworkDataWithFail(jsonObject);
//		// 获取一个Message对象，设置what为1
//		Message msg = Message.obtain();
//		msg.obj = "点赞失败";
//		msg.what = Constant.MSG_FALTH;
//		// 发送这个消息到消息队列中
//		handler.sendMessage(msg);
//	}
//
//	@Override
//	public void handleNetworkDataGetFail(String message) throws JSONException {
//		// TODO Auto-generated method stub
//		super.handleNetworkDataGetFail(message);
//		// 获取一个Message对象，设置what为1
//		Message msg = Message.obtain();
//		msg.obj = message;
//		msg.what = Constant.MSG_FALTH;
//		// 发送这个消息到消息队列中
//		handler.sendMessage(msg);
//	}
//
//	@Override
//	public void handleNetworkDataStart() throws JSONException {
//		// TODO Auto-generated method stub
//		super.handleNetworkDataStart();
//		handler.obtainMessage(Constant.MSG_TESTSTART)
//		.sendToTarget();
//	}
    
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
		CacheModel.cacheLiveData("linkedlist",live_list,carList,LiveActivity.this);
	}
	
	
}
