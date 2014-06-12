package com.moto.main;

import com.moto.main.InOutImageButton;
import com.moto.main.AbstractInOutAnimationSet.Direction;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;

public class AnimationControlUtils {

	// ��������ʱ��λ����
	private static final int DIRECTION = 600;
	
	public static void startAnimations(ViewGroup buttons, Direction directioin) {
		switch (directioin) {
		case IN:
			startAnimationsIn(buttons);
			break;

		case OUT:
			startAnimationsOut(buttons);
			break;
		}
	}
	
	
	/**
	 * @param buttons ���е����ջذ�ť����
	 */
	private static void startAnimationsIn(ViewGroup buttons) {
		final int count = buttons.getChildCount();
		for (int i = 0; i < count	; i++) {
			
			if (buttons.getChildAt(i) instanceof InOutImageButton) {
				// Ϊÿ����ť
				InOutImageButton button = (InOutImageButton) buttons.getChildAt(i);
			
				// ��ť�ƶ�����
				PopupButtonAnimationSet animation = new PopupButtonAnimationSet(Direction.IN, DIRECTION, button);
				long offset = i * 300 / (count - 1);
				// ������������ʱ�䣬Ŀ���ǲ�ͬʱ�ƶ�
				animation.setStartOffset(offset);
				animation.setInterpolator(new OvershootInterpolator(2.0f));
				button.startAnimation(animation);
			}
			
		}
	}
	
	/**
	 * @param composerButtons
	 */
	private static void startAnimationsOut(ViewGroup buttons) {
		final int count = buttons.getChildCount();
		for (int i = 0; i < count	; i++) {
			
			if ((buttons.getChildAt(i) instanceof InOutImageButton)) {
				InOutImageButton button = (InOutImageButton) buttons.getChildAt(i);
				
				PopupButtonAnimationSet animation = new PopupButtonAnimationSet(Direction.OUT, DIRECTION, button);
				long offset = (count -1-i) * 400 / (count - 1);
				animation.setStartOffset(offset);
				animation.setInterpolator(new AnticipateInterpolator(2.0F));
				button.startAnimation(animation);
			}
		}
	}
}
