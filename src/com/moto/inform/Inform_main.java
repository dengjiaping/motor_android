package com.moto.inform;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.moto.main.Moto_MainActivity;
import com.moto.main.R;
import com.moto.toast.ToastClass;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 2014/5/5.
 */
public class Inform_main extends TabActivity implements View.OnClickListener{
    /** Called when the activity is first created. */
    public static TabHost tabHost;
    public static SegmentedGroup radioGroup;
    //	private TextView main_tab_new_message;
    private ImageView inform_add_img;
    public static LinearLayout layout;

    private String type;   //菜单栏跳转type

    private RadioButton inform_button1;
    private RadioButton inform_button2;
    private RadioButton inform_button3;
    private RelativeLayout inform_add_layout;
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.informtest);
        tabHost=this.getTabHost();
        TabHost.TabSpec spec;
        Intent intent;

        type = ToastClass.GetUserType(this);

        inform_button1 = (RadioButton)findViewById(R.id.inform_button1);
        inform_button2 = (RadioButton)findViewById(R.id.inform_button2);
        inform_button3 = (RadioButton)findViewById(R.id.inform_button3);
        inform_button1.setOnClickListener(this);
        inform_button2.setOnClickListener(this);
        inform_button3.setOnClickListener(this);

        intent=new Intent().setClass(this, InformResponseActivity.class);
        spec=tabHost.newTabSpec("response").setIndicator("response").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this,InformTouchmeActivity.class);
        spec=tabHost.newTabSpec("touch").setIndicator("touch").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, InformPrivateActivity.class);
        spec=tabHost.newTabSpec("private").setIndicator("private").setContent(intent);
        tabHost.addTab(spec);


        if(type.equals("2") || type.equals("0"))
        {
            tabHost.setCurrentTab(0);
            inform_button1.setChecked(true);
            inform_button2.setChecked(false);
            inform_button3.setChecked(false);
        }
        else if(type.equals("1"))
        {
            tabHost.setCurrentTab(1);
            inform_button1.setChecked(false);
            inform_button2.setChecked(true);
            inform_button3.setChecked(false);
        }
        else if(type.equals("3"))
        {
            tabHost.setCurrentTab(2);
            inform_button1.setChecked(false);
            inform_button2.setChecked(false);
            inform_button3.setChecked(true);
        }
        Moto_MainActivity.layout.bringToFront();
        ToastClass.setUserType(this,"0");

        layout =(LinearLayout)findViewById(R.id.console_line_bottom);
        radioGroup = (SegmentedGroup) this.findViewById(R.id.inform_segmented);
        radioGroup.setTintColor(Color.BLUE);
        radioGroup.setTintColor(Color.WHITE,getResources().getColor(R.color.turquoise_blue));
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // TODO Auto-generated method stub
//                switch (checkedId) {
//                    case R.id.inform_button1://response
//                        tabHost.setCurrentTabByTag("response");
//                        layout.bringToFront();
//                        break;
//                    case R.id.inform_button2://touch
//                        tabHost.setCurrentTabByTag("touch");
//                        layout.bringToFront();
//
//                        break;
//                    //				case R.id.main_tab_square:
//                    //					tabHost.setCurrentTabByTag("liveown");
//                    //					layout.bringToFront();
//                    //					break;
//                    case R.id.inform_button3://private
//                        tabHost.setCurrentTabByTag("private");
//                        layout.bringToFront();
//                        break;
//                    default:
//
//                        break;
//                }
//            }
//        });
        BaseSpringSystem springSystem = SpringSystem.create();
        // Add a spring to the system.
        final Spring spring = springSystem.createSpring()
                .setSpringConfig(ORIGAMI_SPRING_CONFIG);

        inform_add_img = (ImageView)findViewById(R.id.inform_add_img);
        inform_add_layout= (RelativeLayout)findViewById(R.id.inform_add_layout);
        inform_add_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        spring.setEndValue(1);

                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        Intent intent = new Intent();
                        intent.setClass(Inform_main.this, Inform_Friends.class);
                        startActivity(intent);
                        spring.setEndValue(0);
                        break;
                }
                return false;
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
                inform_add_img.setScaleX(mappedValue);
                inform_add_img.setScaleY(mappedValue);

            }
        });

        layout.bringToFront();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(inform_button1))
        {
            tabHost.setCurrentTabByTag("response");
            layout.bringToFront();
        }
        else if(view.equals(inform_button2))
        {
            tabHost.setCurrentTabByTag("touch");
            layout.bringToFront();
        }
        else if(view.equals(inform_button3))
        {
            tabHost.setCurrentTabByTag("private");
            layout.bringToFront();
        }
    }
}
