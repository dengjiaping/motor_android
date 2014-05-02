package com.moto.square;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.live.WriteLiveActivity;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.SquareNetworkModel;
import com.moto.qiniu.img.Image;
import com.moto.select_morephoto.ImageManager2;
import com.moto.toast.ToastClass;

public class Publish_post extends Moto_RootActivity implements OnClickListener,NetWorkModelListener{
    
	private String filepath;
	private ImageView leftpage;
	private EditText et_sendmessage;
	private String fid;
	private View view;
	private EditText write_theme;
	private ImageView emotion;
	private ImageView send;
	private ImageView own_photos;
	private String photoString = null;
	private SharedPreferences TokenShared;
	private String tokenString;
	private RelativeLayout.LayoutParams layoutParams;
	private ImageView photos;
	private boolean isHavePhoto = false;
	private ImageView camera;
	private Bitmap photoBitmap;
	Image[] files;
	RequestParams param;
	private Handler handler;
	Intent intent;
	private String createThemeUri = "http://damp-reef-9073.herokuapp.com/api/square/createnewtheme";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.square_publish_theme);
		init();
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
                layoutParams.height = 200;
                layoutParams.width = 200;
				own_photos.setLayoutParams(layoutParams);
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
                        DialogMethod.stopProgressDialog();
                        ToastClass.SetImageToast(Publish_post.this,"成功发送主题帖");
                        Publish_post.this.finish();
                        break;
                        //获取成功
                    case Constant.MSG_TESTSTART:
                        DialogMethod.startProgressDialog(Publish_post.this, "正在发送");
				}
				super.handleMessage(msg);
			}
			
		};
	}
	
	private void init() {
		// TODO Auto-generated method stub
		intent = getIntent();
		fid = intent.getStringExtra("fid");
		view = findViewById(R.id.ll_facechoose);
		layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);
		send = (ImageView)findViewById(R.id.square_write_send);
		emotion = (ImageView)findViewById(R.id.square_publish_theme_emotion);
		write_theme = (EditText)findViewById(R.id.square_publish_write_theme);
		et_sendmessage = (EditText)findViewById(R.id.et_sendmessage);
		leftpage = (ImageView)findViewById(R.id.square_publish_theme_return);
		own_photos = (ImageView)findViewById(R.id.square_publish_own_photos);
		photos = (ImageView)findViewById(R.id.square_publish_theme_photos);
		camera = (ImageView)findViewById(R.id.square_publish_theme_camera);
		leftpage.setOnClickListener(this);
		emotion.setOnClickListener(this);
		send.setOnClickListener(this);
		photos.setOnClickListener(this);
		camera.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == leftpage)
		{
			Publish_post.this.finish();
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
			intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 2);
		}
		if(v == emotion)
		{
			// 隐藏表情选择框
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
			} else {
				view.setVisibility(View.VISIBLE);
				manageInput(Publish_post.this);
			}
		}
		if(v == send)
		{
			TokenShared = getSharedPreferences("usermessage", 0);
			tokenString = TokenShared.getString("token", "");
			if(tokenString.equals(""))
			{
				ToastClass.SetToast(Publish_post.this, "需要先登录才能够发送");
				setResult(304);
				Publish_post.this.finish();
			}
			else if(write_theme.getText().toString().replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(Publish_post.this,"请输入标题!");
			}
			else if(et_sendmessage.getText().toString().replaceAll(" ", "").equals("")){
				DialogMethod.dialogShow(Publish_post.this,"请编辑内容!");
			}
			else {
				files  = new Image[1];
				if(isHavePhoto)
				{
					Drawable drawable = own_photos.getDrawable();
					own_photos.setDrawingCacheEnabled(true);
					photoBitmap = Bitmap.createBitmap(own_photos.getDrawingCache());
					own_photos.setDrawingCacheEnabled(false);
					files[0] = new Image(photoBitmap, "file");
				}
				GetAsyData();
			}
		}
	}
	/**
	 * 关闭输入法
	 */
	protected void manageInput(Activity activity)
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive())
		{
			InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            
            
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                                       
                                                       
                                                       InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	private void GetAsyData() {
		// TODO Auto-generated method stub
		param = new RequestParams();
		param.put("token", tokenString);
		param.put("fid",fid);
		param.put("subject", write_theme.getText().toString());
		param.put("message", et_sendmessage.getText().toString());
		SquareNetworkModel squareNetworkModel = new SquareNetworkModel(Publish_post.this, Publish_post.this);
		if(isHavePhoto)
		{
			try {
				Image photofiles = new Image(filepath, "file");
				squareNetworkModel.CreateNewTheme(param,photofiles);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			squareNetworkModel.CreateNewTheme(param);
		}
		
	}
	
	//调用相机返回
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                ImageManager2.from(Publish_post.this).displayImage(own_photos, filepath,R.drawable.default_add_img,100,100);
                
				isHavePhoto = true;
            }
        }
        
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            
            Cursor cursor = getContentResolver().query(selectedImage,
                                                       filePathColumn, null, null, null);
            cursor.moveToFirst();
            
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            filepath = picturePath;
            cursor.close();
            ImageManager2.from(Publish_post.this).displayImage(own_photos, filepath,R.drawable.default_add_img,100,100);
            isHavePhoto = true;
        }
        
    }
    
    @Override
    public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataWithSuccess(JSONObject);
        
        handler.obtainMessage(Constant.MSG_SUCCESS)
        .sendToTarget();
    }
    
    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject)
    throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataWithFail(jsonObject);
    }
    
    @Override
    public void handleNetworkDataGetFail(String message)
    throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataGetFail(message);
    }
    
    @Override
    public void handleNetworkDataStart() throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataStart();
        handler.obtainMessage(Constant.MSG_TESTSTART)
        .sendToTarget();
    }
    
}