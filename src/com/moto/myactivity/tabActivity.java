package com.moto.myactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.loopj.android.http.RequestParams;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class tabActivity extends Activity implements NetWorkModelListener{
	protected Intent intent;
	protected String path_google = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
	protected String mypath = "http://www.h3oteam.pw/moto/api/app/api.php";
    //	protected String mypath = "http://192.168.137.1/moto/api/app/api.php";
	protected ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
	protected String imgPath = "http://motor.qiniudn.com/";
	protected String defaultPath = "http://www.h3oteam.pw/moto/uc_server/images/noavatar_middle.gif";
	protected RequestParams param;
	protected String actionUrl = "http://up.qiniu.com/";
    protected String path = "http://motor-env-e94pufmw8k.elasticbeanstalk.com/";
	public LinearLayout navigationBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initNavigationBar();
	}
	
	public void addContentView(int resource) {
		LayoutInflater  inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams layoutParams  = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View MenuLyaout = inflater.inflate(resource, null);
		addContentView(MenuLyaout, layoutParams);
		this.navigationBar.bringToFront();
	}
	
	private void initNavigationBar() {
		navigationBar = (LinearLayout)findViewById(R.id.navigationBar);
	}
    
	// 把Bitmap转换成Base64
    protected String getBitmapStrBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, 0);
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
	
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			if (event.getRepeatCount() == 0) {
//				AppManager.getInstance().exit();
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    
	@Override
	public void handleNetworkDataWithSuccess(JSONObject JSONObject)
    throws JSONException {
		// TODO Auto-generated method stub
		
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
		
	}
    
	@Override
	public void handleNetworkDataStart() throws JSONException {
		// TODO Auto-generated method stub
		
	}
}
