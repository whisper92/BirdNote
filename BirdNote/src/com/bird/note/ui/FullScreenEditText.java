package com.bird.note.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 编辑文字对应的View
 */
public class FullScreenEditText extends EditText {

	float posX;
	float posY;

	Paint mPaint;
	boolean actionDown = false;

	public FullScreenEditText(Context context) {
		this(context, null);
	}

	public FullScreenEditText(Context context, AttributeSet attrs) {
		this(context, null, 0);

	}

	public FullScreenEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {
		mPaint = getPaint();
	}

	@Override
	public void addTextChangedListener(TextWatcher watcher) {
		// TODO Auto-generated method stub
		super.addTextChangedListener(watcher);
	}

	@Override
	public void append(CharSequence text, int start, int end) {
		// TODO Auto-generated method stub
		super.append(text, start, end);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
/*		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			posX = event.getX();
			posY = event.getY();
			actionDown=true;
			postInvalidate();
			break ;
		}*/
		return super.onTouchEvent(event);
	}
/*	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (actionDown) {
			canvas.drawText("hello", posX, posY, mPaint);
			actionDown=false;
		}
	}*/


}
