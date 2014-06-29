package com.moto.inform;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;

import com.moto.constant.Constant;
import com.moto.listview.SlideCutListView;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.InformNetworkModel;
import com.moto.model.NetWorkModelListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by chen on 2014/5/5.
 */
public class InformPrivateActivity extends Moto_RootActivity {
    private SlideCutListView privateListView;// listView
    private PrivateAdapter privateAdapter;
    private Handler handler;
    private LinkedList<HashMap<String, String>> privateList = new LinkedList<HashMap<String,String>>();
    private HashMap<String, String> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_private);

        init();
        GetAsyData();
        privateAdapter = new PrivateAdapter(this, privateList);
        privateListView.setAdapter(privateAdapter);

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        privateAdapter.notifyDataSetChanged();
                        break;
                }
                super.handleMessage(msg);
            }

        };

        privateListView.setRemoveListener(new SlideCutListView.RemoveListener() {
            @Override
            public void removeItem(int position) {
                SlideCutListView.isSlide = false;
                SlideCutListView.itemView.findViewById(R.id.tv_coating).setVisibility(View.VISIBLE);

                InformNetworkModel informNetworkModel = new InformNetworkModel(new NetWorkModelListener(){
                    @Override
                    public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {

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

                    @Override
                    public void handleNetworkDataStart() throws JSONException {

                    }
                },InformPrivateActivity.this);
                informNetworkModel.deleteconversation(privateList.get(position).get("ucid"));
                privateList.remove(position);
                privateAdapter.notifyDataSetChanged();
            }
        });

        privateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("otherUserName",privateList.get(i).get("username"));
                pushToNextActivity(bundle,Chat_privateActivity.class,304);
            }
        });
    }

    private void init()
    {
        privateListView = (SlideCutListView)findViewById(R.id.inform_private_listview);
    }

    private void GetAsyData()
    {
        InformNetworkModel informNetworkModel = new InformNetworkModel(InformPrivateActivity.this,InformPrivateActivity.this);
        informNetworkModel.readConversation();
    }
    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        String private_details = jsonObject
                .getString("conversation_list");
        JSONArray privatearray = new JSONArray(private_details);
        int b = privatearray.length();
        for (int i = 0; i < b; i++) {
            JSONObject jsonObject2 = (JSONObject) privatearray.get(i);
            privateList.add(GetPrivateMap(jsonObject2));
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

    private HashMap<String, String> GetPrivateMap(JSONObject jsonObject) {
        // TODO Auto-generated method stub
        map = new HashMap<String, String>();
        try {

            JSONObject conversationJson = jsonObject.getJSONObject("conversation");
            String username = jsonObject.getString("username");
            String lastmessage = conversationJson.getString("lastmessage");
            String ucid = conversationJson.getString("ucid");
            String updated_at = conversationJson.getString("updated_at");

            String avatar = jsonObject.getString("avatar");
            map.put("username", username);
            map.put("lastmessage", lastmessage);
            map.put("updated_at", updated_at);
            map.put("ucid", ucid);
            map.put("avatar", avatar);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map;
    }
}
