package com.moto.constant;
import android.app.Activity;
import android.graphics.Bitmap;
import com.moto.img.ImageFetcher;
import com.moto.main.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ImageMethod {
	private static ImageFetcher ImageFetcher;
	private static DisplayImageOptions options;
	
	public static ImageFetcher setImageSourse(Activity activity)
	{
		ImageFetcher = new ImageFetcher(activity, 240);
		ImageFetcher.setLoadingImage(R.drawable.default_icon);
		ImageFetcher.setExitTasksEarly(false);
        return ImageFetcher;
	}
	public static DisplayImageOptions GetOptions() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.male)
		.showImageForEmptyUri(R.drawable.male)
		.showImageOnFail(R.drawable.male)
		.cacheInMemory(true)
		.displayer(new RoundedBitmapDisplayer(360))
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	public static DisplayImageOptions GetOriginalOptions() {
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
        //		.showImageOnLoading(R.drawable.default_icon)
        //		.showImageForEmptyUri(R.drawable.default_icon)
        //		.showImageOnFail(R.drawable.default_icon)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		return options;
	}
	
	public static Bitmap CompressImage(Bitmap image) {
        
        //        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        //        int options = 50;
        //        while ( baos.toByteArray().length / 1024>200) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        //            baos.reset();//重置baos即清空baos
        //            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
        //            options -= 10;//每次都减少10
        //        }
        //        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        //        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        //		OutputStream newimage = Bitmap2Bytes(image);
        //		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
        //        JPEGEncodeParam jep=JPEGCodec.getDefaultJPEGEncodeParam(tag);
        //         /* 压缩质量 */
        //        jep.setQuality((float)0.7, true);
        //        encoder.encode(arg0)
        //        //encoder.encode(tag); //近JPEG编码 
        //        newimage.close(); 
        return image;  
    }
}
