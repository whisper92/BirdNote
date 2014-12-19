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
import android.util.Log;
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
 * The view to draw
 *
 * @author wangxianpeng
 * @since 19/12/14
 */
public class PenView extends View {
	private final float TOUCH_TOLERANCE = 0;
	private Context mContext;
	private Paint mCurentPaint;

	/*
	 * the path of a stroke
	 */
	private Path mPath = null;
	private float posX = 0;
	private float posY = 0;

	private boolean mIsCleanMode = false;

	private Paint mDrawPaint;
	private Canvas mDrawCanvas;
	public Bitmap mDrawBitmap;
	private int mDrawPaintColor = SavedPaint.DEFAULT_PAINT_COLOR;
	private float mDrawPaintWidth = SavedPaint.DEFAULT_PAINT_WIDTH;
	public Bitmap mExistBitmap = null;

	private Paint mCleanPaint;
	private float mCleanPaintWidth = SavedPaint.DEFAULT_CLEAN_PAINT_WIDTH;

	private SavedPaint mSavedPaint;
	private PenDrawPath mDrawPath = null;

	/*
	 * Save all the stroke you drawed used to undo
	 */
	private List<PenDrawPath> mSavePath = null;
	/*
	 * Save the undo path used to redo
	 */
	private List<PenDrawPath> mDeletePath = null;

	public int mCanvasWidth = 0;
	public int mCanvasHeight = 0;

	/*
	 * The circle of the eraser point
	 */
	private Paint mCleanPoint = null;
	private boolean isMoving = false;
	private float mCleanCircleRadius = 0;

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

	private void init(Context context) {
		mContext = context;
		mNoteApplication = (NoteApplication) mContext.getApplicationContext();
		mCanvasWidth = (int) getResources().getDimension(R.dimen.dimen_edit_canvas_width);
		mCanvasHeight = (int) getResources().getDimension(R.dimen.dimen_edit_canvas_height);
		mCurentPaint = new Paint();
		mDrawPaint = DrawPaint.getInstance();
		mCleanPaint = CleanPaint.getInstance();
		mSavedPaint = new SavedPaint(context);
		mPath = new Path();
		initDrawPaint();
		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
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

	/**
	 * Return the Bitmap include exist bitmap and drawed bitmap
	 *
	 * @return the whole bitmap
	 */
	public Bitmap getWholeBitmap() {
		Bitmap wholeBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
		Canvas wholeCanvas = new Canvas(wholeBitmap);
		if (mExistBitmap != null) {
			wholeCanvas.drawBitmap(mExistBitmap, 0, 0, null);
		}
		wholeCanvas.drawBitmap(mDrawBitmap, 0, 0, null);
		return wholeBitmap;
	}

	/**
	 * Set the saved bitmap of the note
	 */
	public void setExistBitmap(Bitmap backBitmap) {
		this.mExistBitmap = backBitmap;
	}

	/**
	 * Invalide the bitmap after setting existed bitmap
	 */
	public void invalidateExistBitmap() {
		if (mExistBitmap != null) {
			mDrawBitmap = Bitmap.createBitmap(getWholeBitmap(), 0, 0, mCanvasWidth, mCanvasHeight);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
		}
		mDrawCanvas.setBitmap(mDrawBitmap);
		postInvalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		if (isMoving == true && mIsCleanMode == true) {
			canvas.drawCircle(posX, posY, mCleanCircleRadius, mCleanPoint);
		}

		canvas.drawBitmap(mDrawBitmap, 0, 0, null);
		if (mPath != null) {
			mDrawCanvas.drawPath(mPath, mCurentPaint);
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
			mCleanCircleRadius = mSavedPaint.getSavedCleanPaintWidth() / 2;
			downx = event.getRawX();
			downy = event.getRawY();
			mDeletePath.clear();
			mPath = new Path();
			mDrawPath = new PenDrawPath();
			mPath.moveTo(x, y);
			mDrawPath.paint = new Paint(mCurentPaint);
			mDrawPath.path = mPath;
			posX = x;
			posY = y;
			if (mNoteApplication != null) {
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
			if ((Math.abs(downx - event.getX())) >= 4
					|| (Math.abs(downy - event.getY())) > 4) {
				mPath.lineTo(posX, posY);
				mDrawCanvas.drawPath(mPath, mCurentPaint);
				mSavePath.add(mDrawPath);
				pathListChangeListener.changeState(mSavePath.size(), mDeletePath.size());
				mPath = null;
				postInvalidate();
			}
			break;
			
		}
		return true;
	}

	/**
	 * Set current using paint
	 *
	 * @param paint
	 */
	public void setPaint(Paint paint) {
		mCurentPaint = paint;
		postInvalidate();
	}

	/**
	 * Clear all of the bitmap
	 */
	public void clearAll() {
		mSavePath.clear();
		mDeletePath.clear();
		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
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
		} else {

		}
		if (mExistBitmap != null) {
			mDrawBitmap = Bitmap.createBitmap(mExistBitmap, 0, 0, mCanvasWidth, mCanvasHeight).copy(Bitmap.Config.ARGB_8888, true);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
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
			mDrawBitmap = Bitmap.createBitmap(mExistBitmap, 0, 0, mCanvasWidth,	mCanvasHeight).copy(Bitmap.Config.ARGB_8888, true);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight, Bitmap.Config.ARGB_8888);
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

	/**
	 * Init the Paint in Draw Mode
	 */
	public void initDrawPaint() {
		mIsCleanMode = false;
		mDrawPaintColor = mSavedPaint.getSavedPaintColor();
		mDrawPaintWidth = mSavedPaint.getSavedPaintWidth();
		mDrawPaint.setColor(mDrawPaintColor);
		mDrawPaint.setStrokeWidth(mDrawPaintWidth);
		mCurentPaint = mDrawPaint;
	}

	/**
	 * Init the Paint in Clean Mode
	 */
	public void setCleanPaint() {
		mIsCleanMode = true;
		mCleanPaintWidth = mSavedPaint.getSavedCleanPaintWidth();
		mCleanPaint.setStrokeWidth(mCleanPaintWidth);
		mCurentPaint = mCleanPaint;
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

	/**
	 * Register a callback tobe invoked when you draw a stroke
	 *
	 * @param listener
	 *            the callback will run
	 */
	public void setOnPathListChangeListenr(OnPathListChangeListener listener) {
		this.pathListChangeListener = listener;
	}
}
