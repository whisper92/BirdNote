package com.bird.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Log;
/**
 * 关于Bitmap的工具类
 * @author wangxianpeng
 *
 */
public class BitmapUtil {
	
	public static byte[] bitmapToBytearray(Bitmap bitmap){
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		if(bitmap !=null){			
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		} else {
			Log.e("wxp","TMD is null");
		}

		return baos.toByteArray();
	}

}
