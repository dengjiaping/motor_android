package com.moto.user;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;

import com.moto.main.R;
import com.moto.myactivity.MyActivity;

public class User_feedbackSetting extends MyActivity implements OnClickListener{
	private ImageView feedback_return;
	private ImageView user_setting_feedback_send;
	private EditText user_setting_feedback_massage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_setting_feedback);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub
		feedback_return = (ImageView)findViewById(R.id.user_setting_feedback_return);
		user_setting_feedback_send = (ImageView)findViewById(R.id.user_setting_feedback_send);
		user_setting_feedback_massage = (EditText)findViewById(R.id.user_setting_feedback_massage);
		user_setting_feedback_send.setOnClickListener(this);
		feedback_return.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == feedback_return)
		{
			User_feedbackSetting.this.finish();
		}
		else if(v == user_setting_feedback_send)
		{
			Intent email = new Intent(android.content.Intent.ACTION_SEND);
            email.setType("plain/text");
            String[] emailReciver = new String[]{"924010725@qq.com","1767999191@qq.com"};
            Log.e("aaaa", "sads");
            String  emailSubject = "意见反馈";
            String emailBody = user_setting_feedback_massage.getText().toString();
            //设置邮件默认地址
            email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
            //设置邮件默认标题
            email.putExtra(android.content.Intent.EXTRA_SUBJECT, emailSubject);
            //设置要默认发送的内容
            email.putExtra(android.content.Intent.EXTRA_TEXT, emailBody);
            //调用系统的邮件系统
            startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
		}
	}
	
}
