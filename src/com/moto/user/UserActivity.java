package com.moto.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.listview.MyGridView;
import com.moto.live.Live_Kids_User;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.SignNetWorkModel;
import com.moto.mymap.MyMapApplication;
import com.moto.photo.ImageBrowserActivity;
import com.moto.qiniu.img.Image;
import com.moto.qiniu.img.UploadImage;
import com.moto.toast.ToastClass;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.moto.validation.Validation;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

public class UserActivity extends Moto_RootActivity implements OnClickListener{
	private MyGridView listView;
	private ScrollView user_scrollview;
	private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
	protected LinkedList<LinkedList<String>> ImgList = new LinkedList<LinkedList<String>>();
	protected LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList = new LinkedList<LinkedList<HashMap<String,Integer>>>();
	private HashMap<String, Object> map;
	private HashMap<String, Object> ownMessageMap = new HashMap<String, Object>();
	private ArrayList<String> ownphotoMessage = new ArrayList<String>();
	private HashMap<String, Object> ownMessageMapNum = new HashMap<String, Object>();
	private ArrayList<String> carList = new ArrayList<String>();
	private MyAdapter adapter;
	private LinearLayout number_post_layout;
	private LinearLayout friends_layout;
	private LinearLayout collect_layout;
	private ImageView user_userimg;
	private TextView no_dongtai;
	private TextView user_signature;
	private TextView user_mototype;
    private TextView user_outputvolume; 
    private TextView user_friends;
    private TextView user_number_post;
	private Handler handler;
	private DisplayImageOptions options;
	private DisplayImageOptions Originaloptions;
	private BootstrapEditText login_name;
	private BootstrapEditText login_password;
	private BootstrapButton login_button;
	private String nameString;
	private String passwordString;
	private String emailString = "";
	private SharedPreferences mshared;
	private LinearLayout moto_photo;
	private Editor editor;
    private PullScrollView pullScrollView;
    private RelativeLayout user_head_layout;

	private Intent intent;
	private RequestParams param;

//    private Bitmap bmap;

	private String token;
	private String recentPostUri = path+"api/me/readrecentpost";
	private String readUri = path+"api/me/readprofile";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mshared = getSharedPreferences("usermessage", 0);
		String str = mshared.getString("token", "");

		if(str.equals(""))
			login();
		else {
			user();
		}
		handler = new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				
				switch(msg.what)
				{
				//获取成功
				case Constant.MSG_SUCCESS:
                    DialogMethod.stopProgressDialog();
					user();
					break;
				case Constant.MSG_NULL:
					DialogMethod.stopProgressDialog();
					DialogMethod.dialogShow(UserActivity.this,"用户名或者密码错误！");
					break;
				case Constant.MSG_FALTH:
					DialogMethod.dialogShow(UserActivity.this,"网络请求超时!");
					break;
				case Constant.MSG_SUCCESSAGAIN:
					user_scrollview.post(new Runnable() {  
						//让scrollview跳转到顶部，必须放在runnable()方法中
					    @Override  
					    public void run() {  
					    	user_scrollview.scrollTo(0, 0);  
					     }  
					});
					if(list.size() == 0)
					{
						no_dongtai.setVisibility(View.VISIBLE);
					}
					adapter.notifyDataSetChanged();
					
					break;
				case Constant.MSG_START:
					SetMessage();
					break;
				case Constant.MSG_TESTFALTH:
					ToastClass.SetToast(UserActivity.this, msg.obj.toString());
					DialogMethod.stopProgressDialog();
					break;
				case Constant.MSG_TESTSTART:
					DialogMethod.startProgressDialog(UserActivity.this,"正在登录");	
					break;
//                case Constant.MSG_GETIMGESUCCESS:
//
//
//                    pullScrollView.destoryBitmap();
//                    BitmapUtils.getInstance(UserActivity.this,bmap).DeleteSDBitmap();
//                    BitmapUtils.getInstance(UserActivity.this,bmap).SetBitmapToSD();
//                    Blur.getInstance(UserActivity.this, bmap).SetBitmapToSD();
//                    SetBackgroundPhoto(R.drawable.male);
//
//                    break;
				}
				super.handleMessage(msg);
			}
			
		};
		
	}
	private void login(){
//		setContentView(R.layout.user_login);
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);

        addContentView(R.layout.user_login, R.string.userlogin, R.string.bit_register,barButtonIconType.barButtonIconType_None, barButtonIconType.barRightTextViewType );
        if(viewGroup.getChildCount() >= 3)
            viewGroup.removeView(viewGroup.getChildAt(0));
		login_init();
	}


    private void user(){
        ViewGroup viewGroup = (ViewGroup) findViewById(android.R.id.content);

//		setContentView(R.layout.user);
		token = mshared.getString("token", "");
//		user_name.setText(mshared.getString("username", ""));
		addContentView(R.layout.user, mshared.getString("username", ""), barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_setting );
        if(viewGroup.getChildCount() >= 3)
            viewGroup.removeView(viewGroup.getChildAt(0));
		navigationBar.setBackgroundColor(Color.rgb(0,0,0));
		navigationBar.getBackground().setAlpha(0);
		init();

		adapter = new MyAdapter(this, list);
		listView.setAdapter(adapter);
        ownMessageMapNum.clear();
        ownphotoMessage.clear();
        ownMessageMap.clear();
        carList.clear();
		GetAsyMessageData();
		GetAsyData();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle extras = new Bundle();
				extras.putString("tid", list.get(position).get("tid").toString());
				extras.putString("subject", list.get(position).get("subject").toString());
				pushToNextActivity(extras, Live_Kids_User.class, 304);
			}
		});
        this.navigationBar.bringToFront();




	}

    private void init() {
		// TODO Auto-generated method stub
		options = ImageMethod.GetOptions();
		Originaloptions = ImageMethod.GetOriginalOptions();
		moto_photo = (LinearLayout)findViewById(R.id.user_moto_photo);
		no_dongtai = (TextView)findViewById(R.id.user_haveno_dongtai);
		listView = (MyGridView)findViewById(R.id.user_dynamic_gridview);
		user_scrollview = (ScrollView)findViewById(R.id.user_scrollview);
//		user_setting = (ImageView)findViewById(R.id.user_setting);
		number_post_layout = (LinearLayout)findViewById(R.id.user_number_post_layout);

		friends_layout = (LinearLayout)findViewById(R.id.user_friends_layout);
		collect_layout = (LinearLayout)findViewById(R.id.user_collect_layout);
//		user_name = (TextView)findViewById(R.id.user_name);
		user_userimg = (ImageView)findViewById(R.id.user_userimg);
		user_signature = (TextView)findViewById(R.id.user_signature);
		user_mototype = (TextView)findViewById(R.id.user_mototype);
		user_outputvolume = (TextView)findViewById(R.id.user_outputvolume);
		user_number_post = (TextView)findViewById(R.id.user_number_post);
		user_friends = (TextView)findViewById(R.id.user_friends);
		number_post_layout.setOnClickListener(this);
		friends_layout.setOnClickListener(this);
		collect_layout.setOnClickListener(this);
//		user_setting.setOnClickListener(this);
        user_head_layout = (RelativeLayout)findViewById(R.id.user_head_layout);
        pullScrollView = (PullScrollView)findViewById(R.id.user_scrollview);
        pullScrollView.setHeader(user_head_layout);
        pullScrollView.setOnTurnListener(new PullScrollView.OnTurnListener() {
            @Override
            public void onTurn() {

            }
        });
//        SetBackgroundPhoto(R.drawable.male);

        LinearLayout rightLinearLayout = (LinearLayout)findViewById(R.id.right_linear_nav);
        rightLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent();
                if(ownMessageMap.size() == 0)
                {
                    intent.putExtra("is", "0");
                }
                else
                {
                    intent.putExtra("is", "1");
                    intent.putExtra("avatar", ownMessageMap.get("avatar")+"");
                    intent.putStringArrayListExtra("motophoto", ownphotoMessage);
                    intent.putExtra("gender", ownMessageMap.get("gender")+"");
                    intent.putExtra("username", ownMessageMap.get("username")+"");
                    intent.putExtra("profile", ownMessageMap.get("profile")+"");
                    intent.putExtra("mototype", ownMessageMap.get("mototype")+"");
                    intent.putExtra("outputvolume", ownMessageMap.get("outputvolume")+"");
                }
                intent.setClass(UserActivity.this, User_SystemSetting.class);
                startActivityForResult(intent, 0);
            }
        });
	}
	
	private void login_init(){
		login_name = (BootstrapEditText)findViewById(R.id.user_login_name);
		login_password = (BootstrapEditText)findViewById(R.id.user_login_password);
		login_button = (BootstrapButton)findViewById(R.id.user_login_button);
		login_button.setOnClickListener(this);
        this.rightBarButton.setVisibility(View.GONE);
        this.rightBarTextView.setVisibility(View.VISIBLE);
        navigationBar.setBackgroundColor(Color.rgb(107,233,242));
        navigationBar.getBackground().setAlpha(255);
        LinearLayout rightLinearLayout = (LinearLayout)findViewById(R.id.right_linear_nav);
        rightLinearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(UserActivity.this, User_register.class);
                startActivity(intent);
            }
        });
	}
	
	class MyAdapter extends BaseAdapter{
		private Context context;
		private LinkedList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
		
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
			ViewHolder holder;
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(context).inflate(R.layout.user_item, null);
			holder = new ViewHolder();
			holder.user_item_text = (TextView)convertView.findViewById(R.id.user_item_text);
			holder.user_item_img = (ImageView)convertView.findViewById(R.id.user_item_img);
//			holder.user_item_img = (ScaleImageView)convertView.findViewById(R.id.user_item_img);
			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();
			
			map = list.get(position);	
			holder.user_item_text.setText("进行中");
//	        if(!map.get("photoname").toString().equals("null"))
//			{
//				String imageUrl = imgPath+map.get("photoname");
//				holder.user_item_img.setVisibility(View.VISIBLE);
//				MyMapApplication.imageLoader.displayImage(imageUrl,  holder.user_item_img,options,null);
//				holder.user_item_img.setImageHeight(80);
//				holder.user_item_img.setImageWidth(100);
//			}
			int num = ImgList.get(position).size();
			if(num > 0)
			{
				String imageUrl = UrlUtils.imageUrl(ImgList.get(position).get(num-1));
				MyMapApplication.imageLoader.displayImage(imageUrl, holder.user_item_img,Originaloptions,null);
			}
	        return convertView;
		}	
		//此类为上面getview里面view的引用，方便快速滑动
		class ViewHolder{
			TextView user_item_text;
			ImageView user_item_img;
		}
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == number_post_layout)
		{
			intent = new Intent();
			intent.setClass(UserActivity.this, User_numPost.class);
			startActivity(intent);
		}
		if(v == friends_layout)
		{

            Bundle bundle = new Bundle();
            bundle.putString("author",ownMessageMap.get("username")+"");
            pushToNextActivity(bundle,User_friends.class,304);
		}
		if(v == collect_layout)
		{
			intent = new Intent();
			intent.setClass(UserActivity.this, User_collect.class);
			startActivity(intent);
		}

		if(v == login_button)
		{
			nameString = login_name.getText().toString();
			passwordString = login_password.getText().toString();
			if(nameString.replaceAll(" ", "").equals(""))
			{
                DialogMethod.dialogShow(UserActivity.this,"请输入用户名!");
			}
			else if (passwordString.replaceAll(" ", "").equals("")) {
				DialogMethod.dialogShow(UserActivity.this,"请输入密码!");
			}
			else {
				login_setData();
			}
		}
	}
	private void login_setData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		if(!Validation.checkEmail(nameString))
		{
			emailString = nameString;
			param.put("email", emailString);
			param.put("password", passwordString);
		}
		else
		{
			param.put("username", nameString);
			param.put("password", passwordString);
		}
		SignNetWorkModel signNetWorkModel = new SignNetWorkModel(this, this);
		signNetWorkModel.signUp(param);

	}
	private void GetAsyMessageData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", token);
		RequstClient.post(readUri, param, new LoadCacheResponseLoginouthandler(
				UserActivity.this, 
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
					JSONObject jsonObject1 = new JSONObject(data);
					if (jsonObject1.getString("is").equals("1")) {
						GetOwnMessageNum(jsonObject1);
						JSONArray array = new JSONArray(jsonObject1.getString("profile"));
						JSONObject jsonObject = (JSONObject) array.get(0);
						GetOwnMessage(jsonObject);
						handler.obtainMessage(Constant.MSG_START)
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
	private void GetOwnMessageNum(JSONObject jsonObject)
	{
		try {
			ownMessageMapNum.put("post_count", jsonObject.getString("post_count"));
			ownMessageMapNum.put("friend_count", jsonObject.getString("friend_count"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private void GetOwnMessage(JSONObject jsonObject){
		try {
			ownMessageMap.put("avatar", jsonObject.getString("avatar"));
			ownMessageMap.put("gender", jsonObject.getString("gender"));
			ownMessageMap.put("username", jsonObject.getString("username"));
			ownMessageMap.put("profile", jsonObject.getString("profile"));
			ownMessageMap.put("mototype", jsonObject.getString("mototype"));
			ownMessageMap.put("outputvolume", jsonObject.getString("outputvolume"));

            editor = mshared.edit();
            editor.putString("avatar", jsonObject.getString("avatar"));

            editor.commit();
			GetOwnPhoto(jsonObject.getString("motophoto"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	private void GetOwnPhoto(String motophoto)
	{
		JSONArray array = null;
		try {
			array = new JSONArray(motophoto);
			int num = array.length();
			for(int i = 0; i < num; i++)
			{
				try {
					ownphotoMessage.add(array.getString(i));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	private void SetMessage(){

        if(!ownMessageMap.get("avatar").toString().equals(""))
        {

            String imageUrl = UrlUtils.imageUrl(ownMessageMap.get("avatar").toString());
//            GetAsyBitmap(imageUrl);
            MotorApplication.imageLoader.displayImage(imageUrl,  user_userimg,options,null);


        }

		int num = ownphotoMessage.size();
		if(num > 0)
		{
			for(int i = 0; i < num; i++)
			{
				String imageUrl = ownphotoMessage.get(i);
				carList.add(imageUrl) ;
			}
			SetMotoPhoto(carList);
			
		}

		if(!ownMessageMap.get("mototype").equals("null"))
			user_mototype.setText(ownMessageMap.get("mototype").toString());
        else{
            user_mototype.setText("无");
        }
		if(!ownMessageMap.get("outputvolume").equals("null"))
			user_outputvolume.setText(ownMessageMap.get("outputvolume").toString());
        else
        {
            user_outputvolume.setText("无");
        }
		if(!ownMessageMap.get("profile").toString().equals(""))
			user_signature.setText(ownMessageMap.get("profile").toString());
		user_number_post.setText(ownMessageMapNum.get("post_count").toString());
		user_friends.setText(ownMessageMapNum.get("friend_count").toString());
	}

//    private void GetAsyBitmap(String image_url){
//        AsyncHttpClient client = new AsyncHttpClient();
//
//        client.get(image_url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                super.onSuccess(statusCode, headers, responseBody);
//
//                bmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
//                handler.obtainMessage(Constant.MSG_GETIMGESUCCESS)
//                        .sendToTarget();
//            }
//        });
//
//    }

//    private void SetBackgroundPhoto(int drawable)
//    {
//        Bitmap bitmap = null;
//        try {
//            bitmap = BitmapUtils.getInstance(UserActivity.this, bitmap).GetBitmap();
//            if(bitmap ==null)
//            {
//
//                bitmap = BitmapFactory.decodeResource(getResources(), drawable);
//
//                BitmapUtils.getInstance(UserActivity.this,bitmap);
//            }
//        }catch (OutOfMemoryError o)
//        {}
//
//        pullScrollView.setOriginbitmap(this, bitmap);
//        if(bitmap != null && !bitmap.isRecycled())
//        {
//
//            bitmap.recycle();
//
//
//            bitmap = null;
//        }
//        System.gc(); //回收
//    }
	
	private void SetMotoPhoto(final ArrayList<String> carList)
	{
		int num = carList.size();
		if (num > 0) {
			moto_photo.setVisibility(View.VISIBLE);
			moto_photo.removeAllViews();
			for (int i = 0; i < num; i++) {
				View mLayout;
				ImageView mPhoto;
				mLayout = LayoutInflater.from(UserActivity.this).inflate(
						R.layout.user_moto_photo_image_item, null);
				mPhoto = (ImageView) mLayout
						.findViewById(R.id.user_moto_photo_img);
				moto_photo.addView(mLayout);
				moto_photo.invalidate();
				MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(carList.get(i)),  mPhoto,Originaloptions,null);
				final int position = i;
				mPhoto.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(UserActivity.this,
								ImageBrowserActivity.class);
						intent.putExtra(ImageBrowserActivity.IMAGE_TYPE,
								ImageBrowserActivity.TYPE_ALBUM);
						intent.putExtra("position", position);
						Bundle bundle = new Bundle();
						bundle.putSerializable("carlist", carList);
						intent.putExtras(bundle);
						startActivity(intent);
						overridePendingTransition(R.anim.zoom_enter, 0);
					}
				});
				
			}
		} else {
			moto_photo.setVisibility(View.GONE);
		}
	}
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", token);
		RequstClient.post(recentPostUri, param, new LoadCacheResponseLoginouthandler(
				UserActivity.this, 
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
					JSONObject jsonObject1 = new JSONObject(data);
					if (jsonObject1.getString("is").equals("1")) {
						String data_details = jsonObject1
								.getString("post_list");
						JSONArray array = new JSONArray(data_details);
						for (int i = 0; i < array.length(); i++) {
							JSONObject jsonObject2 = (JSONObject) array.get(i);
							list.add(GetMap(jsonObject2));
						}
						handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
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
	private HashMap<String, Object> GetMap(JSONObject jsonObject)
	{
		map = new HashMap<String, Object>();
		try {
			
			String author = jsonObject.getString("author");
			String subject = jsonObject.getString("subject");
			String message = jsonObject.getString("message");
			String dateline = jsonObject.getString("dateline");
			String tid = jsonObject.getString("tid");
			map.put("author", author);
			map.put("subject", subject);
			map.put("message", message);
			map.put("dateline", dateline);
			map.put("tid", tid);
			ImgList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
			WidthHeightList.add(StringUtils.hashToWidthHeightArray(jsonObject.getString("photoinfo")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return map;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(resultCode)
		{
		case 301:
			login();
//            pullScrollView.destoryBitmap();
			break;
		}
        switch (requestCode)
        {
            case 0:
                ownMessageMapNum.clear();
                ownphotoMessage.clear();
                ownMessageMap.clear();
                carList.clear();
                GetAsyMessageData();
                break;
        }
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
		// TODO Auto-generated method stub
		JSONObject jsonObject2 = new JSONObject(jsonObject.getString("userinfo"));
		String uidString = jsonObject2.getString("email");
		String usernameString = jsonObject2.getString("username");
		String tokenString = jsonObject2.getString("token");
		editor = mshared.edit();
		editor.putString("email", uidString);
		editor.putString("username", usernameString);
		editor.putString("token", tokenString);
		editor.commit();
		handler.obtainMessage(Constant.MSG_SUCCESS)
		.sendToTarget();
	}
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException{
		handler.obtainMessage(Constant.MSG_NULL)
		.sendToTarget();
	}
	@Override
	public void handleNetworkDataWithUpdate(float progress)
			throws JSONException {
		
	}
	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		super.handleNetworkDataGetFail(message);
		// 获取一个Message对象，设置what为1
		Message msg = Message.obtain();
		msg.obj = message;
		msg.what = Constant.MSG_TESTFALTH;
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

	public class ImageArrayUploadTask extends AsyncTask<ArrayList<Image>, Float, String>{
		private String qiniu_url = "http://up.qiniu.com/";
	
		@Override
		protected String doInBackground(ArrayList<Image>... params) {
			ArrayList<Image> imageArray  = params[0];
			UploadImage uploadImage = new UploadImage();

			for (Iterator iterator = imageArray.iterator(); iterator.hasNext();) {
				Image image = (Image) iterator.next();
				String respose = uploadImage.post(qiniu_url,image);
				imageArray.remove(image);
			}
			return qiniu_url;
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
	
		}
		
		@Override
		protected void onProgressUpdate(Float... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
		}


	}



}