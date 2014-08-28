package com.moto.square;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.moto.listview.CustomScrollView;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.listview.ProgressBarView;
import com.moto.live.User_OwnPage;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.SquareNetworkModel;
import com.moto.mytextview.MarqueeText;
import com.moto.mytextview.ShimmerTextView;
import com.moto.toast.ToastClass;
import com.moto.utils.StringUtils;
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

public class Theme_Post extends Moto_RootActivity{
    
	private NoScrollListview listview;
	private ArrayList<HashMap<String, Object>> GroupList = new ArrayList<HashMap<String,Object>>();
	private ArrayList<ArrayList<HashMap<String, Object>>> ChildList = new ArrayList<ArrayList<HashMap<String,Object>>>();
	private ArrayList<HashMap<String, Object>> list;
    private ArrayList<ArrayList<String>> carList = new ArrayList<ArrayList<String>>();
    private DisplayImageOptions Originaloptions;
	private HashMap<String, Object> map;
	private int count = 0;
	private int num_post = 1;
	private String tid;
    private String fid;
	private MyAdapter adapter;
	private boolean isrefresh = false;
	private MarqueeText post_theme;
    private String subject;
	private DisplayImageOptions options;
	private Handler handler;
    private ShimmerTextView waitText;
    private String author = ""; //楼主

	private TextView post_share;
    private TextView post_publish;
    private TextView post_collect;
    private Intent intent;
    RequestParams param;

	private CustomScrollView scrollView;
	private ImageView leftpage;
	private String tokenString;
	ArrayList<String> pidList = new ArrayList<String>();

	private String readDetailUri = path+"api/square/readdetailtheme";
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
                    case Constant.MSG_FALTH:

                        String str = (String) msg.obj;
                        ToastClass.SetToast(Theme_Post.this, str);
                        DialogMethod.stopProgressDialog();
                        break;
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
                    case Constant.MSG_WAITSUCCESS:
                        String string = (String) msg.obj;
                        ToastClass.SetToast(Theme_Post.this, string);
                        DialogMethod.stopProgressDialog();
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
		
		post_publish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				tokenString = ToastClass.GetTokenString(Theme_Post.this);
				if(tokenString.equals(""))
				{
					ToastClass.SetToast(Theme_Post.this, "需要先登录才能够发送");

				}

				else
				{
                    Bundle bundle = new Bundle();
                    bundle.putString("token",tokenString);
                    bundle.putString("tid",tid);
                    bundle.putString("subject",subject);
                    pushToNextActivity(bundle,Theme_Post_publish.class,305);
				}
			}
		});

        post_share.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(intent, "分享"));
            }
        });

        post_collect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogMethod.startProgressDialog(Theme_Post.this,"收藏中...");
                SendKeepMessage();
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
                Bundle bundle = new Bundle();
                bundle.putString("pid",pidList.get(i));
                bundle.putString("author",GroupList.get(i).get("author").toString());
                bundle.putString("message",GroupList.get(i).get("message").toString());
                bundle.putString("dateline",GroupList.get(i).get("dateline").toString());
                bundle.putString("avatar",GroupList.get(i).get("avatar").toString());
                bundle.putString("authorname",author);
                if(carList.get(i).size() > 0)
                bundle.putString("photoname",carList.get(i).get(0));
                else
                bundle.putString("photoname","");
                pushToNextActivity(bundle,Theme_Post_Kids.class,305);

            }
        });
	}
    
	private void init() {
		// TODO Auto-generated method stub
		options = ImageMethod.GetOptions();
        Originaloptions = ImageMethod.GetOriginalOptions();
		intent = getIntent();
		tid = intent.getStringExtra("tid");
        subject = intent.getStringExtra("subject");
        fid = intent.getStringExtra("fid");
        waitText = (ShimmerTextView)findViewById(R.id.post_waittext);

        post_theme = (MarqueeText)findViewById(R.id.post_title);
        post_theme.setText(subject);
        post_theme.startScroll();
		listview = (NoScrollListview)findViewById(R.id.post_main_list);

        post_collect = (TextView)findViewById(R.id.post_collect);
		post_share = (TextView)findViewById(R.id.post_share);
		post_publish = (TextView)findViewById(R.id.post_publish);

		scrollView = (CustomScrollView)findViewById(R.id.post_scrollview);
		leftpage = (ImageView)findViewById(R.id.return_post);

    }

    //点击收藏执行方法
    private void SendKeepMessage(){
        tokenString = ToastClass.GetTokenString(Theme_Post.this);
        if(tokenString.equals(""))
        {
            ToastClass.SetToast(Theme_Post.this, "登录之后才能够收藏哟");
        }
        else {
            RequestParams param;
            param = new RequestParams();
            param.put("token", tokenString);
            param.put("tid", tid);
            param.put("fid",fid);
            SquareNetworkModel squareNetworkModel= new SquareNetworkModel(this, this);
            squareNetworkModel.squareKeeppost(param);
        }
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
                        carList.clear();
                        pidList.clear();

                    }
                    JSONObject jsonObject = new JSONObject(data);
                    if (jsonObject.getString("is").equals("1")) {

                        String data_details = jsonObject
                        .getString("detail_theme");
                        JSONArray array_detail = new JSONArray(data_details);
                        for (int i = 0; i < array_detail.length(); i++) {
                            JSONObject jsonObject2 = (JSONObject) array_detail.get(i);
                            GroupList.add(GetMap(jsonObject2));
                            author = GroupList.get(0).get("author").toString();
                        }
                        String post_post_list = jsonObject.getString("post_in_post_list");
                        JSONArray array_post = new JSONArray(post_post_list);
                        for(int i = 0; i < array_post.length(); i++)
                        {
                            list = new ArrayList<HashMap<String,Object>>();
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
            map.put("pid", pid);
            map.put("avatar",avatar);
            pidList.add(pid);
            carList.add(StringUtils.hashToArray(photoname));
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
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // TODO Auto-generated method stub
//        menu.add(0, 0, Menu.NONE,"QQ").setIcon(R.drawable.qqimage);
//        menu.add(0, 1, Menu.NONE,"微博").setIcon(R.drawable.sinaimage);
//        menu.add(0, 2, Menu.NONE,"微信").setIcon(R.drawable.wechat_image);
//        return super.onCreateOptionsMenu(menu);
//    }
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
        EmojiconTextView post_item_user_name;
        EmojiconTextView post_item_details;
		TextView post_item_num;
		ImageView post_item_user_img;
        ScaleImageView post_item_detail_img;
        ProgressBarView post_item_progress_View;
		TextView original_item_poster_time;
		LinearLayout square_discuss_kids_post_item_groups;
        RelativeLayout post_user_layout;
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolderKids{
        EmojiconTextView post_item_kidlist_details;
        EmojiconTextView post_item_kidlist_user_name;
        TextView post_item_kidlist_time;
        RelativeLayout post_listitem_user_layout;
	}
    //	内部类实现BaseAdapter  ，自定义适配器
	class MyAdapter extends BaseAdapter{
        
		private Context context;
		private ArrayList<HashMap<String, Object>> GroupList;
		private ArrayList<ArrayList<HashMap<String, Object>>> ChildList;
		private HashMap<String, Object> map;
		private ArrayList<HashMap<String, Object>> list;
        long time = 0;
		public MyAdapter(Activity activity, Context context, ArrayList<HashMap<String, Object>> GroupList, ArrayList<ArrayList<HashMap<String, Object>>> ChildList)
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
            final ViewHolder holder;
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(context).inflate(R.layout.square_discuss_kids_post_item, null);
			holder = new ViewHolder();
			holder.original_item_poster_time = (TextView)convertView.findViewById(R.id.original_item_poster_time);
			holder.post_item_user_name = (EmojiconTextView)convertView.findViewById(R.id.post_item_user_name);
			holder.post_item_details = (EmojiconTextView)convertView.findViewById(R.id.post_item_details);
			holder.post_item_num = (TextView)convertView.findViewById(R.id.post_item_num);
            holder.post_user_layout = (RelativeLayout)convertView.findViewById(R.id.post_user_layout);

			holder.post_item_user_img = (ImageView)convertView.findViewById(R.id.post_item_user_img);
			holder.square_discuss_kids_post_item_groups = (LinearLayout)convertView.findViewById(R.id.square_discuss_kids_post_item_groups);
            holder.post_item_progress_View = (ProgressBarView)convertView.findViewById(R.id.post_item_progress_View);
            holder.post_item_detail_img = (ScaleImageView)convertView.findViewById(R.id.post_item_detail_img);

			
			map = GroupList.get(position);	
			holder.post_item_user_name.setText((CharSequence) map.get("author"));
			holder.post_item_details.setText((CharSequence) map.get("message"));
			holder.post_item_num.setText((CharSequence) map.get("num"));
			holder.original_item_poster_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
            MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(map.get("avatar").toString()),  holder.post_item_user_img,options,null);

            //判断楼主
            if(author.equals(map.get("author")))
            {
                holder.post_user_layout.setVisibility(View.VISIBLE);
            }
            holder.post_item_user_img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ToastClass.IsHaveToken(Theme_Post.this))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("author",GroupList.get(position).get("author").toString());
                        bundle.putString("token",ToastClass.GetTokenString(Theme_Post.this));
                        pushToNextActivity(bundle,User_OwnPage.class,304);
                    }
                    else{
                        ToastClass.SetToast(Theme_Post.this,"请先登录之后再查看");
                    }
                }
            });
            if(carList.get(position).size() > 0)
            {
                String imageUrl = UrlUtils.imageUrl(carList.get(position).get(0));

                holder.post_item_detail_img.setVisibility(View.VISIBLE);
                MotorApplication.imageLoader.displayImage(imageUrl, holder.post_item_detail_img, Originaloptions,new SimpleImageLoadingListener(){
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        // TODO Auto-generated method stub
                        super.onLoadingStarted(imageUri, view);
                        holder.post_item_progress_View.setProgressNotInUiThread(0);
                        holder.post_item_progress_View.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // TODO Auto-generated method stub
                        super.onLoadingFailed(imageUri, view, failReason);
                        holder.post_item_progress_View.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        holder.post_item_progress_View.setVisibility(View.GONE);
                        holder.post_item_progress_View.setProgressNotInUiThread(100);
                    }

                },new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current,
                                                 int total) {
                        // TODO Auto-generated method stub
                        if((System.currentTimeMillis() - time)>1000)
                        {
                            time = System.currentTimeMillis();
                            holder.post_item_progress_View.setProgressNotInUiThread(Math.round(100.0f * current / total));
                        }

                    }
                });
                holder.post_item_detail_img.setImageHeight(80);
                holder.post_item_detail_img.setImageWidth(100);
            }
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
					kidsholder.post_item_kidlist_details = (EmojiconTextView)layout.findViewById(R.id.post_item_kidlist_details);
                    kidsholder.post_listitem_user_layout = (RelativeLayout)layout.findViewById(R.id.post_listitem_user_layout);
					kidsholder.post_item_kidlist_user_name = (EmojiconTextView)layout.findViewById(R.id.post_item_kidlist_user_name);
                    kidsholder.post_item_kidlist_time = (TextView)layout.findViewById(R.id.post_item_kidlist_time);
					layout.setTag(kidsholder);
					kidsholder = (ViewHolderKids) layout.getTag();
                    
					kidsholder.post_item_kidlist_details.setText((CharSequence) list.get(i).get("message"));
					kidsholder.post_item_kidlist_user_name.setText((CharSequence) list.get(i).get("author"));
                    kidsholder.post_item_kidlist_time.setText(com.moto.utils.DateUtils.timestampToDeatil(list.get(i).get("dateline")+""));
//判断楼主
                    if(author.equals(list.get(i).get("author")))
                    {
                        kidsholder.post_listitem_user_layout.setVisibility(View.VISIBLE);
                    }
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
		switch(requestCode)
		{
            case 305:
                scrollView.state = scrollView.REFRESHING;
                scrollView.changeHeaderViewByState();
                isrefresh = true;
                count = 0;
                GetAsyData();
                break;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
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
    }
    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject)
            throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataWithFail(jsonObject);
        // 获取一个Message对象，设置what为1
        Message msg = Message.obtain();
        msg.obj = "收藏失败";
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
}
