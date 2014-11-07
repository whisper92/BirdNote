package com.bird.note.ui;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.bird.note.R;

/**
 * 全屏编辑文字对应的View
 */
public class FullScreenEditText extends EditText {

	private float posX;
	private float posY;

	private Paint mPaint;
	private boolean actionDown = false;
	private int mLines;
	private int mLength;

	public FullScreenEditText(Context context) {
		super(context);
		init(context);
	}

	public FullScreenEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FullScreenEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public String getSpaceString(int length) {
		String sumLine = "";
		for (int i = 0; i < length; i++) {
			sumLine += " ";
		}
		return sumLine;
	}

	public void init(Context context) {
		mPaint = getPaint();
		mLines = Integer.valueOf(context.getResources().getString(
				R.string.fullscreen_edit_line));
		mLength = Integer.valueOf(context.getResources().getString(
				R.string.fullscreen_edit_line_lenth));
		String sum = getSpaceString(mLength) + "\n ";
		for (int j = 1; j < mLines; j++) {
			sum = sum + getSpaceString(mLength) + "\n ";
		}
		setText(sum);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
		Log.d("wxp", "  |  start" + start + "  |  " + "lengthBefore" + lengthBefore
				+ "  |  lengthAfter" + lengthAfter);
	
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}
}
