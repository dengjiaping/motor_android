package com.moto.main;

import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.activeandroid.query.Select;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringConfigRegistry;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.loopj.android.http.RequestParams;
import com.moto.constant.Constant;
import com.moto.inform.Inform_main;
import com.moto.live.LiveActivity;
import com.moto.live.Live_Kids_Own;
import com.moto.live.SendLiveService;
import com.moto.live.WriteLiveActivity;
import com.moto.main.AbstractInOutAnimationSet.Direction;
import com.moto.model.DataBaseModel;
import com.moto.model.NetWorkModelListener;
import com.moto.model.UserNetworkModel;
import com.moto.mydialog.CustomDialog;
import com.moto.square.SquareActivity;
import com.moto.toast.ToastClass;
import com.moto.user.UserActivity;
import com.moto.user.User_Login;
import com.moto.utils.StringUtils;
import com.moto.welcome.MyPushMessageReceiver;
import com.moto.welcome.NetworkBroadCast;
import com.moto.welcome.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Moto_MainActivity extends TabActivity implements View.OnClickListener{
    /** Called when the activity is first created. */
    public static TabHost tabHost;
    public static RadioGroup radioGroup;
    public static RadioButton radioButton;
    public static RadioButton main_tab_hot;
    public static RadioButton main_tab_live;
    public static RadioButton main_tab_inform;
    public static RadioButton main_tab_user;
    public static RelativeLayout layout;

    static boolean hot_check;
    static boolean live_check;
    static boolean inform_check;
    static boolean user_check;


    private ImageView main_add_img;
    private RotateAnimation rAnimation; //设置旋转
    private boolean IsRotate = true;
    private SharedPreferences mshared;
    private SharedPreferences.Editor editor;
    // Create a spring configuration based on Origami values from the Photo Grid example.
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
    //	private TextView main_tab_new_message;


    private InOutRelativeLayout mButtonsWrapper;
    private boolean mAreButtonsShowins ;
    private InOutImageButton button_photo;
    private InOutImageButton button_camera;
    private InOutImageButton button_live;
    private InOutImageButton button_write;
    //照片路径
    private String filepath;
    private ArrayList<String> dataList = new ArrayList<String>();

    //网络监听
    private NetworkBroadCast networkBroadCast;
    private Handler handler;

    //token
    private SharedPreferences TokenShared;
    private String tokenString;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.moto.main.R.layout.activity_moto__main);
        radioButton = (RadioButton)findViewById(com.moto.main.R.id.main_tab_square);
        main_tab_hot = (RadioButton)findViewById(com.moto.main.R.id.main_tab_hot);
        main_tab_live = (RadioButton)findViewById(com.moto.main.R.id.main_tab_live);
        main_tab_inform = (RadioButton)findViewById(com.moto.main.R.id.main_tab_inform);
        main_tab_user = (RadioButton)findViewById(com.moto.main.R.id.main_tab_user);
        main_tab_hot.setOnClickListener(this);
        main_tab_live.setOnClickListener(this);
        main_tab_inform.setOnClickListener(this);
        main_tab_user.setOnClickListener(this);
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;



        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                // TODO Auto-generated method stub

                switch(msg.what)
                {
                    //获取成功
                    case Constant.MSG_HAVENETWORK:
                        List<DataBaseModel> list = new Select().from(DataBaseModel.class).execute();
                        if(list.size() > 0) {
                            Intent intent = new Intent(Moto_MainActivity.this, SendLiveService.class);
                            startService(intent);
                        }

                        break;
                }
                super.handleMessage(msg);
            }

        };

        //注册网络变化监听
        networkBroadCast = new NetworkBroadCast(handler);
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkBroadCast, mFilter);

        //初始化四个图标
        button_camera = (InOutImageButton)findViewById(R.id.button_camera);
        button_photo = (InOutImageButton)findViewById(R.id.button_photo);
        button_live = (InOutImageButton)findViewById(R.id.button_live);
        button_write = (InOutImageButton)findViewById(R.id.button_write);

        intent=new Intent().setClass(this, SquareActivity.class);
        spec=tabHost.newTabSpec("square").setIndicator("square").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this,LiveActivity.class);
        spec=tabHost.newTabSpec("live").setIndicator("live").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, Live_Kids_Own.class);
        spec=tabHost.newTabSpec("liveown").setIndicator("liveown").setContent(intent);
        tabHost.addTab(spec);


        intent=new Intent().setClass(this, Inform_main.class);
        spec=tabHost.newTabSpec("inform").setIndicator("inform").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, UserActivity.class);
        spec=tabHost.newTabSpec("me").setIndicator("me").setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
        layout =(RelativeLayout)findViewById(com.moto.main.R.id.console_line_bottom);
        radioGroup = (RadioGroup) this.findViewById(com.moto.main.R.id.main_tab_group);

//        radioButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//                Intent intent = new Intent();
//                intent.setClass(Moto_MainActivity.this, Live_Kids_Own.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//			}
//		});

        getChecked();

        // 所有弹出收回按钮视图集合
        mButtonsWrapper = (InOutRelativeLayout) findViewById(R.id.buttons_wrapper);

// 设置每个弹出收回按钮的点击事件，点击后放大并隐藏
        for (int i = 0, count = mButtonsWrapper.getChildCount(); i < count; i++) {
            if (mButtonsWrapper.getChildAt(i) instanceof InOutImageButton) {
                View view = mButtonsWrapper.getChildAt(i);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startButtonClickAnimations(view);


                    }
                });
            }
        }



        main_add_img = (ImageView)this.findViewById(com.moto.main.R.id.main_add_img);

        // Create a system to run the physics loop for a set of springs.

        BaseSpringSystem springSystem = SpringSystem.create();

        // Add a spring to the system.
        final Spring spring = springSystem.createSpring()
                .setSpringConfig(ORIGAMI_SPRING_CONFIG);


        // Put our config into a registry. This is optional, but it gives you the ability to live tune
        // the spring using the SpringConfiguratorView which will show up at the bottom of the screen.
        SpringConfigRegistry.getInstance().addSpringConfig(ORIGAMI_SPRING_CONFIG, "origami animation spring");

        radioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        spring.setEndValue(1);
                        if(IsRotate)
                        {
                            IsRotate = false;
                            setRotateToFortyFive();
                        }
                        else
                        {
                            IsRotate = true;
                            setRotateToZero();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        spring.setEndValue(0);
                        //在无网络状态、tid!=-1即续写直播
                        if(!Utils.isNetworkAvailable(Moto_MainActivity.this) && !ToastClass.GetTid(Moto_MainActivity.this).equals("-1")
                                && !ToastClass.GetTid(Moto_MainActivity.this).equals(""))
                        {
                            Intent intent = new Intent();
                            intent.setClass(Moto_MainActivity.this, WriteLiveActivity.class);
                            startActivity(intent);
                        }
                        //在无网状态下、tid=-1即新建直播
                        else if(!Utils.isNetworkAvailable(Moto_MainActivity.this) && ToastClass.GetTid(Moto_MainActivity.this).equals("-1"))
                        {
                            final CustomDialog.Builder builder = new CustomDialog.Builder(Moto_MainActivity.this);
                            builder.setTitle
                                    ("创建您的直播：");
                            builder.setPositiveButton("创建", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    //设置你的操作事项
                                    if(builder.subject.getText().toString().replaceAll(" ", "").equals(""))
                                    {
                                        ToastClass.SetToast(Moto_MainActivity.this, "请输入主题!");
                                    }
                                    else {
                                        Intent intent = new Intent();
                                        intent.putExtra("subject", builder.subject.getText().toString());
                                        intent.setClass(Moto_MainActivity.this, WriteLiveActivity.class);
                                        startActivityForResult(intent, 301);
                                    }
                                }
                            });

                            builder.setNegativeButton("取消",
                                    new android.content.DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });

                            builder.create().show();
                        }
                        //有网状态，或者未登录
                        else
                        {
                            Intent intent = new Intent();
                            intent.setClass(Moto_MainActivity.this, Live_Kids_Own.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_exit);
                        }

//                        //弹球
//                        toggleButton();
                        break;
                }
                return true;
            }
        });
        // Add a listener to observe the motion of the spring.
        spring.addListener(new SimpleSpringListener() {

            @Override
            public void onSpringUpdate(Spring spring) {
                // You can observe the updates in the spring
                // state by asking its current value in onSpringUpdate.
//                float value = (float) spring.getCurrentValue();
//                float scale = 1f - (value * 0.5f);
                float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
                main_add_img.setScaleX(mappedValue);
                main_add_img.setScaleY(mappedValue);

            }
        });
        layout.bringToFront();

        //获取tid，判断是否续写游记
        GetAsyData();

//        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // TODO Auto-generated method stub
//                switch (checkedId) {
//                    case com.moto.main.R.id.main_tab_hot://热门
//                        tabHost.setCurrentTabByTag("square");
//                        layout.bringToFront();
//                        break;
//                    case com.moto.main.R.id.main_tab_live://直播
//                        tabHost.setCurrentTabByTag("live");
//                        layout.bringToFront();
//
//                        break;
//                    //				case R.id.main_tab_square://广场
//                    //					tabHost.setCurrentTabByTag("liveown");
//                    //					layout.bringToFront();
//                    //					break;
//                    case com.moto.main.R.id.main_tab_inform://通知
//                        tabHost.setCurrentTabByTag("inform");
//                        layout.bringToFront();
//                        break;
//                    case com.moto.main.R.id.main_tab_user:
//                        mshared = getSharedPreferences("usermessage", 0);
//                        String str = mshared.getString("token", "");
//                        if(str.equals(""))
//                        {
//                            Intent intent1 = new Intent();
//                            intent1.setClass(Moto_MainActivity.this, User_Login.class);
//                            startActivityForResult(intent1,304);
//                            overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
//                        }
//                        else
//                        {
//                            tabHost.setCurrentTabByTag("me");
//                            layout.bringToFront();
//                        }
//
//                    default:
//
//                        break;
//                }
//            }
//        });
//
//        layout.bringToFront();
    }

    //分别设置旋转角度
    private void setRotateToFortyFive()
    {
        rAnimation = new RotateAnimation(0,45, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //设置执行时间
        rAnimation.setDuration(500);
        //是否保持动画完时的状态
        rAnimation.setFillAfter(true);
        //是否动画完了返回原状态
        rAnimation.setFillBefore(false);
        //隔多长时间动画显示
        rAnimation.setStartOffset(0);
        //动画重复执行次数
        rAnimation.setRepeatCount(0);
        //设置动画效果
        main_add_img.setAnimation(rAnimation);
    }

    //分别设置旋转角度
    private void setRotateToZero()
    {
        rAnimation = new RotateAnimation(45,0, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        //设置执行时间
        rAnimation.setDuration(500);

        //是否保持动画完时的状态
        rAnimation.setFillAfter(true);
        //是否动画完了返回原状态
        rAnimation.setFillBefore(false);
        //隔多长时间动画显示
        rAnimation.setStartOffset(0);
        //动画重复执行次数
        rAnimation.setRepeatCount(0);
        //设置动画效果
        main_add_img.setAnimation(rAnimation);
    }

    /**
     * 弹出收回按钮点击后动画
     *
     * @param view
     */
    private void startButtonClickAnimations(View view) {
        mAreButtonsShowins = false;

        AnimationSet shrinkAnimationSet = new NotClickAnimationSet(600);
        shrinkAnimationSet.setInterpolator(new AnticipateInterpolator(2.0F));
//        shrinkAnimationSet.setAnimationListener(new CustomAnimationListener());
        radioButton.startAnimation(shrinkAnimationSet);

        // 为每一个按钮都设置动画
        for (int i = 0, count = mButtonsWrapper.getChildCount(); i < count; i++) {
            if (mButtonsWrapper.getChildAt(i) instanceof InOutImageButton) {
                View myview = mButtonsWrapper.getChildAt(i);
                if (myview.getId() == view.getId()) {
                    // 点击按钮放大并消失
                    myview.startAnimation(new ClickAnimationSet(600));
                } else {
                    // 未点击按钮缩小并消失
                    myview.startAnimation(new NotClickAnimationSet(600));
                }
            }
        }
        TokenShared = getSharedPreferences("usermessage", 0);
        tokenString = TokenShared.getString("token", "");

        if(tokenString.equals(""))
        {
            ToastClass.SetToast(Moto_MainActivity.this, "请先登录");
            //			Moto_MainActivity.radioGroup.check(R.id.main_tab_user);
        }
        else {
            if(view == button_camera)
            {
                filepath = "/mnt/sdcard/DCIM/Camera/"
                        + System.currentTimeMillis() + ".png";
                final File file = new File(filepath);
                final Uri imageuri = Uri.fromFile(file);
                // TODO Auto-generated method stub
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageuri);
                startActivityForResult(intent, 302);
            }
            else if(view == button_photo)
            {
//                Intent intent = new Intent(Moto_MainActivity.this,
//                        AlbumActivity.class);
//                Bundle bundle = new Bundle();
//                // intent.putArrayListExtra("dataList", dataList);
//                bundle.putStringArrayList("dataList",
//                        StringUtils.getIntentArrayList(dataList));
//                intent.putExtras(bundle);
//                startActivityForResult(intent, 0);
            }
            else if(view == button_write)
            {
                ToastClass.SetToast(Moto_MainActivity.this,"write");
            }
            else if(view == button_live)
            {
                Intent intent = new Intent();
                intent.setClass(Moto_MainActivity.this, Live_Kids_Own.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_exit);
            }
        }

        setRotateToZero();
        IsRotate = true;

    }
    /**
     * 弹出收回开关
     */
    private void toggleButton() {
        if (mAreButtonsShowins) {
            AnimationControlUtils.startAnimations(mButtonsWrapper, Direction.OUT);

        } else {
            AnimationControlUtils.startAnimations(mButtonsWrapper, Direction.IN);

        }

        mAreButtonsShowins = !mAreButtonsShowins;
    }

    /**
     * 跳转回来重置
     */
    private void reshowButton() {

        // 尺寸，透明度渐变
        ResetAnimationSet resetAnimationSet = new ResetAnimationSet(300);
        resetAnimationSet.setInterpolator(new OvershootInterpolator(2.0F));
        radioButton.startAnimation(resetAnimationSet);
    }

    @Override
    public void onClick(View view) {

        if(view.equals(main_tab_hot))
        {
            tabHost.setCurrentTabByTag("square");
            layout.bringToFront();
        }
        else if(view.equals(main_tab_live))
        {
            tabHost.setCurrentTabByTag("live");
            layout.bringToFront();
        }
        else if(view.equals(main_tab_inform))
        {
            tabHost.setCurrentTabByTag("inform");
            layout.bringToFront();
        }
        else if(view.equals(main_tab_user))
        {
            mshared = getSharedPreferences("usermessage", 0);
            String str = mshared.getString("token", "");
            if(str.equals(""))
            {
                main_tab_user.setChecked(user_check);
                main_tab_live.setChecked(live_check);
                main_tab_inform.setChecked(inform_check);
                main_tab_hot.setChecked(hot_check);
                Intent intent1 = new Intent();
                intent1.setClass(Moto_MainActivity.this, User_Login.class);
                startActivityForResult(intent1,304);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_exit);
            }
            else
            {
                tabHost.setCurrentTabByTag("me");
                layout.bringToFront();
            }
        }
    }

    /**
     *	按钮点击监听器
     */
    private class CustomAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {

            setRotateToZero();
            IsRotate = true;

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (resultCode)
        {
            case 100:

                tabHost.setCurrentTabByTag("me");
                layout.bringToFront();
                main_tab_user.setChecked(true);
                break;

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void getChecked()
    {
        //获取check状态
        hot_check = main_tab_hot.isChecked();
        live_check = main_tab_live.isChecked();
        inform_check = main_tab_inform.isChecked();
        user_check = main_tab_user.isChecked();
    }


    private void GetAsyData()
    {
        String token = ToastClass.GetTokenString(Moto_MainActivity.this);
        if(!token.equals(""))
        {
            RequestParams param = new RequestParams();
            param.add("token",token);
            UserNetworkModel userNetworkModel = new UserNetworkModel(new NetWorkModelListener() {
                @Override
                public void handleNetworkDataWithSuccess(JSONObject JSONObject) throws JSONException {

                    mshared = getSharedPreferences("usermessage", 0);
                    editor = mshared.edit();
                    String str = JSONObject.getString("expiredStatus");
                    if(str.equals("expired"))
                    {
                        ToastClass.SetToast(Moto_MainActivity.this,"你的账号已经过期,请重新登录");
                        editor.putString("email", "");
                        editor.putString("username", "");
                        editor.putString("token", "");
                        editor.putString("tid","");
                        editor.putString("subject","");
                    }

                    else
                    {
                        editor.putString("tid",JSONObject.getString("tid"));
                        editor.putString("subject",JSONObject.getString("subject"));
                    }

                    editor.commit();
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
            },Moto_MainActivity.this);

            userNetworkModel.isExpired(param);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkBroadCast);
    }

    @Override
    protected void onResume() {
        super.onResume();
        layout.bringToFront();
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        // TODO Auto-generated method stub
//        if(keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
//        {
//            Log.e("hhhh","break");
//            MyPushMessageReceiver.isStartApp = false;
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}