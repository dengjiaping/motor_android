package com.moto.inform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.moto.constant.Constant;
import com.moto.listview.MyListView;
import com.moto.live.LiveKidsResponse;
import com.moto.live.Live_kidsAllResponse;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.InformNetworkModel;
import com.moto.utils.StringUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by chen on 2014/5/5.
 */
public class InformTouchmeActivity extends Moto_RootActivity {

    private MyListView touchmeListView;// listView
    private TouchmeAdapter touchmeAdapter;
    private LinkedList<HashMap<String,String>> list = new LinkedList<HashMap<String,String>>();
    private HashMap<String,String> map;
    private Handler handler;
    private LinkedList<LinkedList<String>> carList = new LinkedList<LinkedList<String>>();
    private LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList = new LinkedList<LinkedList<HashMap<String,Integer>>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_touchme);

        init();
        GetAsyData();

        touchmeAdapter = new TouchmeAdapter(this, list);
        touchmeListView.setAdapter(touchmeAdapter);// 先设置空对象，要么从数据库中读出

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        touchmeAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }

        };
        touchmeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                String fid = list.get(position).get("fid");
                String tid = list.get(position).get("tid");
                String pid = list.get(position).get("pid");
                String cid = list.get(position).get("cid");
                if(fid.equals("1") && !tid.equals("null") && pid.equals("null") && !cid.equals("null"))
                {
                    intent = new Intent();
                    intent.putExtra("tid", tid);
                    intent.setClass(InformTouchmeActivity.this, Live_kidsAllResponse.class);
                    startActivityForResult(intent, 304);
                }
                else if(fid.equals("1") && !tid.equals("null") && !pid.equals("null") && !cid.equals("null"))
                {
                    intent.putExtra("pid", list.get(position).get("pid").toString());
                    intent.putExtra("subject", list.get(position).get("subject").toString());
                    intent.putExtra("dateline",list.get(position).get("dateline").toString());
                    int num = carList.get(position).size();
                    if(num == 0)
                    {
                        intent.putExtra("photoname", "null");
                    }
                    else {
                        intent.putExtra("photoname", carList.get(position).get(num - 1));
                    }
                    intent.setClass(InformTouchmeActivity.this, LiveKidsResponse.class);
                    startActivityForResult(intent, 304);
                }
            }
        });
    }

    private void init()
    {
        touchmeListView = (MyListView)findViewById(R.id.inform_touchme_listview);
    }

    private void GetAsyData()
    {
        InformNetworkModel informNetworkModel = new InformNetworkModel(InformTouchmeActivity.this,InformTouchmeActivity.this);
        informNetworkModel.readAt();
    }
    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        String touchme_details = jsonObject
                .getString("at_list");
        JSONArray touchmearray = new JSONArray(touchme_details);
        int c = touchmearray.length();
        for (int i = 0; i < c; i++) {
            JSONObject jsonObject2 = (JSONObject) touchmearray.get(i);
            list.add(GetTouchmeMap(jsonObject2));
        }
        handler.obtainMessage(Constant.MSG_SUCCESS)
                .sendToTarget();
    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {
        super.handleNetworkDataGetFail(message);
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        super.handleNetworkDataStart();
    }
    private HashMap<String, String> GetTouchmeMap(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        map = new HashMap<String, String>();
        try {
            String author = jsonObject.getString("author");
            String at_message = jsonObject.getString("at_message");
            String fid = jsonObject.getString("fid");
            String tid = jsonObject.getString("tid");
            String pid = jsonObject.getString("pid");
            String cid = jsonObject.getString("cid");
            String dateline = jsonObject.getString("dateline");
            String avatar = jsonObject.getString("avatar");
            String subject = jsonObject.getString("subject");
            String isread = jsonObject.getString("isread");
            map.put("author", author);
            map.put("at_message", at_message);
            map.put("tid", tid);
            map.put("fid", fid);
            map.put("pid", pid);
            map.put("cid", cid);
            map.put("dateline", dateline);
            map.put("avatar", avatar);
            map.put("subject", subject);
            map.put("isread", isread);

            carList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
            WidthHeightList.add(StringUtils.hashToWidthHeightArray(jsonObject.getString("photoinfo")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
}
