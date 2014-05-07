package com.moto.user;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.SignNetWorkModel;
import com.moto.myactivity.MyActivity;
import com.moto.validation.Validation;

public class User_register extends Moto_RootActivity implements OnClickListener{
	
	private ImageView regiter_return;
	private BootstrapEditText user_name;
	private BootstrapEditText user_email;
	private BootstrapEditText user_password;
	private BootstrapButton register_Button;
	private String nameString;
	private String emailString;
	private String passwordString;
	private String uriString = "http://damp-reef-9073.herokuapp.com/api/user/signin";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		regiter_return = (ImageView)findViewById(R.id.user_register_return);
		user_email = (BootstrapEditText)findViewById(R.id.user_register_email);
		user_name = (BootstrapEditText)findViewById(R.id.user_register_name);
		user_password = (BootstrapEditText)findViewById(R.id.user_register_password);
		register_Button = (BootstrapButton)findViewById(R.id.user_register_button);
		register_Button.setOnClickListener(this);
		regiter_return.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == regiter_return)
		{
			User_register.this.finish();
		}
		if(v == register_Button)
		{
			nameString = user_name.getText().toString();
			emailString = user_email.getText().toString();
			passwordString = user_password.getText().toString();
			if(nameString.replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(User_register.this,"请输入用户名!");
			}
			else if(passwordString.replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(User_register.this,"请输入密码!");
			}
			else if(emailString.replaceAll(" ", "").equals(""))
			{
				DialogMethod.dialogShow(User_register.this,"请输入邮箱!");
			}
			else if(Validation.nameContentCheck(nameString))
			{
				DialogMethod.dialogShow(User_register.this,"用户名仅支持英文,数字和下划线!");
			}
			else if(Validation.nameLengthCheck(nameString))
			{
				DialogMethod.dialogShow(User_register.this,"用户名长度大于2小于11");
			}
			else if(Validation.passwordContentCheck(passwordString))
			{
				DialogMethod.dialogShow(User_register.this,"密码支持英文,数字和符号!");
			}
			else if(Validation.passwordLenghtCheck(passwordString))
			{
				DialogMethod.dialogShow(User_register.this,"密码长度大于5小于16");
			}
			else if(Validation.checkEmail(emailString))
			{
				DialogMethod.dialogShow(User_register.this,"请检查邮箱的正确性!");
			}
			else {
                new SignNetWorkModel(User_register.this,User_register.this).signIn(nameString, passwordString, emailString);
			}
		}
	}

    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        Log.d("log22",jsonObject.getString("is"));
        Toast.makeText(User_register.this, "注册成功", Toast.LENGTH_SHORT).show();
        Log.d("log",jsonObject.getString("is"));
        DialogMethod.stopProgressDialog();
        Log.d("log1",jsonObject.getString("is"));
        new SignNetWorkModel(User_register.this,User_register.this ).registerBpush(User_register.this);
        User_register.this.finish();
    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        Toast.makeText(User_register.this, "注册失败", Toast.LENGTH_SHORT).show();
    }

    //	private void GetAsyData() {
//		// TODO Auto-generated method stub
//		param = new RequestParams();
//		param.put("email", emailString);
//		param.put("username", nameString);
//		param.put("password", passwordString);
//
//		RequstClient.post(uriString, param, new LoadCacheResponseLoginouthandler(
//				User_register.this,
//				new LoadDatahandler(){
//
//			@Override
//			public void onStart() {
//				// TODO Auto-generated method stub
//				super.onStart();
//			}
//
//			@Override
//			public void onLoadCaches(String data) {
//				// TODO Auto-generated method stub
//				super.onLoadCaches(data);
//			}
//
//			@Override
//			public void onSuccess(String data) {
//				// TODO Auto-generated method stub
//				super.onSuccess(data);
//				try {
//                    Log.d("log22",data.toString());
//					JSONObject jsonObject = new JSONObject(data);
////					if (jsonObject.getInt("is") == 1) {
//                        Log.d("log22",jsonObject.getString("is"));
//                        Toast.makeText(User_register.this, "注册成功", Toast.LENGTH_SHORT).show();
//                        Log.d("log",jsonObject.getString("is"));
//                        DialogMethod.stopProgressDialog();
//                        Log.d("log1",jsonObject.getString("is"));
//                        User_register.this.finish();
//					}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//
//			}
//
//			@Override
//			public void onFailure(String error, String message) {
//				// TODO Auto-generated method stub
//				super.onFailure(error, message);
//				DialogMethod.dialogShow(User_register.this,message);
//			}
//
//			@Override
//			public void onFinish() {
//				// TODO Auto-generated method stub
//				super.onFinish();
//			}
//
//		}));
//	}
}
