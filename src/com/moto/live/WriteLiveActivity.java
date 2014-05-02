package com.moto.live;

import java.io.File;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.main.R;
import com.moto.model.LiveNetworkModel;
import com.moto.model.NetWorkModelListener;
import com.moto.myactivity.MyActivity;
import com.moto.mydialog.errorDialog;
import com.moto.qiniu.img.Image;
import com.moto.select_morephoto.AlbumActivity;
import com.moto.select_morephoto.GridImageAdapter;
import com.moto.toast.ToastClass;
import com.moto.utils.StringUtils;

import darko.imagedownloader.ImageLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WriteLiveActivity extends MyActivity implements OnClickListener,NetWorkModelListener{
	private ImageView mention;
	private ImageView camera;
	private ImageView photos;
	private GridView image_grid;
	private View view;
	private TextView end_live;
	private ImageView emotion;
	private LinearLayout position;
	private ImageView return_live;
	private TextView real_position;
	private ImageView write_send;
	private String subject =null;
	private String location;
	private EditText et_sendmessage;
	private SharedPreferences TokenShared;
	private String tokenString;
	private String is;
	private JSONObject jsonObject;
	private TextView live_name;
	private TextView live_user_name;
	private RelativeLayout.LayoutParams layoutParams;
	private boolean isHavePhoto = false;
	Image[] files = new Image[1];
	private Handler handler;
	private String lat = "";
	private String lon = "";
	private String locationsign = "";
	private String filepath;
	
	private ArrayList<String> dataList = new ArrayList<String>();
	private GridImageAdapter gridImageAdapter;
	
	public ImageLoader loader;
	private String writeUri = "http://damp-reef-9073.herokuapp.com/api/live/writelive";
	private String EndLiveUri = "http://damp-reef-9073.herokuapp.com/api/live/endliving";
	private ArrayList<Image> lovecarImage = new ArrayList<Image>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writelive);
		init();
		
		
		gridImageAdapter = new GridImageAdapter(this, dataList);
		image_grid.setAdapter(gridImageAdapter);
		
		image_grid.setOnItemClickListener(new GridView.OnItemClickListener() {
            
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                
                //				if (position == dataList.size() - 1) {
                
                Intent intent = new Intent(WriteLiveActivity.this,
                                           AlbumActivity.class);
                Bundle bundle = new Bundle();
                // intent.putArrayListExtra("dataList", dataList);
                bundle.putStringArrayList("dataList",
                                          StringUtils.getIntentArrayList(dataList));
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                
                //				}
                
			}
            
		});
		
		et_sendmessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				int lines = et_sendmessage.getLineCount();
				int height = et_sendmessage.getLineHeight();
				layoutParams.setMargins(4, lines * height + 30, 0, 0);
				image_grid.setLayoutParams(layoutParams);
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
                        ToastClass.SetImageToast(WriteLiveActivity.this,"成功发送直播");
                        setResult(303);
                        WriteLiveActivity.this.finish();
                        break;
                    case Constant.MSG_SUCCESS:
                        ToastClass.SetImageToast(WriteLiveActivity.this,"成功结束直播");
                        setResult(303);
                        WriteLiveActivity.this.finish();
                        break;
                    case Constant.MSG_FALTH:
                        String messageString = (String) msg.obj;
                        ToastClass.SetToast(WriteLiveActivity.this, messageString);
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
	
	
	
	private void init() {
		// TODO Auto-generated method stub
		layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);
		mention = (ImageView)findViewById(R.id.mention);
		camera = (ImageView)findViewById(R.id.camera);
		photos = (ImageView)findViewById(R.id.photos);
		image_grid = (GridView)findViewById(R.id.own_photos_grid);
		view = findViewById(R.id.ll_facechoose);
		emotion = (ImageView)findViewById(R.id.emotion);
		position = (LinearLayout)findViewById(R.id.position);
		return_live = (ImageView)findViewById(R.id.return_live);
		real_position = (TextView)findViewById(R.id.real_position);
		write_send = (ImageView)findViewById(R.id.write_send);
		et_sendmessage = (EditText)findViewById(R.id.et_sendmessage);
		end_live = (TextView)findViewById(R.id.end_live);
		live_name = (TextView)findViewById(R.id.live_name);
		live_user_name = (TextView)findViewById(R.id.live_user_name);
		end_live.setOnClickListener(this);
		mention.setOnClickListener(this);
		camera.setOnClickListener(this);
		photos.setOnClickListener(this);
		emotion.setOnClickListener(this);
		position.setOnClickListener(this);
		return_live.setOnClickListener(this);
		write_send.setOnClickListener(this);
        
		intent = getIntent();
		subject = intent.getStringExtra("subject");
		live_name.setText(subject);
		TokenShared = getSharedPreferences("usermessage", 0);
		tokenString = TokenShared.getString("token", "");
		live_user_name.setText(TokenShared.getString("username", ""));
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == mention)
		{
			intent = new Intent();
			intent.setClass(WriteLiveActivity.this, ContactActivity.class);
			startActivity(intent);
		}
		
		if(v == camera)
		{
			filepath = "/mnt/sdcard/DCIM/Camera/"
            + System.currentTimeMillis() + ".png";
	        final File file = new File(filepath);
	        final Uri imageuri = Uri.fromFile(file);
	        // TODO Auto-generated method stub
	        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageuri);
	        startActivityForResult(intent, 1);
		}
		
		if(v == photos)
		{
			
			Intent intent = new Intent(WriteLiveActivity.this,
                                       AlbumActivity.class);
			Bundle bundle = new Bundle();
			// intent.putArrayListExtra("dataList", dataList);
			bundle.putStringArrayList("dataList",
                                      StringUtils.getIntentArrayList(dataList));
			intent.putExtras(bundle);
			startActivityForResult(intent, 0);
		}
		
		if(v == emotion)
		{
			// 隐藏表情选择框
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
			} else {
				view.setVisibility(View.VISIBLE);
				manageInput(WriteLiveActivity.this);
			}
		}
		
		if(v == position)
		{
			intent = new Intent();
			intent.setClass(WriteLiveActivity.this, Places.class);
			startActivityForResult(intent, 3);
		}
		
		if(v == return_live)
		{
			this.finish();
		}
		
		if(v == write_send)
		{
			
			if(real_position.getText().toString().equals("插入位置"))
			{
				location = "";
			}
			else
			{
				location = real_position.getText().toString();
			}
			if(et_sendmessage.getText().toString().replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(WriteLiveActivity.this,"请编辑内容!");
			}
			else {
				GetAsyData();
				new AsyncTask<Integer, String, Integer>(){
                    
					@Override
					protected Integer doInBackground(Integer... params) {
						// TODO Auto-generated method stub
                        
                        try {
                            Thread.sleep(40 * 1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
						return null;
					}
					@Override
					protected void onCancelled() {
						DialogMethod.stopProgressDialog();
						super.onCancelled();
					}
					@Override
					protected void onPreExecute() {
						DialogMethod.startProgressDialog(WriteLiveActivity.this,"正在发送");
					}
                    
					@Override
					protected void onPostExecute(Integer result) {
						DialogMethod.stopProgressDialog();
                        //						DialogMethod.dialogShow(User_EditUserMassage.this, "修改失败!");
					}
					
				}.execute();
			}
		}
		if(v == end_live)
		{
            final errorDialog.Builder builder = new errorDialog.Builder(WriteLiveActivity.this);
            //				builder.setTitle("错误提示");
            builder.setMessage("你确定要结束这段直播吗?");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                    new AsyncTask<Integer, String, Integer>(){
                        
                        @Override
                        protected Integer doInBackground(Integer... params) {
                            // TODO Auto-generated method stub
                            try {
                                
                                EndLiving();
                                Thread.sleep(40 * 1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                        @Override
                        protected void onCancelled() {
                            DialogMethod.stopProgressDialog();
                            super.onCancelled();
                        }
                        @Override
                        protected void onPreExecute() {
                            DialogMethod.startProgressDialog(WriteLiveActivity.this,"正在结束");
                        }
                        
                        @Override
                        protected void onPostExecute(Integer result) {
                            DialogMethod.stopProgressDialog();
                            //								DialogMethod.dialogShow(User_EditUserMassage.this, "修改失败!");
                        }
                        
                    }.execute();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });
            builder.create().show();
            
        }
	}
	//调用相机返回
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				
				Bundle bundle = data.getExtras();
				ArrayList<String> tDataList = (ArrayList<String>)bundle.getSerializable("dataList");
				if (tDataList != null) {
					if (tDataList.size() < 5) {
						tDataList.add("default_add_img");
					}
					isHavePhoto = true;
					dataList.clear();
					dataList.addAll(tDataList);
					gridImageAdapter.notifyDataSetChanged();
					
				}
			}
		}
		else if (requestCode == 1) {
			if (resultCode == RESULT_OK) {
                isHavePhoto = true;
                if(StringUtils.getIntentArrayList(dataList).size() >= 5)
                {
                    ToastClass.SetToast(WriteLiveActivity.this, "只能选择5张图片");
                }
                else {
                    ArrayList<String> tDataList = StringUtils.getIntentArrayList(dataList);
                    tDataList.add(filepath);
                    if (tDataList.size() < 5) {
                        tDataList.add("default_add_img");
                    }
                    dataList.clear();
                    dataList.addAll(tDataList);
                    gridImageAdapter.notifyDataSetChanged();
                }
            }
		}
        
        else if(requestCode == 3)
        {
            real_position.setText(data.getStringExtra("data"));
            lat = data.getStringExtra("lat");
            lon = data.getStringExtra("lon");
            locationsign = data.getStringExtra("locationsign");
        }
        else if(requestCode == 4)
        {
            real_position.setText(data.getStringExtra("data"));
        }
	}
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", tokenString);
		param.put("subject", subject);
		param.put("message", et_sendmessage.getText().toString());
		param.put("location", location);
		param.put("longitude", lon);
		param.put("latitude", lat);
		param.put("locationsign", locationsign);
		LiveNetworkModel liveNetworkModel = new LiveNetworkModel(this, this);
		ArrayList<String> tdataList = StringUtils.getIntentArrayList(dataList);
		int num1 = tdataList.size();
		try {
			for (int i = 0; i < num1; i++) {
				lovecarImage.add(new Image(tdataList.get(i),
                                           "file"));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(isHavePhoto)
		{
			liveNetworkModel.writelive(param, lovecarImage,"photo","photoinfo");
		}
		else {
			liveNetworkModel.writelive(param);
		}
	}
	
	//结束直播
	private void EndLiving() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", tokenString);
		RequstClient.post(EndLiveUri, param, new LoadCacheResponseLoginouthandler(
                                                                                  WriteLiveActivity.this,
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
					jsonObject = new JSONObject(data);
					is = jsonObject.getString("is");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(is.equals("1"))
				{
					DialogMethod.stopProgressDialog();
					handler.obtainMessage(Constant.MSG_SUCCESS)
					.sendToTarget();
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
    
	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
		// TODO Auto-generated method stub
		handler.obtainMessage(Constant.MSG_WAITSUCCESS)
		.sendToTarget();
	}
    
	@Override
	public void handleNetworkDataWithFail(JSONObject jsonObject)
    throws JSONException {
		// TODO Auto-generated method stub
		
	}
    
	@Override
	public void handleNetworkDataWithUpdate(float progress)
    throws JSONException {
		// TODO Auto-generated method stub
		
	}
    
    
    
	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		// 获取一个Message对象，设置what为1
	    Message msg = Message.obtain();
		msg.obj = message;
		msg.what = Constant.MSG_FALTH;
		// 发送这个消息到消息队列中
		handler.sendMessage(msg);
		DialogMethod.stopProgressDialog();
	}
    
    
    
	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
