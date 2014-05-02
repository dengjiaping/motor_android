package com.moto.welcome;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.moto.live.LiveActivity;
import com.moto.model.NetWorkModelListener;
import com.moto.model.SignNetWorkModel;

public class MyPushMessageReceiver extends FrontiaPushMessageReceiver {
	/** TAG to Log */
//	public static final String TAG = MyPushMessageReceiver.class.getSimpleName();
	public static EventHandler eventHandler;
	

	public static abstract interface EventHandler {
		public abstract void onMessage(String message, String customContentString);

		public abstract void onBind(String method, int errorCode, String content);

		public abstract void onNotify(String title, String content);

		public abstract void onNetChange(boolean isNetConnected);
	}
	/**
	 * ������PushManager.startWork������sdk������push server���������������������������������������������������������������������������onBind���������
	 * ���������������������������������������������������������channel id���user id���������������server���������������server���������channel id���user id������������������������������������
	 * 
	 * @param context
	 *          BroadcastReceiver���������Context
	 * @param errorCode
     *          ������������������������0 - ������
     * @param appid 
     *          ������id���errorCode���0������null
	 * @param userId
	 *          ������user id���errorCode���0������null
	 * @param channelId
	 *          ������channel id���errorCode���0������null
	 * @param requestId
	 *          ���������������������������id������������������������������
	 * @return
	 *     none
	 */
	@Override
	public void onBind(Context context, int errorCode, String appid, 
				String userId, String channelId, String requestId) {
		String responseString = "onBind errorCode=" + errorCode + " appid="
				+ appid + " userId=" + userId + " channelId=" + channelId
				+ " requestId=" + requestId;
		SharedPreferences  mshared;
		mshared = context.getSharedPreferences("usermessage", 0);
		Editor editor = mshared.edit();
		editor.putString("bpush_id", userId);
		editor.putString("channel_id", channelId);
		editor.commit();
		// ������������������������������flag������������������������������������������������
		if (errorCode == 0) {
			Utils.setBind(context, true);
		}
		// Demo������������������������������������������������������������������������
		updateContent(context, responseString);
		new SignNetWorkModel(new NetWorkModelListener() {
			
			@Override
			public void handleNetworkDataWithUpdate(float progress)
					throws JSONException {
				// TODO Auto-generated method stub
				
			}
			
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
			public void handleNetworkDataGetFail(String message)
					throws JSONException {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void handleNetworkDataStart() throws JSONException {
				// TODO Auto-generated method stub
				
			}
		} ,context).registerBpush(context);
	}

	/**
	 * ������������������������������
	 * 
	 * @param context ���������
	 * @param message ���������������
	 * @param customContentString ���������������,������������json���������
	 */
	@Override
	public void onMessage(Context context, String message, String customContentString) {
		String messageString = "������������ message=\"" + message + "\" customContentString="
				+ customContentString;
		// Demo������������������������������������������������������������������������
		updateContent(context, messageString);
        if (eventHandler != null){
            eventHandler.onMessage(message, customContentString);
        }
	}
	
	/**
	 * ���������������������������������������������������������������������������������������������������������������������
	 * 
	 * @param context ���������
	 * @param title ������������������������
	 * @param description ������������������������
	 * @param customContentString ������������������������������json���������
	 */
	@Override
	public void onNotificationClicked(Context context, String title, 
				String description, String customContentString) {
		String notifyString = "������������ title=\"" + title + "\" description=\""
				+ description + "\" customContent=" + customContentString;
		Log.d(TAG, notifyString);
		
		// ������������������������������mykey���myvalue���������������������������������������������������������
		if (customContentString != null & TextUtils.isEmpty(customContentString)) {
			JSONObject customJson = null;
			try {
				customJson = new JSONObject(customContentString);
				String myvalue = null;
				if (customJson.isNull("mykey")) {
					myvalue = customJson.getString("mykey");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Demo������������������������������������������������������������������������
		updateContent(context, notifyString);
		
		Intent intent = new Intent();
        intent.setClass(context.getApplicationContext(), LiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.getApplicationContext().startActivity(intent);
	}

	/**
	 * setTags() ������������������
	 * 
	 * @param context ���������
	 * @param errorCode ������������0������������tag������������������������0������������tag���������������������
	 * @param successTags ���������������tag
	 * @param failTags ���������������tag
	 * @param requestId ���������������������������������id
	 */
	@Override
	public void onSetTags(Context context, int errorCode, 
				List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onSetTags errorCode=" + errorCode + " sucessTags="
				+ sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		Log.d(TAG, responseString);
		
		// Demo������������������������������������������������������������������������
		updateContent(context, responseString);
	}

	/**
	 * delTags() ������������������
	 * 
	 * @param context ���������
	 * @param errorCode ������������0������������tag������������������������0������������tag������������������
	 * @param successTags ���������������tag
	 * @param failTags ���������������tag
	 * @param requestId ���������������������������������id
	 */
	@Override
	public void onDelTags(Context context, int errorCode, 
				List<String> sucessTags, List<String> failTags, String requestId) {
		String responseString = "onDelTags errorCode=" + errorCode + " sucessTags="
				+ sucessTags + " failTags=" + failTags + " requestId="
				+ requestId;
		Log.d(TAG, responseString);
		
		// Demo������������������������������������������������������������������������
		updateContent(context, responseString);
	}

	/**
	 * listTags() ������������������
	 * 
	 * @param context ���������
	 * @param errorCode  ������������0������������tag������������0���������������
	 * @param tags ���������������������������tag���
	 * @param requestId ���������������������������������id
	 */
	@Override
	public void onListTags(Context context, int errorCode, 
				List<String> tags, String requestId) {
		String responseString = "onListTags errorCode=" + errorCode + " tags=" + tags;
		Log.d(TAG, responseString);
		
		// Demo������������������������������������������������������������������������
		updateContent(context, responseString);
	}

	/**
	 * PushManager.stopWork() ������������������
	 * 
	 * @param context ���������
	 * @param errorCode ������������0���������������������������������������0���������������
	 * @param requestId ���������������������������������id
	 */
	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		String responseString = "onUnbind errorCode=" + errorCode
				+ " requestId = " + requestId;
		Log.d(TAG, responseString);
		
		// ���������������������������������flag���
		if (errorCode == 0) {
			Utils.setBind(context, false);
		}
		// Demo������������������������������������������������������������������������
		updateContent(context, responseString);
	}
	
	private void updateContent(Context context, String content) {
//		Log.d(TAG, "updateContent");
//		String logText = "" + Utils.logStringCache;
//		
//		if (!logText.equals("")) {
//			logText += "\n";
//		}
//		
//		SimpleDateFormat sDateFormat = new SimpleDateFormat("HH-mm-ss");
//		logText += sDateFormat.format(new Date()) + ": ";
//		logText += content;
//		
//		Utils.logStringCache = logText;
//		Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.ic_);
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
//                context).setSmallIcon(R.drawable.ic_lock_idle_alarm)
//                .setContentTitle("title")
//                .setContentText(content);
//        mBuilder.setTicker(content);//���������������������������������������������������
//        mBuilder.setNumber(12);
//        mBuilder.setLargeIcon(btm);
//        mBuilder.setAutoCancel(true);//���������������������������
//        
//        //������������Intent
//        Intent resultIntent = new Intent(context,
//                LiveActivity.class);
//        //������������Intent
//        PendingIntent resultPendingIntent = PendingIntent.getActivity(
//                context, 0, resultIntent,
//                PendingIntent.FLAG_UPDATE_CURRENT);
//        // ���������������������������
//        mBuilder.setContentIntent(resultPendingIntent);
//        //���������������������������
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, mBuilder.build());
	}

}
