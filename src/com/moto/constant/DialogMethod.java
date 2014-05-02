package com.moto.constant;

import android.app.Activity;
import android.content.DialogInterface;

import com.moto.mydialog.CustomProgressDialog;
import com.moto.mydialog.errorDialog;

public class DialogMethod {
	public static CustomProgressDialog progressDialog = null;
	public static void dialogShow(Activity activity, String message){
		final errorDialog.Builder builder = new errorDialog.Builder(activity);
        //		builder.setTitle("错误提示");
		builder.setMessage(message);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
		
	}
	
	public static void startProgressDialog(Activity activity,String message){
		
		if (progressDialog == null){
			progressDialog = CustomProgressDialog.createDialog(activity);
	    	progressDialog.setMessage(message);
		}
		
    	progressDialog.show();
	}
	public static void stopProgressDialog(){
		if (progressDialog != null){
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
