package com.bird.note.utils;

import android.R.integer;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class CommonUtils {

	/*
	 * 将dp转化为px
	 */
	public static int dpToPx(Context context, float dpValue) {
		int px = 200;
		px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				dpValue, context.getResources().getDisplayMetrics());
		return px;
	}

	public static int getScreenWidth(Context context) {
		WindowManager wManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		WindowManager wManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}
}
