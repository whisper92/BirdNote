package com.bird.note.utils;

import java.io.ByteArrayInputStream;
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
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class BitmapUtil {

	private static String TAG = "BitmapUtil";
	private static final int COMPOPTION = 100;
	public static int getPreBgByBg(int bg){
		int prebg = BitmapUtil.EDIT_BGS_PRE[0];
		switch (bg) {
		case R.drawable.th01_skin_00:
			prebg = BitmapUtil.EDIT_BGS_PRE[0];
			break;

		case R.drawable.th01_skin_01:
			prebg = BitmapUtil.EDIT_BGS_PRE[1];
			break;

		case R.drawable.th01_skin_02:
			prebg = BitmapUtil.EDIT_BGS_PRE[2];
			break;

		case R.drawable.th01_skin_03:
			prebg = BitmapUtil.EDIT_BGS_PRE[3];
			break;

		case R.drawable.th01_skin_04:
			prebg = BitmapUtil.EDIT_BGS_PRE[4];
			break;

		default:
			break;
		}
		return prebg;
	}

	public static int getCoverBgByLevel(int bg){
		return BitmapUtil.EDIT_COVER_PRE[bg];
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
		Bitmap.Config config =  Bitmap.Config.ARGB_8888;
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		drawable.draw(canvas);
		return bitmap;
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
	 * 合并图层,bitmap已回收
	 */
	public static Bitmap mergeBitmap(Context context,Bitmap bgBmp,Bitmap drawBmp,Bitmap textBmp){
		int w =context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_width);
		int h = context.getResources().getDimensionPixelSize(R.dimen.dimen_edit_canvas_height);
		Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas();
		canvas.setBitmap(bitmap);
		canvas.drawBitmap(bgBmp,0, 0, null);
		canvas.drawBitmap(drawBmp,0, 0, null);
		canvas.drawBitmap(textBmp,0, 0, null);
		return bitmap;
	}


	/**
	 * 内置背景图片
	 */

	public static int[] EDIT_COVER_PRE= new int[]{R.drawable.th01_cover_00,R.drawable.th01_cover_01, R.drawable.th01_cover_02,R.drawable.th01_cover_03 };

	public static int[] EDIT_BGS_PRE= new int[]{R.drawable.th01_skin_small_00,R.drawable.th01_skin_small_01, R.drawable.th01_skin_small_02,R.drawable.th01_skin_small_03, R.drawable.th01_skin_small_04 };

	public static int[] EDIT_BGS= new int[]{R.drawable.th01_skin_00,R.drawable.th01_skin_01,R.drawable.th01_skin_02,R.drawable.th01_skin_03,R.drawable.th01_skin_04};
}
