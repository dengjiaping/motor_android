package com.moto.myactivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.apache.http.NameValuePair;
import com.loopj.android.http.RequestParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

public class MyActivity extends Activity{
	protected Intent intent;
	protected String path_google = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	protected String mypath = "http://www.h3oteam.pw/moto/api/app/api.php";
    //	protected String mypath = "http://192.168.137.1/moto/api/app/api.php";
	protected ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	protected String imgPath = "http://motor.qiniudn.com/";
	protected String defaultPath = "http://www.h3oteam.pw/moto/uc_server/images/noavatar_middle.gif";
	protected RequestParams param;
	public static String uptoken = "7mGeCoNe_ecBsW210i5a0VDu4Yz8nZ5Ph6xUlV2E:ICehYqwmdzj4bJ5D8Ia8SyjA4to=:ewogICJkZWFkbGluZSIgOiAxNzA5MjA3MTcxLAogICJzY29wZSIgOiAibW90b3IiCn0=";
	protected String actionUrl = "http://up.qiniu.com/";
    
    // 将Bitmap转换成InputStream
    protected InputStream BitmapToInputStream(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        return is;
    }
    
    /**
	 * 关闭输入法
	 */
	protected void manageInput(Activity activity)
	{
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive())
		{
			InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            
            
			inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                                                       
                                                       
                                                       InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 打开输入法
	 */
	protected void manageopen()
	{
		LinearLayout layout = null;
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		// 接受软键盘输入的编辑文本或其它视图
		imm.showSoftInput(layout,InputMethodManager.SHOW_FORCED);
	}
	
	protected Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        int be = 1;
        if (w > h && w > ww) {
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率
        
        newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //      return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
        //其实是无效的,大家尽管尝试  
        return bitmap;  
    }  
}
