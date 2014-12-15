package com.bird.note.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.bird.note.R;
import com.bird.note.model.CleanPaint;
import com.bird.note.model.DrawPaint;
import com.bird.note.model.PenDrawPath;
import com.bird.note.model.SavedPaint;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;

/**
 * 用于涂鸦的View
 * 
 * @author wangxianpeng
 * 
 */
public class PenView extends View {
	private final float TOUCH_TOLERANCE = 0;
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
	public Bitmap mDrawBitmap;
	public Bitmap mExistBitmap = null;
	/*
	 * 擦除模式对应的paint,canvas,bitmap
	 */
	private Paint mCleanPaint;
	private float mCleanPaintWidth = SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH;
	/*
	 * 绘图模式的画笔颜色和粗细
	 */
	private int mDrawPaintColor = SavedPaint.DEFAULT_PAINT_COLOR;
	private float mDrawPaintWidth = SavedPaint.DEFAULT_PAINT_WIDTH;
	private SavedPaint mSavedPaint;
	private PenDrawPath mDrawPath = null;
	/*
	 * 保存绘制过的所有路径，用于撤销
	 */
	private List<PenDrawPath> mSavePath = null;
	/*
	 * 保存刚刚撤销操作的路径，用于重做
	 */
	private List<PenDrawPath> mDeletePath = null;

	public int mCanvasWidth = 0;
	public int mCanvasHeight = 0;
	/*
	 * 清除模式的笔尖圆圈
	 */
	private Paint mCleanPoint = null;
	boolean isMoving = false;
	float mCleanCircleRadius = 0;
	
	private NoteApplication mNoteApplication = null;
	public PenView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init(context);
	}

	public PenView(Context context, AttributeSet attr) {
		super(context, attr);
		init(context);
	}

	public PenView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * 获取完成的bitmap
	 * 
	 * @return
	 */
	public Bitmap getWholeBitmap() {
		Bitmap wholeBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_4444);
		Canvas wholeCanvas = new Canvas(wholeBitmap);
		if (mExistBitmap != null) {
			wholeCanvas.drawBitmap(mExistBitmap, 0, 0, null);
		}
		wholeCanvas.drawBitmap(mDrawBitmap, 0, 0, null);
		return wholeBitmap;
	}

	/**
	 * 如果是更新笔记的话，要设置已经存在的内容
	 */
	public void setExistBitmap(Bitmap backBitmap) {
		this.mExistBitmap = backBitmap;
	}

	private void init(Context context) {
		mContext = context;
		mNoteApplication = (NoteApplication)mContext.getApplicationContext();
		mCanvasWidth = (int) getResources().getDimension(R.dimen.dimen_edit_canvas_width);
		mCanvasHeight = (int) getResources().getDimension(R.dimen.dimen_edit_canvas_height);
		mCurPaint = new Paint();
		mDrawPaint = DrawPaint.getInstance();
		mCleanPaint = CleanPaint.getInstance();
		mSavedPaint = new SavedPaint(context);
		mPath = new Path();
		initDrawPaint();
		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_4444);
		mDrawBitmap.eraseColor(Color.TRANSPARENT);
		mDrawCanvas = new Canvas();
		mDrawCanvas.setBitmap(mDrawBitmap);
		mSavePath = new ArrayList<PenDrawPath>();
		mDeletePath = new ArrayList<PenDrawPath>();	
		mCleanPoint = new Paint();
		mCleanPoint.setColor(Color.GRAY);
		mCleanPoint.setAntiAlias(true);
		mCleanPoint.setDither(true);
		mCleanPoint.setStyle(Paint.Style.STROKE);
		mCleanPoint.setStrokeJoin(Paint.Join.ROUND);
		mCleanPoint.setStrokeCap(Paint.Cap.ROUND);
		mCleanPoint.setStrokeWidth(2.0f);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (isMoving == true && mIsCleanMode ==true) {
			canvas.drawCircle(posX, posY, mCleanCircleRadius, mCleanPoint);
		}
		
		canvas.drawBitmap(mDrawBitmap, 0, 0, null);
		if (mPath != null) {
			mDrawCanvas.drawPath(mPath, mCurPaint);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		float downx = 0;
		float downy = 0;

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mCleanCircleRadius = mSavedPaint.getSavedCleanPaintWidth()/2;
			downx = event.getRawX();
			downy = event.getRawY();
			mDeletePath.clear();
			mPath = new Path();
			mDrawPath = new PenDrawPath();
			mPath.moveTo(x, y);
			mDrawPath.paint = new Paint(mCurPaint);
			mDrawPath.path = mPath;
			posX = x;
			posY = y;
			if (mNoteApplication!=null) {
				mNoteApplication.setEdited(true);
			}
			postInvalidate();

			break;
		case MotionEvent.ACTION_MOVE:
			isMoving = true;
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
			isMoving = false;
			if ((Math.abs(downx - event.getX())) >= 4|| (Math.abs(downy - event.getY())) > 4) {
				mPath.lineTo(posX, posY);
				mDrawCanvas.drawPath(mPath, mCurPaint);
				mSavePath.add(mDrawPath);
				pathListChangeListener.changeState(mSavePath.size(),mDeletePath.size());
				mPath = null;
				postInvalidate();
			}
			break;
		}
		return true;
	}

	public void setPaint(Paint paint) {
		mCurPaint = paint;
		postInvalidate();
	}

	public void savePicture(int mCurrentQuadrant, String filename) {
		BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(mDrawBitmap), "/" + filename+ "" + mCurrentQuadrant);
	}

	public void clearAll() {
		mSavePath.clear();
		mDeletePath.clear();
		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,
				Bitmap.Config.ARGB_4444);
		mDrawCanvas.setBitmap(mDrawBitmap);
		mExistBitmap = null;
		mDrawCanvas.drawBitmap(mDrawBitmap, 0, 0, mCleanPaint);
		postInvalidate();
		mSavePath.clear();
		mDeletePath.clear();
		pathListChangeListener.changeState(mSavePath.size(), mDeletePath.size());

	}

	public void undo() {
		int nSize = mSavePath.size();
		if (nSize >= 1) {
			mDeletePath.add(0, mSavePath.get(nSize - 1));
			mSavePath.remove(nSize - 1);
		}  else {
			return;
		}
		if (mExistBitmap != null) {
			mDrawBitmap = Bitmap.createBitmap(mExistBitmap, 0, 0, mCanvasWidth,mCanvasHeight).copy(Bitmap.Config.ARGB_4444, true);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_4444);
		}
		mDrawCanvas.setBitmap(mDrawBitmap);
		Iterator<PenDrawPath> iter = mSavePath.iterator();
		PenDrawPath temp;
		while (iter.hasNext()) {
			temp = iter.next();
			mDrawCanvas.drawPath(temp.path, temp.paint);
		}
		pathListChangeListener.changeState(mSavePath.size(), mDeletePath.size());
		postInvalidate();

	}

	public void redo() {
		int nSize = mDeletePath.size();
		if (nSize >= 1) {
			mSavePath.add(mDeletePath.get(0));
			mDeletePath.remove(0);
		} else
			return;

		if (mExistBitmap != null) {
			mDrawBitmap = Bitmap.createBitmap(mExistBitmap, 0, 0, mCanvasWidth,mCanvasHeight).copy(Bitmap.Config.ARGB_4444, true);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_4444);
		}
		mDrawCanvas.setBitmap(mDrawBitmap);
		Iterator<PenDrawPath> iter = mSavePath.iterator();
		PenDrawPath temp;
		while (iter.hasNext()) {
			temp = iter.next();
			mDrawCanvas.drawPath(temp.path, temp.paint);
		}
		pathListChangeListener
				.changeState(mSavePath.size(), mDeletePath.size());
		postInvalidate();
	}

	/**
	 * 刷新已经存在的内容
	 */
	public void invalidateExistBitmap() {
		if (mExistBitmap != null) {
			mDrawBitmap = Bitmap.createBitmap(getWholeBitmap(), 0, 0,mCanvasWidth, mCanvasHeight);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_4444);
		}
		mDrawCanvas.setBitmap(mDrawBitmap);
		postInvalidate();
	}

	public void initDrawPaint() {
		mIsCleanMode = false;
		mDrawPaintColor = mSavedPaint.getSavedPaintColor();
		mDrawPaintWidth = mSavedPaint.getSavedPaintWidth();
		mDrawPaint.setColor(mDrawPaintColor);
		mDrawPaint.setStrokeWidth(mDrawPaintWidth);
		mCurPaint = mDrawPaint;
	}

	public void setCleanPaint() {
		mIsCleanMode = true;
		mCleanPaintWidth = mSavedPaint.getSavedCleanPaintWidth();
		mCleanPaint.setStrokeWidth(mCleanPaintWidth);
		mCurPaint = mCleanPaint;
	}

	public void setCleanPaintWidth(float width) {
		this.mCleanPaintWidth = width;
		mCleanPaint.setStrokeWidth(width);
	}

	public float getCleanPaintWidth() {
		return mCleanPaintWidth;
	}

	public int getDrawPaintColor() {
		return mDrawPaintColor;
	}

	public void setDrawPaintColor(int color) {
		this.mDrawPaintColor = color;
		mDrawPaint.setColor(color);
	}

	public float getDrawPaintWidth() {
		return mDrawPaintWidth;
	}

	public void setDrawPaintWidth(float width) {
		this.mDrawPaintWidth = width;
		mDrawPaint.setStrokeWidth(width);
	}

	OnPathListChangeListener pathListChangeListener;

	public interface OnPathListChangeListener {
		public void changeState(int undocount, int redocount);
	}

	public void setOnPathListChangeListenr(OnPathListChangeListener listener) {
		this.pathListChangeListener = listener;
	}
}
