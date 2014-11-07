package com.bird.note.test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bird.note.model.PenDrawPath;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class TestBackPenView extends View {
	private final float TOUCH_TOLERANCE = 4;
	private Context mContext;
	/*
	 * 当前使用的Paint
	 */
	private Paint mCurPaint;

	/*
	 * 每一笔对应的路径
	 */
	private Path mPath = null;
	private float posX = 0;
	private float posY = 0;

	/*
	 * 是否处在擦除模式： false：绘图模式 true：擦出模式
	 */
	private boolean mIsCleanMode = false;

	/*
	 * 绘图模式对应的paint,canvas,bitmap
	 */
	private Paint mDrawPaint;
	private Canvas mDrawCanvas;
	private Bitmap mDrawBitmap;

	/*
	 * 擦除模式对应的paint,canvas,bitmap
	 */
	private Paint mCleanPaint;
	private Canvas mCleanCanvas;
	private Bitmap mCleanBitmap;

	/*
	 * 绘图模式的画笔颜色和粗细
	 */
	private int mDrawPaintColor = 0xFFCCCCCC;
	private int mDrawPaintWidth = 10;

	private DrawPath mDrawPath = null;
	/*
	 * 保存绘制过的所有路径，用于撤销操作
	 */
	
	private List<DrawPath> mSavePath = null;
	/*
	 * 保存刚刚撤销操作的路径，用于重做
	 */
	private List<DrawPath> mDeletePath = null;

	public TestBackPenView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init(context);
	}

	public TestBackPenView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public TestBackPenView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mCurPaint = new Paint();
		mDrawPaint = new Paint();
		mCleanPaint = new Paint();
		mPath = new Path();

		initDrawPaint();

		mDrawBitmap = Bitmap.createBitmap(CommonUtils.getScreenWidth(mContext),
				CommonUtils.getScreenHeight(mContext), Bitmap.Config.ARGB_8888);
		mDrawBitmap.eraseColor(Color.TRANSPARENT);
		
		mCleanBitmap = Bitmap.createBitmap(
				CommonUtils.getScreenWidth(mContext),
				CommonUtils.getScreenHeight(mContext), Bitmap.Config.ARGB_8888);
		mCleanBitmap.eraseColor(Color.TRANSPARENT);

		mDrawCanvas = new Canvas();
		mCleanCanvas = new Canvas();
		mDrawCanvas.setBitmap(mDrawBitmap);
		mCleanCanvas.setBitmap(mCleanBitmap);

		mSavePath = new ArrayList<DrawPath>();
		mDeletePath = new ArrayList<DrawPath>();
	}

	private class DrawPath {
		Path path;
		Paint paint;
	}

	@Override
	public void onDraw(Canvas canvas) {

		canvas.drawBitmap(mDrawBitmap, 0, 0, null);
		if (mPath != null) {
			mDrawCanvas.drawPath(mPath, mCurPaint);
		}

		// canvas.drawRect(10,10,100,100,mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPath = new Path();
			mDrawPath = new DrawPath();
			mPath.moveTo(x, y);
			mDrawPath.paint = new Paint(mCurPaint);
			mDrawPath.path = mPath;
			posX = x;
			posY = y;
			postInvalidate();

			break;
		case MotionEvent.ACTION_MOVE:
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
			mPath.lineTo(posX, posY);
			//mPath.offset(0, -mDrawBitmapDrawHeight);
			// avoid the previous line is cleared when press again
			mDrawCanvas.drawPath(mPath, mCurPaint);
			mSavePath.add(mDrawPath);
			mPath = null;
			postInvalidate();
			break;
		}
		return true;
	}

	public void setPaint(Paint paint) {
		mCurPaint = paint;
		postInvalidate();
	}

	public void savePicture() {
		String filePath = CommonUtils.getSavePath();
		BitmapUtil.writeFile(BitmapUtil.bitmapToBytes(mDrawBitmap), filePath
				+ "/hello.png");
	}

	public void clearImage() {
		mSavePath.clear();
		mDeletePath.clear();

		int width = mDrawCanvas.getWidth();
		int height = mDrawCanvas.getHeight();
		mDrawBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		mDrawCanvas.setBitmap(mDrawBitmap);

		postInvalidate();

	}

	public void undo() {
		int nSize = mSavePath.size();
		if (nSize >= 1) {
			mDeletePath.add(0, mSavePath.get(nSize - 1));
			mSavePath.remove(nSize - 1);
		} else
			return;

		int width = mDrawCanvas.getWidth();
		int height = mDrawCanvas.getHeight();
		mDrawBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		mDrawCanvas.setBitmap(mDrawBitmap);

		Iterator<DrawPath> iter = mSavePath.iterator();
		DrawPath temp;
		while (iter.hasNext()) {
			temp = iter.next();
			mDrawCanvas.drawPath(temp.path, temp.paint);
		}
		postInvalidate();

	}

	public void redo() {
		int nSize = mDeletePath.size();
		if (nSize >= 1) {
			mSavePath.add(mDeletePath.get(0));
			mDeletePath.remove(0);
		} else
			return;

		int width = mDrawCanvas.getWidth();
		int height = mDrawCanvas.getHeight();
		mDrawBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		mDrawCanvas.setBitmap(mDrawBitmap);

		Iterator<DrawPath> iter = mSavePath.iterator();
		DrawPath temp;
		while (iter.hasNext()) {
			temp = iter.next();
			mDrawCanvas.drawPath(temp.path, temp.paint);
		}
		postInvalidate();
	}

	public void initDrawPaint() {
		mIsCleanMode = false;
		mDrawPaint.setAntiAlias(true);
		mDrawPaint.setDither(true);
		mDrawPaint.setColor(mDrawPaintColor);
		mDrawPaint.setStyle(Paint.Style.STROKE);
		mDrawPaint.setStrokeJoin(Paint.Join.ROUND);
		mDrawPaint.setStrokeCap(Paint.Cap.ROUND);
		mDrawPaint.setStrokeWidth(5);
		mCurPaint = new Paint(mDrawPaint);
	}

	public void setCleanPaint() {
		mIsCleanMode = true;

		mCleanPaint.setColor(Color.BLUE);
		mCleanPaint.setAntiAlias(true);
		mCleanPaint.setDither(true);
		mCleanPaint.setStyle(Paint.Style.STROKE);
		mCleanPaint.setStrokeJoin(Paint.Join.ROUND);
		mCleanPaint.setStrokeCap(Paint.Cap.ROUND);
		mCleanPaint.setStrokeWidth(50);

		mCleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
		mCleanPaint.setAlpha(0);
		// 设置画笔的痕迹是透明的，从而可以看到背景图片

		mCurPaint = new Paint(mCleanPaint);
	}
}
