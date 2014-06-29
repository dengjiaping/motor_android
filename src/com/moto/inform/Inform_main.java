package com.moto.inform;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.moto.main.R;

import info.hoang8f.android.segmented.SegmentedGroup;

/**
 * Created by chen on 2014/5/5.
 */
public class Inform_main extends TabActivity{
    /** Called when the activity is first created. */
    private TabHost tabHost;
    public static SegmentedGroup radioGroup;
    //	private TextView main_tab_new_message;
    private ImageView inform_add_img;
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

        intent=new Intent().setClass(this, InformResponseActivity.class);
        spec=tabHost.newTabSpec("response").setIndicator("response").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this,InformTouchmeActivity.class);
        spec=tabHost.newTabSpec("touch").setIndicator("touch").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, InformPrivateActivity.class);
        spec=tabHost.newTabSpec("private").setIndicator("private").setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
        final LinearLayout layout =(LinearLayout)findViewById(R.id.console_line_bottom);
        radioGroup = (SegmentedGroup) this.findViewById(R.id.inform_segmented);
        radioGroup.setTintColor(Color.BLUE);
        radioGroup.setTintColor(Color.WHITE,getResources().getColor(R.color.turquoise_blue));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                switch (checkedId) {
                    case R.id.inform_button1://response
                        tabHost.setCurrentTabByTag("response");
                        layout.bringToFront();
                        break;
                    case R.id.inform_button2://touch
                        tabHost.setCurrentTabByTag("touch");
                        layout.bringToFront();

                        break;
                    //				case R.id.main_tab_square:
                    //					tabHost.setCurrentTabByTag("liveown");
                    //					layout.bringToFront();
                    //					break;
                    case R.id.inform_button3://private
                        tabHost.setCurrentTabByTag("private");
                        layout.bringToFront();
                        break;
                    default:

                        break;
                }
            }
        });
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
}
