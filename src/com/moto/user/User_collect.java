package com.moto.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.moto.constant.Constant;
import com.moto.constant.ImageMethod;
import com.moto.img.ScaleImageView;
import com.moto.listview.ProgressBarView;
import com.moto.live.Live_Kids_User;
import com.moto.main.Moto_RootActivity;
import com.moto.main.MotorApplication;
import com.moto.main.R;
import com.moto.model.MeNetworkModel;
import com.moto.square.Theme_Post;
import com.moto.toast.ToastClass;
import com.moto.utils.StringUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class User_collect extends Moto_RootActivity{
    
	private ListView mylistView;
    private Handler handler;
	private MyAdapter adapter;
    private DisplayImageOptions options;
    private LinkedList<HashMap<String, Object>> collect_list = new LinkedList<HashMap<String,Object>>();
    private LinkedList<LinkedList<String>> carList = new LinkedList<LinkedList<String>>();
    private HashMap<String, Object> map;
    private DisplayImageOptions Originaloptions;
    private String tokenString;
    private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        addContentView(R.layout.user_collect,R.string.mycollect,barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);

		init();
        GetKeepMessage();

		adapter = new MyAdapter(this,this, collect_list);
		mylistView.setAdapter(adapter);


        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {

                    case Constant.MSG_FALTH:

                        String str = (String) msg.obj;
                        ToastClass.SetToast(User_collect.this, str);
                        break;
                    //获取成功
                    case Constant.MSG_SUCCESS:

                        adapter.notifyDataSetChanged();
                        break;


                }
                super.handleMessage(msg);
            }

        };

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if(collect_list.get(arg2).get("photoinfo").equals("true"))
                {
                    Bundle extras = new Bundle();
                    extras.putString("tid", collect_list.get(arg2).get("tid").toString());
                    extras.putString("subject", collect_list.get(arg2).get("subject").toString());
                    pushToNextActivity(extras, Live_Kids_User.class, 304);
                }
                else
                {
                    intent = new Intent();
                    intent.putExtra("tid", collect_list.get(arg2).get("tid").toString());
                    intent.putExtra("fid","37");
                    intent.putExtra("subject", collect_list.get(arg2).get("subject").toString());
                    intent.setClass(User_collect.this, Theme_Post.class);
                    startActivityForResult(intent, 305);
                }

            }
        });
	}
    
	private void init() {
		// TODO Auto-generated method stub
        options = ImageMethod.GetOptions();
        Originaloptions = ImageMethod.GetOriginalOptions();
		mylistView = (ListView)findViewById(R.id.user_collect_listview);
	}

    //点击收藏执行方法
    private void GetKeepMessage(){
        tokenString = ToastClass.GetTokenString(User_collect.this);
        RequestParams param;
        param = new RequestParams();
        param.put("token", tokenString);
        MeNetworkModel meNetworkModel= new MeNetworkModel(this, this);
        meNetworkModel.GetMeKeeppost(param);
    }


    //	内部类实现BaseAdapter  ，自定义适配器
    class MyAdapter extends BaseAdapter{

        private Context context;
        private LinkedList<HashMap<String, Object>> list;
        private HashMap<String, Object> map;
        //		private ImageLoader imageLoader;
        private Activity activity;
        long time = 0;


        public MyAdapter(Activity activity, Context context, LinkedList<HashMap<String, Object>> list)
        {
            this.context = context;
            this.list = list;
            this.activity = activity;
            //			imageLoader = new ImageLoader(context);

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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            final ViewHolder holder;
            // TODO Auto-generated method stub
            //			if(convertView == null)
            //			{
            convertView = LayoutInflater.from(context).inflate(R.layout.user_collect_item, null);


            holder = new ViewHolder();
            holder.user_collect_item_img = (ImageView)convertView.findViewById(R.id.user_collect_item_img);
            holder.user_collect_item_username = (EmojiconTextView)convertView.findViewById(R.id.user_collect_item_username);
            holder.user_collect_item_thing_img = (ScaleImageView)convertView.findViewById(R.id.user_collect_item_thing_img);
            holder.user_collect_item_detail_thing = (EmojiconTextView)convertView.findViewById(R.id.user_collect_item_detail_thing);
            holder.user_collect_item_subject_text = (EmojiconTextView)convertView.findViewById(R.id.user_collect_item_subject_text);

            holder.user_collect_item_time = (TextView)convertView.findViewById(R.id.user_collect_item_time);
            holder.user_collect_item_item_layout = (RelativeLayout)convertView.findViewById(R.id.user_collect_item_item_layout);
            holder.user_collect_item_progress_View = (ProgressBarView)convertView.findViewById(R.id.user_collect_item_progress_View);

            //					convertView.setTag(holder);
            //			}
            //			else {
            //				holder = (ViewHolder) convertView.getTag();
            //			}
            map = list.get(position);
            holder.user_collect_item_time.setText(com.moto.utils.DateUtils.StimestampToDeatil(map.get("dateline")+""));
            holder.user_collect_item_username.setText((CharSequence)map.get("author"));
            holder.user_collect_item_detail_thing.setText((CharSequence) map.get("message"));
            holder.user_collect_item_subject_text.setText(map.get("subject").toString());
            if(carList.get(position).size() > 0)
            {
                holder.user_collect_item_item_layout.setVisibility(View.VISIBLE);
                MotorApplication.imageLoader.displayImage(UrlUtils.imageUrl(carList.get(position).get(carList.get(position).size()-1)),  holder.user_collect_item_thing_img,Originaloptions,new SimpleImageLoadingListener(){

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        // TODO Auto-generated method stub
                        super.onLoadingStarted(imageUri, view);
                        holder.user_collect_item_progress_View.setProgressNotInUiThread(0);
                        holder.user_collect_item_progress_View.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view,
                                                FailReason failReason) {
                        // TODO Auto-generated method stub
                        super.onLoadingFailed(imageUri, view, failReason);
                        holder.user_collect_item_progress_View.setVisibility(View.GONE);
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {
                        // TODO Auto-generated method stub
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        holder.user_collect_item_progress_View.setVisibility(View.GONE);
                        holder.user_collect_item_progress_View.setProgressNotInUiThread(100);
                    }

                },new ImageLoadingProgressListener() {

                    @Override
                    public void onProgressUpdate(String imageUri, View view, int current,
                                                 int total) {
                        // TODO Auto-generated method stub
                        if((System.currentTimeMillis() - time)>1000)
                        {
                            time = System.currentTimeMillis();
                            holder.user_collect_item_progress_View.setProgressNotInUiThread(Math.round(100.0f * current / total));
                        }

                    }
                });

                holder.user_collect_item_thing_img.setImageHeight(80);
                holder.user_collect_item_thing_img.setImageWidth(100);
            }
            if(!map.get("avatar").toString().equals("null"))
            {
                MotorApplication.imageLoader.displayImage(UrlUtils.avatarUrl(map.get("avatar").toString()),  holder.user_collect_item_img,options,null);
            }

            return convertView;
        }


    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolder{
        ImageView user_collect_item_img;
        EmojiconTextView user_collect_item_username;
        ScaleImageView user_collect_item_thing_img;
        RelativeLayout user_collect_item_item_layout;
        ProgressBarView user_collect_item_progress_View;
        EmojiconTextView user_collect_item_detail_thing;
        EmojiconTextView user_collect_item_subject_text;
        TextView user_collect_item_time;
    }


    @Override
    public void handleNetworkDataWithSuccess(JSONObject JSONObject)
            throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataWithSuccess(JSONObject);
        String data_details = JSONObject
                .getString("keep_list");
        JSONArray array = new JSONArray(data_details);
        int data_num = array.length();
        for (int i = 0; i < data_num; i++) {
            JSONObject jsonObject2 = (JSONObject) array.get(i);
            collect_list.add(GetMap(jsonObject2));
        }
        handler.obtainMessage(Constant.MSG_SUCCESS)
                .sendToTarget();

    }

    @Override
    public void handleNetworkDataWithFail(JSONObject jsonObject) throws JSONException {
        super.handleNetworkDataWithFail(jsonObject);
        // 获取一个Message对象，设置what为1
        Message msg = Message.obtain();
        msg.obj = "获取失败";
        msg.what = Constant.MSG_FALTH;
        // 发送这个消息到消息队列中
        handler.sendMessage(msg);
    }

    @Override
    public void handleNetworkDataGetFail(String message) throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataGetFail(message);
        // 获取一个Message对象，设置what为1
        Message msg = Message.obtain();
        msg.obj = message;
        msg.what = Constant.MSG_FALTH;
        // 发送这个消息到消息队列中
        handler.sendMessage(msg);
    }

    @Override
    public void handleNetworkDataStart() throws JSONException {
        // TODO Auto-generated method stub
        super.handleNetworkDataStart();

    }

    private HashMap<String, Object> GetMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {

            String author = jsonObject.getString("author");
            String subject = jsonObject.getString("subject");
            String message = jsonObject.getString("message");
            String dateline = jsonObject.getString("updated_at");
            String tid = jsonObject.getString("tid");
            String avatar = jsonObject.getString("avatar");
            boolean photoinfo = jsonObject.has("photoinfo");
            if(photoinfo)
            {
                map.put("photoinfo","true");
            }
            else
            {
                map.put("photoinfo","false");
            }
            map.put("author", author);
            map.put("subject", subject);
            map.put("message", message);
            map.put("dateline", dateline);
            map.put("tid", tid);
            map.put("avatar", avatar);
            carList.add(StringUtils.hashToArray(jsonObject.getString("photoname").toString()));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;

    }
    
}
