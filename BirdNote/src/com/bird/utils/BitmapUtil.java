package com.bird.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Log;
import android.content.Context;
import android.graphics.drawable.*;
import android.graphics.*;
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
	
	public static Bitmap drawableToBitmap(Context context,int drawableid){
	
	Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(),drawableid);
      return bitmap;
	}

}
