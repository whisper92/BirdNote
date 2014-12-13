package com.bird.note.customer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;

/**
 * 编辑笔记时用于切换象限的View
 * 
 * @author wangxianpeng
 * 
 */
public class QuadrantThumbnail extends View {

	private Context mContext;
	/*
	 * 当前象限
	 */
	private int mCurQua = 0;
	private LayoutInflater inflater;
	/*
	 * 是否展开
	 */
	private boolean mScaled = false;
	/*
	 * 当前的宽度
	 */
	private int mWidth;
	/*
	 * 当前的高度
	 */
	private int mHeight;

	/*
	 * 点击的位置
	 */
	private float posX;
	private float posY;
	/*
	 * QuadrantThumbnail的左上角顶点坐标
	 */
	private int mLeft;
	private int mTop;

	public QuadrantThumbnail(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public QuadrantThumbnail(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public QuadrantThumbnail(Context context) {
		this(context, null);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawQuadrant(canvas, mCurQua);
		super.onDraw(canvas);
	}

	public void init(Context context) {
		mContext = context;
		setBackgroundResource(R.drawable.quadrant_thumbnail);

	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		mLeft = left;
		mTop = top;
		super.onLayout(changed, left, top, right, bottom);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height;
		Drawable bg = getBackground();
		int bgHeight = BitmapUtil.decodeDrawableToBitmap(bg).getHeight();
		int bgWidth = BitmapUtil.decodeDrawableToBitmap(bg).getWidth();
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = bgWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = bgHeight;
		}

		mWidth = width;
		mHeight = height;

		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			posX = event.getX();
			posY = event.getY();
			judgeCurrentQuadrantByClick(posX, posY);
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			toggleScale();
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 通过点击的区域判断点击的是哪个象限
	 */
	public void judgeCurrentQuadrantByClick(float posX, float posY) {
		int halfWidth = mWidth / 2;
		int halfHeight = mHeight / 2;
		if (mScaled) {
			if (posX > 0 && posX < halfWidth && posY > 0 && posY < halfHeight) {
				mCurQua = 0;
			}
			if (posX > halfWidth && posX < mWidth && posY > 0&& posY < halfHeight) {
				mCurQua = 1;
			}
			if (posX > 0 && posX < halfWidth && posY > halfHeight&& posY < mHeight) {
				mCurQua = 2;
			}
			if (posX > halfWidth && posX < mWidth && posY > halfHeight&& posY < mHeight) {
				mCurQua = 3;
			}
			quadrantChangeListener.changeQua(mCurQua);
		}
		
	}

	/**
	 * 绘制当前所在的象限对应的方块
	 * 
	 * @param canvas
	 * @param qua
	 */
	public void drawQuadrant(Canvas canvas, int qua) {
		int x = 0;
		int y = 0;
		switch (qua) {
		case 0:
			x = 2;
			y = 2;
			break;
		case 1:
			x = mWidth / 2 + 1;
			y = 2;
			break;
		case 2:
			x = 2;
			y = mHeight / 2 + 1;
			break;
		case 3:
			x = mWidth / 2 + 1;
			y = mHeight / 2 + 1;
			break;

		default:
			break;
		}
		canvas.drawBitmap(BitmapUtil.decodeDrawableToBitmap(getResources().getDrawable(mScaled ? R.drawable.switch_4: R.drawable.switch_4_small)), x, y, null);
	}

	/**
	 * 触发展开或关闭
	 */
	public void toggleScale() {
		if (mScaled) {
			setBackgroundResource(R.drawable.quadrant_thumbnail);
			mScaled = false;
		} else {
			setBackgroundResource(R.drawable.switch_3);
			mScaled = true;
		}
		invalidate();
	}

	private OnQuadrantChangeListener quadrantChangeListener;

	public interface OnQuadrantChangeListener {
		public void changeQua(int qua);
	}

	public void setQuadrantChangeListener(OnQuadrantChangeListener listener) {
		this.quadrantChangeListener = listener;
	}
}
