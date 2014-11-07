package com.bird.note.customer;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
	private int curQua;

	private LayoutInflater inflater;

	/*
	 * 是否展开
	 */
	private boolean mScaled = false;

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
		if (mScaled) {
			canvas.drawBitmap(
					BitmapUtil.drawableToBitmap(getResources().getDrawable(
							R.drawable.switch_3)), 0, 0, null);
		} else {
			canvas.drawBitmap(
					BitmapUtil.drawableToBitmap(getResources().getDrawable(
							R.drawable.switch_1)), 0, 0, null);
		}
		super.onDraw(canvas);
	}

	public void init(Context context) {
		mContext = context;
		 setBackgroundResource(R.drawable.quadrant_thumbnail);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int width;
		int height ;
		Drawable bg=getBackground();
		int bgHeight=BitmapUtil.drawableToBitmap(bg).getHeight();
		int bgWidth=BitmapUtil.drawableToBitmap(bg).getWidth();
		if (widthMode == MeasureSpec.EXACTLY)
		{
			width = widthSize;
		} else
		{
		
			width = bgWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY)
		{
			height = heightSize;
		} else
		{
			height = bgHeight;
		}
		Log.e("wxp","width:"+width+"   |   height:"+height);
		setMeasuredDimension(width, height);
	}

	
	

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			toggleScale();
			Log.e("wxp", "clickyourmother" + mScaled);
		}
		return super.onTouchEvent(event);
	}

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
}
