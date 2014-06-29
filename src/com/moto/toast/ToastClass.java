package com.moto.toast;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moto.main.R;

public class ToastClass {
    private static SharedPreferences TokenShared;
    private static String tokenString;
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

    public static boolean IsHaveToken(Activity activity)
    {
        TokenShared = activity.getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("token", "");
        if(tokenString.equals(""))
        {
            return false;

        }
        return true;
    }

    public static String GetTokenString(Activity activity)
    {
        TokenShared = activity.getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("token", "");
        if(tokenString.equals(""))
        {
            return "";

        }
        return tokenString;
    }
}
