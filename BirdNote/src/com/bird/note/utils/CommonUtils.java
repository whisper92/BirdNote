package com.bird.note.utils;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.bird.note.R;

import android.content.Context;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class CommonUtils {

	/*
	 * 将dp转化为px
	 */
	public static int dpToPx(Context context, float dpValue) {
		int px = 200;
		px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
		return px;
	}
//
	public static int getScreenWidth(Context context) {
		WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;
	}

	public static int getScreenHeight(Context context) {
		WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		wManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.heightPixels;
	}

	public static String getSavePath() {
		String filePath = "";
		filePath = Environment.getExternalStorageDirectory() + "/BirdNotePicture";
		File dirFile = new File(filePath);
		if (dirFile.exists()) {

		} else {
			dirFile.mkdir();
		}
		return filePath;
	}

	public static String getCurrentTime() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	public static String getCurrentDate() {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("ddhhmm");
		String date = sDateFormat.format(new java.util.Date());
		return date;
	}

	public static String formatUpdateTime(String dateString) {
		String d1 = dateString.substring(0, 10);
		return d1;
	}

	public static String getDefaultTitle(Context context) {
		return context.getString(R.string.app_name) + "" +( ((NoteApplication) context.getApplicationContext()).getNotescount()+1);
	}

	public static String spliteTitle(String origTitle) {
		if (origTitle.length() >= 10) {
			return origTitle.substring(0, 10) + "...";
		} else {
			return origTitle;
		}
	}

	public static int dpToPx(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
	}

	public static int pxToDp(Context context, int px) {
		int dp = 0;
		dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
		return dp;
	}
}
