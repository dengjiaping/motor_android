package com.moto.main;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

/**
 *	�����ջذ�ť�ƶ��켣��������
 */
public class PopupButtonAnimationSet extends AbstractInOutAnimationSet {

	private static final int mXOffset = 16;
	private static final int mYOffset = 243;
	
	public PopupButtonAnimationSet(Direction directioin, long duration,
			View subject) {
		super(directioin, duration, new View[]{ subject});
	}

/**  480 * 800�ֻ���ֵ�仯����
����
x = 0.0 , y = 456.0
x = -62.0 , y = 446.0
x = -118.0 , y = 418.0
x = -162.0 , y = 374.0
x = -190.0 , y = 318.0
x = -200.0 , y = 256.0
*/
	@Override
	protected void addInAnimation(View[] views) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) views[0].getLayoutParams();
		// ����ÿ�������ջذ�ť���ƶ��켣
		float x = -layoutParams.leftMargin + mXOffset;
		float y = layoutParams.bottomMargin + mYOffset;
		
		// �ƶ�λ�ö���
		TranslateAnimation translateAnimation = new TranslateAnimation(x, 0.0f, y, 0.0f);
		addAnimation(translateAnimation);
	}
	
/**
�ջ�
x = 0.0 , y = 456.0
x = -62.0 , y = 446.0
x = -118.0 , y = 418.0
x = -162.0 , y = 374.0
x = -190.0 , y = 318.0
x = -200.0 , y = 256.0
 */	

	@Override
	protected void addOutAnimation(View[] views) {
		ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) views[0].getLayoutParams();
		float x = -layoutParams.leftMargin + mXOffset;
		float y = layoutParams.bottomMargin + mYOffset;
		TranslateAnimation animation = new TranslateAnimation(0.0F, x, 0.0F, y);
		addAnimation(animation);
	}

}
