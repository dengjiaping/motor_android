package com.moto.welcome;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.UpdateNetworkModel;
import com.moto.mydialog.errorDialog;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.WindowManager;
public class UpdateServise extends Service implements NetWorkModelListener{
    private int len;  
    private NotificationManager manager;  
    private Notification notif;
    private long time = 0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		new UpdateNetworkModel(this, this);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		
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
	}
	
	private void mydialog(final String uri)
	{
		final errorDialog.Builder builder = new errorDialog.Builder(UpdateServise.this);
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

	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
			throws JSONException {
		// TODO Auto-generated method stub
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
	
	private void downloadApk(String uri)
	
	{
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notif = new Notification();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("机车党")
                .setSmallIcon(R.drawable.icon_main)
                .setTicker("正在下载...");
//        notif.icon = R.drawable.icon_main;
//        notif.tickerText = "正在下载...";
        //通知栏显示所用到的布局文件  
//        notif.contentView = new RemoteViews(getPackageName(), R.layout.update_notification_progress);
        builder.setProgress(100, 0, false);
        manager.notify(0, builder.build());
		AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.get(uri, new AsyncHttpResponseHandler(){

			@Override
			public void onProgress(int bytesWritten, int totalSize) {
				// TODO Auto-generated method stub
                Log.e("bytesWritten",bytesWritten+"");
				super.onProgress(bytesWritten, totalSize);
				len = bytesWritten * 100 / totalSize;
				if(bytesWritten < totalSize && (System.currentTimeMillis() - time)>1000)
				{
	                time = System.currentTimeMillis();
//	                notif.contentView.setTextViewText(R.id.content_view_text1, len+"%");
//	                notif.contentView.setProgressBar(R.id.content_view_progress, 100, len, false);
                    builder.setProgress(100,len,false);
	                manager.notify(0, builder.build());
				}
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1, arg2);
//				notif.contentView.setTextViewText(R.id.content_view_text1, 100+"%");
//              notif.contentView.setProgressBar(R.id.content_view_progress, 100, 100, false);
                // When the loop is finished, updates the notification
                builder.setContentText("下载完成")
                        // Removes the progress bar
                        .setProgress(0,0,false);
                manager.notify(0, builder.build());
//              manager.notify(0, notif);
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

	@Override
	public void handleNetworkDataGetFail(String message) throws JSONException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
