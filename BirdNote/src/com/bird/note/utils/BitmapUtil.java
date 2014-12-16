package com.bird.note.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.bird.note.R;

/**
 * 关于Bitmap的工具类
 * 
 * @author wangxianpeng
 * 
 */
public class BitmapUtil {

	private static String TAG = "BitmapUtil";
	
	public static int getPreBgByBg(int bg){
		int prebg = BitmapUtil.EDIT_BGS_PRE[0];
		switch (bg) {
		case R.drawable.skin_00:
			prebg = BitmapUtil.EDIT_BGS_PRE[0];
			break;
		case R.drawable.skin_01:
			prebg = BitmapUtil.EDIT_BGS_PRE[1];
			break;
		case R.drawable.skin_02:
			prebg = BitmapUtil.EDIT_BGS_PRE[2];
			break;
		case R.drawable.skin_03:
			prebg = BitmapUtil.EDIT_BGS_PRE[3];
			break;
		case R.drawable.skin_04:
			prebg = BitmapUtil.EDIT_BGS_PRE[4];
			break;
			
		default:
			break;
		}
		return prebg;
	}
	
	
	public static byte[] decodeBitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} else {
			Log.e(TAG, "bitmap is null");
			return null;
		}
	}

	public static Bitmap decodeBytesToBitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static Bitmap decodeDrawableToBitmap(Drawable drawable) {
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_4444: Bitmap.Config.RGB_565;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
	}
	
	public static Drawable decodeBitmapToDrawable(Context context,Bitmap bitmap){
			 BitmapDrawable bd= new BitmapDrawable(context.getResources(), bitmap);
			 return bd;
	}

	public static byte[] decodeDrawableToBytes(Context context, int sourceID) {
		return decodeBitmapToBytes(decodeDrawableToBitmap(context.getResources().getDrawable(sourceID)));
	}

	/**
	 * 将byte数组写入sd卡文件中
	 * @param byteArray 图片的Byte数组
	 * @param fileName 保存的文件名称 e.g. hello.png
	 */
	public static void writeBytesToFile(byte[] byteArray, String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(CommonUtils.getSavePath()+"/"+fileName+".png"));
			fos.write(byteArray);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成缩略图
	 * @param context
	 * @param origBitmap
	 * @return byte[]
	 */
	public static Bitmap generateThumbnailBytes(Context context,Bitmap origBitmap){
		//int width = context.getResources().getDrawable(R.drawable.preview_style00).getIntrinsicWidth();
		//int height = context.getResources().getDrawable(R.drawable.preview_style00).getIntrinsicHeight();
		//Log.e("wxp","width : "+width+" ; height : "+height);
		//Bitmap thumbBitmap=Bitmap.createScaledBitmap(origBitmap, (int) context.getResources().getDimension(R.dimen.dimen_create_thumbnail_width), (int)context.getResources().getDimension(R.dimen.dimen_create_thumbnail_height), false);
		Bitmap thumbBitmap=Bitmap.createScaledBitmap(origBitmap,153, 253, false);
		return thumbBitmap;
	}
	
	/**
	 * 合并图层
	 */
	public static Bitmap mergeBitmap(Context context,Bitmap drawBmp,Bitmap textBmp){
		int w =context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_width);
		int h = context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_height);
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		canvas.drawBitmap(Bitmap.createScaledBitmap(drawBmp, w, h, false),0, 0, null);	
		canvas.drawBitmap(Bitmap.createScaledBitmap(textBmp, w, h, false),0, 0, null);
		return bitmap;
	}
	/**
	 * 合并图层
	 */
	public static Bitmap mergeBitmap(Context context,Bitmap bgBmp,Bitmap drawBmp,Bitmap textBmp){
		int w =context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_width);
		int h = context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_height);
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		canvas.drawBitmap(Bitmap.createScaledBitmap(bgBmp, w, h, false),0, 0, null);	
		canvas.drawBitmap(Bitmap.createScaledBitmap(drawBmp, w, h, false),0, 0, null);	
		canvas.drawBitmap(Bitmap.createScaledBitmap(textBmp, w, h, false),0, 0, null);
		return bitmap;
	}
	/**
	 * 内置背景图片
	 */
	public static int[] EDIT_BGS_PRE= new int[]{R.drawable.skin_small_00,
		R.drawable.skin_small_01, R.drawable.skin_small_02,
		R.drawable.skin_small_03, R.drawable.skin_small_04 };
	public static int[] EDIT_BGS= new int[]{R.drawable.skin_00,R.drawable.skin_01,R.drawable.skin_02,R.drawable.skin_03,R.drawable.skin_04};
}
