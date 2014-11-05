package com.bird.note.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.*;
import android.graphics.*;

/**
 * 关于Bitmap的工具类
 * 
 * @author wangxianpeng
 * 
 */
public class BitmapUtil {

	public static byte[] bitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if (bitmap != null) {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} else {
			Log.e("wxp", "TMD is null");
			return null;
		}
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
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

	public static Bitmap bytesToBitmap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	public static byte[] drawableToBytes(Context context,int sourceID){
		return bitmapToBytes(drawableToBitmap(context.getResources().getDrawable(sourceID)));
	}
}
