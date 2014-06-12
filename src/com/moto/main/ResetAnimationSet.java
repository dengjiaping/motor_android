package com.moto.main;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class ResetAnimationSet extends AbstractInOutAnimationSet {

	public ResetAnimationSet(long duration) {
		super(Direction.IN, duration, null);
	}

	@Override
	protected void addInAnimation(View[] views) {
		// �ߴ磬͸���Ƚ���
		addAnimation(new ScaleAnimation(0.0F, 1.0F, 0.0F, 1.0F, 1, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F));
		addAnimation(new AlphaAnimation(0.0F, 1.0F));
	}

	@Override
	protected void addOutAnimation(View[] views) {
	}

}
