package com.moto.live;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
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

import com.moto.constant.ImageMethod;
import com.moto.listview.MyGridView;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.moto.toast.ToastClass;
import com.moto.user.PullScrollView;
import com.moto.utils.BitmapUtils;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by chen on 14-6-21.
 */
public class User_OwnPage extends Moto_RootActivity{
    private SharedPreferences mshared;
    private String token;
    private String subject;
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
    private DisplayImageOptions Originaloptions;
    protected LinkedList<LinkedList<String>> ImgList = new LinkedList<LinkedList<String>>();
    protected LinkedList<LinkedList<HashMap<String,Integer>>> WidthHeightList = new LinkedList<LinkedList<HashMap<String,Integer>>>();
    private LinkedList<HashMap<String, Object>> list = new LinkedList<HashMap<String,Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mshared = getSharedPreferences("usermessage", 0);
        token = mshared.getString("token", "");
        if(token.equals(""))
        {
            ToastClass.SetToast(User_OwnPage.this,"请先登录再查看");
        }
        else{
            Bundle bundle = getIntent().getExtras();
            addContentView(R.layout.user, bundle.getString("subject"), barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_setting );
            init();
        }

    }

    private void init(){
        navigationBar.setBackgroundColor(Color.rgb(0, 0, 0));
        navigationBar.getBackground().setAlpha(0);

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
        Bitmap bitmap = null;
        try {
            bitmap = BitmapUtils.getInstance(User_OwnPage.this, bitmap).GetBitmap();
            if(bitmap ==null)
            {
                bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cuttedbackground_me);

                BitmapUtils.getInstance(User_OwnPage.this,bitmap);
            }
        }catch (OutOfMemoryError o)
        {}

        pullScrollView.setOriginbitmap(this, bitmap);
        if(bitmap != null && !bitmap.isRecycled())
        {

            bitmap.recycle();
            bitmap = null;
        }
        System.gc(); //回收

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
