package com.moto.live;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.listview.MyGridView;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.NetWorkModelListener;
import com.moto.model.UserNetworkModel;
import com.moto.mymap.MyMapApplication;
import com.moto.photo.ImageBrowserActivity;
import com.moto.toast.ToastClass;
import com.moto.user.PullScrollView;
import com.moto.user.User_friends;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by chen on 14-6-21.
 */
public class User_OwnPage extends Moto_RootActivity{
    private String author;
    private String token;
    private MyGridView listView;
    private MyAdapter adapter;
    private ImageView user_userimg;
    private TextView no_dongtai;
    private TextView user_signature;
    private TextView user_mototype;
    private TextView user_outputvolume;
    private TextView user_friends;
    private TextView user_number_post;
    private RelativeLayout user_head_layout;
    private ScrollView user_scrollview;
    private LinearLayout moto_photo;
    private PullScrollView pullScrollView;
    private LinearLayout number_post_layout;
    private LinearLayout friends_layout;
    private LinearLayout collect_layout;

    private boolean Isfollow = false;

    private DisplayImageOptions Originaloptions;
    private DisplayImageOptions options;
    private Handler handler;
    private HashMap<String, Object> ownMessageMap = new HashMap<String, Object>();
    private HashMap<String, Object> ownMessageMapNum = new HashMap<String, Object>();
    private ArrayList<String> ownphotoMessage = new ArrayList<String>();
    protected LinkedList<LinkedList<String>> ImgList = new LinkedList<LinkedList<String>>();
    protected LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList = new LinkedList<LinkedList<HashMap<String,Integer>>>();
    private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
    private ArrayList<String> carList = new ArrayList<String>();
    private HashMap<String, Object> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();

        author = bundle.getString("author");
        token = bundle.getString("token");
        addContentView(R.layout.user, bundle.getString("author"), R.string.focus,barButtonIconType.barButtonIconType_Back, barButtonIconType.barRightTextViewType );
        init();

        UserNetworkModel userNetworkModel = new UserNetworkModel(this,this);
        userNetworkModel.readUserproFile(author,token);
        GetAsyData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Bundle extras = new Bundle();
                extras.putString("tid", list.get(position).get("tid").toString());
                extras.putString("subject", list.get(position).get("subject").toString());
                pushToNextActivity(extras, Live_Kids_User.class, 304);
            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {

                    case Constant.MSG_START:
                        SetMessage();
                        break;
                    case Constant.MSG_SUCCESSAGAIN:
                        user_scrollview.post(new Runnable() {
                            //让scrollview跳转到顶部，必须放在runnable()方法中
                            @Override
                            public void run() {
                                user_scrollview.scrollTo(0, 0);
                            }
                        });
                        if(list.size() == 0)
                        {
                            no_dongtai.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();

                        break;
                    case Constant.MSG_SUCCESS:
                        if(Isfollow)
                        {
                            setRightBarText(R.string.nofocus);
                            ToastClass.SetToast(User_OwnPage.this,"关注成功");
                        }
                        else{
                            setRightBarText(R.string.focus);
                            ToastClass.SetToast(User_OwnPage.this,"成功取消关注");
                        }
                        break;
                    case Constant.MSG_FALTH:
                        ToastClass.SetToast(User_OwnPage.this, msg.obj.toString());

                        break;
                }
                super.handleMessage(msg);
            }

        };

        friends_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("author",ownMessageMap.get("username")+"");
                pushToNextActivity(bundle,User_friends.class,304);
            }
        });
    }

    private void init(){
        navigationBar.setBackgroundColor(Color.rgb(0, 0, 0));
        navigationBar.getBackground().setAlpha(0);

        options = ImageMethod.GetOptions();
        Originaloptions = ImageMethod.GetOriginalOptions();

        moto_photo = (LinearLayout)findViewById(R.id.user_moto_photo);
        no_dongtai = (TextView)findViewById(R.id.user_haveno_dongtai);
        listView = (MyGridView)findViewById(R.id.user_dynamic_gridview);
        user_scrollview = (ScrollView)findViewById(R.id.user_scrollview);
//		user_setting = (ImageView)findViewById(R.id.user_setting);
        number_post_layout = (LinearLayout)findViewById(R.id.user_number_post_layout);

        friends_layout = (LinearLayout)findViewById(R.id.user_friends_layout);
        collect_layout = (LinearLayout)findViewById(R.id.user_collect_layout);
//		user_name = (TextView)findViewById(R.id.user_name);
        user_userimg = (ImageView)findViewById(R.id.user_userimg);
        user_signature = (TextView)findViewById(R.id.user_signature);
        user_mototype = (TextView)findViewById(R.id.user_mototype);
        user_outputvolume = (TextView)findViewById(R.id.user_outputvolume);
        user_number_post = (TextView)findViewById(R.id.user_number_post);
        user_friends = (TextView)findViewById(R.id.user_friends);
//        number_post_layout.setOnClickListener(this);
//        friends_layout.setOnClickListener(this);
//        collect_layout.setOnClickListener(this);
//		user_setting.setOnClickListener(this);
        user_head_layout = (RelativeLayout)findViewById(R.id.user_head_layout);
        pullScrollView = (PullScrollView)findViewById(R.id.user_scrollview);
        pullScrollView.setHeader(user_head_layout);
        pullScrollView.setOnTurnListener(new PullScrollView.OnTurnListener() {
            @Override
            public void onTurn() {

            }
        });


        adapter = new MyAdapter(this, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                Bundle extras = new Bundle();
                extras.putString("tid", list.get(position).get("tid").toString());
                extras.putString("subject", list.get(position).get("subject").toString());
                pushToNextActivity(extras, Live_Kids_User.class, 304);
            }
        });
        this.navigationBar.bringToFront();
    }

    private void GetAsyData() {
        // TODO Auto-generated method stub
        UserNetworkModel userNetworkModel = new UserNetworkModel(new NetWorkModelListener() {
            @Override
            public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {
                String data_details = JSONObject
                        .getString("post_list");
                JSONArray array = new JSONArray(data_details);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject2 = (JSONObject) array.get(i);
                    list.add(GetMap(jsonObject2));
                }
                handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                        .sendToTarget();
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
        },User_OwnPage.this);
        userNetworkModel.readrecentpost(author);
    }

    @Override
    public void rightBarButtonItemEvent() {
        super.rightBarButtonItemEvent();
        UserNetworkModel userNetworkModel = new UserNetworkModel(new NetWorkModelListener() {
            @Override
            public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {
                String follow = JSONObject
                        .getString("status");
                if(follow.equals("followed"))
                    Isfollow = true;
                else
                    Isfollow = false;
                handler.obtainMessage(Constant.MSG_SUCCESS)
                        .sendToTarget();
            }

            @Override
            public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {

            }

            @Override
            public void handleNetworkDataWithUpdate(float progress) throws JSONException {

            }

            @Override
            public void handleNetworkDataGetFail(String message) throws JSONException {
                Message msg = Message.obtain();
                msg.obj = message;
                msg.what = Constant.MSG_FALTH;
                // 发送这个消息到消息队列中
                handler.sendMessage(msg);
            }

            @Override
            public void handleNetworkDataStart() throws JSONException {

            }
        },User_OwnPage.this);
        userNetworkModel.followuser(author,token);
    }

    private HashMap<String, Object> GetMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {

            String author = jsonObject.getString("author");
            String subject = jsonObject.getString("subject");
            String message = jsonObject.getString("message");
            String dateline = jsonObject.getString("dateline");
            String tid = jsonObject.getString("tid");
            map.put("author", author);
            map.put("subject", subject);
            map.put("message", message);
            map.put("dateline", dateline);
            map.put("tid", tid);
            ImgList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
            WidthHeightList.add(StringUtils.hashToWidthHeightArray(jsonObject.getString("photoinfo")));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;
    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {
        super.handleNetworkDataGetFail(message);
        // 获取一个Message对象，设置what为1
        Message msg = Message.obtain();
        msg.obj = message;
        msg.what = Constant.MSG_FALTH;
        // 发送这个消息到消息队列中
        handler.sendMessage(msg);
    }

    @Override
    public void handleNetworkDataWithUpdate(float progress) throws JSONException {
        super.handleNetworkDataWithUpdate(progress);
    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
    }

    @Override
    public void handleNetworkDataWithSuccess(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithSuccess(jsonObject);
        GetOwnMessageNum(jsonObject);
        JSONArray array = new JSONArray(jsonObject.getString("profile"));
        JSONObject jsonObject1 = (JSONObject) array.get(0);
        GetOwnMessage(jsonObject1);
        handler.obtainMessage(Constant.MSG_START)
                .sendToTarget();
    }

    private void GetOwnMessageNum(JSONObject jsonObject)
    {
        try {
            ownMessageMapNum.put("post_count", jsonObject.getString("post_count"));
            ownMessageMapNum.put("friend_count", jsonObject.getString("friend_count"));
            ownMessageMapNum.put("follow",jsonObject.getString("follow"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }
    private void GetOwnMessage(JSONObject jsonObject){
        try {
            ownMessageMap.put("avatar", jsonObject.getString("avatar"));
            ownMessageMap.put("gender", jsonObject.getString("gender"));
            ownMessageMap.put("username", jsonObject.getString("username"));
            ownMessageMap.put("profile", jsonObject.getString("profile"));
            ownMessageMap.put("mototype", jsonObject.getString("mototype"));
            ownMessageMap.put("outputvolume", jsonObject.getString("outputvolume"));
            GetOwnPhoto(jsonObject.getString("motophoto"));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    private void GetOwnPhoto(String motophoto)
    {
        JSONArray array = null;
        try {
            array = new JSONArray(motophoto);
            int num = array.length();
            for(int i = 0; i < num; i++)
            {
                try {
                    ownphotoMessage.add(array.getString(i));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    private void SetMessage(){

        if(!ownMessageMap.get("avatar").toString().equals(""))
        {

            String imageUrl = UrlUtils.imageUrl(ownMessageMap.get("avatar").toString());
//            GetAsyBitmap(imageUrl);
            MotorApplication.imageLoader.displayImage(imageUrl,  user_userimg,options,null);


        }

        int num = ownphotoMessage.size();
        if(num > 0)
        {
            for(int i = 0; i < num; i++)
            {
                String imageUrl = ownphotoMessage.get(i);
                carList.add(imageUrl) ;
            }
            SetMotoPhoto(carList);

        }

        if(!ownMessageMap.get("mototype").toString().equals(""))
            user_mototype.setText(ownMessageMap.get("mototype").toString());
        if(!ownMessageMap.get("outputvolume").toString().equals(""))
            user_outputvolume.setText(ownMessageMap.get("outputvolume").toString());
        if(!ownMessageMap.get("profile").toString().equals(""))
            user_signature.setText(ownMessageMap.get("profile").toString());
        user_number_post.setText(ownMessageMapNum.get("post_count").toString());
        user_friends.setText(ownMessageMapNum.get("friend_count").toString());
        if(ownMessageMapNum.get("follow").equals("1"))
        {
            setRightBarText(R.string.nofocus);
        }
    }

    private void SetMotoPhoto(final ArrayList<String> carList)
    {
        int num = carList.size();
        if (num > 0) {
            moto_photo.setVisibility(View.VISIBLE);
            moto_photo.removeAllViews();
            for (int i = 0; i < num; i++) {
                View mLayout;
                ImageView mPhoto;
                mLayout = LayoutInflater.from(User_OwnPage.this).inflate(
                        R.layout.user_moto_photo_image_item, null);
                mPhoto = (ImageView) mLayout
                        .findViewById(R.id.user_moto_photo_img);
                moto_photo.addView(mLayout);
                moto_photo.invalidate();
                MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(carList.get(i)),  mPhoto,Originaloptions,null);
                final int position = i;
                mPhoto.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(User_OwnPage.this,
                                ImageBrowserActivity.class);
                        intent.putExtra(ImageBrowserActivity.IMAGE_TYPE,
                                ImageBrowserActivity.TYPE_ALBUM);
                        intent.putExtra("position", position);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("carlist", carList);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        overridePendingTransition(R.anim.zoom_enter, 0);
                    }
                });

            }
        } else {
            moto_photo.setVisibility(View.GONE);
        }
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        super.handleNetworkDataStart();
    }

    class MyAdapter extends BaseAdapter {
        private Context context;
        private LinkedList<HashMap<String, Object>> list;
        private HashMap<String, Object> map;

        public MyAdapter(Context context, LinkedList<HashMap<String, Object>> list)
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

            convertView = LayoutInflater.from(context).inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.user_item_text = (TextView)convertView.findViewById(R.id.user_item_text);
            holder.user_item_img = (ImageView)convertView.findViewById(R.id.user_item_img);
//			holder.user_item_img = (ScaleImageView)convertView.findViewById(R.id.user_item_img);
            convertView.setTag(holder);
            holder = (ViewHolder) convertView.getTag();

            map = list.get(position);
            holder.user_item_text.setText("进行中");
//	        if(!map.get("photoname").toString().equals("null"))
//			{
//				String imageUrl = imgPath+map.get("photoname");
//				holder.user_item_img.setVisibility(View.VISIBLE);
//				MyMapApplication.imageLoader.displayImage(imageUrl,  holder.user_item_img,options,null);
//				holder.user_item_img.setImageHeight(80);
//				holder.user_item_img.setImageWidth(100);
//			}
            int num = ImgList.get(position).size();
            if(num > 0)
            {
                String imageUrl = UrlUtils.imageUrl(ImgList.get(position).get(num - 1));
                MyMapApplication.imageLoader.displayImage(imageUrl, holder.user_item_img,Originaloptions,null);
            }
            return convertView;
        }
        //此类为上面getview里面view的引用，方便快速滑动
        class ViewHolder{
            TextView user_item_text;
            ImageView user_item_img;
        }
    }
}
