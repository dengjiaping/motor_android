package com.moto.live;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.loopj.android.http.RequestParams;
import com.moto.main.R;
import com.moto.model.DataBaseModel;
import com.moto.model.LiveNetworkModel;
import com.moto.model.NetWorkModelListener;
import com.moto.qiniu.img.Image;
import com.moto.utils.CompressUtils;
import com.moto.welcome.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by chen on 14-7-8.
 */
public class SendLiveService extends Service implements NetWorkModelListener{
    private List<DataBaseModel> list = new ArrayList<DataBaseModel>();
    private ArrayList<String> dataList = new ArrayList<String>();
    protected LinkedList<String> compressList = new LinkedList<String>();
    protected ArrayList<Image> lovecarImage = new ArrayList<Image>();
    private SharedPreferences mshared;
    private SharedPreferences.Editor editor;
    private NotificationManager manager;
    private int num = 0;
    @Override
    public void onCreate() {
        super.onCreate();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notif = new Notification();
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("机车党")
                .setSmallIcon(R.drawable.icon_main)
                .setTicker("正在发送...");
//        notif.icon = R.drawable.icon_main;
//        notif.tickerText = "正在下载...";
        //通知栏显示所用到的布局文件
//        notif.contentView = new RemoteViews(getPackageName(), R.layout.update_notification_progress);
        builder.setProgress(100, 0, false);
        manager.notify(0, builder.build());
        list = new Select().from(DataBaseModel.class).execute();
        Log.e("sdsdsd",list.size()+"");
        if(Utils.isNetworkAvailable(this) && list.size() > 0)
        {
            sendAsyData();
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {

    }

    @Override
    public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {
        CompressUtils.deleteLinkFile(compressList);
        dataList.clear();
        compressList.clear();
        lovecarImage.clear();
        //删除数据库里面的数据
        new Delete().from(DataBaseModel.class).where("id = ?", list.get(num).getId()).execute();
        DataBaseModel a;
        num++;
        Log.e("hhhhh",num+"");
        mshared = getSharedPreferences("usermessage", 0);
        editor = mshared.edit();
        editor.putString("tid","1");
        editor.commit();
        if(Utils.isNetworkAvailable(this) && num < list.size())
        {
            sendAsyData();
        }

    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {

    }

    @Override
    public void handleNetworkDataWithUpdate(float progress) throws JSONException {

    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {

    }

    private void sendAsyData(){
        LiveNetworkModel liveNetworkModel = new LiveNetworkModel(this, this);
        DataBaseModel dataBaseModel = list.get(num);
        Log.e("ghghghg",num+"");
        RequestParams param = new RequestParams();
        param.put("token", dataBaseModel.token);
        param.put("subject", dataBaseModel.subject);
        param.put("message", dataBaseModel.message);
        param.put("location", dataBaseModel.location);
        param.put("longitude", dataBaseModel.longitude);
        param.put("latitude", dataBaseModel.latitude);
        param.put("locationsign", dataBaseModel.locationsign);
        if(dataBaseModel.IsHaveUserName)
        {
            param.put("atuser",dataBaseModel.atuser);
        }
        if(dataBaseModel.isHavePhotoarray)
        {
            try {
                JSONArray array = new JSONArray(dataBaseModel.arrayimagepath);
                int length = array.length();
                for(int i = 0; i < length; i++)
                {
                    dataList.add(array.get(i).toString());
                }

                int arrayLength = dataList.size();
                compressList.clear();
                for (int i = 0; i < arrayLength; i++) {
                    String compressPath = CompressUtils.GetCompressPath(dataList.get(i), 480);
                    lovecarImage.add(new Image(compressPath,
                            "file"));

                    compressList.add(compressPath);
                }
            }catch (Exception e)
            {}

            liveNetworkModel.writelive(param, lovecarImage,"photo","photoinfo");
        }
        else
        {
            liveNetworkModel.writelive(param);
        }


    }
}
