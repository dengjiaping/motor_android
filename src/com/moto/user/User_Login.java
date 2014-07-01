package com.moto.user;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.SignNetWorkModel;
import com.moto.toast.ToastClass;
import com.moto.validation.Validation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by chen on 14-7-1.
 */
public class User_Login extends Moto_RootActivity implements View.OnClickListener{
    private BootstrapEditText login_name;
    private BootstrapEditText login_password;
    private BootstrapButton login_button;
    private String nameString;
    private String passwordString;
    private RequestParams param;
    private String emailString = "";
    private SharedPreferences.Editor editor;
    private SharedPreferences mshared;
    private Handler handler;
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
    }
    private void init(){
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
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            User_Login.this.finish();
            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);

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
        JSONObject jsonObject2 = new JSONObject(jsonObject.getString("userinfo"));
        String uidString = jsonObject2.getString("email");
        String usernameString = jsonObject2.getString("username");
        String tokenString = jsonObject2.getString("token");
        mshared = getSharedPreferences("usermessage", 0);
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
