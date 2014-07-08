package com.moto.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by chen on 2014/5/13.
 */
public class CompressUtils {

    /**
    * 根据路径删除图片数组
    *
    * @param list
    */
    public static void deleteLinkFile(LinkedList<String> list)
    {
        int num = list.size();
        for(int i = 0; i < num; i++)
        {
            deleteTempFile(list.get(i));
        }
    }

    /**
     * 根据路径删除图片
     *
     * @param path
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 根据路径压缩图片并保存
     *
     * @param photoPath
     * @param bitmapMaxWidth
     */
    public static String GetCompressPath(String photoPath, int bitmapMaxWidth)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, options);
        int height = options.outHeight;
        int width = options.outWidth;
        int reqHeight = 0;
        int reqWidth = bitmapMaxWidth;
        reqHeight = (reqWidth * height)/width;
        // 在内存中创建bitmap对象，这个对象按照缩放大小创建的
        options.inSampleSize = calculateInSampleSize(options, bitmapMaxWidth, reqHeight);
//        System.out.println("calculateInSampleSize(options, 480, 800);==="
//                        + calculateInSampleSize(options, 480, 800));
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        //Log.e("asdasdas", "reqWidth->"+reqWidth+"---reqHeight->"+reqHeight);
        Bitmap bbb = compressImage(Bitmap.createScaledBitmap(bitmap, bitmapMaxWidth, reqHeight, false));
        return saveMyBitmap(bbb);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                      int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 1024&& options != 10) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            options -= 10;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    private static String saveMyBitmap(Bitmap mBitmap){
        String compressPath = "/sdcard/" + System.currentTimeMillis() + ".png";
        File f = new File(compressPath);
        try {
            f.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block

        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return compressPath;
    }
}
