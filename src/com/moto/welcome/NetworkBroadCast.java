package com.moto.welcome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.moto.constant.Constant;

/**
 * Created by chen on 14-7-9.
 */
public class NetworkBroadCast extends BroadcastReceiver {
    private Handler myHandler;
    public NetworkBroadCast (Handler h){
        this.myHandler = h;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Utils.isNetworkAvailable(context))
        {
            myHandler.obtainMessage(Constant.MSG_HAVENETWORK).sendToTarget();
        }
    }
}
