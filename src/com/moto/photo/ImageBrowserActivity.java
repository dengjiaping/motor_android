package com.moto.photo;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.moto.main.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class ImageBrowserActivity extends Activity implements
		OnPageChangeListener, OnClickListener {

	private ScrollViewPager mSvpPager;
	private PhotoTextView mPtvPage;
	private ImageView mIvDownload;
	private ImageBrowserAdapter mAdapter;
	private String mType;
	private int mPosition;
	private int mTotal;
	private ArrayList<String> carlist = new ArrayList<String>();

	public static final String IMAGE_TYPE = "image_type";
	public static final String TYPE_ALBUM = "image_album";
	public static final String TYPE_PHOTO = "image_photo";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagebrowser);
		initViews();
		initEvents();
		init();
	}

	protected void initViews() {
		mSvpPager = (ScrollViewPager) findViewById(R.id.imagebrowser_svp_pager);
		mPtvPage = (PhotoTextView) findViewById(R.id.imagebrowser_ptv_page);
		mIvDownload = (ImageView) findViewById(R.id.imagebrowser_iv_download);
	}
	protected void initEvents() {
		mSvpPager.setOnPageChangeListener(this);
		mIvDownload.setOnClickListener(this);
	}

	private void init() {
		mType = getIntent().getStringExtra(IMAGE_TYPE);
		if (TYPE_ALBUM.equals(mType)) {
			mIvDownload.setVisibility(View.GONE);
			mPosition = getIntent().getIntExtra("position", 0);
			carlist = (ArrayList<String>) getIntent().getExtras().getSerializable("carlist");
			mTotal = carlist.size();
			if (mPosition > mTotal) {
				mPosition = mTotal - 1;
			}
			if (mTotal > 0) {
				mPosition += 1000 * mTotal;
				mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
				mAdapter = new ImageBrowserAdapter(carlist, mType,ImageBrowserActivity.this);
				mSvpPager.setAdapter(mAdapter);
				mSvpPager.setCurrentItem(mPosition, false);
			}
		} else if (TYPE_PHOTO.equals(mType)) {
			mIvDownload.setVisibility(View.VISIBLE);
			String path = getIntent().getStringExtra("path");
			List<String> photos = new ArrayList<String>();
			photos.add(path);
			mPtvPage.setText("1/1");
//			mAdapter = new ImageBrowserAdapter(mApplication, photos, mType);
			mSvpPager.setAdapter(mAdapter);
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		mPosition = arg0;
		mPtvPage.setText((mPosition % mTotal) + 1 + "/" + mTotal);
	}

	@Override
	public void onClick(View arg0) {
	}

	@Override
	public void onBackPressed() {
		this.finish();
		overridePendingTransition(0, R.anim.zoom_exit);
	}

}
