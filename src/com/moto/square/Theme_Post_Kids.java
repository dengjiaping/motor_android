package com.moto.square;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.moto.asydata.LoadCacheResponseLoginouthandler;
import com.moto.asydata.LoadDatahandler;
import com.moto.asydata.RequstClient;
import com.moto.constant.Constant;
import com.moto.constant.DialogMethod;
import com.moto.constant.ImageMethod;
import com.moto.img.ScaleImageView;
import com.moto.listview.CustomScrollView;
import com.moto.listview.CustomScrollView.OnLoadListener;
import com.moto.listview.CustomScrollView.OnRefreshListener;
import com.moto.listview.NoScrollListview;
import com.moto.listview.ProgressBarView;
import com.moto.live.User_OwnPage;
import com.moto.main.Moto_RootActivity;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.moto.mytextview.ShimmerTextView;
import com.moto.toast.ToastClass;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedList;

public class Theme_Post_Kids extends Moto_RootActivity{

    private ImageView send;
    private EmojiconEditText editText;
    private NoScrollListview listview;
    private LinkedList<HashMap<String, Object>> Item_list = new LinkedList<HashMap<String,Object>>();
    private HashMap<String, Object> map;
    private int count = 0;
    private KidsAdapter adapter;
    private boolean isrefresh = false;
    private String pid;
    private String author;
    private String message;
    private String dateline;
    private String avatar;
    private String photoname;
    private long time = 0;
    private Intent intent;
    RequestParams param;
    private String authorname = ""; //楼主

    private ImageView kids_post_response_avatar;
    private EmojiconTextView kids_post_response_username;
    private EmojiconTextView kids_post_response_details;
    private ScaleImageView kids_post_response_detail_img;
    private ProgressBarView kids_post_response_progress_View;
    private TextView kids_post_response_time;

    private CustomScrollView scrollView;
    private String response_theme_message;
    private SharedPreferences TokenShared;
    private String tokenString;
    private DisplayImageOptions options;
    private DisplayImageOptions Originaloptions;
    private Handler handler;
    private ShimmerTextView waitText;


    private String readcommentUri = path+"api/square/readcommentforpost";
    private String CreatecommentUri = path+"api/square/createcommentforpost";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addContentView(R.layout.square_discuss_kids_post_kids_response,R.string.userresponse,barButtonIconType.barButtonIconType_Back,barButtonIconType.barButtonIconType_None);
        init();
        adapter = new KidsAdapter(this, Item_list);
        listview.setAdapter(adapter);
        GetItemAsyData();

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_SUCCESS:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        break;

                    case Constant.MSG_SUCCESSAGAIN:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        adapter.notifyDataSetChanged();
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_NULL:
                        listview.setVisibility(View.VISIBLE);
                        waitText.setVisibility(View.GONE);
                        isrefresh = false;
                        scrollView.onRefreshComplete();
                        scrollView.onLoadComplete();
                        break;
                    case Constant.MSG_HAVENOTHING:

                        waitText.setText("暂时还没有任何数据哟");
                        break;
                }
                super.handleMessage(msg);
            }

        };

        //必须在这里面添加head或者foot
        scrollView.addHeadFootView();

        scrollView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(1500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isrefresh = true;
                        count = 0;
                        GetItemAsyData();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onRefreshComplete();
                    }

                }.execute();
            }
        });
        scrollView.setOnLoadListener(new OnLoadListener() {
            public void onLoad() {
                new AsyncTask<Void, Void, Void>() {
                    protected Void doInBackground(Void... params) {
                        try {
                            Thread.sleep(2000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        GetItemAsyData();
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        //						refresh_scrollview.onLoadComplete();
                    }

                }.execute();
            }
        });

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                response_theme_message = editText.getText().toString();
                TokenShared = getSharedPreferences("usermessage", 0);
                tokenString = TokenShared.getString("token", "");
                if(tokenString.equals(""))
                {
                    ToastClass.SetToast(Theme_Post_Kids.this, "需要先登录才能够发送");
                    setResult(304);
                    Theme_Post_Kids.this.finish();
                }
                else if(response_theme_message.replaceAll(" ", "").equals(""))
                {
                    DialogMethod.dialogShow(Theme_Post_Kids.this,"请输入回复内容!");
                }
                else
                {
                    SetAsyResponse();
                }
            }
        });

    }

    private void init() {
        // TODO Auto-generated method stub
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        pid = bundle.getString("pid");
        author = bundle.getString("author");
        message = bundle.getString("message");
        dateline = bundle.getString("dateline");
        avatar = bundle.getString("avatar");
        photoname = bundle.getString("photoname");
        authorname = bundle.getString("authorname");

        options = ImageMethod.GetOptions();
        Originaloptions = ImageMethod.GetOriginalOptions();
        waitText = (ShimmerTextView)findViewById(R.id.kids_post_response_waittext);

        send = (ImageView)findViewById(R.id.post_kids_send);
        editText = (EmojiconEditText)findViewById(R.id.post_kids_response_theme_edit);
        listview = (NoScrollListview)findViewById(R.id.kids_post_response_listview);
        scrollView = (CustomScrollView)findViewById(R.id.kids_post_response_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ChangeScrollviewAlpha(scrollView, navigationBar);
                return  false;
            }
        });

        kids_post_response_avatar = (ImageView)findViewById(R.id.kids_post_response_avatar);
        kids_post_response_username = (EmojiconTextView)findViewById(R.id.kids_post_response_username);
        kids_post_response_details = (EmojiconTextView)findViewById(R.id.kids_post_response_details);
        kids_post_response_detail_img = (ScaleImageView)findViewById(R.id.kids_post_response_detail_img);
        kids_post_response_progress_View = (ProgressBarView)findViewById(R.id.kids_post_response_progress_View);
        kids_post_response_time = (TextView)findViewById(R.id.kids_post_response_time);

        SetThemeMessage();
    }

    private void SetThemeMessage(){
        kids_post_response_username.setText(author);
        kids_post_response_details.setText(message);
        kids_post_response_time.setText(com.moto.utils.DateUtils.timestampToDeatil(dateline));
        MyMapApplication.imageLoader.displayImage(UrlUtils.imageUrl(avatar),kids_post_response_avatar  ,options,null);
        kids_post_response_avatar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ToastClass.IsHaveToken(Theme_Post_Kids.this))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("author",author);
                    bundle.putString("token",ToastClass.GetTokenString(Theme_Post_Kids.this));
                    pushToNextActivity(bundle,User_OwnPage.class,304);
                }
                else{
                    ToastClass.SetToast(Theme_Post_Kids.this,"请先登录之后再查看");
                }
            }
        });
        if(!photoname.equals(""))
        {
            String imageUrl = UrlUtils.imageUrl(photoname);

            kids_post_response_detail_img.setVisibility(View.VISIBLE);
            MyMapApplication.imageLoader.displayImage(imageUrl, kids_post_response_detail_img, Originaloptions,new SimpleImageLoadingListener(){
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    // TODO Auto-generated method stub
                    super.onLoadingStarted(imageUri, view);
                    kids_post_response_progress_View.setProgressNotInUiThread(0);
                    kids_post_response_progress_View.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view,
                                            FailReason failReason) {
                    // TODO Auto-generated method stub
                    super.onLoadingFailed(imageUri, view, failReason);
                    kids_post_response_progress_View.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingComplete(String imageUri, View view,
                                              Bitmap loadedImage) {
                    // TODO Auto-generated method stub
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    kids_post_response_progress_View.setVisibility(View.GONE);
                    kids_post_response_progress_View.setProgressNotInUiThread(100);
                }

            },new ImageLoadingProgressListener() {

                @Override
                public void onProgressUpdate(String imageUri, View view, int current,
                                             int total) {
                    // TODO Auto-generated method stub
                    if((System.currentTimeMillis() - time)>1000)
                    {
                        time = System.currentTimeMillis();
                        kids_post_response_progress_View.setProgressNotInUiThread(Math.round(100.0f * current / total));
                    }

                }
            });
            kids_post_response_detail_img.setImageHeight(80);
            kids_post_response_detail_img.setImageWidth(100);
        }
    }

    private void SetAsyResponse() {
        // TODO Auto-generated method stub
        param = new RequestParams();
        param.put("pid", pid);
        param.put("token", tokenString);
        param.put("message", response_theme_message);

        RequstClient.post(CreatecommentUri, param, new LoadCacheResponseLoginouthandler(
                Theme_Post_Kids.this,
                new LoadDatahandler(){

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        DialogMethod.startProgressDialog(Theme_Post_Kids.this,"正在发送");
                    }

                    @Override
                    public void onLoadCaches(String data) {
                        // TODO Auto-generated method stub
                        super.onLoadCaches(data);
                    }

                    @Override
                    public void onSuccess(String data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(data);
                        try {
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.getString("is").equals("1")) {
                                DialogMethod.stopProgressDialog();
                                Toast.makeText(Theme_Post_Kids.this, "发送成功", Toast.LENGTH_SHORT).show();

                                Theme_Post_Kids.this.finish();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                    @Override
                    public void onFailure(String error, String message) {
                        // TODO Auto-generated method stub
                        super.onFailure(error, message);
                        ToastClass.SetToast(Theme_Post_Kids.this,message);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                    }

                }));
    }

    private void GetItemAsyData() {
        // TODO Auto-generated method stub
        param = new RequestParams();
        param.put("pid", pid);
        param.put("page", ""+count);

        RequstClient.post(readcommentUri, param, new LoadCacheResponseLoginouthandler(
                Theme_Post_Kids.this,
                new LoadDatahandler(){

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onLoadCaches(String data) {
                        // TODO Auto-generated method stub
                        super.onLoadCaches(data);
                    }

                    @Override
                    public void onSuccess(String data) {
                        // TODO Auto-generated method stub
                        super.onSuccess(data);
                        try {
                            if(isrefresh)
                            {
                                Item_list.clear();
                            }
                            JSONObject jsonObject = new JSONObject(data);
                            if (jsonObject.getString("is").equals("1")) {
                                String data_details = jsonObject
                                        .getString("comment_list");
                                JSONArray array = new JSONArray(data_details);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonObject2 = (JSONObject) array.get(i);
                                    Item_list.add(GetMap(jsonObject2));
                                }
                                if(Item_list.size() > 0)
                                {
                                    if (count == 0) {
                                        if (isrefresh)
                                            handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                                                    .sendToTarget();
                                        else
                                            handler.obtainMessage(Constant.MSG_SUCCESS)
                                                    .sendToTarget();
                                    } else {
                                        handler.obtainMessage(Constant.MSG_SUCCESSAGAIN)
                                                .sendToTarget();
                                    }
                                }
                                else{
                                    handler.obtainMessage(Constant.MSG_HAVENOTHING)
                                            .sendToTarget();
                                }
                                count++;

                            } else {

                                handler.obtainMessage(Constant.MSG_NULL).sendToTarget();
                            }
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                    }

                    @Override
                    public void onFailure(String error, String message) {
                        // TODO Auto-generated method stub
                        super.onFailure(error, message);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                    }

                }));
    }
    private HashMap<String, Object> GetMap(JSONObject jsonObject)
    {
        map = new HashMap<String, Object>();
        try {
            String dateline = jsonObject.getString("dateline");
            String author = jsonObject.getString("author");
            String message = jsonObject.getString("message");
            String avatar = jsonObject.getString("avatar");
            map.put("author", author);
            map.put("message", message);
            map.put("avatar",avatar);
            map.put("dateline",dateline);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return map;

    }
    //内部类实现BaseAdapter  ，自定义适配器
    class KidsAdapter extends BaseAdapter{

        private Context context;
        LinkedList<HashMap<String, Object>> list;
        private HashMap<String, Object> map;

        public KidsAdapter(Context context, LinkedList<HashMap<String, Object>> list)
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
        public View getView(final int position, View convertView, ViewGroup parent)
        {
            ViewHolderKids holder;
            // TODO Auto-generated method stub

            convertView = LayoutInflater.from(context).inflate(R.layout.square_discuss_kids_post_kids_item, null);
            holder = new ViewHolderKids();
            holder.square_discuss_kids_post_kids_details = (EmojiconTextView)convertView.findViewById(R.id.square_discuss_kids_post_kids_details);
            holder.square_discuss_kids_post_kids_img = (ImageView)convertView.findViewById(R.id.square_discuss_kids_post_kids_img);
            holder.square_discuss_kids_post_kids_username = (EmojiconTextView)convertView.findViewById(R.id.square_discuss_kids_post_kids_username);
            holder.square_discuss_kids_post_kids_time = (TextView)convertView.findViewById(R.id.square_discuss_kids_post_kids_time);
            holder.square_discuss_kids_post_kids_user_layout = (RelativeLayout)convertView.findViewById(R.id.square_discuss_kids_post_kids_user_layout);
            convertView.setTag(holder);
            holder = (ViewHolderKids) convertView.getTag();

            map = list.get(position);
            if(authorname.equals(map.get("author")))
            {
                holder.square_discuss_kids_post_kids_user_layout.setVisibility(View.VISIBLE);
            }
            holder.square_discuss_kids_post_kids_details.setText((CharSequence) map.get("message"));
            holder.square_discuss_kids_post_kids_username.setText((CharSequence) map.get("author"));
            holder.square_discuss_kids_post_kids_time.setText(com.moto.utils.DateUtils.timestampToDeatil(map.get("dateline").toString()));
            MyMapApplication.imageLoader.displayImage(UrlUtils.imageUrl(map.get("avatar").toString()),  holder.square_discuss_kids_post_kids_img,options,null);
            holder.square_discuss_kids_post_kids_img.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ToastClass.IsHaveToken(Theme_Post_Kids.this))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("author",list.get(position).get("author").toString());
                        bundle.putString("token",ToastClass.GetTokenString(Theme_Post_Kids.this));
                        pushToNextActivity(bundle,User_OwnPage.class,304);
                    }
                    else{
                        ToastClass.SetToast(Theme_Post_Kids.this,"请先登录之后再查看");
                    }
                }
            });
            return convertView;
        }


    }
    //此类为上面getview里面view的引用，方便快速滑动
    class ViewHolderKids{
        EmojiconTextView square_discuss_kids_post_kids_details;
        ImageView square_discuss_kids_post_kids_img;
        EmojiconTextView square_discuss_kids_post_kids_username;
        TextView square_discuss_kids_post_kids_time;
        RelativeLayout square_discuss_kids_post_kids_user_layout;
    }
}