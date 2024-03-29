package com.moto.logindialog;
import com.moto.constant.DialogMethod;
import com.moto.main.R;
import com.moto.user.UserActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewSwitcher;


public class LoginDialog extends Activity{
	
	private ViewSwitcher mViewSwitcher;
	private ImageButton btn_close;
	private Button btn_login;
	private EditText mAccount;
	private EditText mPwd;
	private AnimationDrawable loadingAnimation;
	private View loginLoading;
	private int curLoginType;
	private InputMethodManager imm;
	
	public final static int LOGIN_OTHER = 0x00;
	public final static int LOGIN_MAIN = 0x01;
	public final static int LOGIN_SETTING = 0x02;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog);
        
        imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        
        curLoginType = getIntent().getIntExtra("LOGINTYPE", LOGIN_OTHER);
        
        mViewSwitcher = (ViewSwitcher)findViewById(R.id.logindialog_view_switcher);       
        loginLoading = (View)findViewById(R.id.login_loading);
        mAccount = (EditText)findViewById(R.id.login_account);
        mPwd = (EditText)findViewById(R.id.login_password);
        
        btn_close = (ImageButton)findViewById(R.id.login_close_button);
        btn_close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LoginDialog.this.finish();
			}
		});        
        
        btn_login = (Button)findViewById(R.id.login_btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//隐藏软键盘
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);  
				
				String account = mAccount.getText().toString();
				String pwd = mPwd.getText().toString();
				//判断输入
				if(account.replaceAll(" ", "").equals(""))
				{
	                DialogMethod.dialogShow(LoginDialog.this,"请输入用户名!");
				}
				else if (pwd.replaceAll(" ", "").equals("")) {
					DialogMethod.dialogShow(LoginDialog.this,"请输入密码!");
				}
				else {
					new AsyncTask<Integer, String, Integer>(){

						@Override
						protected Integer doInBackground(Integer... params) {
							// TODO Auto-generated method stub
							try {
								
//								login_setData();
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
							DialogMethod.startProgressDialog(LoginDialog.this,"正在登录");	
						}

						@Override
						protected void onPostExecute(Integer result) {
							DialogMethod.stopProgressDialog();
//							DialogMethod.dialogShow(User_EditUserMassage.this, "修改失败!");
						}
						
					}.execute();
				}
			
				
		        btn_close.setVisibility(View.GONE);
		        loadingAnimation = (AnimationDrawable)loginLoading.getBackground();
		        loadingAnimation.start();
		        mViewSwitcher.showNext();
		        
//		        login(account, pwd, isRememberMe);
			}
		});

//        //是否显示登录信息
//        AppContext ac = (AppContext)getApplication();
//        User user = ac.getLoginInfo();
//        if(user==null || !user.isRememberMe()) return;
//        if(!StringUtils.isEmpty(user.getAccount())){
//        	mAccount.setText(user.getAccount());
//        	mAccount.selectAll();
//        	chb_rememberMe.setChecked(user.isRememberMe());
//        }
//        if(!StringUtils.isEmpty(user.getPwd())){
//        	mPwd.setText(user.getPwd());
//        }
    }
    
    //登录验证
//    private void login(final String account, final String pwd, final boolean isRememberMe) {
//		final Handler handler = new Handler() {
//			public void handleMessage(Message msg) {
//				if(msg.what == 1){
//					User user = (User)msg.obj;
//					if(user != null){
//						//清空原先cookie
//						ApiClient.cleanCookie();
//						//发送通知广播
//						UIHelper.sendBroadCast(LoginDialog.this, user.getNotice());
//						//提示登陆成功
//						UIHelper.ToastMessage(LoginDialog.this, R.string.msg_login_success);
//						if(curLoginType == LOGIN_MAIN){
//							//跳转--加载用户动态
//							Intent intent = new Intent(LoginDialog.this, Main.class);
//							intent.putExtra("LOGIN", true);
//							startActivity(intent);
//						}else if(curLoginType == LOGIN_SETTING){
//							//跳转--用户设置页面
//							Intent intent = new Intent(LoginDialog.this, Setting.class);
//							intent.putExtra("LOGIN", true);
//							startActivity(intent);
//						}
//						finish();
//					}
//				}else if(msg.what == 0){
//					mViewSwitcher.showPrevious();
//					btn_close.setVisibility(View.VISIBLE);
//					UIHelper.ToastMessage(LoginDialog.this, getString(R.string.msg_login_fail)+msg.obj);
//				}else if(msg.what == -1){
//					mViewSwitcher.showPrevious();
//					btn_close.setVisibility(View.VISIBLE);
//					((AppException)msg.obj).makeToast(LoginDialog.this);
//				}
//			}
//		};
//		new Thread(){
//			public void run() {
//				Message msg =new Message();
//				try {
//					AppContext ac = (AppContext)getApplication(); 
//	                User user = ac.loginVerify(account, pwd);
//	                user.setAccount(account);
//	                user.setPwd(pwd);
//	                user.setRememberMe(isRememberMe);
//	                Result res = user.getValidate();
//	                if(res.OK()){
//	                	ac.saveLoginInfo(user);//保存登录信息
//	                	msg.what = 1;//成功
//	                	msg.obj = user;
//	                }else{
//	                	ac.cleanLoginInfo();//清除登录信息
//	                	msg.what = 0;//失败
//	                	msg.obj = res.getErrorMessage();
//	                }
//	            } catch (AppException e) {
//	            	e.printStackTrace();
//			    	msg.what = -1;
//			    	msg.obj = e;
//	            }
//				handler.sendMessage(msg);
//			}
//		}.start();
//    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	if(keyCode == KeyEvent.KEYCODE_BACK) {
    		this.onDestroy();
    	}
    	return super.onKeyDown(keyCode, event);
    }
}
