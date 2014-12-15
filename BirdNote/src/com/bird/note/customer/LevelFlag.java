package com.bird.note.customer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
	public int mCurrentLevel = 0;
	private OnLevelChangeListener onLevelChangeListener = null;
	private int[] levelImages = { R.drawable.mark_blue, R.drawable.mark_green,
			R.drawable.mark_yellow, R.drawable.mark_red };

	public int getCurrentLeve() {
		return mCurrentLevel;
	}

	public void setCurrentLeve(int mCurrentLevel) {
		this.mCurrentLevel = mCurrentLevel;
		setBackgroundResource(levelImages[mCurrentLevel]);
	}

	public LevelFlag(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        setBackgroundResource(levelImages[mCurrentLevel]);
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
		int bgHeight = 76;//BitmapUtil.decodeDrawableToBitmap(bg).getHeight();
		int bgWidth = 40;//BitmapUtil.decodeDrawableToBitmap(bg).getWidth();
		
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
			onLevelChangeListener.changeLevel(mCurrentLevel);
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
		
		if (mCurrentLevel == 3) {
			mCurrentLevel = 0;
			return mCurrentLevel;
		} else {
			mCurrentLevel= mCurrentLevel +1;
           return mCurrentLevel;
		}	
	}
	
	public interface OnLevelChangeListener{
		public void changeLevel(int level);
	}
	
	public void setOnLevelChangeListener(OnLevelChangeListener listener){
		this.onLevelChangeListener = listener;
	}
}
