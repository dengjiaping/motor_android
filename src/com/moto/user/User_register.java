package com.moto.user;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.SignNetWorkModel;
import com.moto.validation.Validation;

import org.json.JSONException;
import org.json.JSONObject;

public class User_register extends Moto_RootActivity implements OnClickListener{
	
	private ImageView regiter_return;
	private BootstrapEditText user_name;
	private BootstrapEditText user_email;
	private BootstrapEditText user_password;
	private BootstrapButton register_Button;
    private TextView register_policy;
    private CheckBox checkBox;
	private String nameString;
	private String emailString;
	private String passwordString;
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
        register_policy = (TextView)findViewById(R.id.user_register_policy);
        checkBox = (CheckBox)findViewById(R.id.user_register_checkbox);
		register_Button.setOnClickListener(this);
		regiter_return.setOnClickListener(this);
        register_policy.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == regiter_return)
		{
			User_register.this.finish();
		}

        if(v == register_policy)
        {
            pushToNextActivity(TermActivity.class);
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
				DialogMethod.dialogShow(User_register.this, "请检查邮箱的正确性!");
			}
            else if(!checkBox.isChecked())
            {
                DialogMethod.dialogShow(User_register.this, "请阅读并同意机车党使用协议");
            }
			else {

                new SignNetWorkModel(User_register.this,User_register.this).signIn(nameString, passwordString, emailString);
			}
		}
	}

    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        Toast.makeText(User_register.this, "注册成功", Toast.LENGTH_SHORT).show();
        DialogMethod.stopProgressDialog();
        new SignNetWorkModel(User_register.this,User_register.this ).registerBpush(User_register.this);
        User_register.this.finish();
    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        Toast.makeText(User_register.this, "注册失败", Toast.LENGTH_SHORT).show();
        DialogMethod.stopProgressDialog();
    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {
        super.handleNetworkDataGetFail(message);
        Toast.makeText(User_register.this, message, Toast.LENGTH_SHORT).show();
        DialogMethod.stopProgressDialog();
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        super.handleNetworkDataStart();
        DialogMethod.startProgressDialog(User_register.this,"注册中...");
    }
}
