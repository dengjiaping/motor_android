package com.moto.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;

import com.moto.constant.ImageMethod;
import com.moto.listview.ProgressBarView;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
public class ImageBrowserAdapter extends PagerAdapter {

	private ArrayList<String> mPhotos = new ArrayList<String>();
	private String mType;
	protected DisplayImageOptions Originaloptions;
	private int num;
	private Context context;
	private RelativeLayout viewLayout;
	private long time = 0;

	public ImageBrowserAdapter(
			ArrayList<String> photos, String type,Context context) {
		if (photos != null) {
			mPhotos = photos;
		}
		this.mType = type;
		Originaloptions = ImageMethod.GetOriginalOptions();
		num = mPhotos.size();
		this.context = context;
	}

	@Override
	public int getCount() {
		if (mPhotos.size() > 1) {
			return Integer.MAX_VALUE;
		}
		return mPhotos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		final ViewHolder holder;
			viewLayout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.activity_imagebrowser_item, null);
			holder = new ViewHolder();
			holder.img = (PhotoView)viewLayout.findViewById(R.id.activity_imagebrowser_item_img);
			holder.progressBarView = (ProgressBarView)viewLayout.findViewById(R.id.activity_imagebrowser_item_progress_View);
		if(mPhotos.size() > 0)
		{
			MyMapApplication.imageLoader.displayImage("http://motor.qiniudn.com/"+mPhotos.get(position % num), holder.img,Originaloptions,new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					super.onLoadingStarted(imageUri, view);
					holder.progressBarView.setProgressNotInUiThread(0);
					holder.progressBarView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					super.onLoadingFailed(imageUri, view, failReason);
					holder.progressBarView.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					holder.progressBarView.setVisibility(View.GONE);
					holder.progressBarView.setProgressNotInUiThread(100);
				}
				
			},new ImageLoadingProgressListener() {
				
				@Override
				public void onProgressUpdate(String imageUri, View view, int current,
						int total) {
					// TODO Auto-generated method stub
					if((System.currentTimeMillis() - time)>1000)
					{
						time = System.currentTimeMillis();
						holder.progressBarView.setProgressNotInUiThread(Math.round(100.0f * current / total));
					}
					
				}
			});
			container.addView(viewLayout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}
		
		return viewLayout;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((RelativeLayout) object);
	}
	
	//此类为上面getview里面view的引用，方便快速滑动
	class ViewHolder{
		PhotoView img;// 照片
		ProgressBarView progressBarView;
	}
}
