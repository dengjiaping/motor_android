package com.moto.main;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;

/**
 * Created by chen on 14-6-9.
 */
public class Moto_CenterActivity extends Activity {
    private static final SpringConfig ORIGAMI_SPRING_CONFIG = SpringConfig.fromOrigamiTensionAndFriction(40, 7);
    private Spring mSpring;
    private View center_buttonone;
    private View center_buttontwo;
    private View center_buttonthree;
    private View center_button_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center);
        init();
    }
    private void init()
    {
        center_buttonone = (ImageView)findViewById(R.id.center_buttonone);
        center_buttontwo = (ImageView)findViewById(R.id.center_buttontwo);
        center_buttonthree = (ImageView)findViewById(R.id.center_buttonthree);
        center_button_close = (ImageView)findViewById(R.id.center_button_close);
        mSpring = SpringSystem
                .create()
                .createSpring()
                .setSpringConfig(ORIGAMI_SPRING_CONFIG)
                .addListener(new SimpleSpringListener() {
                    @Override
                    public void onSpringUpdate(Spring spring) {
                        // Just tell the UI to update based on the springs current state.
                        // Get the current spring value.
                        double value = mSpring.getCurrentValue();

                        // Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
                        float barPosition_one =
                                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1,400, 0);
                        center_buttonone.setTranslationY(barPosition_one);

                        // Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
                        float barPosition_two =
                                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 400, 0);
                        center_buttontwo.setTranslationY(barPosition_two);

                        // Map the spring to the feedback bar position so that its hidden off screen and bounces in on tap.
                        float barPosition_three =
                                (float) SpringUtil.mapValueFromRangeToRange(value, 0, 1, 400, 0);
                        center_buttonthree.setTranslationY(barPosition_three);
                    }
                });
        mSpring.setEndValue(1);

        center_button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSpring.getEndValue() == 0) {
//                    mFeedbackBar.setVisibility(View.VISIBLE);
                    mSpring.setEndValue(1);
                } else {
                    mSpring.setEndValue(0);
//                    mFeedbackBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
