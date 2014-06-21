package com.moto.inform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.model.InformNetworkModel;
import com.moto.utils.DateUtils;
import com.moto.welcome.MyPushMessageReceiver;
import com.moto.welcome.MyPushMessageReceiver.EventHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat_privateActivity extends Moto_RootActivity implements EventHandler{
    private ImageView sendimage = null;
    private EditText contentEditText = null;
    private ListView chatListView = null;
    private List<ChatEntity> chatList = null;
    private ChatAdapter chatAdapter = null;
    private ImageView inform_chat_return;

    public String otherUserName;
    public String meName;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        addContentView(R.layout.inform_chat, "聊天",barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_None);
        init();
        chatList = new ArrayList<ChatEntity>(8);
        Intent intent = this.getIntent();
        Bundle extras = getIntent().getExtras();
        otherUserName = extras.getString("otherUserName");
        SharedPreferences sharepreferences = this.getSharedPreferences("usermessage", 0);
        meName = sharepreferences.getString("username", "");

        chatAdapter = new ChatAdapter(this,chatList);
        chatListView.setAdapter(chatAdapter);

        sendimage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().equals("")) {
                    send();
                }else {
                    Toast.makeText(Chat_privateActivity.this, "Content is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inform_chat_return.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new InformNetworkModel(this,this).readPrivateMessage(otherUserName, DateUtils.getUTCCurrentTimestamp());
    }

    private void init() {
        contentEditText = (EditText) this.findViewById(R.id.inform_chat_content);
        sendimage = (ImageView) this.findViewById(R.id.inform_chat_send);
        chatListView = (ListView) this.findViewById(R.id.inform_chat_listview);
        inform_chat_return = (ImageView)findViewById(R.id.inform_chat_return);
    }

    private void send(){
        ChatEntity chatEntity = new ChatEntity();
        String message = contentEditText.getText().toString();
        chatEntity.setContent(message);
        chatEntity.setComeMsg(false);
        chatList.add(chatEntity);
        chatAdapter.notifyDataSetChanged();
        chatListView.setSelection(chatList.size() - 1);
        new InformNetworkModel(this,this).writePrivateMessage(otherUserName, message);
        contentEditText.setText("");
    }



    @Override
    protected void onPause() {
        super.onPause();
        MyPushMessageReceiver.eventHandler = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyPushMessageReceiver.eventHandler = Chat_privateActivity.this;
    }



    private  class ChatAdapter extends BaseAdapter{
        private Context context = null;
        private List<ChatEntity> myList = null;
        private LayoutInflater inflater = null;
        private int COME_MSG = 0;
        private int TO_MSG = 1;

        public ChatAdapter(Context context,List<ChatEntity> chatList){
            this.context = context;
            this.myList = chatList;
            inflater = LayoutInflater.from(this.context);
        }

        @Override
        public int getCount() {
            return myList.size();
        }

        @Override
        public Object getItem(int position) {
            return myList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            // 区别两种view的类型，标注两个不同的变量来分别表示各自的类型
            ChatEntity entity = myList.get(position);
            if (entity.isComeMsg())
            {
                return COME_MSG;
            }else{
                return TO_MSG;
            }
        }

        @Override
        public int getViewTypeCount() {
            // 这个方法默认返回1，如果希望listview的item都是一样的就返回1，我们这里有两种风格，返回2
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ChatHolder chatHolder = null;
            if (convertView == null) {
                chatHolder = new ChatHolder();
                if (myList.get(position).isComeMsg()) {
                    convertView = inflater.inflate(R.layout.inform_chat_from_item, null);
                }else {
                    convertView = inflater.inflate(R.layout.inform_chat_to_item, null);
                }
                chatHolder.timeTextView = (TextView) convertView.findViewById(R.id.inform_chat_time);
                chatHolder.contentTextView = (TextView) convertView.findViewById(R.id.inform_chat_content);
                chatHolder.userImageView = (ImageView) convertView.findViewById(R.id.inform_chat_user_image);
                convertView.setTag(chatHolder);
            }else {
                chatHolder = (ChatHolder)convertView.getTag();
            }

            chatHolder.timeTextView.setText(myList.get(position).getChatTime());
            chatHolder.contentTextView.setText(myList.get(position).getContent());
            chatHolder.userImageView.setImageResource(myList.get(position).getUserImage());

            return convertView;
        }

        private class ChatHolder{
            private TextView timeTextView;
            private ImageView userImageView;
            private TextView contentTextView;
        }

    }



    public void onMessage(String message, String customContentString) {
        // TODO Auto-generated method stub
        new InformNetworkModel(this,this).readPrivateMessage(otherUserName, DateUtils.getUTCCurrentTimestamp());
    }

    public void onBind(String method, int errorCode, String content) {
        // TODO Auto-generated method stub

    }

    public void onNotify(String title, String content) {
        // TODO Auto-generated method stub

    }

    public void onNetChange(boolean isNetConnected) {
        // TODO Auto-generated method stub

    }


    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("pm");

        String firstListTimestamp = chatList.size() > 0 ? chatList.get(0).getUtcTimeStamp():"1990-12-12T12:12:12.123Z";
        String lastListTimestamp  = chatList.size() > 0 ? chatList.get(chatList.size()-1).getUtcTimeStamp():"1990-12-12T12:12:12.123Z";

        String firstJsonTimestamp = jsonArray.getJSONObject(0).getString("created_at");
        String lastJsonTimestamp = jsonArray.getJSONObject(jsonArray.length()-1).getString("created_at");

        if (DateUtils.compareUTCTimestamp(firstJsonTimestamp,lastListTimestamp))
        {
            for (int i = jsonArray.length()-1; i > -1 ; i--) {
                jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String utcTimestamp = jsonObject.getString("created_at");
                String timestamp = DateUtils.timestampToDeatil(utcTimestamp);
                String message = jsonObject.getString("message");
                ChatEntity chatEntity = new ChatEntity();
                chatEntity.setComeMsg(isMe(username));
                chatEntity.setContent(message);
                chatEntity.setChatTime(timestamp);
                chatEntity.setUtcTimeStamp(utcTimestamp);


                String firstTimestamp = chatList.size() > 0 ? chatList.get(0).getUtcTimeStamp():"1990-12-12T12:12:12.122Z";
                String lastTimestamp  = chatList.size() > 0 ? chatList.get(chatList.size()-1).getUtcTimeStamp():"1990-12-12T12:12:12.122Z";
                if (DateUtils.compareUTCTimestamp(utcTimestamp,lastTimestamp)){
                    chatList.add(chatEntity);
                }

                if (!DateUtils.compareUTCTimestamp(utcTimestamp,firstTimestamp)){
                    chatList.add(0, chatEntity);
                }
            }
        }else{
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String username = jsonObject.getString("username");
                String utcTimestamp = jsonObject.getString("created_at");
                String timestamp = DateUtils.timestampToDeatil(utcTimestamp);
                String message = jsonObject.getString("message");

                ChatEntity chatEntity = new ChatEntity();
                chatEntity.setComeMsg(isMe(username));
                chatEntity.setContent(message);
                chatEntity.setChatTime(timestamp);
                chatEntity.setUtcTimeStamp(utcTimestamp);

                String firstTimestamp = chatList.size() > 0 ? chatList.get(0).getUtcTimeStamp():"1990-12-12T12:12:12.122Z";
                String lastTimestamp  = chatList.size() > 0 ? chatList.get(chatList.size()-1).getUtcTimeStamp():"1990-12-12T12:12:12.122Z";
                if (DateUtils.compareUTCTimestamp(utcTimestamp,lastTimestamp)){
                    chatList.add(chatEntity);
                }

                if (!DateUtils.compareUTCTimestamp(utcTimestamp,firstTimestamp)){
                    chatList.add(0, chatEntity);
                }
            }
        }
        chatAdapter.notifyDataSetChanged();
    }


    public boolean isMe(String username)
    {
        if (username.equals(meName))
            return true;
        else
            return false;
    }
    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
    }
}