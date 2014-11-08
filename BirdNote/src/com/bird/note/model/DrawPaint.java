package com.bird.note.model;

import android.graphics.Paint;

public class DrawPaint  {

	private static Paint drawPaint = new Paint();

	private DrawPaint() {
	}

	public static Paint getInstance() {
		drawPaint.setAntiAlias(true);
		drawPaint.setDither(true);
		drawPaint.setStyle(Paint.Style.STROKE);
		drawPaint.setStrokeJoin(Paint.Join.ROUND);
		drawPaint.setStrokeCap(Paint.Cap.ROUND);
		return drawPaint;
	}
}
