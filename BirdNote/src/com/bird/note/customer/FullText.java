package com.bird.note.customer;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;

import com.bird.note.R;
import com.bird.note.model.BirdMessage;
import com.bird.note.utils.NoteApplication;

public class FullText extends EditText {

	private Paint mPaint;
	private boolean mFirstDown = true;
	private float mClickPosX = 0;
	private float mClickPosY = 0;
	/*
	 * 点击的是第几行
	 */
	private int mClickLine = 0;
	private int mFullTextWidth = 0;
	private int mFullTextHeight = 0;
	private float mLineHeight = 0;
	private Editable editable;
	private int mSelectSatrt = 0;
	private int mMaxLines = 0;
	private int lineStart = 0;
	private int lineCount = 0;
	private int temp = 0;
	private int dstart = 0;
	private Toast mToast = null;
	private Context mContext;
	private NoteApplication mNoteApplication = null;
	
	public FullText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public FullText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public FullText(Context context) {
		super(context);
		init(context);
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				if (mToast == null) {
					mToast = Toast.makeText(mContext,
							mContext.getString(R.string.fulltext_max),
							Toast.LENGTH_SHORT);
				} else {
					mToast.setText(mContext.getString(R.string.fulltext_max));
				}
				mToast.show();
			}
		};
	};

	public void init(Context context) {
		
		mContext = context;
		mNoteApplication = (NoteApplication)mContext.getApplicationContext();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setTextSize(getTextSize());
		mLineHeight = getLineHeight();
		myHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence s, int start,int before, int count) {
					    mNoteApplication.setEdited(true);
						int lines = getLineCount();
						if (lines > mMaxLines) {
							myHandler.sendEmptyMessage(0);
							String str = s.toString();
							int cursorStart = getSelectionStart();
							int cursorEnd = getSelectionEnd();
							if (cursorStart == cursorEnd&& cursorStart < str.length()&& cursorStart >= 1) {
								getEditableText().delete(start, start + count);
							} else {
								str = str.substring(0, s.length() - 1);
								setText(str);
								setSelection(getText().length());
							}
						}
					}

					@Override
					public void beforeTextChanged(CharSequence s, int start,
							int count, int after) {
					}

					@Override
					public void afterTextChanged(Editable s) {

					}
				});

			}
		},500);

	}

	public void measureLine() {

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		mFullTextWidth = widthSize;
		mFullTextHeight = heightSize;
		mMaxLines = (int) (mFullTextHeight / mLineHeight);
		setMeasuredDimension(mFullTextWidth, mFullTextHeight);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,int lengthBefore, int lengthAfter) {
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		editable = getEditableText();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mNoteApplication!=null) {
				mNoteApplication.setEdited(true);
			}
			mSelectSatrt = getSelectionStart();
			lineStart = getOffsetForPosition(0, mClickPosY);
			lineCount = getLineCount();
			if (mFirstDown) {
				mClickPosX = event.getX();
				mClickPosY = event.getY();
				mClickLine = (int) (mClickPosY / mLineHeight);
				if (mClickLine > mMaxLines) {
					mClickLine = mMaxLines;
				}
				mFirstDown = false;
			}

			if ((mClickLine + 1) > lineCount) {
				setSelection(getText().length(), getText().length());
				for (int i = 0; i < (mClickLine + 1 - lineCount); i++) {
					editable.append("\n");
				}
				lineStart = getOffsetForPosition(0, mClickPosY);
				mSelectSatrt = getSelectionStart();
				while (mPaint.measureText(editable.toString(), lineStart,mSelectSatrt) < mClickPosX) {
					editable.append(" ");
					mSelectSatrt++;
				}	
			} else {
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				setSelection(woqu, woqu);
				mSelectSatrt = getSelectionStart();
				lineStart = getOffsetForPosition(0, mClickPosY);
				if ((mClickLine + 1) == lineCount) {
					dstart = mSelectSatrt - lineStart;
					setSelection(woqu, woqu);
					temp = 0;
							while (mPaint.measureText(editable.toString(),lineStart, lineStart + dstart + temp) < mClickPosX) {
								editable.append(" ");
								temp++;
							}
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			mSelectSatrt = getSelectionStart();
			if ((mClickLine + 1) == lineCount) {
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				lineStart = getOffsetForPosition(0, mClickPosY);
				if (mClickLine == 0) {
					dstart = mSelectSatrt - lineStart;
					setSelection(woqu, woqu);
					temp = 0;
							while (mPaint.measureText(editable.toString(),lineStart, lineStart + dstart + temp) < mClickPosX) {
								editable.append(" ");
								temp++;
							}
						
				} else {
					if (mSelectSatrt <= woqu) {					
					} 
				}

			} else if ((mClickLine + 1) < lineCount) {
				int woqu = getOffsetForPosition(mClickPosX, mClickPosY);
				lineStart = getOffsetForPosition(0, mClickPosY);				
				String a = "\n";
					if (woqu == 0) {
						char c = getText().charAt(woqu);
						if (String.valueOf(c).equals(a)) {
							setSelection(woqu, woqu);
							lineStart = getOffsetForPosition(0, mClickPosY);
							mSelectSatrt = getSelectionStart();
							while (mPaint.measureText(editable.toString(), lineStart,mSelectSatrt) < mClickPosX) {
								editable.insert(mSelectSatrt, " ");
								mSelectSatrt++;
							}

						} else {

						}
					} else {
						char b = getText().charAt(woqu - 1);
						if (woqu <getText().length()) {
							char c = getText().charAt(woqu);
							if (String.valueOf(b).equals(a) || String.valueOf(c).equals(a)) {
								setSelection(woqu, woqu);
								lineStart = getOffsetForPosition(0, mClickPosY);
								mSelectSatrt = getSelectionStart();
								while (mPaint.measureText(editable.toString(), lineStart,mSelectSatrt) < mClickPosX) {
									editable.insert(mSelectSatrt, " ");
									mSelectSatrt++;
								}

							} else {

							}
						}
						
					}

			}

			mFirstDown = true;

			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

}
