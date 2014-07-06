package com.moto.user;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.moto.constant.DialogMethod;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.UpdateNetworkModel;
import com.moto.mydialog.errorDialog;
import com.moto.mymap.MyMapApplication;
import com.moto.switchbutton.SwitchButton;
import com.moto.toast.ToastClass;
import com.moto.utils.SystemSoundPlayer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class User_SystemSetting extends Moto_RootActivity implements OnClickListener,NetWorkModelListener{
//	private RelativeLayout browse_setting;
	private RelativeLayout feedback_setting;
    private RelativeLayout user_system_draftbox;
	private RelativeLayout checkVersion;
	private BootstrapButton user_exit;

    private SwitchButton sound_swicthbutton;

	private SharedPreferences mshared;
	private Editor editor;
	private RelativeLayout clear;
	private RelativeLayout user_edit_ownmassage;
	private HashMap<String, Object> ownMessageMap = new HashMap<String, Object>();
	private ArrayList<String> ownphotoMessage = new ArrayList<String>();
	
	private int len;  
    private NotificationManager manager;  
    private Notification notif;
    private long time = 0;
    private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        addContentView(R.layout.user_system_setting,R.string.system_setting,barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);
		init();
	}
	private void init() {
		// TODO Auto-generated method stub

//		browse_setting = (RelativeLayout)findViewById(R.id.user_browse_setting);
		feedback_setting = (RelativeLayout)findViewById(R.id.user_feedback_setting);
		user_exit = (BootstrapButton )findViewById(R.id.user_setting_exit);
		clear = (RelativeLayout)findViewById(R.id.user_system_setting_clear);
		user_edit_ownmassage = (RelativeLayout)findViewById(R.id.user_own_setting_message);
		feedback_setting.setOnClickListener(this);

		checkVersion = (RelativeLayout)findViewById(R.id.user_system_setting_checkversion);
        user_system_draftbox = (RelativeLayout)findViewById(R.id.user_system_draftbox);
//		browse_setting.setOnClickListener(this);

        sound_swicthbutton = (SwitchButton)findViewById(R.id.sound_swicthbutton);
        SystemSoundPlayer.getInstance(User_SystemSetting.this).setSwitchButtonOnOff(sound_swicthbutton);

		user_exit.setOnClickListener(this);
		clear.setOnClickListener(this);
		user_edit_ownmassage.setOnClickListener(this);
		checkVersion.setOnClickListener(this);
        user_system_draftbox.setOnClickListener(this);
		
		intent = getIntent();
		if(intent.getStringExtra("is").equals("1"))
		{
			ownMessageMap.put("avatar", intent.getStringExtra("avatar"));
			ownMessageMap.put("motophoto", intent.getStringExtra("motophoto"));
			ownMessageMap.put("gender", intent.getStringExtra("gender"));
			ownMessageMap.put("username", intent.getStringExtra("username"));
			ownMessageMap.put("profile", intent.getStringExtra("profile"));
			ownMessageMap.put("mototype", intent.getStringExtra("mototype"));
			ownMessageMap.put("outputvolume", intent.getStringExtra("outputvolume"));
			ownphotoMessage = intent.getStringArrayListExtra("motophoto");
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
        if(v == user_system_draftbox)
        {
            pushToNextActivity(User_SystemSetting_Draftbox.class);
        }
//		if(v == browse_setting)
//		{
//			intent = new Intent();
//			intent.setClass(User_SystemSetting.this, User_browseSetting.class);
//			startActivity(intent);
//		}
		if(v == feedback_setting)
		{
			intent = new Intent();
			intent.setClass(User_SystemSetting.this, User_feedbackSetting.class);
			startActivity(intent);
		}
		if(v == user_exit)
		{
			mshared = getSharedPreferences("usermessage", 0);
			editor = mshared.edit();
			editor.putString("email", "");
			editor.putString("username", "");
			editor.putString("token", "");
			editor.commit();  //设置一下   必须加上这一句
			setResult(301);
			User_SystemSetting.this.finish();
		}
		if(v == clear)
		{
			new AsyncTask<Integer, String, Integer>(){

				@Override
				protected Integer doInBackground(Integer... params) {
					// TODO Auto-generated method stub
					try {
						MyMapApplication.imageLoader.clearDiscCache();
						MyMapApplication.imageLoader.clearMemoryCache();
//						MyMapApplication.imageLoader.destroy();
						Thread.sleep(5 * 1000);
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
					DialogMethod.startProgressDialog(User_SystemSetting.this,"正在清除");	
				}

				@Override
				protected void onPostExecute(Integer result) {
					DialogMethod.stopProgressDialog();
					ToastClass.SetToast(User_SystemSetting.this, "清除成功");
				}
				
			}.execute();
		}
		
		if(v == user_edit_ownmassage)
		{
			intent = new Intent();
			if(ownMessageMap.size() == 0)
			{
				intent.putExtra("is", "0");
			}
			else
			{
				intent.putExtra("is", "1");
				intent.putExtra("avatar", ownMessageMap.get("avatar").toString());
				intent.putStringArrayListExtra("motophoto", ownphotoMessage);
				intent.putExtra("gender", ownMessageMap.get("gender").toString());
				intent.putExtra("username", ownMessageMap.get("username").toString());
				intent.putExtra("profile", ownMessageMap.get("profile").toString());
				intent.putExtra("mototype", ownMessageMap.get("mototype").toString());
				intent.putExtra("outputvolume", ownMessageMap.get("outputvolume").toString());
			}
			
			intent.setClass(User_SystemSetting.this, User_EditUserMassage.class);
			startActivityForResult(intent, 301);
		}
		if(v == checkVersion) {
            new UpdateNetworkModel(User_SystemSetting.this, User_SystemSetting.this);

        }
	}

    @Override
    protected void onStop() {
        super.onStop();
        SystemSoundPlayer.getInstance(User_SystemSetting.this).toggleSoundPlayerOn(sound_swicthbutton.isChecked());
    }

    @Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
			throws JSONException {
		// TODO Auto-generated method stub
		DialogMethod.stopProgressDialog();
		Getcode(JSONObject);
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
		DialogMethod.stopProgressDialog();
	}
    @Override
    public void handleNetworkDataStart() throws JSONException {
        // TODO Auto-generated method stub
        DialogMethod.startProgressDialog(User_SystemSetting.this,"正在检查");

    }
	private void Getcode(JSONObject jsonObject){
		int versionCode = 0;
		int code = 0;
		String uri = "";
		try {
			// TODO Auto-generated method stub
			PackageInfo pinfo = getPackageManager().getPackageInfo(
					"com.moto.main", PackageManager.GET_CONFIGURATIONS);
			versionCode = pinfo.versionCode;
			code = Integer.parseInt(jsonObject.getString("versionCode"));
			uri = jsonObject.getString("link");
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(code > versionCode)
		{
			mydialog(uri);
		}
		else {
			ToastClass.SetToast(User_SystemSetting.this, "已经是最新版本");
		}
	}
	
	private void mydialog(final String uri)
	{
		final errorDialog.Builder builder = new errorDialog.Builder(this);
//		builder.setTitle("错误提示");
		builder.setMessage("版本有更新，是否下载？");
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						downloadApk(uri);
					}
				}).start();
				dialog.dismiss();
				
			}
		});
		builder.setNegativeButton("稍后下载", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		errorDialog dialog = builder.create();//need a <span style="font-family: 'Microsoft YaHei';">AlertDialog</span>  
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//use alert.  
        // dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY);  
        dialog.show();
	}
	
private void downloadApk(String uri)
	
	{ 
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);  
        notif = new Notification();  
        notif.icon = R.drawable.icon_main;  
        notif.tickerText = "正在下载...";  
        //通知栏显示所用到的布局文件  
        notif.contentView = new RemoteViews(getPackageName(), R.layout.update_notification_progress);  
        manager.notify(0, notif);    
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(uri, new AsyncHttpResponseHandler(){

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
				super.onProgress(bytesWritten, totalSize);
				len = bytesWritten * 100 / totalSize;
				if(bytesWritten < totalSize && (System.currentTimeMillis() - time)>1000)
				{
	                time = System.currentTimeMillis();
	                notif.contentView.setTextViewText(R.id.content_view_text1, len+"%");  
	                notif.contentView.setProgressBar(R.id.content_view_progress, 100, len, false);  
	                manager.notify(0, notif);
				}
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1, arg2);
				notif.contentView.setTextViewText(R.id.content_view_text1, 100+"%");  
              notif.contentView.setProgressBar(R.id.content_view_progress, 100, 100, false); 
              manager.notify(0, notif);
              File newFolder = new File(Environment.getExternalStorageDirectory(), "moto");
              if (!newFolder.exists()) {
                  newFolder.mkdir();
              }
              try {
              	File file = new File(newFolder, "Moto" + ".apk");
                  file.createNewFile();
					FileOutputStream fileOutputStream = new FileOutputStream(
							file);
					fileOutputStream.write(arg2); //记得关闭输入流 
					fileOutputStream.close();
				} catch (Exception e) {
					// TODO: handle exception
				} 
              installApk(Environment.getExternalStorageDirectory()+"/moto/Moto.apk");
			}
			
		});

	}
	//安装apk文件
	 private void installApk(String filename)
	 {
	  File file = new File(filename);
	  Intent intent = new Intent();
	  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	  intent.setAction(Intent.ACTION_VIEW);     //浏览网页的Action(动作)
	  String type = "application/vnd.android.package-archive";
	  intent.setDataAndType(Uri.fromFile(file), type);  //设置数据类型
	  startActivity(intent);
	 }


}