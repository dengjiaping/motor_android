package com.moto.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by chen on 14-6-8.
 */
public class BitmapUtils {
    private static Bitmap bitmap = null;
    private static Context context;


    private static class BitmapHolder {
        public static BitmapUtils bitmapUtils = init();

        private static BitmapUtils init(){
            BitmapUtils bitmapUtils1 = new BitmapUtils();
            bitmapUtils1.SetBitmapToSD();
            return bitmapUtils1;
        }

    }
    public static BitmapUtils getInstance(Context c, Bitmap b)
    {
        bitmap = b;
        context = c;
        return BitmapHolder.bitmapUtils;
    }

    public void SetBitmapToSD()
    {
        String str1 = "icon"+4;
        try {
            FileOutputStream localFileOutputStream1 = context.openFileOutput(str1, 0);


            Bitmap.CompressFormat localCompressFormat = Bitmap.CompressFormat.JPEG;

            bitmap.compress(localCompressFormat, 100, localFileOutputStream1);

            localFileOutputStream1.close();
        }catch (Exception ex){

        }
    }

    public Bitmap GetBitmap()
    {
        return GetBitmapFromSD();
    }
    private Bitmap GetBitmapFromSD()
    {
        String localIconNormal = "icon"+4;
        Bitmap bitmap = null;
        FileInputStream localStream = null;
        try {
            localStream = context.openFileInput(localIconNormal);

        }catch (Exception e)
        {}
        try {
            bitmap = BitmapFactory.decodeStream(localStream);
        }catch (OutOfMemoryError o)
        {}
        return bitmap;
    }

    public void DeleteSDBitmap(){
        for(int i = 0; i < 5; i++)
        {
            String filepath = context.getFilesDir().getAbsolutePath();
            CompressUtils.deleteTempFile(filepath+"/icon4");
        }
    }
}
