package com.moto.square;
import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class JazzyAdapter extends PagerAdapter{
	
	private ImageView[] mImageViews;
	private JazzyViewPager mViewPager;
	private List<String> mImageUrls = new ArrayList<String>();
	public JazzyAdapter(ImageView[] mImageViews,JazzyViewPager mViewPager,List<String> mImageUrls){
		this.mImageUrls = mImageUrls;
		this.mViewPager = mViewPager;
		this.mImageViews = mImageViews;
	}
	@Override
	public int getCount() {
		return mImageViews.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		if (view instanceof OutlineContainer) {
			return ((OutlineContainer) view).getChildAt(0) == obj;
		} else {
			return view == obj;
		}
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(mViewPager
				.findViewFromObject(position));
	}

	@Override
	public Object instantiateItem(View container, final int position) {
		ImageLoader.getInstance().displayImage(mImageUrls.get(position),
				mImageViews[position]);
		((ViewPager) container).addView(mImageViews[position], 0);
		mViewPager.setObjectForPosition(mImageViews[position], position);
		mImageViews[position].setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		return mImageViews[position];
	}

}
