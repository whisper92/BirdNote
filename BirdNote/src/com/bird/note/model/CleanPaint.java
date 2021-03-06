package com.bird.note.model;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class CleanPaint {

	private static Paint cleanPaint = new Paint();

	private CleanPaint() { }

	public static Paint getInstance() {
		cleanPaint.setAntiAlias(true);
		cleanPaint.setDither(true);
		cleanPaint.setStyle(Paint.Style.STROKE);
		cleanPaint.setStrokeJoin(Paint.Join.ROUND);
		cleanPaint.setStrokeCap(Paint.Cap.ROUND);
		cleanPaint.setColor(Color.BLUE);
		cleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		cleanPaint.setAlpha(0);
		return cleanPaint;
	}

}
