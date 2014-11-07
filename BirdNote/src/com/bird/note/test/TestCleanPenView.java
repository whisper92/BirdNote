package com.bird.note.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bird.note.R;
import com.bird.note.model.PenDrawPath;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 绘制图像对应的PenView
 * 
 * @author wangxianpeng
 * 
 */
public class TestCleanPenView extends View {
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

	/*
	 * 最新使用的画笔和路径
	 */
	private PenDrawPath mDrawPath;

	/*
	 * 保存绘制过的所有路径，用于撤销操作
	 */
	private List<PenDrawPath> mSavePaths;
	/*
	 * 保存刚刚撤销操作的路径，用于重做
	 */
	private List<PenDrawPath> mSaveRedoPaths;

	private OnUndoListener onUndoListener;
	private OnRedoListener onRedoListener;

	public TestCleanPenView(Context context) {
		this(context, null);
	}

	public TestCleanPenView(Context context, AttributeSet attrs) {
		this(context, null, 0);
	}

	public TestCleanPenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		init();
	}

	public void init() {
		/*
		 * 设置背景 setBackgroundResource(R.drawable.ic_launcher);
		 */

		mDrawPaint = new Paint();
		mCleanPaint = new Paint();
		mPath = new Path();
		initDrawPaint();

		mDrawBitmap = Bitmap.createBitmap(CommonUtils.getScreenWidth(mContext),
				CommonUtils.getScreenHeight(mContext), Bitmap.Config.ARGB_8888);
		mCleanBitmap = Bitmap.createBitmap(
				CommonUtils.getScreenWidth(mContext),
				CommonUtils.getScreenHeight(mContext), Bitmap.Config.ARGB_8888);
		mCleanBitmap.eraseColor(Color.TRANSPARENT);

		mDrawCanvas = new Canvas();
		mCleanCanvas = new Canvas();
		mDrawCanvas.setBitmap(mDrawBitmap);
		mCleanCanvas.setBitmap(mCleanBitmap);

				
		/* 绘制蒙版
		  mDrawCanvas.drawBitmap(BitmapUtil.drawableToBitmap(getResources().getDrawable(R.drawable.preview_frame)), 0, 0, null);
		*/		
		mSavePaths = new ArrayList<PenDrawPath>();
		mSaveRedoPaths = new ArrayList<PenDrawPath>();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mDrawBitmap, 0, 0, null);		
		if (mPath != null) {
			mDrawCanvas.drawPath(mPath, mCurPaint);
		}
	//	super.onDraw(canvas);
	}

	/*
	 * 擦除模式的绘制方法
	 */
	public void drawClean() {
		if (mSavePaths != null) {
			for (PenDrawPath drawPath : mSavePaths) {
				mDrawCanvas.drawPath(drawPath.path, drawPath.paint);
			}
		}
	}

	/*
	 * 绘图模式的绘制方法
	 */
	public void drawPen() {
		if (mSavePaths != null) {
			for (PenDrawPath drawPath : mSavePaths) {
				mDrawCanvas.drawPath(drawPath.path, drawPath.paint);
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
			touch_start(x, y);
			invalidate();
			break;

		case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			touch_up();		
			invalidate();
			break;

		}
		return true;
	}

	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mDrawPath = new PenDrawPath();
		mDrawPath.paint = new Paint(mCurPaint);
		mDrawPath.path = mPath;		
		mSavePaths.add(mDrawPath);
		posX = x;
		posY = y;
	}

	private void touch_move(float x, float y) {
		float dx = Math.abs(x - posX);
		float dy = Math.abs(y - posY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(posX, posY, (x + posX) / 2, (y + posY) / 2);
			posX = x;
			posY = y;
		}
	}

	private void touch_up() {
		mPath.lineTo(posX, posY);
		// commit the path to our offscreen
		mCleanCanvas.drawPath(mPath, mCleanPaint);
		// kill this so we don't double draw
		mPath.reset();
	}

	/**
	 * 撤销一笔
	 */
	public void Undo() {
		int undoSize = mSavePaths.size();

		if (undoSize >= 1) {
			PenDrawPath undoPath = (PenDrawPath) mSavePaths.get(undoSize - 1);
			mSaveRedoPaths.add(0, undoPath);
			mSavePaths.remove(undoSize - 1);
			// onUndoListener.undo(undoSize - 1);
			postInvalidate();
		}

	}

	/**
	 * 重做一笔
	 */
	public void Redo() {
		int redoSize = mSaveRedoPaths.size();
		if (redoSize >= 1) {
			PenDrawPath redoPath = (PenDrawPath) mSaveRedoPaths.get(0);
			mSavePaths.add(redoPath);
			mSaveRedoPaths.remove(0);
			postInvalidate();
		}
	}

	public void savePicture() {
		String filePath = CommonUtils.getSavePath();
		BitmapUtil.writeFile(BitmapUtil.bitmapToBytes(mDrawBitmap), filePath
				+ "/hello.png");
	}

	public interface OnUndoListener {
		public void undo(int undoCount);
	}

	public interface OnRedoListener {
		public void redo(int redoCount);
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

	public int getmDrawPaintColor() {
		return mDrawPaintColor;
	}

	public void setmDrawPaintColor(int mDrawPaintColor) {
		this.mDrawPaintColor = mDrawPaintColor;
	}

	public int getmDrawPaintWidth() {
		return mDrawPaintWidth;
	}

	public void setmDrawPaintWidth(int mDrawPaintWidth) {
		this.mDrawPaintWidth = mDrawPaintWidth;
	}

}
