package com.moto.photopicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.moto.tryimage.ImageFactory;
import com.moto.tryimage.ImageFactoryActivity;
import com.moto.tryimage.ImageFactoryExtends;

public class FileUtils {
    // 图片在SD卡中的缓存路径
    private static final String IMAGE_PATH = "/mnt/sdcard/DCIM/Camera/";

    // 滤镜图片的RequestCode
    public static final int INTENT_REQUEST_CODE_FLITER = 5;

    //滤镜单张图片的RequestCode
    public static final int INTENT_REQUEST_CODE_FLITER_SINGLE = 6;
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/formats/";

	public static void saveBitmap(Bitmap bm, String picName) {
		Log.e("", "保存图片");
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".JPEG"); 
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			Log.e("", "已经保存");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    /**
     * 删除图片缓存目录
     */
    public static void deleteImageFile() {
        File dir = new File(IMAGE_PATH);
        if (dir.exists()) {
            com.moto.tryimage.FileUtils.delFolder(IMAGE_PATH);
        }
    }

    /**
     * 滤镜图片
     *
     * @param context
     * @param activity
     * @param path
     *            需要滤镜的图片路径
     */
    public static void fliterPhoto(Context context, Activity activity,
                                   String path,int position) {
        Intent intent = new Intent(context, ImageFactoryActivity.class);
        if (path != null) {
            intent.putExtra("path", path);
            intent.putExtra("position",position+"");
            intent.putExtra(ImageFactoryActivity.TYPE,
                    ImageFactoryActivity.FLITER);
        }
        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER);
    }

    /**
     * 滤镜图片
     *
     * @param context
     * @param activity
     * @param path
     *            需要滤镜的图片路径
     */
    public static void fliterPhoto(Context context, Activity activity,
                                   String path) {
        Intent intent = new Intent(context, ImageFactoryExtends.class);
        if (path != null) {
            intent.putExtra("path", path);
            intent.putExtra("position","0");
            intent.putExtra(ImageFactoryExtends.TYPE,
                    ImageFactoryExtends.FLITER);
        }
        activity.startActivityForResult(intent, INTENT_REQUEST_CODE_FLITER_SINGLE);
    }

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); // 删除所有文件
			else if (file.isDirectory())
				deleteDir(); // 递规的方式删除文件夹
		}
		dir.delete();// 删除目录本身
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
