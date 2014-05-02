package com.moto.tryimage;

import com.moto.main.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class ImageFactoryActivity extends FragmentActivity {
	private ViewFlipper mVfFlipper;
	private Button mBtnLeft;
	private Button mBtnRight;
	private ImageView toprightView;
	private TextView title;

	private ImageFactoryCrop mImageFactoryCrop;
	private ImageFactoryFliter mImageFactoryFliter;
	private String mPath;
	private String mNewPath;
	private int mIndex = 0;
	private String mType;

	public static final String TYPE = "type";
	public static final String CROP = "crop";
	public static final String FLITER = "fliter";

	/**
	 * 屏幕的宽度、高度、密度
	 */
	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imagefactory);
		
		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;
		
		initViews();
		initEvents();
		init();
	}
	protected void initViews() {
		mVfFlipper = (ViewFlipper) findViewById(R.id.imagefactory_vf_viewflipper);
		mBtnLeft = (Button) findViewById(R.id.imagefactory_btn_left);
		mBtnRight = (Button) findViewById(R.id.imagefactory_btn_right);
	}
	protected void initEvents() {
		mBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 0) {
					setResult(RESULT_CANCELED);
					finish();
				} else {
					if (FLITER.equals(mType)) {
						setResult(RESULT_CANCELED);
						finish();
					} else {
						mIndex = 0;
						initImageFactory();
						mVfFlipper.setInAnimation(ImageFactoryActivity.this,
								R.anim.push_right_in);
						mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
								R.anim.push_right_out);
						mVfFlipper.showPrevious();
					}
				}
			}
		});
		mBtnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mIndex == 1) {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryFliter
							.getBitmap());
					Intent intent = new Intent();
					intent.putExtra("path", mNewPath);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					mNewPath = PhotoUtils.savePhotoToSDCard(mImageFactoryCrop
							.cropAndSave());
					mIndex = 1;
					initImageFactory();
					mVfFlipper.setInAnimation(ImageFactoryActivity.this,
							R.anim.push_left_in);
					mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
							R.anim.push_left_out);
					mVfFlipper.showNext();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (mIndex == 0) {
			setResult(RESULT_CANCELED);
			finish();
		} else {
			if (FLITER.equals(mType)) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				mIndex = 0;
				initImageFactory();
				mVfFlipper.setInAnimation(ImageFactoryActivity.this,
						R.anim.push_right_in);
				mVfFlipper.setOutAnimation(ImageFactoryActivity.this,
						R.anim.push_right_out);
				mVfFlipper.showPrevious();
			}
		}
	}

	private void init() {
		mPath = getIntent().getStringExtra("path");
		mType = getIntent().getStringExtra(TYPE);
		mNewPath = new String(mPath);
		if (CROP.equals(mType)) {
			mIndex = 0;
		} else if (FLITER.equals(mType)) {
			mIndex = 1;
			mVfFlipper.showPrevious();
		}
		initImageFactory();
	}

	private void initImageFactory() {
		switch (mIndex) {
		case 0:
			if (mImageFactoryCrop == null) {
				mImageFactoryCrop = new ImageFactoryCrop(this,
						mVfFlipper.getChildAt(0));
			}
			mImageFactoryCrop.init(mPath, mScreenWidth, mScreenHeight);
			toprightView = (ImageView)findViewById(R.id.photo_round);
			toprightView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (mIndex) {
					case 0:
						if (mImageFactoryCrop != null) {
							mImageFactoryCrop.Rotate();
						}
						break;

					case 1:
						if (mImageFactoryFliter != null) {
							mImageFactoryFliter.Rotate();
						}
						break;
					}
				}
			});
			title = (TextView)findViewById(R.id.photo);
			title.setText("裁切图片");
			mBtnLeft.setText("取    消");
			mBtnRight.setText("确    认");

			break;

		case 1:
			if (mImageFactoryFliter == null) {
				mImageFactoryFliter = new ImageFactoryFliter(this,
						mVfFlipper.getChildAt(1));
			}
			mImageFactoryFliter.init(mNewPath);
			title.setText("滤镜图片");
			mBtnLeft.setText("取    消");
			mBtnRight.setText("完    成");
			break;
		}
	}
}
