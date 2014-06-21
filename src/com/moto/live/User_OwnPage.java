package com.moto.live;

import android.os.Bundle;

import com.moto.main.Moto_RootActivity;
import com.moto.main.R;

/**
 * Created by chen on 14-6-21.
 */
public class User_OwnPage extends Moto_RootActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addContentView(R.layout.user, "", barButtonIconType.barButtonIconType_None, barButtonIconType.barButtonIconType_setting );
    }
}
