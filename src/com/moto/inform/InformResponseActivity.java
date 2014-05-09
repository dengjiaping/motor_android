package com.moto.inform;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.moto.constant.Constant;
import com.moto.listview.MyListView;
import com.moto.live.LiveKidsResponse;
import com.moto.live.Live_kidsAllResponse;
import com.moto.main.Moto_MainActivity;
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
public class InformResponseActivity extends Moto_RootActivity {

    private MyListView responseListview;// listView
    private ResponseAdapter responseAdapter;
    private LinkedList<HashMap<String,String>> list = new LinkedList<HashMap<String,String>>();
    private HashMap<String,String> map;
    private Handler handler;
    private LinkedList<LinkedList<String>> carList = new LinkedList<LinkedList<String>>();
    private LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList = new LinkedList<LinkedList<HashMap<String,Integer>>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_response);

        init();
        GetAsyData();

        responseAdapter = new ResponseAdapter(this, list);
        responseListview.setAdapter(responseAdapter);// 先设置空对象，要么从数据库中读出

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        responseAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }

        };
        responseListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    intent.setClass(InformResponseActivity.this, Live_kidsAllResponse.class);
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
                    intent.setClass(InformResponseActivity.this, LiveKidsResponse.class);
                    startActivityForResult(intent, 304);
                }
            }
        });

    }

    private void init()
    {
        responseListview = (MyListView)findViewById(R.id.inform_response_listview);
    }

    private void GetAsyData()
    {
        InformNetworkModel informNetworkModel = new InformNetworkModel(InformResponseActivity.this,InformResponseActivity.this);
        informNetworkModel.readReply();

    }
    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        String response_details = jsonObject
                .getString("reply_list");
        JSONArray responsearray = new JSONArray(response_details);
        int a = responsearray.length();
        for (int i = 0; i < a; i++) {
            JSONObject jsonObject1 = (JSONObject) responsearray.get(i);
            list.add(GetReaponseMap(jsonObject1));
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

    private HashMap<String, String> GetReaponseMap(
            JSONObject jsonObject) {
        // TODO Auto-generated method stub
        map = new HashMap<String, String>();
        try {
            String author = jsonObject.getString("author");
            String reply_message = jsonObject.getString("reply_message");
            String dateline = jsonObject.getString("dateline");
            String tid = jsonObject.getString("tid");
            String avatar = jsonObject.getString("avatar");
            String fid = jsonObject.getString("fid");
            String pid = jsonObject.getString("pid");
            String cid = jsonObject.getString("cid");
            String subject = jsonObject.getString("subject");
            String isread = jsonObject.getString("isread");
            map.put("author", author);
            map.put("fid", fid);
            map.put("reply_message", reply_message);
            map.put("dateline", dateline);
            map.put("pid", pid);
            map.put("tid", tid);
            map.put("cid", cid);
            map.put("avatar", avatar);
            map.put("subject",subject);
            map.put("isread",isread);

            carList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
            WidthHeightList.add(StringUtils.hashToWidthHeightArray(jsonObject.getString("photoinfo")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
}
