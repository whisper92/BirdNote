package com.bird.note.customer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;

/**
 * 编辑界面用于指示当前等级的View
 * 
 * @author wangxianpeng
 * 
 */

public class LevelFlag extends View {
	private int mCurrentLeve = 0;

	private int[] levelImages = { R.drawable.mark_blue, R.drawable.mark_green,
			R.drawable.mark_yellow, R.drawable.mark_red };

	public int getCurrentLeve() {
		return mCurrentLeve;
	}

	public void setCurrentLeve(int mCurrentLeve) {
		this.mCurrentLeve = mCurrentLeve;
	}

	public LevelFlag(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        setBackgroundResource(R.drawable.mark_blue);
	}

	public LevelFlag(Context context, AttributeSet attrs) {
		this(context, attrs, 0);

	}

	public LevelFlag(Context context) {
		this(context, null);

	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
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
		setMeasuredDimension(width, height);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			setBackgroundResource(levelImages[nextLevel()]);
			break;
			
		case MotionEvent.ACTION_UP:
			invalidate();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 循环切换level
	 */
	public int nextLevel() {
		if (mCurrentLeve == 3) {
			mCurrentLeve = 0;
			Log.e("wxp", "level------->"+mCurrentLeve);
			return mCurrentLeve;
		} else {
			Log.e("wxp", "level------->"+mCurrentLeve);
			mCurrentLeve= mCurrentLeve +1;
           return mCurrentLeve;
		}
		
		
	}
}
