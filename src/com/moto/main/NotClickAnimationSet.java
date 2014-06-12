package com.moto.main;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 *	�����ջذ�ť����󶯻�
 */
public class NotClickAnimationSet extends AbstractInOutAnimationSet {

	public NotClickAnimationSet(long duration) {
		// ֻ�ǿ���ֱ�ӵ���addOutAnimation
		super(Direction.OUT, duration, null);
	}

	@Override
	protected void addInAnimation(View[] views) {
	}

	@Override
	protected void addOutAnimation(View[] views) {
		addAnimation(new ScaleAnimation(1.0F, 0.0F, 1.0F, 0.0F, 
				Animation.RELATIVE_TO_SELF, 0.5F, 1, 0.5F));
		addAnimation(new AlphaAnimation(1.0F, 0.0F));
	}

}
