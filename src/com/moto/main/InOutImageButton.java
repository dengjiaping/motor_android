package com.moto.main;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

/**
 *	�������ջذ�ť
 */
public class InOutImageButton extends ImageButton {
	
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

	public InOutImageButton(Context context) {
		super(context);
	}

	public InOutImageButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public InOutImageButton(Context context, AttributeSet attrs) {
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
		if (mAnimation instanceof AbstractInOutAnimationSet) {
			setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onAnimationEnd() {
		super.onAnimationEnd();
		if (mAnimation instanceof AbstractInOutAnimationSet) {
			AbstractInOutAnimationSet animation = (AbstractInOutAnimationSet) mAnimation;
			if (animation.getDirection() == AbstractInOutAnimationSet.Direction.OUT) {
				Log.e("Test", "Button GONE");
				setVisibility(View.GONE);


			} else {
				Log.e("Test", "Button VISIBLE");
				setVisibility(View.VISIBLE);
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
