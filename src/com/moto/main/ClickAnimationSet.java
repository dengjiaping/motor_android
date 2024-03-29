package com.moto.main;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 *	�����ť�Ŵ���ʧ��������
 */
public class ClickAnimationSet extends AbstractInOutAnimationSet {

	public ClickAnimationSet(long duration) {
		super(Direction.OUT, duration, null);
	}

	@Override
	protected void addInAnimation(View[] views) {
		
	}

	@Override
	protected void addOutAnimation(View[] views) {
		addAnimation(new ScaleAnimation(1.0F, 5.0F, 1.0F, 5.0F, 1, 0.5F,
				Animation.RELATIVE_TO_SELF, 0.5F));
    	addAnimation(new AlphaAnimation(1.0F, 0.0F));
	}

}
