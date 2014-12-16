package com.bird.note.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.bird.note.model.SavedPaint;

/**
 * 用于展示画笔粗细和颜色的线条
 * 
 * @author root
 * 
 */
public class ColorLine extends View {

	private Paint mPaint;
	private float mPaintWidth = 5f;
	private int mPaintColor = 0xff000000;

	private int mWidth = 0;
	private int mHeight = 0;
	
	double mSqrt =0;
	double mRadius = 0;
	
	private RECT r1;
	private RECT r2;
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
		mSqrt = Math.sqrt((double)2);
		r1 = new RECT();
		r2 = new RECT();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		SharedPreferences sp = context.getSharedPreferences(SavedPaint.SP_PAINT_KEY, Context.MODE_PRIVATE);
		mPaintColor = sp.getInt(SavedPaint.SP_PAINT_COLOR,SavedPaint.DEFAULT_PAINT_COLOR);
		mPaintWidth = sp.getFloat(SavedPaint.SP_PAINT_WIDTH,SavedPaint.DEFAULT_PAINT_WIDTH);
		mPaint.setColor(mPaintColor);
		mPaint.setStrokeWidth(mPaintWidth);
	}

	public void setPaint(Paint savedPaint) {
		mPaint.setColor(savedPaint.getColor());
		mPaint.setStrokeWidth(savedPaint.getStrokeWidth());
	}

	public void setPaintWidth(float width) {
		this.mPaintWidth = width;
		mPaint.setStrokeWidth(width);
		invalidate();
	}

	public void setPaintColor(int color) {
		this.mPaintColor = color;
		mPaint.setColor(color);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int widthSize  = 312;//MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = 72;//MeasureSpec.getSize(heightMeasureSpec);

		int width,height;
		width = widthSize;
		height = heightSize;

		mWidth = width;
		mHeight = height;
		mRadius = height/mSqrt;

		r1.left = (float)(mHeight-mRadius);
		r1.top = (float)r1.left;
		r1.right = (float)(mHeight+mRadius);
		r1.bottom = (float)r1.right;
		
		r2.left = (float)(2*mHeight-mRadius);
		r2.top = (float)(-mRadius);
		r2.right =(float) (2*mHeight+mRadius);
		r2.bottom = (float)mRadius;
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		RectF oval1 = new RectF(r1.left, r1.top , r1.right, r1.bottom);
		canvas.drawArc(oval1, 225, 90, false, mPaint);
		oval1.set(r2.left, r2.top, r2.right , r2.bottom);
		canvas.drawArc(oval1, 45, 90, false, mPaint);
	}
	
	public class RECT{
		float left;
		float top;
		float right;
		float bottom;
	}

}
