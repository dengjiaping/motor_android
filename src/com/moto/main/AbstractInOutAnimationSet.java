package com.moto.main;

import android.view.View;
import android.view.animation.AnimationSet;

public abstract class AbstractInOutAnimationSet extends AnimationSet {
	
	// ===========================================================
	// Constants
	// ===========================================================

	/**
	 * ��¼��ǰ��������
	 */
	private final Direction mDirectioin;
	
	public enum Direction {
		IN, OUT
	}

	// ===========================================================
	// Constructors
	// ===========================================================

	public AbstractInOutAnimationSet(Direction directioin, long duration, View[] views) {
		super(true);
		
		mDirectioin = directioin;
		
		switch (directioin) {
		case IN:
			addInAnimation(views);
			break;

		case OUT:
			addOutAnimation(views);
			break;
		}
		
		// ����궯��֮��������ִ����ʱ��
		setDuration(duration);
	}
	
	// ===========================================================
	// Public Methods
	// ===========================================================

	public Direction getDirection() {
		return mDirectioin;
	}
	
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	/**
	 * @param views
	 */
	protected abstract void addInAnimation(View[] views);
	
	/**
	 * @param views
	 */
	protected abstract void addOutAnimation(View[] views);
	
	// ===========================================================
	// Private Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================




}
