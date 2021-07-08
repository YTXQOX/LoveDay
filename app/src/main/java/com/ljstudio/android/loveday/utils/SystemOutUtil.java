package com.ljstudio.android.loveday.utils;


import android.animation.ValueAnimator;
import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

public class SystemOutUtil {

//	private final static boolean isRelease = true;
	private final static boolean isRelease = false;
	private final static String APPLICATION_TAG = "LOVELY_DAY---->";
	
	
	public static void sysOut(Object object) {
		if (isRelease) {
			// 软件发布
		} else {
			System.out.println(APPLICATION_TAG + object.toString());
		}
	}

	/**
	 * Activity窗口快速变暗
	 */
	private void dimBackground(Activity context, final float from, final float to) {
		final Window window = context.getWindow();
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
		valueAnimator.setDuration(500);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				WindowManager.LayoutParams params = window.getAttributes();
				params.alpha = (Float) animation.getAnimatedValue();
				window.setAttributes(params);
			}
		});
	}

}
