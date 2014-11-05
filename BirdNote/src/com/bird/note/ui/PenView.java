package com.bird.note.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bird.note.model.DrawPath;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 绘制图像对应的View
 * 
 * @author wangxianpeng
 * 
 */
public class PenView extends View {
	private final float TOUCH_TOLERANCE = 4;
	private Context mContext;
	private Paint mPaint;
	Paint mBitmapPaint = null; // used to draw bitmap
	private int mPaintColor = 0x000000;
	private int mPaintWidth = 10;

	private Path mPath = null;
	private float posX = 0;
	private float posY = 0;

	private Canvas mCanvas;

	private Bitmap mDrawBitmap;

	private DrawPath mDrawPath;
	private List<DrawPath> mDrawPathList;

	public PenView(Context context) {
		this(context, null);
	}

	public PenView(Context context, AttributeSet attrs) {
		this(context, null, 0);
	}

	public PenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public void init() {
		mPaint = new Paint();
		/*
		 * mPaint.setColor(mPaintColor); mPaint.setStrokeWidth(mPaintWidth);
		 * mPaint.setAntiAlias(true); mPaint.setStyle(Paint.Style.STROKE);
		 * mPaint.setStrokeJoin(Paint.Join.ROUND);
		 * mPaint.setStrokeCap(Paint.Cap.ROUND);
		 */
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(0xFFCCCCCC);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(5);

		mDrawBitmap = Bitmap.createBitmap(CommonUtils.getScreenWidth(mContext),
				CommonUtils.getScreenHeight(mContext), Bitmap.Config.ARGB_8888);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mDrawPathList = new ArrayList<DrawPath>();
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if (mPath!=null) {
			canvas.drawPath(mPath, mPaint);
		}
		if (mDrawPathList != null) {
			for (DrawPath drawPath : mDrawPathList) {
				canvas.drawPath(drawPath.path, mPaint);
			}

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		float x = event.getX();
		float y = event.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.e("wxp", "onTouchEvent-->ACTION_DOWN");
			// mPaint.reset();
			mPath = new Path();
			mDrawPath = new DrawPath();
			mDrawPath.paint = new Paint(mPaint);
			mDrawPath.path = mPath;

			posX = x;
			posY = y;
			mPath.moveTo(x, y);
			
			postInvalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			Log.e("wxp", "onTouchEvent-->ACTION_MOVE");
			float dx = Math.abs(x - posX);
			float dy = Math.abs(y - posY);
			if (dx >= TOUCH_TOLERANCE || dy > TOUCH_TOLERANCE) {
				mPath.quadTo(posX, posY, (x + posX) / 2, (y + posY) / 2);
				posX = x;
				posY = y;
			}
			postInvalidate();
			break;
		case MotionEvent.ACTION_UP:
			Log.e("wxp", "onTouchEvent-->UP");
			// mPath.lineTo(posX, posY);
			// mPath.offset(0, -mBottomBitmapDrawHeight);
			// avoid the previous line is cleared when press again
			// mCanvas.drawPath(mPath, mPaint);
			// mSavePath.add(mDrawPath);
			// mPath.reset();
			mDrawPathList.add(mDrawPath);
			mPath = null;
			postInvalidate();
			break;

		}
		return true;
	}

	public int getmPaintColor() {
		return mPaintColor;
	}

	public void setmPaintColor(int mPaintColor) {
		this.mPaintColor = mPaintColor;
	}

	public int getmPaintWidth() {
		return mPaintWidth;
	}

	public void setmPaintWidth(int mPaintWidth) {
		this.mPaintWidth = mPaintWidth;
	}

}
