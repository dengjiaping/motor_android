package com.moto.inform;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.moto.constant.Constant;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.UserNetworkModel;
import com.moto.toast.ToastClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Inform_Friends extends Moto_RootActivity implements OnClickListener,NetWorkModelListener{
    protected ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
    protected ListView friendsListView;
    protected FriendsAdapter adapter;
    protected ImageView inform_private_friends_return;
    protected Intent intent;
    protected HashMap<String,Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inform_private_friends);
        init();

        //获取好友
        UserNetworkModel userNetworkModel = new UserNetworkModel(new NetWorkModelListener() {
            @Override
            public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {
                list.clear();
                String data = JSONObject.getString("friend_list");
                JSONArray array = new JSONArray(data);
                int num = array.length();
                for(int i = 0; i < num; i++)
                {
                    list.add(GetMap((JSONObject) array.get(i)));
                }
                adapter.notifyDataSetChanged();
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
        },this);
        userNetworkModel.readfriendlist(ToastClass.GetAuthorName(Inform_Friends.this));


        friendsListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                intent = new Intent();
                HashMap <String, Object> map = list.get(position);
                Bundle bundle = new Bundle();
                bundle.putString("otherUserName", (String)map.get("name"));
                intent.putExtras(bundle);
                intent.setClass(Inform_Friends.this, Chat_privateActivity.class);
                startActivity(intent);
            }
        });
    }

    private HashMap<String, Object> GetMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {

            String username = jsonObject.getString("username");
            String avatar = jsonObject.getString("avatar");
            String profile = jsonObject.getString("profile");
            map.put("name", username);
            map.put("avatar", avatar);
            map.put("details", profile);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }
    private void init() {
        inform_private_friends_return = (ImageView)findViewById(R.id.inform_private_friends_return);
        friendsListView = (ListView)findViewById(R.id.inform_private_friends_listview);

        adapter = new FriendsAdapter(this, this, list);
        friendsListView.setAdapter(adapter);
        inform_private_friends_return.setOnClickListener(this);

        com.moto.contact.ClearEditText filter_edit = (com.moto.contact.ClearEditText)findViewById(R.id.filter_edit);
        filter_edit.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                UserNetworkModel userNetworkModel = new UserNetworkModel(Inform_Friends.this, Inform_Friends.this);
                userNetworkModel.searchUser(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v == inform_private_friends_return)
        {
            finish();
        }
    }

    public void handleNetworkDataWithSuccess(JSONObject jsonObject)
            throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("user_list");
        list.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            jsonObject = jsonArray.getJSONObject(i);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("avatar",jsonObject.getString("avatar"));
            map.put("name", jsonObject.getString("username"));
            map.put("details", jsonObject.getString("profile"));
            list.add(map);
        }
        adapter.notifyDataSetChanged();
    }
    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject)
            throws JSONException {

    }
    @Override
    public void handleNetworkDataWithUpdate(float progress)
            throws JSONException {

    }
    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {

    }
    @Override
    public void handleNetworkDataStart() throws JSONException {

    }

}
