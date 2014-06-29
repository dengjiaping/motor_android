package com.moto.inform;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.listview.MyListView;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.InformNetworkModel;
import com.moto.toast.ToastClass;
import com.moto.utils.DateUtils;
import com.moto.utils.UrlUtils;
import com.moto.welcome.MyPushMessageReceiver;
import com.moto.welcome.MyPushMessageReceiver.EventHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Chat_privateActivity extends Moto_RootActivity implements EventHandler{
    private ImageView sendimage = null;
    private EditText contentEditText = null;
    private MyListView chatListView = null;
    private List<ChatEntity> chatList = null;
    private ChatAdapter chatAdapter = null;

    public String otherUserName;
    public String meImage;
    public String meName;

    private Handler handler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Intent intent = this.getIntent();
        Bundle extras = getIntent().getExtras();
        otherUserName = extras.getString("otherUserName");

        addContentView(R.layout.inform_chat, otherUserName,barButtonIconType.barButtonIconType_Back, barButtonIconType.barButtonIconType_None);
        init();
        chatList = new ArrayList<ChatEntity>(8);

        SharedPreferences sharepreferences = this.getSharedPreferences("usermessage", 0);
        meName = sharepreferences.getString("username", "");
        meImage = sharepreferences.getString("avatar","");

        chatAdapter = new ChatAdapter(this,chatList);
        chatListView.setAdapter(chatAdapter);

        sendimage.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!contentEditText.getText().toString().trim().equals("")) {
                    send();
                }else {
                    ToastClass.SetToast(Chat_privateActivity.this, "请输入要说的话");
                }
            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        chatAdapter.notifyDataSetChanged();
                        chatListView.onRefreshComplete();
                        chatListView.post(new Runnable() {
                            @Override
                            public void run() {
                                chatListView.setSelection(chatListView.getCount());
                            }
                        });
                        break;
                }
                super.handleMessage(msg);
            }

        };

        chatListView.setonRefreshListener(new MyListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new InformNetworkModel(Chat_privateActivity.this,Chat_privateActivity.this).readPrivateMessage(otherUserName, chatList.get(0).getUtcTimeStamp());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onRefreshComplete();
                    }

                }.execute();
            }
        });


        new InformNetworkModel(this,this).readPrivateMessage(otherUserName, DateUtils.getUTCCurrentTimestamp());
    }

    private void init() {
        contentEditText = (EditText) this.findViewById(R.id.inform_chat_content);
        sendimage = (ImageView) this.findViewById(R.id.inform_chat_send);
        chatListView = (MyListView) this.findViewById(R.id.inform_chat_listview);

    }

    private void send(){
        ChatEntity chatEntity = new ChatEntity();
        String message = contentEditText.getText().toString();
        chatEntity.setContent(message);
        chatEntity.setComeMsg(false);
        chatEntity.setUserImage(meImage);
        chatEntity.setChatTime(DateUtils.timestampToDeatil(DateUtils.getUTCCurrentTimestamp()));
        chatEntity.setUtcTimeStamp(DateUtils.getUTCCurrentTimestampWithMillisecond());
        chatEntity.setUsername(meName);
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
        private DisplayImageOptions options;

        public ChatAdapter(Context context,List<ChatEntity> chatList){
            this.context = context;
            this.myList = chatList;
            inflater = LayoutInflater.from(this.context);
            options = ImageMethod.GetOptions();
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
                chatHolder.inform_chat_user_username = (TextView)convertView.findViewById(R.id.inform_chat_user_username);
                convertView.setTag(chatHolder);
            }else {
                chatHolder = (ChatHolder)convertView.getTag();
            }

            chatHolder.timeTextView.setText(myList.get(position).getChatTime());
            chatHolder.contentTextView.setText(myList.get(position).getContent());
            chatHolder.inform_chat_user_username.setText(myList.get(position).getUsername());
            MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(myList.get(position).getUserImage()),  chatHolder.userImageView,options,null);

            return convertView;
        }

        private class ChatHolder{
            TextView timeTextView;
            ImageView userImageView;
            TextView contentTextView;
            TextView inform_chat_user_username;
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
            int num = jsonArray.length()-1;
            for (int i = num; i > -1 ; i--) {
                jsonObject = jsonArray.getJSONObject(i);
                String avatar = jsonObject.getString("avatar");
                String username = jsonObject.getString("username");
                String utcTimestamp = jsonObject.getString("created_at");
                String timestamp = DateUtils.StimestampToDeatil(utcTimestamp);
                String message = jsonObject.getString("message");

                ChatEntity chatEntity = new ChatEntity();
                chatEntity.setUsername(username);
                chatEntity.setUserImage(avatar);
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
            int num = jsonArray.length();
            for (int i = 0; i < num; i++) {
                jsonObject = jsonArray.getJSONObject(i);
                String avatar = jsonObject.getString("avatar");
                String username = jsonObject.getString("username");
                String utcTimestamp = jsonObject.getString("created_at");
                String timestamp = DateUtils.StimestampToDeatil(utcTimestamp);
                String message = jsonObject.getString("message");

                ChatEntity chatEntity = new ChatEntity();
                chatEntity.setUserImage(avatar);
                chatEntity.setUsername(username);
                chatEntity.setUsername(username);
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
        handler.obtainMessage(Constant.MSG_SUCCESS)
                .sendToTarget();

    }


    public boolean isMe(String username)
    {
        if (username.equals(meName))
            return false;
        else
            return true;
    }
    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
    }
}