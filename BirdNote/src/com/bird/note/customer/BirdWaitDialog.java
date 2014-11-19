package com.bird.note.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bird.note.R;

/**
 * 自定义等待对话框
 */
public class BirdWaitDialog extends Dialog {
	Context mContext;
	TextView mTextView;
	String mWaitContent="wait...";
	public BirdWaitDialog(Context context) {
		super(context);
		init(context);
	}

	public BirdWaitDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public void init(Context context) {
		this.mContext = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.birdwaitdialog);
		mTextView = (TextView) findViewById(R.id.id_wait_dialog_content);
		mTextView.setText(this.mWaitContent);
	}
	
	public void setWaitContent(String textString){
		this.mWaitContent=textString;
	}

}
