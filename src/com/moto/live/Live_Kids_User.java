package com.moto.live;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnInfoWindowClickListener;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.Polyline;
import com.amap.api.maps2d.model.PolylineOptions;
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
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.CacheModel;
import com.moto.model.LiveNetworkModel;
import com.moto.mymap.MyMapApplication;
import com.moto.photo.ImageBrowserActivity;
import com.moto.photo.UserPhotosView;
import com.moto.photo.UserPhotosView.onPagerPhotoItemClickListener;
import com.moto.toast.ToastClass;
import com.moto.utils.DateUtils;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Live_Kids_User extends Moto_RootActivity implements OnMarkerClickListener,
OnInfoWindowClickListener, InfoWindowAdapter{
    
//	private CustomScrollView scrollView;
    private LinearLayout live_kids_user_bottom;
    private Handler handler;
    private String tid;
    private String subject;
    private String avatar = "";
    private ImageView user_img;
    private TextView live_title;
    private RelativeLayout comment;
    private RelativeLayout live_kids_share;
    private RelativeLayout live_kids_collect;
    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
    private ArrayList<HashMap<String, Object>> LocationList = new ArrayList<HashMap<String,Object>>();
    private ArrayList<ArrayList<String>> carList = new ArrayList<ArrayList<String>>();
    private ArrayList<ArrayList<HashMap<String,Integer>>> WidthHeightList = new ArrayList<ArrayList<HashMap<String,Integer>>>();
    private HashMap<String, Object> map;
    private CustomListView myListView;
    private SharedPreferences TokenShared;
    private View headview;
    private MyAdapter adapter;
    private boolean isRefresh = false;
    private boolean isload = false;
    private int count = 0;
    private DisplayImageOptions options;
    private DisplayImageOptions Originaloptions2;
    private DisplayImageOptions Originaloptions3;
    private ProgressBar loadingProgressBar;
    private int screenWidth;
    private String uriString = path+"api/live/readdetaillive";
	//定位所需变量
    private AMap aMap;
    private MapView mapView;
	private MarkerOptions markerOption;   //添加位置图层
	//添加到地图上面那一层
    private TextView text_map;
    //划线
    private Polyline polyline;
    private PolylineOptions polylineOptions;

    private Intent intent;
    private String imgPath = "http://motor.qiniudn.com/";
    private RequestParams param;
    private String tokenString;
    private LiveNetworkModel liveNetworkModel;

    private String firstDate;   //最开始的时间
    private String pageDate;    //阶段时间

    private int like_num = 0;
    private int position = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		
		addContentView(R.layout.live_kids, subject, barButtonIconType.barButtonIconType_Back, barButtonIconType.barButtonIconType_None);



        //为listview添加头部

        headview = LayoutInflater.from(this).inflate(R.layout.live_kids_head, null);
        mapView = (MapView)headview.findViewById(R.id.live_kids_map);
        user_img = (ImageView)headview.findViewById(R.id.live_kids_user_img);
        live_title = (TextView)headview.findViewById(R.id.live_kids_title);
        loadingProgressBar = (ProgressBar)headview.findViewById(R.id.live_kids_loading_progressBar);
        text_map = (TextView)headview.findViewById(R.id.live_kids_text_map);
        myListView = (CustomListView)findViewById(R.id.live_kids_listview);
        myListView.addHeaderView(headview,null,false);

		mapView.onCreate(savedInstanceState);// 此方法必须重写
		init();
		WindowManager wm = this.getWindowManager();
        
		screenWidth = wm.getDefaultDisplay().getWidth();
		
		scrollListener();
		SetListAdapter();
		SetCommentListener();
		SetLeftListener();
		SetHandler();
	}
    
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
	}
	
	private void setUpMap() {
        //		aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
		aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
		aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
		aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        //		addMarkersToMap();// 往地图上添加marker
        if(list.size()>0)
        {
            aMap.clear();
            addMarkersToMap();// 往地图上添加marker
        }
	}
    
	protected void scrollListener()
	{
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



        user_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.size() == 0)
                {
                    ToastClass.SetToast(Live_Kids_User.this,"查看失败");
                }
                else if(!ToastClass.IsHaveToken(Live_Kids_User.this))
                {
                    ToastClass.SetToast(Live_Kids_User.this,"请先登录之后再查看");
                }
                else{
                    Bundle bundle = new Bundle();
                    bundle.putString("author",list.get(0).get("author").toString());
                    bundle.putString("token",ToastClass.GetTokenString(Live_Kids_User.this));
                    pushToNextActivity(bundle,User_OwnPage.class,304);

                }

            }
        });
	}
	//设置handler
	protected void SetHandler()
	{
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isRefresh = false;
                        isload = false;
                        loadingProgressBar.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                        //					SetGaoDeMap();
                        aMap.clear();
                        addMarkersToMap();// 往地图上添加marker
//                        scrollView.post(new Runnable() {
//                            //让scrollview跳转到顶部，必须放在runnable()方法中
//                            @Override
//                            public void run() {
//                                scrollView.scrollTo(0, CacheModel.getHeight("scrollviewheight"+tid, Live_Kids_User.this));
//                            }
//                        });
                        if(list.size() > 0)
                        {
                            avatar = list.get(0).get("avatar").toString();
                            if(!avatar.equals("null"))
                            {
                                MyMapApplication.imageLoader.displayImage(UrlUtils.avatarUrl(avatar),  user_img,options,null);
                            }
                            live_title.setText(list.get(0).get("author").toString());
                        }
//                        scrollView.onRefreshComplete();
//                        scrollView.onLoadComplete();
                        myListView.onRefreshComplete();
                        myListView.onLoadMoreComplete();
                        break;
                        //获取成功
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isRefresh = false;
                        isload = false;
                        //					SetGaoDeMap();
                        aMap.clear();
                        addMarkersToMap();// 往地图上添加marker
                        adapter.notifyDataSetChanged();
                        loadingProgressBar.setVisibility(View.GONE);
                        if(list.size() > 0)
                        {
                            avatar = list.get(0).get("avatar").toString();
                            if(!avatar.equals("null"))
                            {
                                MyMapApplication.imageLoader.displayImage(UrlUtils.avatarUrl(avatar),  user_img,options,null);
                            }
                            live_title.setText(list.get(0).get("author").toString());
                        }
//                        scrollView.onRefreshComplete();
//                        scrollView.onLoadComplete();
                        myListView.onRefreshComplete();
                        myListView.onLoadMoreComplete();
                        break;
                    case Constant.MSG_NULL:
                        isRefresh = false;
                        isload = false;
                        loadingProgressBar.setVisibility(View.GONE);
//                        scrollView.onRefreshComplete();
//                        scrollView.onLoadComplete();
                        myListView.onRefreshComplete();
                        myListView.onLoadMoreComplete();
                        break;
                    case Constant.MSG_FALTH:
                        isRefresh = false;
                        isload = false;
                        String str = (String) msg.obj;
                        ToastClass.SetToast(Live_Kids_User.this, str);
                        DialogMethod.stopProgressDialog();
                        break;
                    case Constant.MSG_TESTSTART:
                        isRefresh = false;
                        isload = false;

                        break;
                    case Constant.MSG_WAITSUCCESS:
                        isRefresh = false;
                        isload = false;
                        String string = (String) msg.obj;
                        ToastClass.SetToast(Live_Kids_User.this, string);
                        adapter.notifyDataSetChanged();
                        DialogMethod.stopProgressDialog();
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
	//为返回建添加监听
	protected void SetLeftListener()
	{
		
		live_kids_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(Intent.createChooser(intent, "分享"));
			}
		});
		
		text_map.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(Live_Kids_User.this, Live_Kids_Map.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("list", LocationList);
				intent.putExtras(bundle);
				startActivityForResult(intent, 0);
			}
		});
		
		live_kids_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SendKeepMessage();
			}
		});
	}
	//点击收藏执行方法
	private void SendKeepMessage(){
		tokenString = ToastClass.GetTokenString(Live_Kids_User.this);
		if(tokenString.equals(""))
		{
			ToastClass.SetToast(Live_Kids_User.this, "登录之后才能够收藏哟");
		}
		else {
            DialogMethod.startProgressDialog(Live_Kids_User.this,"收藏中...");
			RequestParams param;
			param = new RequestParams();
			param.put("token", tokenString);
			param.put("tid", tid);
			liveNetworkModel = new LiveNetworkModel(this, this);
			liveNetworkModel.liveKeeppost(param);
		}
	}
	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataWithSuccess(JSONObject);
		String status = JSONObject.getString("status");
		if(status.equals("keep it!"))
		{
			// 获取一个Message对象，设置what为1
			Message msg = Message.obtain();
			msg.obj = "收藏成功";
			msg.what = Constant.MSG_WAITSUCCESS;
			// 发送这个消息到消息队列中
			handler.sendMessage(msg);
		}
		else if(status.equals("unkeep it!")){
			// 获取一个Message对象，设置what为1
			Message msg = Message.obtain();
			msg.obj = "成功取消收藏";
			msg.what = Constant.MSG_WAITSUCCESS;
			// 发送这个消息到消息队列中
			handler.sendMessage(msg);
		}
        else if(status.equals("like it!"))
        {
            list.get(position).put("like_count",++like_num);
            // 获取一个Message对象，设置what为1
            Message msg = Message.obtain();
            msg.obj = "点赞成功";
            msg.what = Constant.MSG_WAITSUCCESS;
            // 发送这个消息到消息队列中
            handler.sendMessage(msg);
        }
        else if(status.equals("unlike it!")){
            // 获取一个Message对象，设置what为1
            list.get(position).put("like_count",--like_num);
            Message msg = Message.obtain();
            msg.obj = "成功取消点赞";
            msg.what = Constant.MSG_WAITSUCCESS;
            // 发送这个消息到消息队列中
            handler.sendMessage(msg);
        }

	}
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject)
    throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataWithFail(jsonObject);
		// 获取一个Message对象，设置what为1
		Message msg = Message.obtain();
		msg.obj = "失败";
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

	}
    
	//为listview设置适配器并添加监听
	protected void SetListAdapter()
	{
		adapter = new MyAdapter(this);
		myListView.setAdapter(adapter);
		myListView.setOnItemClickListener(new OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
		// TODO Auto-generated method stub
		intent = new Intent();
		intent.putExtra("pid", list.get(arg2-2).get("pid").toString());
		intent.putExtra("subject", subject);
        intent.putExtra("dateline",list.get(arg2-2).get("dateline").toString());
	    int num = carList.get(arg2-2).size();
		if(num == 0)
	    {
		    intent.putExtra("photoname", "null");
		}
		else {
			intent.putExtra("photoname", carList.get(arg2-2).get(num - 1));
		}
		intent.setClass(Live_Kids_User.this, LiveKidsResponse.class);
		startActivityForResult(intent, 304);
			}
		});
	}
	//为回复评论button添加监听
	protected void SetCommentListener()
	{
		comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("tid", tid);
				intent.setClass(Live_Kids_User.this, Live_kidsAllResponse.class);
				startActivityForResult(intent, 304);
			}
		});

	}
	protected void init() {
		// TODO Auto-generated method stub
        intent = getIntent();
        tid = intent.getStringExtra("tid");
        subject = intent.getStringExtra("subject");
        setNavigationBarTitle(subject);


		options = ImageMethod.GetOptions();
		Originaloptions2 = ImageMethod.GetOriginalOptions();
        Originaloptions3 = ImageMethod.GetOriginalOptions();
		
		list = CacheModel.getCacheLiveDate("kidslinkedlist"+tid,Live_Kids_User.this);
		carList = CacheModel.getPhotoCacheLiveDate("kidslinkedlist"+tid,Live_Kids_User.this);
		LocationList = CacheModel.getLocationCacheLiveDate("kidslinkedlist"+tid, Live_Kids_User.this);
		WidthHeightList = CacheModel.getWidthHeightCacheLiveDate("kidslinkedlist"+tid, Live_Kids_User.this);
        dateList.clear();
        getNumDate();



		live_kids_share = (RelativeLayout)findViewById(R.id.live_kids_share);

		
		live_kids_user_bottom = (LinearLayout)findViewById(R.id.live_kids_user_bottom);
		live_kids_user_bottom.setVisibility(View.VISIBLE);
		live_kids_user_bottom.getBackground().setAlpha(20);//设置透明度

        myListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                ChangeListviewAlpha(myListView, navigationBar);
                return false;
            }
        });
		comment = (RelativeLayout)findViewById(R.id.live_kids_comment);
		live_kids_collect = (RelativeLayout)findViewById(R.id.live_kids_record);

		
		if(list.size() == 0)
		{
            Log.e("aaa","ass");
            loadingProgressBar.setVisibility(View.VISIBLE);
			GetAsyData();
		}
        else{
            avatar = list.get(0).get("avatar").toString();
            if(!avatar.equals("null"))
            {
                MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(avatar),  user_img,options,null);
            }
            live_title.setText(list.get(0).get("author").toString());
        }
	}
	
	protected void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("tid", tid);

        if(isload)
        {
            if(list.size() == 0)
            {
                param.put("timestamp", DateUtils.getUTCStartTimestamp());
            }
            else
            {
                String time = list.get(list.size()-1).get("dateline").toString();
                param.put("timestamp", DateUtils.getUTCTimestamp(time));
            }

        }
        else
        {
            param.put("timestamp", DateUtils.getUTCStartTimestamp());
        }
		
		RequstClient.post(uriString, param, new LoadCacheResponseLoginouthandler(
                                                                                 Live_Kids_User.this,
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
						list = new ArrayList<HashMap<String, Object>>();
						LocationList = new ArrayList<HashMap<String, Object>>();
						WidthHeightList = new ArrayList<ArrayList<HashMap<String, Integer>>>();
						carList = new ArrayList<ArrayList<String>>();
					}
					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
						String data_details = jsonObject
                        .getString("detail_live");
						JSONArray array = new JSONArray(data_details);
						int a = array.length();
						for (int i = 0; i < a; i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							list.add(GetMap(jsonObject2));
                            //							getNumDate(list);
						}

                        dateList.clear();
                        getNumDate();
                        String location_data = jsonObject.getString("location_list");
                        JSONArray array2 = new JSONArray(location_data);
                        int a2 = array2.length();
                        for (int i = 0; i < a2; i++) {
                            JSONObject jsonObject2 = (JSONObject) array2.get(i);
                            LocationList.add(GetLocationMap(jsonObject2));
                            //								getNumDate(list);
                        }
						if (!isload) {
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
				ToastClass.SetToast(Live_Kids_User.this, message);
			}
            
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
	private HashMap<String, Object> GetLocationMap(
                                                   JSONObject jsonObject) {
		// TODO Auto-generated method stub
		map =new HashMap<String, Object>();
		try {
			
			String latitude = jsonObject.getString("latitude");
			String longitude = jsonObject.getString("longitude");
			String location = jsonObject.getString("location");
			map.put("latitude", latitude);
			map.put("longitude", longitude);
			map.put("location", location);
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	private HashMap<String, Object> GetMap(JSONObject jsonObject)
	{
		map = new HashMap<String, Object>();
		try {
			
			String author = jsonObject.getString("author");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String pid = jsonObject.getString("pid");
			String avatar = jsonObject.getString("avatar");
			String location = jsonObject.getString("location");
			String username = jsonObject.getString("username");
            String like_count = jsonObject.getString("like_count");
            String comment_count = jsonObject.getString("comment_count");
			map.put("author", author);
			map.put("message", message);
			map.put("dateline", dateline);
			map.put("pid", pid);
			map.put("username", username);
			map.put("avatar", avatar);
			this.avatar = avatar;
			map.put("location", location);
            map.put("like_count",like_count);
            map.put("comment_count",comment_count);
			carList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
			WidthHeightList.add(StringUtils.hashToWidthHeightArray(jsonObject.getString("photoinfo")));
            //			map.put("date", DateUtils.getDate(DateUtils.GetData(dateline)));
            //			map.put("dateNum", "第1天");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
		
		return map;
	}

    //获取天数
    	private void getNumDate()
    	{
            dateList.clear();
            int num = list.size();
            if(num > 0)
            {
                firstDate = list.get(0).get("dateline").toString();
                pageDate = firstDate;
            }
            dateList.add("第1天");
            for(int i = 1; i < num; i++)
            {
                long date = DateUtils.getLocalDistDatas(pageDate,list.get(i).get("dateline").toString());

                if(date == 0)
                {
                    dateList.add("");
                }
                else
                {
                    long datenum = DateUtils.getLocalDistDatas(firstDate,list.get(i).get("dateline").toString());
                    dateList.add("第"+(datenum+1)+"天");
                    pageDate = list.get(i).get("dateline").toString();
                }
            }
    	}
	
    //	内部类实现BaseAdapter  ，自定义适配器
	class MyAdapter extends BaseAdapter{
        
		private Context context;
		private HashMap<String, Object> map;
		private long time = 0;
        //定义三个int常量标记不同的Item视图
        public static final int HAVE_PHOTO_ITEM = 0;

        public static final int NO_PHOTO_ITEM = 1;

        public static final int HAVE_SAMLLPHOTO_ITEM = 2;
		public MyAdapter(Context context)
		{
			this.context = context;
		}
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

        @Override
        public int getItemViewType(int position) {
            int num = carList.get(position).size();
            if(num == 1)
            {
                return HAVE_PHOTO_ITEM;
            }
            else if(num > 1)
            {
                return HAVE_SAMLLPHOTO_ITEM;
            }
            else
            {
                return NO_PHOTO_ITEM;
            }

        }

        @Override
        public int getViewTypeCount() {
            return 3;
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
            final NoViewHolder holder1;
            final PhotoViewHolder holder2;
            final SmallViewHolder holder3;
			// TODO Auto-generated method stub
            int type = getItemViewType(position);
            // TODO Auto-generated method stub
            switch (type)
            {
                case NO_PHOTO_ITEM:
                    if(convertView == null)
                    {
                        convertView = LayoutInflater.from(context).inflate(R.layout.live_kids_item_nophoto, null);
                        holder1 = new NoViewHolder();
                        holder1.live_kids_item_username = (TextView) convertView.findViewById(R.id.live_kids_item_username);
                        holder1.live_kids_item_details = (TextView) convertView.findViewById(R.id.live_kids_item_details);
                        holder1.live_kids_item_time_text = (TextView) convertView.findViewById(R.id.live_kids_item_time_text);
                        holder1.live_kids_item_position_text = (TextView) convertView.findViewById(R.id.live_kids_item_position_text);
                        holder1.live_kids_item_user_img = (ImageView) convertView.findViewById(R.id.live_kids_item_user_img);
                        holder1.live_kids_item_timelayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_timelayout);
                        holder1.live_kids_item_userlayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_userlayout);
                        holder1.live_kids_item_datenum = (TextView) convertView.findViewById(R.id.live_kids_item_datenum);
                        holder1.live_kids_item_date = (TextView) convertView.findViewById(R.id.live_kids_item_date);
                        holder1.live_kids_item_week = (TextView) convertView.findViewById(R.id.live_kids_item_week);

                        holder1.live_item_like_layout = (RelativeLayout)convertView.findViewById(R.id.live_item_like_layout);
                        holder1.live_kids_comment_num = (TextView)convertView.findViewById(R.id.live_kids_comment_num);
                        holder1.live_kids_like_people_num = (TextView)convertView.findViewById(R.id.live_kids_like_people_num);
                        convertView.setTag(holder1);
                    }
                    else
                    {
                        holder1 = (NoViewHolder) convertView.getTag();
                        holder1.live_kids_item_timelayout.setVisibility(View.GONE);
                        holder1.live_kids_item_userlayout.setVisibility(View.GONE);
                    }

                    map = list.get(position);

                    holder1.live_kids_comment_num.setText(map.get("comment_count").toString());
                    holder1.live_kids_like_people_num.setText(map.get("like_count").toString());
                    if(dateList.get(position).equals(""))
                    {
                        holder1.live_kids_item_userlayout.setVisibility(View.VISIBLE);
                        holder1.live_kids_item_username.setText(map.get("username").toString());
                        holder1.live_kids_item_time_text.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
//                holder.live_kids_item_text_week.setText(DateUtils.getLocalweek(map.get("dateline").toString()));
                        MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder1.live_kids_item_user_img,options,null);
                    }
                    else{
                        holder1.live_kids_item_timelayout.setVisibility(View.VISIBLE);
                        holder1.live_kids_item_datenum.setText(dateList.get(position));
                        holder1.live_kids_item_date.setText(DateUtils.getYearMonthDay(map.get("dateline").toString()));
                        holder1.live_kids_item_week.setText(DateUtils.getLocalweek(map.get("dateline").toString()));
                    }

                    holder1.live_kids_item_details.setText((CharSequence) map.get("message"));

                    if(!map.get("location").toString().equals("null"))
                    {
//            	holder.live_kids_item_position.setVisibility(View.VISIBLE);
                        holder1.live_kids_item_position_text.setText((CharSequence) map.get("location"));
                    }
                    else {
                        holder1.live_kids_item_position_text.setText(R.string.unknownposition);
                    }

                    holder1.live_item_like_layout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            SendLikeMessage(position, map);
                        }
                    });

                    break;
                case HAVE_PHOTO_ITEM:
                    if(convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.live_kids_item_havephoto, null);
                        holder2 = new PhotoViewHolder();
                        holder2.progressBarView = (ProgressBarView) convertView.findViewById(R.id.live_kids_item_progress_View);
                        holder2.live_kids_item_img = (ScaleImageView) convertView.findViewById(R.id.live_kids_item_img);
                        holder2.live_kids_item_img_cover = (ScaleImageView) convertView.findViewById(R.id.live_kids_item_img_cover);
                        holder2.live_kids_item_username = (TextView) convertView.findViewById(R.id.live_kids_item_username);
                        holder2.live_kids_item_details = (TextView) convertView.findViewById(R.id.live_kids_item_details);
                        holder2.live_kids_item_time_text = (TextView) convertView.findViewById(R.id.live_kids_item_time_text);
                        holder2.live_kids_item_position_text = (TextView) convertView.findViewById(R.id.live_kids_item_position_text);
                        holder2.live_kids_item_user_img = (ImageView) convertView.findViewById(R.id.live_kids_item_user_img);
                        holder2.live_kids_item_timelayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_timelayout);
                        holder2.live_kids_item_userlayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_userlayout);
                        holder2.live_kids_item_datenum = (TextView) convertView.findViewById(R.id.live_kids_item_datenum);
                        holder2.live_kids_item_date = (TextView) convertView.findViewById(R.id.live_kids_item_date);
                        holder2.live_kids_item_week = (TextView) convertView.findViewById(R.id.live_kids_item_week);

                        holder2.live_item_like_layout = (RelativeLayout)convertView.findViewById(R.id.live_item_like_layout);
                        holder2.live_kids_comment_num = (TextView)convertView.findViewById(R.id.live_kids_comment_num);
                        holder2.live_kids_like_people_num = (TextView)convertView.findViewById(R.id.live_kids_like_people_num);

                        holder2.live_kids_item_img.setImageHeight(900);
                        holder2.live_kids_item_img.setImageWidth(screenWidth);
                        holder2.live_kids_item_img_cover.setImageHeight(900);
                        holder2.live_kids_item_img_cover.setImageWidth(screenWidth);
                        convertView.setTag(holder2);
                    }
                    else
                    {
                        holder2 = (PhotoViewHolder) convertView.getTag();
                        holder2.live_kids_item_timelayout.setVisibility(View.GONE);
                        holder2.live_kids_item_userlayout.setVisibility(View.GONE);


                    }
                    map = list.get(position);
                    holder2.live_kids_comment_num.setText(map.get("comment_count").toString());
                    holder2.live_kids_like_people_num.setText(map.get("like_count").toString());
                    if(dateList.get(position).equals(""))
                    {
                        holder2.live_kids_item_userlayout.setVisibility(View.VISIBLE);
                        holder2.live_kids_item_username.setText(map.get("username").toString());
                        holder2.live_kids_item_time_text.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
                        MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder2.live_kids_item_user_img,options,null);
                    }
                    else{
                        holder2.live_kids_item_timelayout.setVisibility(View.VISIBLE);
                        holder2.live_kids_item_datenum.setText(dateList.get(position));
                        holder2.live_kids_item_date.setText(DateUtils.getYearMonthDay(map.get("dateline").toString()));
                        holder2.live_kids_item_week.setText(DateUtils.getLocalweek(map.get("dateline").toString()));
                    }

                    holder2.live_kids_item_details.setText((CharSequence) map.get("message"));

                    if(!map.get("location").toString().equals("null"))
                    {
                        holder2.live_kids_item_position_text.setText((CharSequence) map.get("location"));
                    }
                    else {
                        holder2.live_kids_item_position_text.setText(R.string.unknownposition);
                    }



                    String imageUrl = UrlUtils.imageUrl_avatar(carList.get(position).get(carList.get(position).size()-1),480);
                    MyMapApplication.imageLoader.displayImage(imageUrl, holder2.live_kids_item_img,Originaloptions2,new SimpleImageLoadingListener(){
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
                            super.onLoadingStarted(imageUri, view);
                            holder2.progressBarView.setProgressNotInUiThread(0);
                            holder2.progressBarView.setVisibility(View.VISIBLE);
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
                            holder2.progressBarView.setVisibility(View.GONE);
                            holder2.progressBarView.setProgressNotInUiThread(100);
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



                    final int m = position;

                    holder2.live_kids_item_img_cover.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(Live_Kids_User.this,
                                    ImageBrowserActivity.class);
                            intent.putExtra(ImageBrowserActivity.IMAGE_TYPE,
                                    ImageBrowserActivity.TYPE_ALBUM);
                            intent.putExtra("position", carList.get(m).size()-1);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("carlist", new ArrayList<String>(carList.get(m)));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_enter, 0);
                        }
                    });

                    holder2.live_item_like_layout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            SendLikeMessage(position, map);
                        }
                    });
                    break;
                case HAVE_SAMLLPHOTO_ITEM:
                    if(convertView == null) {
                        convertView = LayoutInflater.from(context).inflate(R.layout.live_kids_item_havesmallphoto, null);
                        holder3 = new SmallViewHolder();
                        holder3.progressBarView = (ProgressBarView) convertView.findViewById(R.id.live_kids_item_progress_View);
                        holder3.live_kids_item_img = (ScaleImageView) convertView.findViewById(R.id.live_kids_item_img);
                        holder3.live_kids_item_img_cover = (ScaleImageView) convertView.findViewById(R.id.live_kids_item_img_cover);
                        holder3.live_kids_item_username = (TextView) convertView.findViewById(R.id.live_kids_item_username);
                        holder3.live_kids_item_details = (TextView) convertView.findViewById(R.id.live_kids_item_details);
                        holder3.live_kids_item_time_text = (TextView) convertView.findViewById(R.id.live_kids_item_time_text);
                        holder3.live_kids_item_position_text = (TextView) convertView.findViewById(R.id.live_kids_item_position_text);
                        holder3.live_kids_item_user_img = (ImageView) convertView.findViewById(R.id.live_kids_item_user_img);
                        holder3.live_kids_item_timelayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_timelayout);
                        holder3.live_kids_item_userlayout = (LinearLayout) convertView.findViewById(R.id.live_kids_item_userlayout);
                        holder3.live_kids_item_datenum = (TextView) convertView.findViewById(R.id.live_kids_item_datenum);
                        holder3.live_kids_item_date = (TextView) convertView.findViewById(R.id.live_kids_item_date);
                        holder3.live_kids_item_week = (TextView) convertView.findViewById(R.id.live_kids_item_week);
                        holder3.mUpvPhotos = (UserPhotosView) convertView.findViewById(R.id.otherprofile_upv_photos);

                        holder3.live_item_like_layout = (RelativeLayout)convertView.findViewById(R.id.live_item_like_layout);
                        holder3.live_kids_comment_num = (TextView)convertView.findViewById(R.id.live_kids_comment_num);
                        holder3.live_kids_like_people_num = (TextView)convertView.findViewById(R.id.live_kids_like_people_num);

                        holder3.live_kids_item_img.setImageHeight(540);
                        holder3.live_kids_item_img.setImageWidth(screenWidth);
                        holder3.live_kids_item_img_cover.setImageHeight(540);
                        holder3.live_kids_item_img_cover.setImageWidth(screenWidth);
                        convertView.setTag(holder3);
                    }
                    else
                    {
                        holder3 = (SmallViewHolder) convertView.getTag();
                        holder3.live_kids_item_timelayout.setVisibility(View.GONE);
                        holder3.live_kids_item_userlayout.setVisibility(View.GONE);

                    }
                    map = list.get(position);
                    holder3.live_kids_comment_num.setText(map.get("comment_count").toString());
                    holder3.live_kids_like_people_num.setText(map.get("like_count").toString());
                    if(dateList.get(position).equals(""))
                    {
                        holder3.live_kids_item_userlayout.setVisibility(View.VISIBLE);
                        holder3.live_kids_item_username.setText(map.get("username").toString());
                        holder3.live_kids_item_time_text.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
                        MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder3.live_kids_item_user_img,options,null);
                    }
                    else{
                        holder3.live_kids_item_timelayout.setVisibility(View.VISIBLE);
                        holder3.live_kids_item_datenum.setText(dateList.get(position));
                        holder3.live_kids_item_date.setText(DateUtils.getYearMonthDay(map.get("dateline").toString()));
                        holder3.live_kids_item_week.setText(DateUtils.getLocalweek(map.get("dateline").toString()));
                    }

                    holder3.live_kids_item_details.setText((CharSequence) map.get("message"));

                    if(!map.get("location").toString().equals("null"))
                    {
                        holder3.live_kids_item_position_text.setText((CharSequence) map.get("location"));
                    }
                    else {
                        holder3.live_kids_item_position_text.setText(R.string.unknownposition);
                    }
                    final int m3 = position;
                    holder3.mUpvPhotos.setPhotos(carList.get(position).subList(0, carList.get(position).size()-1));
                    //浏览监听
                    holder3.mUpvPhotos.setOnPagerPhotoItemClickListener(new onPagerPhotoItemClickListener() {

                        @Override
                        public void onItemClick(View view, int position) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(Live_Kids_User.this,
                                    ImageBrowserActivity.class);
                            intent.putExtra(ImageBrowserActivity.IMAGE_TYPE,
                                    ImageBrowserActivity.TYPE_ALBUM);
                            intent.putExtra("position", position);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("carlist", new ArrayList<String>(carList.get(m3)));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_enter, 0);
                        }
                    });

                    String imageUrl3 = UrlUtils.imageUrl_avatar(carList.get(position).get(carList.get(position).size()-1),480);

                    MyMapApplication.imageLoader.displayImage(imageUrl3, holder3.live_kids_item_img,Originaloptions3,new SimpleImageLoadingListener(){
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            // TODO Auto-generated method stub
                            super.onLoadingStarted(imageUri, view);
                            holder3.progressBarView.setProgressNotInUiThread(0);
                            holder3.progressBarView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view,
                                                    FailReason failReason) {
                            // TODO Auto-generated method stub
                            super.onLoadingFailed(imageUri, view, failReason);
                            holder3.progressBarView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view,
                                                      Bitmap loadedImage) {
                            // TODO Auto-generated method stub
                            super.onLoadingComplete(imageUri, view, loadedImage);
                            holder3.progressBarView.setVisibility(View.GONE);
                            holder3.progressBarView.setProgressNotInUiThread(100);
                        }

                    },new ImageLoadingProgressListener() {

                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current,
                                                     int total) {
                            // TODO Auto-generated method stub
                            if((System.currentTimeMillis() - time)>1000)
                            {
                                time = System.currentTimeMillis();
                                holder3.progressBarView.setProgressNotInUiThread(Math.round(100.0f * current / total));
                            }

                        }
                    });


                    holder3.live_kids_item_img_cover.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(Live_Kids_User.this,
                                    ImageBrowserActivity.class);
                            intent.putExtra(ImageBrowserActivity.IMAGE_TYPE,
                                    ImageBrowserActivity.TYPE_ALBUM);
                            intent.putExtra("position", carList.get(m3).size()-1);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("carlist", new ArrayList<String>(carList.get(m3)));
                            intent.putExtras(bundle);
                            startActivity(intent);
                            overridePendingTransition(R.anim.zoom_enter, 0);
                        }
                    });

                    holder3.live_item_like_layout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            SendLikeMessage(position, map);
                        }
                    });
                    break;
			}
            return convertView;
		}
        
		
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class PhotoViewHolder{
		ScaleImageView live_kids_item_img;
		ScaleImageView live_kids_item_img_cover;
		TextView live_kids_item_username;
		TextView live_kids_item_details;
		TextView live_kids_item_time_text;
		TextView live_kids_item_position_text;
		ImageView live_kids_item_user_img;
		ProgressBarView progressBarView;
        LinearLayout live_kids_item_timelayout;
        LinearLayout live_kids_item_userlayout;
        TextView live_kids_item_datenum;
        TextView live_kids_item_date;
        TextView live_kids_item_week;

        RelativeLayout live_item_like_layout;
        TextView live_kids_comment_num;
        TextView live_kids_like_people_num;
	}

    class NoViewHolder{
        TextView live_kids_item_username;
        TextView live_kids_item_details;
        TextView live_kids_item_time_text;
        TextView live_kids_item_position_text;
        ImageView live_kids_item_user_img;
        LinearLayout live_kids_item_timelayout;
        LinearLayout live_kids_item_userlayout;
        TextView live_kids_item_datenum;
        TextView live_kids_item_date;
        TextView live_kids_item_week;

        RelativeLayout live_item_like_layout;
        TextView live_kids_comment_num;
        TextView live_kids_like_people_num;
    }
    class SmallViewHolder{
        ScaleImageView live_kids_item_img;
        ScaleImageView live_kids_item_img_cover;
        TextView live_kids_item_username;
        TextView live_kids_item_details;
        TextView live_kids_item_time_text;
        TextView live_kids_item_position_text;
        ImageView live_kids_item_user_img;
        UserPhotosView mUpvPhotos;// 照片
        ProgressBarView progressBarView;
        LinearLayout live_kids_item_timelayout;
        LinearLayout live_kids_item_userlayout;
        TextView live_kids_item_datenum;
        TextView live_kids_item_date;
        TextView live_kids_item_week;

        RelativeLayout live_item_like_layout;
        TextView live_kids_comment_num;
        TextView live_kids_like_people_num;
    }

    private void SendLikeMessage(int position,  HashMap<String, Object> map){
        TokenShared = getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("token", "");
        like_num = Integer.parseInt(map.get("like_count").toString());
        this.position = position;
        if(tokenString.equals(""))
        {
            ToastClass.SetToast(Live_Kids_User.this, "登录之后才能够点赞哟");
        }
        else {
            DialogMethod.startProgressDialog(Live_Kids_User.this,"点赞中...");
            RequestParams param;
            param = new RequestParams();
            param.put("token", tokenString);
            param.put("pid", map.get("pid").toString());
            liveNetworkModel = new LiveNetworkModel(this, this);
            liveNetworkModel.likelivepost(param);
        }
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
            case 303:
                Live_Kids_User.this.finish();
                break;
            case 304:
                setResult(304);
                Live_Kids_User.this.finish();
                break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 在地图上添加marker
	 */
	private void addMarkersToMap() {
		int a = LocationList.size();

		polylineOptions = new PolylineOptions();
		for(int i = 0; i < a; i++)
		{
			markerOption = new MarkerOptions();
			markerOption.position(new LatLng(Double.parseDouble(LocationList.get(i).get("latitude").toString()), Double.parseDouble(LocationList.get(i).get("longitude").toString())));
			markerOption.title(LocationList.get(i).get("location").toString());
			polylineOptions.add(new LatLng(Double.parseDouble(LocationList.get(i).get("latitude").toString()), Double.parseDouble(LocationList.get(i).get("longitude").toString())));
			aMap.addMarker(markerOption);
		}
		if(a > 0)
		{
			LatLngBounds bounds = new LatLngBounds.Builder()
			.include(new LatLng(Double.parseDouble(LocationList.get(0).get("latitude").toString()),
                                Double.parseDouble(LocationList.get(0).get("longitude").toString())))
			.build();
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
            // 绘制线条
            polyline = aMap.addPolyline(polylineOptions
                                        .width(3).color(Color.rgb(4, 234, 188)));
		}
		aMap.invalidate();//刷新地图
	}
	/**
	 * marker点击时跳动一下
	 */
	public void jumpPoint(final Marker marker) {
		final Handler handler = new Handler();
		final long start = SystemClock.uptimeMillis();
		final LatLng latLng = marker.getPosition();
		Projection proj = aMap.getProjection();
		Point startPoint = proj.toScreenLocation(latLng);
		startPoint.offset(0, -100);
		final LatLng startLatLng = proj.fromScreenLocation(startPoint);
		final long duration = 1500;
        
		final Interpolator interpolator = new BounceInterpolator();
		handler.post(new Runnable() {
			@Override
			public void run() {
				long elapsed = SystemClock.uptimeMillis() - start;
				float t = interpolator.getInterpolation((float) elapsed
                                                        / duration);
				double lng = t * latLng.longitude + (1 - t)
                * startLatLng.longitude;
				double lat = t * latLng.latitude + (1 - t)
                * startLatLng.latitude;
				marker.setPosition(new LatLng(lat, lng));
				aMap.invalidate();// 刷新地图
				if (t < 1.0) {
					handler.postDelayed(this, 16);
				}
			}
		});
        
	}
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();
        //		SetGaoDeMap();
		mapView.onResume();
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
		CacheModel.cacheLiveData("kidslinkedlist"+tid,list,carList,WidthHeightList,LocationList,Live_Kids_User.this);
//		int height = scrollView.getScrollY();
//		CacheModel.cacheScrollviewHeight("scrollviewheight"+tid,height,Live_Kids_User.this);
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
    
	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
        //		imageLoader.destroy();
		mapView.onDestroy();
	}
    
	@Override
	public View getInfoContents(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public View getInfoWindow(Marker arg0) {
		// TODO Auto-generated method stub
		return null;
	}
    
	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		if (aMap != null) {
			jumpPoint(marker);
		}
		return false;
	}
}
