package com.bird.note.customer;

import com.bird.note.R;

import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class ChangeQua extends LinearLayout implements OnClickListener{

	private String TAG = "ChangeQua";
	
	LayoutInflater mInflater = null;
	Button mPreButton = null;
	Button mNextButton = null;
	TextView mIndex = null;
	int qua = 0;
	public ChangeQua(Context context, AttributeSet attrs) {
		super(context, attrs);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.edit_note_change_qua, this);
		mPreButton = (Button)view.findViewById(R.id.id_change_qua_pre);
		mNextButton = (Button)view.findViewById(R.id.id_change_qua_next);
		mIndex =(TextView) view.findViewById(R.id.id_change_qua_index);
		
		mPreButton.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
		
		mIndex.setText((qua+1)+"/4");
		mPreButton.setEnabled(false);
		

	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	OnChangeQuaListener mOnChangeQuaListener = null;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_change_qua_pre:
				Log.e(TAG, "qua : "+qua);
				qua = qua -1;
				mIndex.setText((qua+1)+"/4");
				mOnChangeQuaListener.changeQua(qua);
				mNextButton.setEnabled(true);
				if (qua-1<0) {
					mPreButton.setEnabled(false);
				}
			
			break;
		case R.id.id_change_qua_next:
				Log.e(TAG, "qua : "+qua);
				qua = qua +1;
				mIndex.setText((qua+1)+"/4");
				mOnChangeQuaListener.changeQua(qua);
				mPreButton.setEnabled(true);
				if (qua+1>3) {
					mNextButton.setEnabled(false);
				}
			
			break;
		default:
			break;
		}
		
	}
	
	public int getCurrentQua(){
		return qua;
	}
	public interface OnChangeQuaListener{
		public void changeQua(int qua);
	}
	
	public void setOnChangeQuaListener(OnChangeQuaListener listener){
		this.mOnChangeQuaListener = listener ;
	}

}
