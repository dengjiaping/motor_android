package com.moto.main;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.moto.main.AbstractInOutAnimationSet.Direction;

public class InOutRelativeLayout extends RelativeLayout {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private Animation mAnimation;
	
	// ===========================================================
	// Constructors
	// ===========================================================

	public InOutRelativeLayout(Context context) {
		super(context);
	}

	public InOutRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InOutRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	// ===========================================================
	// Public Methods
	// ===========================================================
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onAnimationStart() {
		super.onAnimationStart();
		if (mAnimation instanceof AnimationSet) {
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		
		if (mAnimation instanceof AbstractInOutAnimationSet) {
			AbstractInOutAnimationSet animationSet = (AbstractInOutAnimationSet) mAnimation;
			if (animationSet.getDirection() == Direction.OUT) {
				setVisibility(View.GONE);
				Log.e("Test", "RelativeLayout GONE");
			} else {
				setVisibility(View.VISIBLE);
				Log.e("Test", "RelativeLayout VISIBLE");
			}
		}
	}

	@Override
	public void startAnimation(Animation animation) {
		super.startAnimation(animation);
		mAnimation = animation;
		getRootView().postInvalidate();
	}
	
	// ===========================================================
	// Private Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================



	
	
	
}
