package com.moto.square;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.img.ScaleImageView;
import com.moto.listview.CustomScrollView;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.listview.ProgressBarView;
import com.moto.main.R;
import com.moto.myactivity.tabActivity;
import com.moto.mymap.MyMapApplication;
import com.moto.mytextview.ShimmerTextView;
import com.moto.square.JazzyViewPager.TransitionEffect;
import com.moto.utils.DateUtils;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SquareActivity extends tabActivity{
	private NoScrollListview listview;
	private CustomScrollView scrollView;
    private ShimmerTextView waitText;
	private TextView edit_theme;
	private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
	private LinkedList<LinkedList<String>> carList = new LinkedList<LinkedList<String>>();
	private HashMap<String, Object> map;
	private boolean isRefresh = false;
    private boolean isload = false;
	private int count = 0;
	private MyAdapter adapter;
	private String fid;
	private Handler handler;
	private TextView title;
	protected DisplayImageOptions options;
	private DisplayImageOptions Originaloptions;
	private String readUri = "http://damp-reef-9073.herokuapp.com/api/square/readforumbriefpost";

	// ============== 广告切换 ===================
    private JazzyViewPager mViewPager = null;
    /**
     * 装指引的ImageView数组
     */
    private ImageView[] mIndicators;

    private Handler mHandler;

    /**
     * 装ViewPager中ImageView的数组
     */
    private ImageView[] mImageViews;
    private List<String> mImageUrls = new ArrayList<String>();
    private LinearLayout mIndicator = null;
    private String mImageUrl = null;
    private static final int MSG_CHANGE_PHOTO = 1;
    /** 图片自动切换时间 */
    private static final int PHOTO_CHANGE_TIME = 3000;
    // ============== 广告切换 ===================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square);
		init();
		adapter = new MyAdapter(this, list);
		listview.setAdapter(adapter);
		GetAsyData();
		edit_theme.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("fid", fid);

				intent.setClass(SquareActivity.this, Publish_post.class);
				startActivityForResult(intent, 304);
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
						isRefresh = true;
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
                        isload = false;
                        isRefresh = false;
                        adapter.notifyDataSetChanged();
                        break;

                    case Constant.MSG_SUCCESSAGAIN:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        if(isRefresh)
                        {
                            isRefresh = false;
                            adapter.notifyDataSetChanged();
                            scrollView.onRefreshComplete();
                            scrollView.onLoadComplete();
                        }
                        else {
                            adapter.notifyDataSetChanged();
                            scrollView.onRefreshComplete();
                            scrollView.onLoadComplete();
                        }
                        isload = false;
                        isRefresh = false;
                        break;
                    case Constant.MSG_NULL:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isload = false;
                        isRefresh = false;
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_HAVENOTHING:

                        waitText.setText("暂时还没有任何帖子哟");
                        break;
				}
				super.handleMessage(msg);
			}
		};

		mHandler = new Handler(getMainLooper()) {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
                    case MSG_CHANGE_PHOTO:
                        int index = mViewPager.getCurrentItem();
                        if (index == mImageUrls.size() - 1) {
                            index = -1;
                        }
                        mViewPager.setCurrentItem(index + 1);
                        mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
                                                         PHOTO_CHANGE_TIME);
				}
			}

		};
		initdata();
		initview();

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.putExtra("tid", list.get(arg2).get("tid").toString());
//				intent.putExtra("author", list.get(arg2).get("author").toString());
//				intent.putExtra("message", list.get(arg2).get("message").toString());
//				intent.putExtra("dateline", list.get(arg2).get("dateline").toString());
//                if(carList.get(arg2).toString().equals("[]"))
//                    intent.putExtra("photoname", "null");
//                else
//				    intent.putExtra("photoname", carList.get(arg2).get(0));
//
				intent.putExtra("subject", list.get(arg2).get("subject").toString());
				intent.setClass(SquareActivity.this, Theme_Post.class);
				startActivityForResult(intent, 304);
                //				Intent intent = new Intent(SquareActivity.this,LoginDialog.class);
                //				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //				startActivity(intent);
			}
		});
		
	}

	private void init() {
		// TODO Auto-generated method stub
		fid = "37";
		options = ImageMethod.GetOptions();
		Originaloptions = ImageMethod.GetOriginalOptions();
		String title_name = "广场";
		listview = (NoScrollListview)findViewById(R.id.square_listview);
		scrollView = (CustomScrollView)findViewById(R.id.discuss_kids_scrollview);

		mViewPager = (JazzyViewPager) findViewById(R.id.square_images_container);
		mIndicator = (LinearLayout) findViewById(R.id.square_images_indicator);

		edit_theme = (TextView)findViewById(R.id.sqare_theme);
		title = (TextView)findViewById(R.id.discuss_kids_title);
		title.setText(title_name);

        waitText = (ShimmerTextView)findViewById(R.id.discuss_kids_waittext);
	}

	private void initdata(){
		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);

		mImageUrl = "drawable://" + R.drawable.banner;
		mImageUrls.add(mImageUrl);
	}

	private void initview(){
		// ======= 初始化ViewPager ========
		mIndicators = new ImageView[mImageUrls.size()];
		if (mImageUrls.size() <= 1) {
			mIndicator.setVisibility(View.GONE);
		}

		for (int i = 0; i < mIndicators.length; i++) {
			ImageView imageView = new ImageView(this);
			LayoutParams params = new LayoutParams(0,
                                                   LayoutParams.WRAP_CONTENT, 1.0f);
			if (i != 0) {
				params.leftMargin = 5;
			}
			imageView.setLayoutParams(params);
			mIndicators[i] = imageView;
			if (i == 0) {
				mIndicators[i]
                .setBackgroundResource(R.drawable.guide_dot_white);
			} else {
				mIndicators[i]
                .setBackgroundResource(R.drawable.guide_dot_black);
			}

			mIndicator.addView(imageView);
		}

		mImageViews = new ImageView[mImageUrls.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setCurrentItem(0);
		mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);

		mViewPager.setAdapter(new JazzyAdapter(mImageViews, mViewPager, mImageUrls));
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (mImageUrls.size() == 0 || mImageUrls.size() == 1)
					return true;
				else
					return false;
			}
		});

		// ======= 初始化ViewPager ========
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 *
	 * @author Administrator
	 *
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			setImageBackground(position);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 设置选中的tip的背景
	 *
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mIndicators.length; i++) {
			if (i == selectItemsIndex) {
				mIndicators[i]
                .setBackgroundResource(R.drawable.guide_dot_white);
			} else {
				mIndicators[i]
                .setBackgroundResource(R.drawable.guide_dot_black);
			}
		}
	}
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("fid", fid);
        if(isload)
        {
            if(list.size() == 0)
            {
                param.put("timestamp", DateUtils.getUTCCurrentTimestamp());
            }
            else
            {
                String time = list.get(list.size()-1).get("dateline").toString();
                param.put("timestamp", DateUtils.getUTCTimestamp(time));
            }

        }
        else
        {
            param.put("timestamp", DateUtils.getUTCCurrentTimestamp());
        }

		RequstClient.post(readUri, param, new LoadCacheResponseLoginouthandler(
                                                                               SquareActivity.this,
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
						carList.clear();
					}
					JSONObject jsonObject = new JSONObject(data);
					if (jsonObject.getString("is").equals("1")) {
						String data_details = jsonObject
                        .getString("fourm_post_list");
						JSONArray array = new JSONArray(data_details);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							list.add(GetMap(jsonObject2));
						}
						if(list.size() > 0)
                        {
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
                        }
                        else{
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
			String subject = jsonObject.getString("subject");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String photoname = jsonObject.getString("photoname");
			String tid = jsonObject.getString("tid");
            //			String avatar = jsonObject.getString("avatar");
            //			map.put("avatar", avatar);
			map.put("author", author);
			map.put("subject", subject);
			map.put("message", message);
			map.put("dateline", dateline);
			map.put("photoname", photoname);
			map.put("tid", tid);
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
		LinkedList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
		long time = 0;

		public MyAdapter(Context context, LinkedList<HashMap<String, Object>> list)
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
			final ViewHolder holder;
			// TODO Auto-generated method stub

            convertView = LayoutInflater.from(context).inflate(R.layout.square_item, null);
            holder = new ViewHolder();
            holder.square_item_title = (EmojiconTextView)convertView.findViewById(R.id.square_item_title);
            holder.square_item_image = (ScaleImageView)convertView.findViewById(R.id.square_item_image);
            holder.square_item_details = (EmojiconTextView)convertView.findViewById(R.id.square_item_details);
            holder.square_item_time = (TextView)convertView.findViewById(R.id.square_item_time);
            holder.square_item_user = (EmojiconTextView)convertView.findViewById(R.id.square_item_user);
            holder.progressBarView = (ProgressBarView)convertView.findViewById(R.id.square_item_progress_View);
            //				convertView.setTag(holder);
            //				holder = (ViewHolder) convertView.getTag();
			map = list.get(position);
			holder.square_item_title.setText((CharSequence) map.get("subject"));
			holder.square_item_details.setText((CharSequence) map.get("message"));
            //			holder.square_discuss_kids_num.setText("30");
			holder.square_item_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
			holder.square_item_user.setText((CharSequence) map.get("author"));
			if(carList.get(position).size() > 0)
			{
				String imageUrl = UrlUtils.imageUrl(carList.get(position).get(0));
                Log.e("aaassss",imageUrl);
				holder.square_item_image.setVisibility(View.VISIBLE);
				MyMapApplication.imageLoader.displayImage(imageUrl, holder.square_item_image, Originaloptions,new SimpleImageLoadingListener(){
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
				holder.square_item_image.setImageHeight(80);
				holder.square_item_image.setImageWidth(100);
			}
            return convertView;
		}
	}
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
        EmojiconTextView square_item_title;
		ScaleImageView square_item_image;
        EmojiconTextView square_item_details;
		TextView square_item_time;
        EmojiconTextView square_item_user;
		ProgressBarView progressBarView;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
            case 304:
                setResult(304);
                break;
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
