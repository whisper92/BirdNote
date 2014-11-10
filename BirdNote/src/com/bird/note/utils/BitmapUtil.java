package com.bird.note.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.bird.note.R;

import android.graphics.Bitmap;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.*;
import android.graphics.*;
import android.media.ThumbnailUtils;

/**
 * 关于Bitmap的工具类
 * 
 * @author wangxianpeng
 * 
 */
public class BitmapUtil {

	public static byte[] decodeBitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} else {
			Log.e("wxp", "TMD is null");
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
		// 取 drawable 的长宽
		int w = drawable.getIntrinsicWidth();
		int h = drawable.getIntrinsicHeight();
		// 取 drawable 的颜色格式
		Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565;
		// 建立对应 bitmap
		Bitmap bitmap = Bitmap.createBitmap(w, h, config);
		// 建立对应 bitmap 的画布
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, w, h);
		// 把 drawable 内容画到画布中
		drawable.draw(canvas);
		return bitmap;
	}

	public static byte[] decodeDrawableToBytes(Context context, int sourceID) {
		return decodeBitmapToBytes(decodeDrawableToBitmap(context
				.getResources().getDrawable(sourceID)));
	}

	/**
	 * 将byte数组写入sd卡文件中
	 * @param byteArray 图片的Byte数组
	 * @param fileName 保存的文件名称 e.g. hello.png
	 */
	public static void writeBytesToFile(byte[] byteArray, String fileName) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(new File(CommonUtils.getSavePath()+"/"+fileName));
			fos.write(byteArray);
			fos.flush();
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 生成缩略图
	 * @param context
	 * @param origBitmap
	 * @return byte[]
	 */
	public static byte[] generateThumbnailBytes(Context context,Bitmap origBitmap){
		Bitmap thumbBitmap=ThumbnailUtils.extractThumbnail(origBitmap, (int) context.getResources().getDimension(R.dimen.dimen_create_thumbnail_width), (int)context.getResources().getDimension(R.dimen.dimen_create_thumbnail_height));
		BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(thumbBitmap), "thumb.png");
		return BitmapUtil.decodeBitmapToBytes(origBitmap);
	}
}
