package com.moto.square;

import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.loopj.android.http.RequestParams;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by chen on 14-6-22.
 */
public class Theme_Post_publish extends Moto_RootActivity implements View.OnClickListener,EmojiconsFragment.OnEmojiconBackspaceClickedListener,EmojiconGridFragment.OnEmojiconClickedListener{
    private Intent intent;
    private String tid;
    private String token;
    private String subject;
    private Handler handler;
    private EmojiconEditText et_sendmessage;
    private RelativeLayout.LayoutParams layoutParams;
    private ImageView own_photos;
    private ImageView emotion;
    private ImageView send;
    private ImageView photos;
    private ImageView camera;
    private ImageView mention;
    private String mentionUsername;
    private boolean IsHaveUserName = false;
    private View view;
    private String filepath="";
    private boolean isHavePhoto = false;
    private RequestParams param;
    private Image photofiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        tid = bundle.getString("tid");
        token = bundle.getString("token");
        subject = bundle.getString("subject");
        addContentView(R.layout.square_discuss_kids_post_publish,subject,barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);
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
                    FileUtils.fliterPhoto(Theme_Post_publish.this, Theme_Post_publish.this, filepath);
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
                        ToastClass.SetImageToast(Theme_Post_publish.this, "发送成功");
                        Theme_Post_publish.this.finish();
                        break;
                    //获取成功
                    case Constant.MSG_TESTSTART:
                        DialogMethod.startProgressDialog(Theme_Post_publish.this, "正在发送");
                        break;
                    case Constant.MSG_FALTH:
                        String messageString = (String) msg.obj;
                        ToastClass.SetToast(Theme_Post_publish.this, messageString);
                        DialogMethod.stopProgressDialog();
                        CompressUtils.deleteTempFile(filepath);
                        break;
                    case Constant.MSG_NULL:
                        DialogMethod.stopProgressDialog();
                        CompressUtils.deleteTempFile(filepath);
                        ToastClass.SetToast(Theme_Post_publish.this,"发送失败");
                        break;
                }
                super.handleMessage(msg);
            }

        };

    }

    private void init(){
        view = findViewById(R.id.ll_facechoose);
        layoutParams =  new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.FILL_PARENT);
        et_sendmessage = (EmojiconEditText)findViewById(R.id.et_sendmessage);
        own_photos = (ImageView)findViewById(R.id.square_discuss_kids_publish_img);
        photos = (ImageView)findViewById(R.id.square_discuss_kids_publish_photos);
        camera = (ImageView)findViewById(R.id.square_discuss_kids_publish_camera);
        send = (ImageView)findViewById(R.id.square_discuss_kids_publish_send);
        emotion = (ImageView)findViewById(R.id.square_discuss_kids_publish_emotion);
        mention = (ImageView)findViewById(R.id.square_discuss_kids_publish_mention);

        emotion.setOnClickListener(this);
        send.setOnClickListener(this);
        photos.setOnClickListener(this);
        camera.setOnClickListener(this);
        mention.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
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
            Bimp.MaxPictrueSize = 1;
            Intent intent = new Intent(Theme_Post_publish.this,
                    ImgPicActivity.class);
            startActivityForResult(intent, 2);
        }
        if(v == emotion)
        {
            // 隐藏表情选择框
            if (view.getVisibility() == View.VISIBLE) {
                view.setVisibility(View.GONE);
            } else {
                view.setVisibility(View.VISIBLE);
                manageInput(Theme_Post_publish.this);
            }
        }
        if(v == send)
        {
            if(et_sendmessage.getText().toString().trim().equals(""))
            {
                ToastClass.SetToast(Theme_Post_publish.this,"还没有输入贴子内容哟");
            }
            else{
                SetAsyResponse();
            }
        }
        if(v == mention)
        {
            pushToNextActivity(Theme_Post_Touchme.class,304);
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
                    ToastClass.SetToast(Theme_Post_publish.this, "最多选择1张图片");
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
                ToastClass.SetToast(Theme_Post_publish.this, "最多选择1张图片");
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
    private void SetAsyResponse() {
        // TODO Auto-generated method stub
        param = new RequestParams();
        param.put("tid", tid);
        param.put("token", token);
        param.put("subject", "");
        param.put("message", et_sendmessage.getText().toString());
        if(IsHaveUserName)
        {
            param.put("atuser",mentionUsername);
        }
        SquareNetworkModel squareNetworkModel = new SquareNetworkModel(this,this);
        if(isHavePhoto)
        {

            try {
                filepath = CompressUtils.GetCompressPath(filepath, 480);
                photofiles = new Image(filepath, "file");
                squareNetworkModel.CreateNewPostForTheme(param,photofiles);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        else {
            squareNetworkModel.CreateNewPostForTheme(param);
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
        DialogMethod.stopProgressDialog();
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataStart();
        handler.obtainMessage(Constant.MSG_TESTSTART)
                .sendToTarget();
    }

    @Override
    public void onEmojiconBackspaceClicked(View view) {
        EmojiconsFragment.backspace(et_sendmessage);
    }
    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(et_sendmessage, emojicon);
    }


}
