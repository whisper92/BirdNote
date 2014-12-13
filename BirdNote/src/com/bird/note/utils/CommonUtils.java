package com.bird.note.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Paint;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 通用工具类
 * @author wangxianpeng
 *
 */
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

	public static String getSavePath() {
		String filePath = "";
		filePath = Environment.getExternalStorageDirectory()
				+ "/BirdNotePicture";
		File dirFile = new File(filePath);
		if (dirFile.exists()) {

		} else {
			dirFile.mkdir();
		}
		return filePath;
	}
	
	
	public static String getCurrentTime(){
		SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("yyMMddhhmmss");     
		String   date   =   sDateFormat.format(new   java.util.Date()); 
		return date;
	}
	
	public static String getCurrentDate(){
		SimpleDateFormat   sDateFormat   =   new   SimpleDateFormat("ddhhmm");     
		String   date   =   sDateFormat.format(new   java.util.Date()); 
		return date;
	}
	public static String getDefaultTitle(){
		return "N"+getCurrentDate();
	}
	
	/**
	 * 将整个内容平分成每行宽度相等的字符串
	 * @param origContent
	 * @return
	 */
	public static List<String> measureTextAndSplit(Context context,String origContent,float textsize,int maxWidth){
		List<String> textLines=new ArrayList<String>();
		
		Paint paint=new Paint();
		paint.setTextSize(dpToPx(context, textsize));
		int count = origContent.length();
		int i = 0;
		int j = 0;

		for ( i = 0; i <count; i++) {
			
		}
		return textLines;
	}
	
	public static int dpToPx(Context context,int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}
	
	public static int pxToDp(Context context,int px){
		int dp=0;
		dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
		return dp;
	}
}
