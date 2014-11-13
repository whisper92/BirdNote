package com.bird.note.customer;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 用于展示画笔粗细和颜色的线条
 * 
 * @author root
 * 
 */
public class ColorLine extends View {

	Paint mPaint;
    float mPaintWidth=1.0f;
    int mPaintColor=0x16cc79;
	public ColorLine(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ColorLine(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ColorLine(Context context) {
		this(context, null);
	}

	public void init(Context context) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setColor(mPaintColor);
		mPaint.setStrokeWidth(mPaintWidth);
	}

	public void setPaint(Paint savedPaint){
		mPaint.setColor(savedPaint.getColor());
		mPaint.setStrokeWidth(savedPaint.getStrokeWidth());
	}
	
	public void setPaintWidth(float width){
		this.mPaintWidth=width;
		mPaint.setStrokeWidth(width);
		invalidate();
	}
	
	public void setPaintColor(int color){
		this.mPaintColor=color;
		mPaint.setColor(color);
		invalidate();
	}
	@Override
	protected void onDraw(Canvas canvas) {

		RectF oval1 = new RectF(70, 20, 170, 120);
		canvas.drawArc(oval1, 225, 90, false, mPaint);
		oval1.set(141, -50, 241, 50);
		canvas.drawArc(oval1, 45, 90, false, mPaint);
	}

}
