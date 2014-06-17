package com.moto.square;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.main.R;
import com.moto.myactivity.MyActivity;
import com.moto.mymap.MyMapApplication;
import com.moto.mytextview.MarqueeText;
import com.moto.mytextview.ShimmerTextView;
import com.moto.toast.ToastClass;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Theme_Post extends MyActivity{
    
	private NoScrollListview listview;
	private LinkedList<HashMap<String, Object>> GroupList = new LinkedList<HashMap<String,Object>>();
	private LinkedList<LinkedList<HashMap<String, Object>>> ChildList = new LinkedList<LinkedList<HashMap<String,Object>>>();
	private LinkedList<HashMap<String, Object>> list;
	private HashMap<String, Object> map;
	private int count = 0;
	private int num_post = 1;
	private String tid;
	private ImageView own_photos;
	private String photoString = null;
	private String response_theme_message;
	private MyAdapter adapter;
	private boolean isrefresh = false;
	private MarqueeText post_theme;
	private DisplayImageOptions options;
	private Handler handler;
    private ShimmerTextView waitText;
	
	private EditText response_theme;
	private ImageView post_send;
	private CustomScrollView scrollView;
	private ImageView leftpage;
	private SharedPreferences TokenShared;
	private String tokenString;
	ArrayList<String> pidList = new ArrayList<String>();
	private String CreateCommentUri = "http://damp-reef-9073.herokuapp.com/api/square/createpostfortheme";
	private String readDetailUri = "http://damp-reef-9073.herokuapp.com/api/square/readdetailtheme";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_discuss_kids_post);
		init();
		adapter = new MyAdapter(this, this, GroupList, ChildList);
		listview.setAdapter(adapter);
		GetAsyData();
		
		handler = new Handler(){
            
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
                        //获取成功
                    case Constant.MSG_SUCCESS:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        break;
                        
                    case Constant.MSG_SUCCESSAGAIN:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_NULL:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_TESTSTART:
                        DialogMethod.startProgressDialog(Theme_Post.this,"正在发送");
                        break;
                    case Constant.MSG_TESTFALTH:
                        ToastClass.SetToast(Theme_Post.this, msg.obj.toString());
                        DialogMethod.stopProgressDialog();
                        break;
                    case Constant.MSG_HAVENOTHING:

                        waitText.setText("暂时还没有任何数据哟");
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
		
		post_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				response_theme_message = response_theme.getText().toString();
				TokenShared = getSharedPreferences("usermessage", 0);
				tokenString = TokenShared.getString("token", "");
				if(tokenString.equals(""))
				{
					ToastClass.SetToast(Theme_Post.this, "需要先登录才能够发送");
					setResult(304);
					Theme_Post.this.finish();
				}
				else if(response_theme_message.replaceAll(" ", "").equals(""))
				{
					DialogMethod.dialogShow(Theme_Post.this,"请输入回复内容!");
				}
				else
				{
					SetAsyResponse();
				}
			}
		});
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
		
		leftpage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Theme_Post.this.finish();
			}
		});

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                intent = new Intent();
                intent.putExtra("pid", pidList.get(i));
                intent.setClass(Theme_Post.this, Theme_Post_Kids.class);
                startActivityForResult(intent, 304);
            }
        });
	}
    
	private void init() {
		// TODO Auto-generated method stub
		options = ImageMethod.GetOptions();
		intent = getIntent();
		tid = intent.getStringExtra("tid");

        waitText = (ShimmerTextView)findViewById(R.id.post_waittext);

        post_theme = (MarqueeText)findViewById(R.id.post_title);
        post_theme.setText(intent.getStringExtra("subject"));
        post_theme.startScroll();
		listview = (NoScrollListview)findViewById(R.id.post_main_list);
		response_theme = (EditText)findViewById(R.id.post_response_theme_edit);
		post_send = (ImageView)findViewById(R.id.post_send);
		own_photos = (ImageView)findViewById(R.id.post_camera);
		scrollView = (CustomScrollView)findViewById(R.id.post_scrollview);
		leftpage = (ImageView)findViewById(R.id.return_post);

    }
	private void SetAsyResponse() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("tid", tid);
		param.put("token", tokenString);
		param.put("subject", "");
		param.put("message", response_theme_message);
		param.put("photos", photoString);
		
		RequstClient.post(CreateCommentUri, param, new LoadCacheResponseLoginouthandler(
                                                                                        Theme_Post.this,
                                                                                        new LoadDatahandler(){
            
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				handler.obtainMessage(Constant.MSG_TESTSTART)
				.sendToTarget();
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
						Toast.makeText(Theme_Post.this, "发送成功", Toast.LENGTH_SHORT).show();
						Theme_Post.this.finish();
					}
					DialogMethod.stopProgressDialog();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
            
			@Override
			public void onFailure(String error, String message) {
				// TODO Auto-generated method stub
				super.onFailure(error, message);
				// 获取一个Message对象，设置what为1
				Message msg = Message.obtain();
				msg.obj = message;
				msg.what = Constant.MSG_TESTFALTH;
				// 发送这个消息到消息队列中
				handler.sendMessage(msg);
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
        
        RequstClient.post(readDetailUri, param, new LoadCacheResponseLoginouthandler(
                                                                                     Theme_Post.this,
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
                        GroupList.clear();
                        num_post = 1;
                        ChildList.clear();
                    }
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.getString("is").equals("1")) {

                        String data_details = jsonObject
                        .getString("detail_theme");
                        JSONArray array_detail = new JSONArray(data_details);
                        for (int i = 0; i < array_detail.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) array_detail.get(i);
                            GroupList.add(GetMap(jsonObject2));
                        }
                        String post_post_list = jsonObject.getString("post_in_post_list");
                        JSONArray array_post = new JSONArray(post_post_list);
                        for(int i = 0; i < array_post.length(); i++)
                        {
                            list = new LinkedList<HashMap<String,Object>>();
                            JSONArray post_kids = new JSONArray(array_post.get(i).toString());
                            
                            for (int j = 0; j < post_kids.length(); j++) {
                                JSONObject jsonObject2 = (JSONObject) post_kids.get(j);
                                list.add(GetPostMap(jsonObject2));
                            }
                            ChildList.add(list);
                        }
                        
                        if(GroupList.size() > 0)
                        {
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
                        }
                        else
                        {
                            handler.obtainMessage(Constant.MSG_HAVENOTHING)
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
            String photoname = jsonObject.getString("photoname");
            String pid = jsonObject.getString("pid");
            String avatar = jsonObject.getString("avatar");
            map.put("author", author);
            map.put("message", message);
            map.put("dateline", dateline);
            map.put("photoname", photoname);
            map.put("pid", pid);
            map.put("avatar",avatar);
            pidList.add(pid);
            map.put("num", num_post+"");
            num_post++;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return map;
        
    }
    private HashMap<String, Object> GetPostMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {
            String author = jsonObject.getString("author");
            String message = jsonObject.getString("message");
            String dateline = jsonObject.getString("dateline");
//            String avatar = jsonObject.getString("avatar");
            map.put("author", author);
            map.put("message", message);
            map.put("dateline", dateline);
//            map.put("avatar",avatar);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return map;
        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        menu.add(0, 0, Menu.NONE,"QQ").setIcon(R.drawable.qqimage);
        menu.add(0, 1, Menu.NONE,"微博").setIcon(R.drawable.sinaimage);
        menu.add(0, 2, Menu.NONE,"微信").setIcon(R.drawable.wechat_image);
        return super.onCreateOptionsMenu(menu);
    }
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		TextView post_item_user_name;
		TextView post_item_details;
		TextView post_item_num;
		LinearLayout post_item_reaponse;
		ImageView post_item_user_img;
		TextView original_item_poster_time;
		LinearLayout square_discuss_kids_post_item_groups;
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolderKids{
		TextView post_item_kidlist_details;
		TextView post_item_kidlist_user_name;
        TextView post_item_kidlist_time;
	}
    //	内部类实现BaseAdapter  ，自定义适配器
	class MyAdapter extends BaseAdapter{
        
		private Context context;
		private LinkedList<HashMap<String, Object>> GroupList;
		private LinkedList<LinkedList<HashMap<String, Object>>> ChildList;
		private HashMap<String, Object> map;
		private LinkedList<HashMap<String, Object>> list;
		private int num;
		public MyAdapter(Activity activity, Context context, LinkedList<HashMap<String, Object>> GroupList, LinkedList<LinkedList<HashMap<String, Object>>> ChildList)
		{
			this.context = context;
			this.GroupList = GroupList;
			this.ChildList = ChildList;
		}
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return GroupList.size();
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
			ViewHolder holder = null;
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(context).inflate(R.layout.square_discuss_kids_post_item, null);
			holder = new ViewHolder();
			holder.original_item_poster_time = (TextView)convertView.findViewById(R.id.original_item_poster_time);
			holder.post_item_user_name = (TextView)convertView.findViewById(R.id.post_item_user_name);
			holder.post_item_details = (TextView)convertView.findViewById(R.id.post_item_details);
			holder.post_item_num = (TextView)convertView.findViewById(R.id.post_item_num);
			holder.post_item_reaponse = (LinearLayout)convertView.findViewById(R.id.post_item_reaponse);
			holder.post_item_user_img = (ImageView)convertView.findViewById(R.id.post_item_user_img);
			holder.square_discuss_kids_post_item_groups = (LinearLayout)convertView.findViewById(R.id.square_discuss_kids_post_item_groups);
			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();
			
			map = GroupList.get(position);	
			holder.post_item_user_name.setText((CharSequence) map.get("author"));
			holder.post_item_details.setText((CharSequence) map.get("message"));
			holder.post_item_num.setText((CharSequence) map.get("num"));
			holder.original_item_poster_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
			MyMapApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder.post_item_user_img,options,null);
			num = position;
			holder.post_item_reaponse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					intent = new Intent();
					intent.putExtra("pid", pidList.get(position));
					intent.setClass(Theme_Post.this, Theme_Post_Kids.class);
					startActivityForResult(intent, 304);
				}
			});
			list = ChildList.get(position);
			int num = list.size();
			if(num > 0)
			{
				for(int i = 0; i < num; i++)
				{
					ViewHolderKids kidsholder = new ViewHolderKids();
					LayoutInflater inflater = LayoutInflater.from(context);
					LinearLayout layout = (LinearLayout) inflater.inflate(
                                                                          R.layout.square_discuss_kids_post_kidslistitem, null);
					kidsholder.post_item_kidlist_details = (TextView)layout.findViewById(R.id.post_item_kidlist_details);

					kidsholder.post_item_kidlist_user_name = (TextView)layout.findViewById(R.id.post_item_kidlist_user_name);
                    kidsholder.post_item_kidlist_time = (TextView)layout.findViewById(R.id.post_item_kidlist_time);
					layout.setTag(kidsholder);
					kidsholder = (ViewHolderKids) layout.getTag();
                    
					kidsholder.post_item_kidlist_details.setText((CharSequence) list.get(i).get("message"));
					kidsholder.post_item_kidlist_user_name.setText((CharSequence) list.get(i).get("author"));
                    kidsholder.post_item_kidlist_time.setText(com.moto.utils.DateUtils.timestampToDeatil(list.get(i).get("dateline")+""));

					holder.square_discuss_kids_post_item_groups.addView(layout);
				}
                LayoutInflater inflater = LayoutInflater.from(context);
                LinearLayout line = (LinearLayout)inflater.inflate(R.layout.theme_post_textline, null);
                holder.square_discuss_kids_post_item_groups.addView(line);
			}
			return convertView;
		}
        
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
            case 304:
                setResult(304);
                Theme_Post.this.finish();
                break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
}
