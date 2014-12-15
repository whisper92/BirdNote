package com.bird.note.customer;

import android.R.integer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.bird.note.model.SavedPaint;
import com.bird.note.utils.BitmapUtil;

public class ColorCircle extends View {

	private Paint mPaint;
	private float width = SavedPaint.DEFAULT_PAINT_WIDTH;
	private int color = SavedPaint.DEFAULT_PAINT_COLOR;

	private int mWidth = 0;
	private int mHeight = 0;
	public ColorCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ColorCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public ColorCircle(Context context) {
		this(context, null);
	}

	public void init(Context context) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		SharedPreferences sp = context.getSharedPreferences(SavedPaint.SP_PAINT_KEY, Context.MODE_PRIVATE);
		color = sp.getInt(SavedPaint.SP_PAINT_COLOR,SavedPaint.DEFAULT_PAINT_COLOR);
		width = sp.getFloat(SavedPaint.SP_PAINT_WIDTH,SavedPaint.DEFAULT_PAINT_WIDTH);

		mPaint.setColor(color);
		mPaint.setStrokeWidth(width);
	}

	public void setPaintWidth(float width) {
		this.width = width;
		invalidate();
	}

	public void setPaintColor(int color) {
		this.color = color;
		mPaint.setColor(color);
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int widthSize  = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		
		Drawable gb = getBackground();
		//int bgW = BitmapUtil.decodeDrawableToBitmap(gb).getWidth();
		//int bgH = BitmapUtil.decodeDrawableToBitmap(gb).getHeight();
		int bgW = 312;
		int bgH = 72;
		int width,height;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = bgW;
		}
		
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = bgH;
		}
		
		mWidth = width;
		mHeight = height;
		setMeasuredDimension(width, height);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawCircle(mWidth/2, mHeight/2, width / 2, mPaint);
	}

}
