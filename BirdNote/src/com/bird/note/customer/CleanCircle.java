package com.bird.note.customer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.bird.note.model.SavedPaint;

/**
 * The circle view show the with of current paint in the PopEraserBox
 *
 * @author wangxianpeng
 * @since 19/12/14
 */
public class CleanCircle extends View {

	private Paint mPaint;
	private float width = SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH;

	private int mWidth = 0;
	private int mHeight = 0;

	public CleanCircle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CleanCircle(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CleanCircle(Context context) {
		this(context, null);
	}

	private void init(Context context) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		SharedPreferences sp = context.getSharedPreferences(SavedPaint.SP_PAINT_KEY, Context.MODE_PRIVATE);
		width = sp.getFloat(SavedPaint.SP_PAINT_CLEAN_WIDTH,SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH);
		mPaint.setStrokeWidth(width);
		mPaint.setColor(Color.WHITE);
	}

	/**
	 * Set the width of Eraser Paint
	 *
	 * @param width
	 *            the width of paint
	 */
	public void setCleanPaintWidth(float width) {
		this.width = width;
		invalidate();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int bgW = 50;// BitmapUtil.decodeDrawableToBitmap(gb).getWidth();
		int bgH = 50;// BitmapUtil.decodeDrawableToBitmap(gb).getHeight();

		int width, height;
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
		canvas.drawCircle(mWidth / 2, mHeight / 2, width / 2, mPaint);
	}

}
