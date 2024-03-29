package com.moto.photo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.moto.constant.ImageMethod;
import com.moto.listview.ProgressBarView;
import com.moto.main.R;
import com.moto.mymap.MyMapApplication;
import com.moto.utils.UrlUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class UserPhotosView extends LinearLayout implements OnClickListener {
	private View mView;
	private ScrollViewPager mViewPager;

	private LayoutInflater mInflater;

	private View[] mPhotoBlocks;
	private View mPhotoPage1;
	private View mPhotoPage2;
	private View mPhotoPage3;
	private List<View> mPageViews = new ArrayList<View>();
	private List<String> mPhotos = new LinkedList<String>();
	private int mTotalPage;
	private int mWidth;
	private long time = 0;
	private DisplayImageOptions Originaloptions;

	private onPagerPhotoItemClickListener mOnPagerPhotoItemClickListener;

	public UserPhotosView(Context context) {
		super(context);
		init(context);
	}

	public UserPhotosView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public UserPhotosView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	private void init(Context context) {
		Originaloptions = ImageMethod.GetOriginalOptions();
		DisplayMetrics metric = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(metric);
		mWidth = metric.widthPixels;
		mInflater = LayoutInflater.from(context);
		mView = mInflater.inflate(R.layout.common_userphoto, null);
		mViewPager = (ScrollViewPager) mView
				.findViewById(R.id.userphoto_scrollviewpager);

	}

	public void setPhotos(List<String> photos) {
		removeAllViews();
		if (photos == null || photos.isEmpty()) {
			return;
		}
		mPhotos = photos;
		if (mPhotos.size() <= 8) {
			mTotalPage = 1;
		} else if (mPhotos.size() <= 16) {
			mTotalPage = 2;
		} else {
			mTotalPage = 3;
		}
		initViewPager();
		initViewPagerParams();
		addView(mView);
	}

	private void initViewPager() {
		switch (mTotalPage) {
		case 1:
			mPhotoPage1 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			initPhotoBlocks(mPhotoPage1, 1);
			mPageViews.add(mPhotoPage1);
			break;

		case 2:
			mPhotoPage1 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			mPhotoPage2 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			initPhotoBlocks(mPhotoPage1, 1);
			initPhotoBlocks(mPhotoPage2, 2);
			mPageViews.add(mPhotoPage1);
			mPageViews.add(mPhotoPage2);
			break;

		case 3:
			mPhotoPage1 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			mPhotoPage2 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			mPhotoPage3 = mInflater.inflate(R.layout.common_userphoto_avatar,
					null);
			initPhotoBlocks(mPhotoPage1, 1);
			initPhotoBlocks(mPhotoPage2, 2);
			initPhotoBlocks(mPhotoPage3, 3);
			mPageViews.add(mPhotoPage1);
			mPageViews.add(mPhotoPage2);
			mPageViews.add(mPhotoPage3);
			break;
		}
		mViewPager.setAdapter(new ViewPagerAdapter());
	}

	private void initPhotoBlocks(View view, int page) {
		mPhotoBlocks = new View[4];
		mPhotoBlocks[0] = view
				.findViewById(R.id.userphoto_avatar_layout_photo_block1);
		mPhotoBlocks[1] = view
				.findViewById(R.id.userphoto_avatar_layout_photo_block2);
		mPhotoBlocks[2] = view
				.findViewById(R.id.userphoto_avatar_layout_photo_block3);
		mPhotoBlocks[3] = view
				.findViewById(R.id.userphoto_avatar_layout_photo_block4);
//		mPhotoBlocks[4] = view
//				.findViewById(R.id.userphoto_avatar_layout_photo_block5);
//		mPhotoBlocks[5] = view
//				.findViewById(R.id.userphoto_avatar_layout_photo_block6);
//		mPhotoBlocks[6] = view
//				.findViewById(R.id.userphoto_avatar_layout_photo_block7);
//		mPhotoBlocks[7] = view
//				.findViewById(R.id.userphoto_avatar_layout_photo_block8);
		int margin = getMargin(2);
		int widthAndHeight = (mWidth - margin * 8) / 4;
		for (int i = 0; i < mPhotoBlocks.length; i++) {
			ViewGroup.LayoutParams params = mPhotoBlocks[i].getLayoutParams();
			params.width = widthAndHeight;
			params.height = widthAndHeight;
			mPhotoBlocks[i].setLayoutParams(params);
			mPhotoBlocks[i].setVisibility(View.GONE);
		}
		view.findViewById(R.id.userphoto_avatar_layout_page_line1).invalidate();
//		view.findViewById(R.id.userphoto_avatar_layout_page_line2).invalidate();
		int startPosition = (page - 1) * 4;
		int endPosition = page * 4 < mPhotos.size() ? page * 4 : mPhotos.size();
		endPosition = endPosition < 24 ? endPosition : 24;
		for (int i = startPosition; i < endPosition; i++) {
			View root = mPhotoBlocks[i - startPosition];
			root.setVisibility(View.VISIBLE);
			ImageView photo = (ImageView) root
					.findViewById(R.id.userphoto_avatar_item_riv_avatar);
			final ProgressBarView progressBarView = (ProgressBarView)root.findViewById(R.id.userphoto_avatar_item_progress_View);
			MyMapApplication.imageLoader.displayImage(UrlUtils.imageUrl_avatar(mPhotos.get(i),100),  photo,Originaloptions,new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					// TODO Auto-generated method stub
					super.onLoadingStarted(imageUri, view);
					progressBarView.setProgressNotInUiThread(0);
					progressBarView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					// TODO Auto-generated method stub
					super.onLoadingFailed(imageUri, view, failReason);
					progressBarView.setVisibility(View.GONE);
				}

				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					// TODO Auto-generated method stub
					super.onLoadingComplete(imageUri, view, loadedImage);
					progressBarView.setVisibility(View.GONE);
					progressBarView.setProgressNotInUiThread(100);
				}
				
			},new ImageLoadingProgressListener() {
				
				@Override
				public void onProgressUpdate(String imageUri, View view, int current,
						int total) {
					// TODO Auto-generated method stub
					if((System.currentTimeMillis() - time)>1000)
					{
						time = System.currentTimeMillis();
						progressBarView.setProgressNotInUiThread(Math.round(100.0f * current / total));
					}
					
				}
			});
			ImageView cover = (ImageView) root
					.findViewById(R.id.userphoto_avatar_item_iv_cover);
			cover.setTag(i);
			cover.setOnClickListener(this);
		}
		for (int i = endPosition - startPosition; i < 4; i++) {
			mPhotoBlocks[i].setVisibility(View.INVISIBLE);
		}
	}

	private void initViewPagerParams() {
		int height;
		int margin = getMargin(2);
		if (mPhotos.size() <= 4) {
			height = (mWidth - margin * 8) / 4 + 2;
		} else {
			height = (mWidth - margin * 8) / 4 + 2;
		}
		ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
		params.height = height;
		mViewPager.setLayoutParams(params);
	}

	@Override
	public void onClick(View v) {
		if (mOnPagerPhotoItemClickListener != null) {
			int position = (Integer) v.getTag();
			mOnPagerPhotoItemClickListener.onItemClick(v, position);
		}
	}

	private int getMargin(int value) {
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, value, getResources()
						.getDisplayMetrics());
		return margin;
	}

	public void setOnPagerPhotoItemClickListener(onPagerPhotoItemClickListener l) {
		mOnPagerPhotoItemClickListener = l;
	}

	private class ViewPagerAdapter extends PagerAdapter {

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mPageViews.get(arg1));
		}

		public void finishUpdate(View arg0) {

		}

		public int getCount() {

			return mPageViews.size();
		}

		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mPageViews.get(arg1));
			return mPageViews.get(arg1);

		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		public Parcelable saveState() {
			return null;
		}

		public void startUpdate(View arg0) {

		}
	}

	public interface onPagerPhotoItemClickListener {
		void onItemClick(View view, int position);
	}
}
