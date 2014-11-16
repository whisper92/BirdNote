package com.bird.note.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.bird.note.R;

/**
 * 自定义提醒对话框
 */
public class BirdAlertDialog extends Dialog {
	Context mContext;
	android.view.View.OnClickListener listener;

	public BirdAlertDialog(Context context,
			android.view.View.OnClickListener listener) {
		super(context);
		init(context, listener);
	}

	public BirdAlertDialog(Context context, int theme,
			android.view.View.OnClickListener listener) {
		super(context, theme);
		init(context, listener);
	}

	public void init(Context context, android.view.View.OnClickListener listener) {
		this.mContext = context;
		this.listener = listener;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.birdalertdialog);

		TextView mCancleTextView = (TextView) findViewById(R.id.id_alertdiaolg_cancel);
		TextView mConfirmTextView = (TextView) findViewById(R.id.id_alertdiaolg_confirm);
		mConfirmTextView.setOnClickListener(listener);
		mCancleTextView.setOnClickListener(new android.view.View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
