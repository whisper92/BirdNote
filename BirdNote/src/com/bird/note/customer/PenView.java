package com.bird.note.customer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bird.note.R;
import com.bird.note.model.CleanPaint;
import com.bird.note.model.DrawPaint;
import com.bird.note.model.PenDrawPath;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.CommonUtils;

/**
 * 用于涂鸦的View
 * 
 * @author wangxianpeng
 * 
 */
public class PenView extends View {
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
	public Bitmap mDrawBitmap;
	private Bitmap mExistBitmap=null;
	
	/*
	 * 擦除模式对应的paint,canvas,bitmap
	 */
	private Paint mCleanPaint;

	/*
	 * 绘图模式的画笔颜色和粗细
	 */
	private int mDrawPaintColor = 0xFFCCCCCC;
	private int mDrawPaintWidth = 10;

	private PenDrawPath mDrawPath = null;

	/*
	 * 保存绘制过的所有路径，用于撤销
	 */
	private List<PenDrawPath> mSavePath = null;
	/*
	 * 保存刚刚撤销操作的路径，用于重做
	 */
	private List<PenDrawPath> mDeletePath = null;

	private int mCanvasWidth=0;
	private int mCanvasHeight=0;
	
	
	
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
	 * @return
	 */
	public Bitmap getWholeBitmap(){
		Bitmap wholeBitmap=Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_8888);
		Canvas wholeCanvas=new Canvas(wholeBitmap);
		if (mExistBitmap!=null) {
			wholeCanvas.drawBitmap(mExistBitmap, 0, 0,null);
		}
		wholeCanvas.drawBitmap(mDrawBitmap, 0, 0,null);
		return wholeBitmap;
	}
	
	/**
	 * 如果是更新笔记的话，要设置已经存在的内容
	 */
	public void setExistBitmap(Bitmap backBitmap) {
		this.mExistBitmap= backBitmap;
		//setBackgroundDrawable(BitmapUtil.decodeBitmapToDrawable(mContext, mExistBitmap));
	}
	
	private void init(Context context) {
		mContext = context;
		mCanvasWidth=(int)getResources().getDimension(R.dimen.dimen_edit_canvas_width);
		mCanvasHeight=(int)getResources().getDimension(R.dimen.dimen_edit_canvas_height);
		
		mCurPaint = new Paint();
		mDrawPaint = DrawPaint.getInstance();
		mCleanPaint = CleanPaint.getInstance();
		mPath = new Path();

		initDrawPaint();

		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth,mCanvasHeight, Bitmap.Config.ARGB_8888);
		mDrawBitmap.eraseColor(Color.TRANSPARENT);

		mDrawCanvas = new Canvas();
		mDrawCanvas.setBitmap(mDrawBitmap);

		mSavePath = new ArrayList<PenDrawPath>();
		mDeletePath = new ArrayList<PenDrawPath>();
	}

	@Override
	public void onDraw(Canvas canvas) {

		canvas.drawBitmap(mDrawBitmap, 2, 2, null);
/*		if (mExistBitmap!=null) {
			canvas.drawBitmap(getWholeBitmap(), 0, 0,null);
		}*/
		if (mPath != null) {
			mDrawCanvas.drawPath(mPath, mCurPaint);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDeletePath.clear();
			mPath = new Path();
			mDrawPath = new PenDrawPath();
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
			mDrawCanvas.drawPath(mPath, mCurPaint);
			mSavePath.add(mDrawPath);
			pathListChangeListener.changeState(mSavePath.size(),
					mDeletePath.size());

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

	public void savePicture(int mCurrentQuadrant) {
		BitmapUtil.writeBytesToFile(BitmapUtil.decodeBitmapToBytes(mDrawBitmap),  "/hello"+mCurrentQuadrant+".png");
	}

	public void clearImage() {
		mSavePath.clear();
		mDeletePath.clear();
		mDrawBitmap = Bitmap.createBitmap(mCanvasWidth, mCanvasHeight,Bitmap.Config.ARGB_8888);
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
		
		if (mExistBitmap!=null) {
			mDrawBitmap = Bitmap.createBitmap(getWholeBitmap(),0,0,mCanvasWidth, mCanvasHeight);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth,mCanvasHeight, Bitmap.Config.ARGB_8888);
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

	public void redo() {
		int nSize = mDeletePath.size();
		if (nSize >= 1) {
			mSavePath.add(mDeletePath.get(0));
			mDeletePath.remove(0);
		} else
			return;

		if (mExistBitmap!=null) {
			mDrawBitmap = Bitmap.createBitmap(getWholeBitmap(),0,0,mCanvasWidth, mCanvasHeight);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth,mCanvasHeight, Bitmap.Config.ARGB_8888);
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
	
	public void invalidateExistBitmap(){
		if (mExistBitmap!=null) {
			mDrawBitmap = Bitmap.createBitmap(getWholeBitmap(),0,0,mCanvasWidth, mCanvasHeight);
		} else {
			mDrawBitmap = Bitmap.createBitmap(mCanvasWidth,mCanvasHeight, Bitmap.Config.ARGB_8888);
		}
		mDrawCanvas.setBitmap(mDrawBitmap);
		postInvalidate();
	}

	public void initDrawPaint() {
		mIsCleanMode = false;
		mDrawPaint.setColor(mDrawPaintColor);
		mDrawPaint.setStrokeWidth(5);
		mCurPaint = mDrawPaint;
	}

	public void setCleanPaint() {
		mIsCleanMode = true;
		mCleanPaint.setStrokeWidth(50);
		mCurPaint = mCleanPaint;
	}

	OnPathListChangeListener pathListChangeListener;

	public interface OnPathListChangeListener {
		public void changeState(int undocount, int redocount);
	}

	public void setOnPathListChangeListenr(OnPathListChangeListener listener) {
		this.pathListChangeListener = listener;
	}
}
