package com.moto.user;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.inform.Chat_privateActivity;
import com.moto.live.User_OwnPage;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.UserNetworkModel;
import com.moto.toast.ToastClass;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class User_friends extends Moto_RootActivity implements OnClickListener{
	private ListView mylistView;
	private ImageView friends_return;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
	private HashMap<String, Object> map;
	private MyAdapter adapter;
    private Handler handler;
    private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_friends);
		init();

        UserNetworkModel userNetworkModel = new UserNetworkModel(this,this);
        userNetworkModel.readfriendlist(getIntent().getExtras().getString("author"));
		adapter = new MyAdapter(this, list);
		mylistView.setAdapter(adapter);

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                bundle.putString("author",list.get(i).get("username").toString());
                bundle.putString("token", ToastClass.GetTokenString(User_friends.this));
                pushToNextActivity(bundle,User_OwnPage.class,304);
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
                        adapter.notifyDataSetChanged();

                        break;

                }
                super.handleMessage(msg);
            }

        };
	}
    
	private void init() {
		// TODO Auto-generated method stub
        options = ImageMethod.GetOptions();
		mylistView = (ListView)findViewById(R.id.user_friends_listview);
		friends_return = (ImageView)findViewById(R.id.user_friends_return);

		friends_return.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == friends_return)
		{
			User_friends.this.finish();
		}
	}

    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        String data = jsonObject.getString("friend_list");
        JSONArray array = new JSONArray(data);
        int num = array.length();
        for(int i = 0; i < num; i++)
        {
            list.add(GetMap((JSONObject) array.get(i)));
        }
        handler.obtainMessage(Constant.MSG_SUCCESS)
                .sendToTarget();

    }
    private HashMap<String, Object> GetMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {

            String username = jsonObject.getString("username");
            String avatar = jsonObject.getString("avatar");
            String profile = jsonObject.getString("profile");
            map.put("username", username);
            map.put("avatar", avatar);
            map.put("profile", profile);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
    }

    @Override
    public void handleNetworkDataWithUpdate(float progress) throws JSONException {
        super.handleNetworkDataWithUpdate(progress);
    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {
        super.handleNetworkDataGetFail(message);
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        super.handleNetworkDataStart();
    }

    class MyAdapter extends BaseAdapter{
		private Context context;
		private ArrayList<HashMap<String, Object>> list;
		private HashMap<String, Object> map;
		
		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list)
		{
			this.context = context;
			this.list = list;
		}
        
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}
        
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
        
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}
        
		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			ViewHolder holder;
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(context).inflate(R.layout.user_friends_item, null);
			holder = new ViewHolder();
			holder.user_friends_username = (TextView)convertView.findViewById(R.id.user_friends_username);
			holder.user_friends_write = (TextView)convertView.findViewById(R.id.user_friends_write);
			holder.user_friends_userimg = (ImageView)convertView.findViewById(R.id.user_friends_userimg);
            holder.user_friends_item_chat = (ImageView)convertView.findViewById(R.id.user_friends_item_chat);
			convertView.setTag(holder);
			holder = (ViewHolder) convertView.getTag();
			
			map = list.get(position);

			holder.user_friends_username.setText((CharSequence)map.get("username"));
	        holder.user_friends_write.setText((CharSequence)map.get("profile"));
            MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(map.get("avatar")+""),  holder.user_friends_userimg,options,null);

            holder.user_friends_item_chat.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("otherUserName", (String)map.get("username"));
                    pushToNextActivity(bundle,Chat_privateActivity.class);

                }
            });
	        return convertView;
		}
		//此类为上面getview里面view的引用，方便快速滑动
		class ViewHolder{
			TextView user_friends_username;
			TextView user_friends_write;
			ImageView user_friends_userimg;
            ImageView user_friends_item_chat;
		}
	}
}
