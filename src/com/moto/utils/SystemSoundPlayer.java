package com.moto.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

import com.moto.main.R;
import com.moto.switchbutton.SwitchButton;

/**
 * Created by hu on 14-5-20.
 */
public class SystemSoundPlayer {
    private boolean on;
    private static SharedPreferences mshared;
    private static Context mcontext;
    private SharedPreferences.Editor editor;
    private static MediaPlayer mMediaPlayer01 = null;

    private static class SoundPlayerHolder{
        public static SystemSoundPlayer play = init();

        private static SystemSoundPlayer init(){
            SystemSoundPlayer play = new SystemSoundPlayer();
            mshared = play.mcontext.getSharedPreferences("sound",0);
            //创建MediaPlayer对象,将raw文件夹下的mail_sent.mp3
            mMediaPlayer01 = MediaPlayer.create(play.mcontext, R.raw.mail_sent);
            play.on = true;
            //从xml文档中读取
            play.on = mshared.getBoolean("on",true);

            return play;
        }
    }

    public static SystemSoundPlayer getInstance(Context context){
        mcontext = context;

        return SoundPlayerHolder.play;
    }

    public void playSendSound(){
        if(this.on) {
            mMediaPlayer01.start();
        }
//        mMediaPlayer01.stop();
//        mMediaPlayer01.release();
//        mMediaPlayer01 = null;
    }
    public void toggleSoundPlayerOn(boolean on)
    {
        this.on = on;
        //写入xml文档中
        editor = mshared.edit();
        editor.putBoolean("on",on);
        editor.commit();
    }

    /**
          * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private SystemSoundPlayer() {

    }

    public void setSwitchButtonOnOff(SwitchButton buttonOnOff)
    {
        if(this.on)
        {
            buttonOnOff.setChecked(true);
        }
        else
        {
            buttonOnOff.setChecked(false);
        }
    }
}
