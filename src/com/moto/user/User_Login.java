package com.moto.user;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.SignNetWorkModel;
import com.moto.toast.ToastClass;
import com.moto.validation.Validation;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.QZoneSsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Created by chen on 14-7-1.
 */
public class User_Login extends Moto_RootActivity implements View.OnClickListener{
    private BootstrapEditText login_name;
    private BootstrapEditText login_password;
    private BootstrapButton login_button;
    private String nameString;
    private String passwordString;
    private RelativeLayout sinaquicklogin;
    private RelativeLayout qqquicklogin;
    private RequestParams param;
    private String emailString = "";
    private SharedPreferences.Editor editor;
    private SharedPreferences mshared;
    private Handler handler;
    //快速登录
    UMSocialService mController ;
    private String usid = "";
    private String avatar = "";
    private String username = "";
    private String oauth_type = "1";
    private String gender = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.user_login, R.string.userlogin, R.string.bit_register,barButtonIconType.barButtonIconType_Back, barButtonIconType.barRightTextViewType );
        init();

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        DialogMethod.stopProgressDialog();
                        setResult(100);
                        User_Login.this.finish();
                        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);

                        break;
                    case Constant.MSG_NULL:
                        DialogMethod.stopProgressDialog();
                        DialogMethod.dialogShow(User_Login.this,"用户名或者密码错误！");
                        break;
                    case Constant.MSG_FALTH:
                        ToastClass.SetToast(User_Login.this, msg.obj.toString());
                        DialogMethod.stopProgressDialog();
                        break;

                }
                super.handleMessage(msg);
            }

        };


        mController = UMServiceFactory.getUMSocialService("com.umeng.login",
                RequestType.SOCIAL);

        //为了避免每次都从服务器获取APP ID、APP KEY，请设置APP ID跟APP KEY
        mController.getConfig().setSsoHandler( new QZoneSsoHandler(User_Login.this, "100424468", "53c0b8f456240b865200cc6b") );
//        //设置新浪SSO handler
//        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//
//        SocializeConfig config = mController.getConfig();
//        //添加关注对象(平台，关注用户的uid)
//        config.addFollow(SHARE_MEDIA.SINA, "1914100420");
//        //添加follow 时的回调
//        config.setOauthDialogFollowListener(new SocializeListeners.MulStatusListener() {
//            @Override
//            public void onStart() {
//                Log.d("TestData", "Follow Start");
//            }
//
//            @Override
//            public void onComplete(MultiStatus multiStatus, int st, SocializeEntity entity) {
//                if (st == 200) {//follow 成功
//                    //TODO do something
//                }
//            }
//        });


        //新浪快速登录
        sinaquicklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.doOauthVerify(User_Login.this, SHARE_MEDIA.SINA,new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            //获取返回值
                            mController.getPlatformInfo(User_Login.this, SHARE_MEDIA.SINA, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {
                                    Toast.makeText(User_Login.this, "拉取数据...", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(int status, Map<String, Object> info) {

                                    if (status == 200 && info != null) {
//                                        StringBuilder sb = new StringBuilder();
                                        Set<String> keys = info.keySet();
                                        for (String key : keys) {
                                            Log.e("aaaa",info.get(key).toString());
                                            if(key.equals("uid"))
                                            {
                                                usid = info.get(key).toString();
                                            }
                                            else if(key.equals("gender"))
                                            {
                                                String m = info.get(key).toString();
                                                if(m.equals("1"))
                                                {
                                                    gender = m;
                                                }
                                                else
                                                {
                                                    gender = "2";
                                                }
                                            }
                                            else if(key.equals("profile_image_url"))
                                            {
                                                avatar = info.get(key).toString();
                                            }
                                            else if(key.equals("screen_name"))
                                            {
                                                username = info.get(key).toString();
                                            }
//                                            sb.append(key + "=" + info.get(key).toString() + "\r\n");
                                        }
                                           quickLogin();
//                                        Log.e("TestData", sb.toString());
                                    } else {
                                        Log.e("TestData", "发生错误：" + status);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(User_Login.this, "授权失败",                       Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {}
                    @Override
                    public void onStart(SHARE_MEDIA platform) {}
                });

            }
        });

        //QQ快速登录
        qqquicklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mController.doOauthVerify(User_Login.this, SHARE_MEDIA.QZONE,new SocializeListeners.UMAuthListener() {
                    @Override
                    public void onError(SocializeException e, SHARE_MEDIA platform) {
                    }
                    @Override
                    public void onComplete(Bundle value, SHARE_MEDIA platform) {
                        if (value != null && !TextUtils.isEmpty(value.getString("uid"))) {
                            //获取返回值
                            mController.getPlatformInfo(User_Login.this, SHARE_MEDIA.QZONE, new SocializeListeners.UMDataListener() {
                                @Override
                                public void onStart() {
                                    Toast.makeText(User_Login.this, "拉取数据...", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onComplete(int status, Map<String, Object> info) {
                                    if (status == 200 && info != null) {
//                                        StringBuilder sb = new StringBuilder();
                                        Set<String> keys = info.keySet();
                                        for (String key : keys) {
                                            if(key.equals("uid"))
                                            {
                                                usid = info.get(key).toString();
                                            }
                                            else if(key.equals("gender"))
                                            {
                                                String m = info.get(key).toString();
                                                if(m.equals("1"))
                                                {
                                                    gender = m;
                                                }
                                                else
                                                {
                                                    gender = "2";
                                                }
                                            }
                                            else if(key.equals("profile_image_url"))
                                            {
                                                avatar = info.get(key).toString();
                                            }
                                            else if(key.equals("screen_name"))
                                            {
                                                username = info.get(key).toString();
                                            }
//                                            sb.append(key + "=" + info.get(key).toString() + "\r\n");
                                        }
                                        quickLogin();
//                                        Log.e("TestData", sb.toString());
                                    } else {
                                        Log.e("TestData", "发生错误：" + status);
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(User_Login.this, "授权失败",                       Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancel(SHARE_MEDIA platform) {}
                    @Override
                    public void onStart(SHARE_MEDIA platform) {}
                });

            }
        });



    }

    private void quickLogin() {
        // TODO Auto-generated method stub
        param = new RequestParams();
        param.put("usid", usid);
        param.put("oauth_type", oauth_type);
        param.put("avatar", avatar);
        param.put("username",username);
        param.put("gender",gender);
        String uriString = path+"api/user/signinoauth";
        RequstClient.post(uriString, param, new LoadCacheResponseLoginouthandler(
                User_Login.this,
                new LoadDatahandler() {

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
                        Log.e("sss", data);
                        try {
                            JSONObject jsonObject1 = new JSONObject(data);
                            if (jsonObject1.getString("is").equals("1")) {
                                String tid = jsonObject1.getString("tid");
                                String subject = jsonObject1.getString("subject");
                                JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("userinfo"));
                                String uidString = jsonObject2.getString("email");
                                String usernameString = jsonObject2.getString("username");
                                String tokenString = jsonObject2.getString("token");

                                mshared = getSharedPreferences("usermessage", 0);
                                editor = mshared.edit();
                                editor.putString("email", uidString);
                                editor.putString("username", usernameString);
                                editor.putString("token", tokenString);
                                editor.putString("tid",tid);
                                editor.putString("subject",subject);
                                editor.commit();
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
                        // 获取一个Message对象，设置what为1
                        Message msg = Message.obtain();
                        msg.obj = message;
                        msg.what = Constant.MSG_FALTH;
                        // 发送这个消息到消息队列中
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                    }

                }
        ));
    }
    private void init(){
        sinaquicklogin = (RelativeLayout)findViewById(R.id.sinaquicklogin);
        qqquicklogin = (RelativeLayout)findViewById(R.id.qqquicklogin);
        login_name = (BootstrapEditText)findViewById(R.id.user_login_name);
        login_password = (BootstrapEditText)findViewById(R.id.user_login_password);
        login_button = (BootstrapButton)findViewById(R.id.user_login_button);
        login_button.setOnClickListener(this);
        this.rightBarButton.setVisibility(View.GONE);
        this.rightBarTextView.setVisibility(View.VISIBLE);
        navigationBar.setBackgroundColor(Color.rgb(107, 233, 242));
        navigationBar.getBackground().setAlpha(255);
        LinearLayout rightLinearLayout = (LinearLayout)findViewById(R.id.right_linear_nav);
        rightLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushToNextActivity(User_register.class);

            }
        });
    }

    @Override
    public void leftBarButtonItemEvent() {
        // TODO Auto-generated method stub
        User_Login.this.finish();
        overridePendingTransition(0, R.anim.bottom_out);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            User_Login.this.finish();
            overridePendingTransition(0, R.anim.bottom_out);

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        if(view == login_button)
        {
            nameString = login_name.getText().toString();
            passwordString = login_password.getText().toString();
            if(nameString.replaceAll(" ", "").equals(""))
            {
                DialogMethod.dialogShow(User_Login.this, "请输入用户名!");
            }
            else if (passwordString.replaceAll(" ", "").equals("")) {
                DialogMethod.dialogShow(User_Login.this,"请输入密码!");
            }
            else {
                DialogMethod.startProgressDialog(User_Login.this,"正在登录");
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

    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        // TODO Auto-generated method stub
        String tid = jsonObject.getString("tid");
        String subject = jsonObject.getString("subject");
        JSONObject jsonObject2 = new JSONObject(jsonObject.getString("userinfo"));
        String uidString = jsonObject2.getString("email");
        String usernameString = jsonObject2.getString("username");
        String tokenString = jsonObject2.getString("token");

        mshared = getSharedPreferences("usermessage", 0);
        editor = mshared.edit();
        editor.putString("email", uidString);
        editor.putString("username", usernameString);
        editor.putString("token", tokenString);
        editor.putString("tid",tid);
        editor.putString("subject",subject);
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
        msg.what = Constant.MSG_FALTH;
        // 发送这个消息到消息队列中
        handler.sendMessage(msg);
    }
    @Override
    public void handleNetworkDataStart() throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataStart();

    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // TODO Auto-generated method stub
//
//        super.onActivityResult(requestCode, resultCode, data);
//        /**使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
//        if(ssoHandler != null){
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//    }
}
