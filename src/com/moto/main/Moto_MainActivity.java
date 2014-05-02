package com.moto.main;
import com.moto.inform.InformActivity;
import com.moto.live.LiveActivity;
import com.moto.live.Live_Kids_Own;
import com.moto.square.SquareActivity;
import com.moto.user.UserActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

public class Moto_MainActivity extends TabActivity {
    /** Called when the activity is first created. */
	private TabHost tabHost;
	public static RadioGroup radioGroup;
	private RadioButton radioButton;
    //	private TextView main_tab_new_message;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_moto__main);
        radioButton = (RadioButton)findViewById(R.id.main_tab_square);
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


        intent=new Intent().setClass(this, InformActivity.class);
        spec=tabHost.newTabSpec("inform").setIndicator("inform").setContent(intent);
        tabHost.addTab(spec);

        intent=new Intent().setClass(this, UserActivity.class);
        spec=tabHost.newTabSpec("me").setIndicator("me").setContent(intent);
        tabHost.addTab(spec);
        tabHost.setCurrentTab(0);
		final LinearLayout layout =(LinearLayout)findViewById(R.id.console_line_bottom);
        radioGroup = (RadioGroup) this.findViewById(R.id.main_tab_group);
        
        radioButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(Moto_MainActivity.this, Live_Kids_Own.class);
                startActivity(intent);
                overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
			}
		});
        radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
                    case R.id.main_tab_hot://热门
                        tabHost.setCurrentTabByTag("square");
                        layout.bringToFront();
                        break;
                    case R.id.main_tab_live://直播
                        tabHost.setCurrentTabByTag("live");
                        layout.bringToFront();
                        
                        break;
                        //				case R.id.main_tab_square://广场
                        //					tabHost.setCurrentTabByTag("liveown");
                        //					layout.bringToFront();
                        //					break;
                    case R.id.main_tab_inform://通知
                        tabHost.setCurrentTabByTag("inform");
                        layout.bringToFront();
                        break;
                    case R.id.main_tab_user:
                        tabHost.setCurrentTabByTag("me");
                        layout.bringToFront();
                    default:
                        
                        break;
				}
			}
		});
        
		layout.bringToFront();
    }
    
    
}