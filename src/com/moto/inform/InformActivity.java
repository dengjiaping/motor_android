package com.moto.inform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import info.hoang8f.android.segmented.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.listview.MyListView;
import com.moto.listview.MyListView.OnRefreshListener;
import com.moto.main.Moto_MainActivity;
import com.moto.main.R;
import com.moto.myactivity.tabActivity;
import com.moto.toast.ToastClass;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 好友列表的Activity
 *
 * @author way
 *
 */
public class InformActivity extends tabActivity implements OnClickListener {

	private static final int PAGE1 = 0;// 页面1
	private static final int PAGE2 = 1;// 页面2
	private static final int PAGE3 = 2;// 页面3
	private MyListView responseListView;// 好友列表自定义listView
	private RelativeLayout inform_bar1, inform_bar2, inform_bar3;
	private MyListView privateListView;// 群组listView
	private TextView inform_new;    //新建私信
	private ViewPager mPager;
	private MyListView touchmeListView;
	private List<View> mListViews;// Tab页面
	private TextView inform_response;// 回复
	private TextView inform_touchme;// 提到我的
	private TextView inform_private;// 私信
    
	private int currentIndex = PAGE2; // 默认选中第2个，可以动态的改变此参数值
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	
	//获取数据
	private boolean isrefresh = false;
	private SharedPreferences TokenShared;
	private String tokenString;
	private Handler handler;
	private ResponseAdapter responseAdapter;
	private PrivateAdapter privateAdapter;
	private TouchmeAdapter touchmeAdapter;
	private ProgressBar inform_loading_progressBar;
	private String uriString = "http://damp-reef-9073.herokuapp.com/api/inform/readconversation";
	private LinkedList<HashMap<String, String>> responseList = new LinkedList<HashMap<String,String>>();
	private LinkedList<HashMap<String, String>> privateList = new LinkedList<HashMap<String,String>>();
	private LinkedList<HashMap<String, String>> touchmeList = new LinkedList<HashMap<String,String>>();
	private HashMap<String, String> map;


    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        //		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.inform);
		
		
		initUI();// 初始化界面
		
		
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        isrefresh = false;
                        inform_loading_progressBar.setVisibility(View.GONE);
                        responseAdapter.notifyDataSetChanged();
                        touchmeAdapter.notifyDataSetChanged();
                        privateAdapter.notifyDataSetChanged();
                        responseListView.onRefreshComplete();
                        privateListView.onRefreshComplete();
                        touchmeListView.onRefreshComplete();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        isrefresh = false;
                        inform_loading_progressBar.setVisibility(View.GONE);
                        responseAdapter.notifyDataSetChanged();
                        touchmeAdapter.notifyDataSetChanged();
                        privateAdapter.notifyDataSetChanged();
                        responseListView.onRefreshComplete();
                        privateListView.onRefreshComplete();
                        touchmeListView.onRefreshComplete();
                        break;
                    case Constant.MSG_NULL:
                        isrefresh = false;
                        inform_loading_progressBar.setVisibility(View.GONE);
                        responseListView.onRefreshComplete();
                        privateListView.onRefreshComplete();
                        touchmeListView.onRefreshComplete();
                        break;
                    case Constant.MSG_FALTH:
                        Moto_MainActivity.radioGroup.check(R.id.main_tab_user);
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
		
		responseListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
						isrefresh = true;
						GetAsyData();
					}
                    
				}.execute();
			}
		});
		touchmeListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
                        isrefresh = true;
                        GetAsyData();
					}
                    
				}.execute();
			}
		});
		privateListView.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							Thread.sleep(1000);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						return null;
					}
                    
					@Override
					protected void onPostExecute(Void result) {
						isrefresh = true;
						GetAsyData();
					}
                    
				}.execute();
			}
		});
		
		TokenShared = getSharedPreferences("usermessage", 0);
		tokenString = TokenShared.getString("token", "");
		if(tokenString.equals(""))
		{
			ToastClass.SetToast(InformActivity.this, "需要先登录才能够获取数据 ");
			handler.obtainMessage(Constant.MSG_FALTH)
			.sendToTarget();
		}
		else {
			GetAsyData();
		}
	}
	private void initUI() {
		inform_loading_progressBar = (ProgressBar)findViewById(R.id.inform_loading_progressBar);
		inform_bar1 = (RelativeLayout)findViewById(R.id.inform_bar1);
		inform_bar2 = (RelativeLayout)findViewById(R.id.inform_bar2);
		inform_bar3 = (RelativeLayout)findViewById(R.id.inform_bar3);
        
		inform_new = (TextView)findViewById(R.id.inform_new);
		inform_new.setOnClickListener(this);
		
		inform_response = (TextView) findViewById(R.id.inform_response);
		inform_response.setOnClickListener(this);
		inform_touchme = (TextView) findViewById(R.id.inform_touchme);
		inform_touchme.setOnClickListener(this);
		inform_private = (TextView) findViewById(R.id.inform_private);
		inform_private.setOnClickListener(this);
		mPager = (ViewPager) findViewById(R.id.viewPager);
		mListViews = new ArrayList<View>();
		LayoutInflater inflater = LayoutInflater.from(this);
		View lay1 = inflater.inflate(R.layout.inform_response, null);
		View lay2 = inflater.inflate(R.layout.inform_touchme, null);
		View lay3 = inflater.inflate(R.layout.inform_private, null);
		mListViews.add(lay1);
		mListViews.add(lay2);
		mListViews.add(lay3);
		mPager.setAdapter(new MyPagerAdapter(mListViews));
		mPager.setCurrentItem(PAGE1);
		setImageBackground(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        
		responseListView = (MyListView) lay1.findViewById(R.id.inform_response_listview);
		responseAdapter = new ResponseAdapter(this, responseList);
		responseListView.setAdapter(responseAdapter);// 先设置空对象，要么从数据库中读出
        
		touchmeListView = (MyListView) lay2.findViewById(R.id.inform_touchme_listview);
		touchmeAdapter = new TouchmeAdapter(this, touchmeList);
		touchmeListView.setAdapter(touchmeAdapter);
        
		// 下面是群组界面处理
		privateListView = (MyListView) lay3.findViewById(R.id.inform_private_listview);
		privateAdapter = new PrivateAdapter(this, privateList);
		privateListView.setAdapter(privateAdapter);
	}
	
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", tokenString);
		RequstClient.post(uriString, param, new LoadCacheResponseLoginouthandler(
                                                                                 InformActivity.this,
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
						responseList.clear();
						privateList.clear();
						touchmeList.clear();
					}
					JSONObject jsonObject1 = new JSONObject(data);
					if (jsonObject1.getString("is").equals("1")) {
						String response_details = jsonObject1
                        .getString("reply_list");
						JSONArray responsearray = new JSONArray(response_details);
						int a = responsearray.length();
						for (int i = 0; i < a; i++) {
							JSONObject jsonObject2 = (JSONObject) responsearray.get(i);
							responseList.add(GetReaponseMap(jsonObject2));
						}
						
						String private_details = jsonObject1
                        .getString("conversation_list");
						JSONArray privatearray = new JSONArray(private_details);
						int b = privatearray.length();
						for (int i = 0; i < b; i++) {
							JSONObject jsonObject2 = (JSONObject) privatearray.get(i);
							privateList.add(GetPrivateMap(jsonObject2));
						}
						
						String touchme_details = jsonObject1
                        .getString("at_list");
						JSONArray touchmearray = new JSONArray(touchme_details);
						int c = touchmearray.length();
						for (int i = 0; i < c; i++) {
							JSONObject jsonObject2 = (JSONObject) touchmearray.get(i);
							touchmeList.add(GetTouchmeMap(jsonObject2));
						}
						
						
						
						if (isrefresh)
							handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                            .sendToTarget();
						else
							handler.obtainMessage(Constant.MSG_SUCCESS)
                            .sendToTarget();
                        
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
				ToastClass.SetToast(InformActivity.this, message);
			}
            
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
			
		}));
	}
    
	private HashMap<String, String> GetReaponseMap(
                                                   JSONObject jsonObject) {
		// TODO Auto-generated method stub
		map = new HashMap<String, String>();
		try {
			String author = jsonObject.getString("author");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String tid = jsonObject.getString("tid");
			String avatar = jsonObject.getString("avatar");
			String fid = jsonObject.getString("fid");
			String pid = jsonObject.getString("pid");
			String cid = jsonObject.getString("cid");
			map.put("author", author);
			map.put("fid", fid);
			map.put("message", message);
			map.put("dateline", dateline);
			map.put("pid", pid);
			map.put("tid", tid);
			map.put("cid", cid);
			map.put("avatar", avatar);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	private HashMap<String, String> GetPrivateMap(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		map = new HashMap<String, String>();
		try {
			String username = jsonObject.getString("username");
			String lastmessage = jsonObject.getString("lastmessage");
			String ucid = jsonObject.getString("ucid");
			String updated_at = jsonObject.getString("updated_at");
			String avatar = jsonObject.getString("avatar");
			map.put("username", username);
			map.put("lastmessage", lastmessage);
			map.put("updated_at", updated_at);
			map.put("ucid", ucid);
			map.put("avatar", avatar);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	private HashMap<String, String> GetTouchmeMap(JSONObject jsonObject) {
		// TODO Auto-generated method stub
		map = new HashMap<String, String>();
		try {
			String author = jsonObject.getString("author");
			String message = jsonObject.getString("message");
			String fid = jsonObject.getString("fid");
			String tid = jsonObject.getString("tid");
			String pid = jsonObject.getString("pid");
			String cid = jsonObject.getString("cid");
			String dateline = jsonObject.getString("dateline");
			String avatar = jsonObject.getString("avatar");
			map.put("author", author);
			map.put("message", message);
			map.put("tid", tid);
			map.put("fid", fid);
			map.put("pid", pid);
			map.put("cid", cid);
			map.put("dateline", dateline);
			map.put("avatar", avatar);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
            case R.id.inform_response:
                mPager.setCurrentItem(PAGE1);// 点击页面1
                setImageBackground(0);
                break;
            case R.id.inform_touchme:
                mPager.setCurrentItem(PAGE2);// 点击页面1
                setImageBackground(1);
                break;
            case R.id.inform_private:
                mPager.setCurrentItem(PAGE3);// 点击页面1
                setImageBackground(2);
                break;
            case R.id.inform_new:
                intent = new Intent();
                intent.setClass(InformActivity.this, Inform_Friends.class);
                startActivity(intent);
                break;
            default:
                break;
		}
	}
	
	/**
	 * 标题栏状态更改
	 * @param position
	 */
	private void setImageBackground(int position) {
		switch (position) {
            case 0:
                inform_bar1.setBackgroundResource(R.color.radiobg_press);
                inform_bar2.setBackgroundResource(R.drawable.inform_title_center_style);
                inform_bar3.setBackgroundResource(R.drawable.inform_title_right_style);
                inform_response.setTextColor(Color.rgb(0,157,184));
                inform_touchme.setTextColor(Color.WHITE);
                inform_private.setTextColor(Color.WHITE);
                break;
            case 1:
                inform_bar1.setBackgroundResource(R.drawable.inform_title_left_style);
                inform_bar2.setBackgroundResource(R.color.radiobg_press);
                inform_bar3.setBackgroundResource(R.drawable.inform_title_right_style);
                inform_response.setTextColor(Color.WHITE);
                inform_touchme.setTextColor(Color.rgb(0,157,184));
                inform_private.setTextColor(Color.WHITE);
                break;
            case 2:
                inform_bar1.setBackgroundResource(R.drawable.inform_title_left_style);
                inform_bar2.setBackgroundResource(R.drawable.inform_title_center_style);
                inform_bar3.setBackgroundResource(R.color.radiobg_press);
                inform_response.setTextColor(Color.WHITE);
                inform_touchme.setTextColor(Color.WHITE);
                inform_private.setTextColor(Color.rgb(0,157,184));
                break;
            default:
                break;
                
		}
        
	}
	
	// ViewPager页面切换监听
    class MyOnPageChangeListener implements OnPageChangeListener {
        
        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        
        public void onPageSelected(int arg0) {
            // TODO Auto-generated method stub
            Animation animation = null;
            switch (arg0) {
				case PAGE1:// 切换到页卡1
					if (currentIndex == PAGE2) {// 如果之前显示的是页卡2
						animation = new TranslateAnimation(0, -one, 0, 0);
					} else if (currentIndex == PAGE3) {// 如果之前显示的是页卡3
						animation = new TranslateAnimation(one, -one, 0, 0);
					}
					setImageBackground(0);
					break;
				case PAGE2:// 切换到页卡2
					if (currentIndex == PAGE1) {// 如果之前显示的是页卡1
						animation = new TranslateAnimation(-one, 0, 0, 0);
					} else if (currentIndex == PAGE3) {// 如果之前显示的是页卡3
						animation = new TranslateAnimation(one, 0, 0, 0);
					}
					setImageBackground(1);
					break;
				case PAGE3:// 切换到页卡3
					if (currentIndex == PAGE1) {// 如果之前显示的是页卡1
						animation = new TranslateAnimation(-one, one, 0, 0);
					} else if (currentIndex == PAGE2) {// 如果之前显示的是页卡2
						animation = new TranslateAnimation(0, one, 0, 0);
					}
					setImageBackground(2);
					break;
				default:
					break;
            }
        }
        
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
            
        }
        
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
            
        }
    }
}
