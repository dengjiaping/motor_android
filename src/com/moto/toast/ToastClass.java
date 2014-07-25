package com.moto.toast;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.moto.main.R;

public class ToastClass {
    private static SharedPreferences TokenShared;
    private static String tokenString;
    private static SharedPreferences.Editor editor;
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

    public static String GetTid(Activity activity)
    {
        TokenShared = activity.getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("tid", "");
        if(tokenString.equals(""))
        {
            return "";

        }
        return tokenString;
    }

    public static String GetAuthorName(Activity activity)
    {
        TokenShared = activity.getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("username", "");
        if(tokenString.equals(""))
        {
            return "";

        }
        return tokenString;
    }

    public static void setUserType(Context context,String type)
    {
        TokenShared = context.getSharedPreferences("usermessage", 0);
        editor = TokenShared.edit();
        editor.putString("type", type);

        editor.commit();
    }

    public static String GetUserType(Activity activity)
    {
        TokenShared = activity.getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("type", "");
        if(tokenString.equals(""))
        {
            return "";

        }
        return tokenString;
    }
}
