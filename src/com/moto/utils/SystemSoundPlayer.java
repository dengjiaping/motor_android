package com.moto.utils;

/**
 * Created by hu on 14-5-20.
 */
public class SystemSoundPlayer {
    private boolean on;

    private static class SoundPlayerHolder{
        public static SystemSoundPlayer play = init();

        private static SystemSoundPlayer init(){
            SystemSoundPlayer play = new SystemSoundPlayer();
            play.on = true;
            //从xml文档中读取
            return play;
        }
    }

    public static SystemSoundPlayer getInstance(){
        return SoundPlayerHolder.play;
    }

    public void playSendSound(){
        if(this.on){

        }
    }
    public void toggleSoundPlayerOn(boolean on)
    {
        this.on = on;
        //写入xml文档中
    }
}
