package com.moto.toast;

import com.moto.main.R;

import android.app.Activity;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastClass {
	public static void SetToast(Activity activity, String string)
	{
		Toast.makeText(activity, string, Toast.LENGTH_SHORT).show();
	}
	public static void SetImageToast(Activity activity,String message)
	{
		Toast toast = Toast.makeText(activity,
                                     message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastView = (LinearLayout) toast.getView();
        ImageView imageCodeProject = new ImageView(activity);
        imageCodeProject.setImageResource(R.drawable.affirm);
        toastView.addView(imageCodeProject, 0);
        toast.show();
        
	}
}
