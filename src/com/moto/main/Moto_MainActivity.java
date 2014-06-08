package com.moto.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringConfigRegistry;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.moto.inform.Inform_main;
import com.moto.live.LiveActivity;
import com.moto.live.Live_Kids_Own;
import com.moto.square.SquareActivity;
import com.moto.user.UserActivity;

public class Moto_MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	public static RadioGroup radioGroup;
	private RadioButton radioButton;
    private ImageView main_add_img;
    private RotateAnimation rAnimation; //设置旋转
    private boolean IsRotate = true;
    private ImageView mFeedbackBar;
    // Create a spring configuration based on Origami values from the Photo Grid example.
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
    //	private TextView main_tab_new_message;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.moto.main.R.layout.activity_moto__main);
        radioButton = (RadioButton)findViewById(com.moto.main.R.id.main_tab_square);
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;
        
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
		final RelativeLayout layout =(RelativeLayout)findViewById(com.moto.main.R.id.console_line_bottom);
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
//                        Intent intent = new Intent();
//                        intent.setClass(Moto_MainActivity.this, OrigamiActivity.class);
//                        startActivity(intent);
//                        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                        break;
                }
                return true;
            }
        });
        mFeedbackBar = (ImageView)findViewById(com.moto.main.R.id.feedback);
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

                // Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
                float barPosition =
                        (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, mFeedbackBar.getHeight(), 0);
                mFeedbackBar.setTranslationY(barPosition);
            }
        });
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
                    case com.moto.main.R.id.main_tab_hot://热门
                        tabHost.setCurrentTabByTag("square");
                        layout.bringToFront();
                        break;
                    case com.moto.main.R.id.main_tab_live://直播
                        tabHost.setCurrentTabByTag("live");
                        layout.bringToFront();
                        
                        break;
                        //				case R.id.main_tab_square://广场
                        //					tabHost.setCurrentTabByTag("liveown");
                        //					layout.bringToFront();
                        //					break;
                    case com.moto.main.R.id.main_tab_inform://通知
                        tabHost.setCurrentTabByTag("inform");
                        layout.bringToFront();
                        break;
                    case com.moto.main.R.id.main_tab_user:
                        tabHost.setCurrentTabByTag("me");
                        layout.bringToFront();
                    default:
                        
                        break;
				}
			}
		});
        
		layout.bringToFront();
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
    
    
}