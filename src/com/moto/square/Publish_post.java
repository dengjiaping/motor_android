package com.moto.square;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.SquareNetworkModel;
import com.moto.photopicker.Bimp;
import com.moto.photopicker.FileUtils;
import com.moto.photopicker.ImgPicActivity;
import com.moto.qiniu.img.Image;
import com.moto.toast.ToastClass;
import com.moto.utils.CompressUtils;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Publish_post extends Moto_RootActivity implements OnClickListener,NetWorkModelListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener,EmojiconGridFragment.OnEmojiconClickedListener{
    
	protected String filepath="";
    protected ImageView leftpage;
    protected EmojiconEditText et_sendmessage;
    protected String fid;
    protected View view;
    protected EditText write_theme;
    protected ImageView mention;
    protected ImageView emotion;
    protected ImageView send;
    protected ImageView own_photos;
    protected SharedPreferences TokenShared;
    protected String tokenString;
    protected RelativeLayout.LayoutParams layoutParams;
    protected ImageView photos;
    protected boolean isHavePhoto = false;
    protected ImageView camera;
    protected Image photofiles;
    protected RequestParams param;
    protected Handler handler;
    protected String mentionUsername = "";
    protected boolean IsHaveUserName = false;
	Intent intent;
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

        own_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHavePhoto)
                {
                    FileUtils.fliterPhoto(Publish_post.this, Publish_post.this, filepath);
                }
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
                        setResult(304);
                        break;
                        //获取成功
                    case Constant.MSG_TESTSTART:
                        DialogMethod.startProgressDialog(Publish_post.this, "正在发送");
                        break;
                    case Constant.MSG_FALTH:
                        String messageString = (String) msg.obj;
                        ToastClass.SetToast(Publish_post.this, messageString);
                        DialogMethod.stopProgressDialog();
                        CompressUtils.deleteTempFile(filepath);
                        break;
                    case Constant.MSG_NULL:
                        DialogMethod.stopProgressDialog();
                        CompressUtils.deleteTempFile(filepath);
                        ToastClass.SetToast(Publish_post.this,"发送失败");
                        break;
				}
				super.handleMessage(msg);
			}
			
		};
	}
    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(et_sendmessage);
    }
    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_sendmessage, emojicon);
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
		et_sendmessage = (EmojiconEditText)findViewById(R.id.et_sendmessage);
		leftpage = (ImageView)findViewById(R.id.square_publish_theme_return);
		own_photos = (ImageView)findViewById(R.id.square_publish_own_photos);
		photos = (ImageView)findViewById(R.id.square_publish_theme_photos);
		camera = (ImageView)findViewById(R.id.square_publish_theme_camera);
        mention = (ImageView)findViewById(R.id.square_publish_theme_mention);
		leftpage.setOnClickListener(this);
		emotion.setOnClickListener(this);
		send.setOnClickListener(this);
		photos.setOnClickListener(this);
		camera.setOnClickListener(this);
        mention.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        if(v == mention)
        {
            pushToNextActivity(Theme_Post_Touchme.class,304);
        }
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
            Bimp.drr.clear();
            Bimp.MaxPictrueSize = 1;
            Intent intent = new Intent(Publish_post.this,
                    ImgPicActivity.class);
            startActivityForResult(intent, 2);
//			intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//			startActivityForResult(intent, 2);
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
        handler.obtainMessage(Constant.MSG_TESTSTART)
                .sendToTarget();
		param = new RequestParams();
		param.put("token", tokenString);
		param.put("fid",fid);
		param.put("subject", write_theme.getText().toString());
		param.put("message", et_sendmessage.getText().toString());
        if(IsHaveUserName)
        {
            param.put("atuser",mentionUsername);
        }
		SquareNetworkModel squareNetworkModel = new SquareNetworkModel(Publish_post.this, Publish_post.this);
		if(isHavePhoto)
		{

			try {
                filepath = CompressUtils.GetCompressPath(filepath, 480);
                photofiles = new Image(filepath, "file");

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
                if(isHavePhoto)
                {
                    ToastClass.SetToast(Publish_post.this, "最多选择1张图片");
                }
                else
                {
                    try {
                        Bitmap b = Bimp.revitionImageSize(filepath);
                        own_photos.setImageBitmap(b);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
//                ImageManager2.from(Publish_post.this).displayImage(own_photos, filepath,R.drawable.default_add_img,100,100);

                    isHavePhoto = true;
                }
            }
        }

        if (requestCode == 2) {
            if(isHavePhoto)
            {
                ToastClass.SetToast(Publish_post.this, "最多选择1张图片");
            }
            else
            {
                if(Bimp.drr.size() > 0)
                {
                    filepath = Bimp.drr.get(0);
                    try {
                        Bitmap b = Bimp.revitionImageSize(filepath);
                        own_photos.setImageBitmap(b);
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
//            ImageManager2.from(Publish_post.this).displayImage(own_photos, filepath,R.drawable.default_add_img,100,100);
                    isHavePhoto = true;
                }
            }
        }
        if(resultCode == 3)
        {
            mentionUsername = data.getExtras().getString("name");
            IsHaveUserName = true;
            et_sendmessage.setText(et_sendmessage.getText().toString()+"@"+mentionUsername);
        }

        if(requestCode == 6)
        {
            if(resultCode == RESULT_OK)
            {

                try {
                    String path = data.getStringExtra("path");
                    filepath = path;
                    isHavePhoto = true;
                    Bitmap b = Bimp.revitionImageSize(filepath);
                    own_photos.setImageBitmap(b);
                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
            if(resultCode == 202)
            {
                filepath = "";
                isHavePhoto = false;
                own_photos.setImageBitmap(null);
            }
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
        handler.obtainMessage(Constant.MSG_NULL)
                .sendToTarget();
    }
    
    @Override
    public void handleNetworkDataGetFail(String message)
    throws JSONException {
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
