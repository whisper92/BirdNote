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

	private android.view.View.OnClickListener listener;
	public BirdAlertDialog(Context context) {
		super(context);
	}

	public BirdAlertDialog(Context context, int theme) {
		super(context, theme);
	}

	public void setOnConfirmListener(android.view.View.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birdalertdialog);
		TextView mCancleTextView = (TextView) findViewById(R.id.id_alertdiaolg_cancel);
		TextView mConfirmTextView = (TextView) findViewById(R.id.id_alertdiaolg_confirm);
		mConfirmTextView.setOnClickListener(listener);
		mCancleTextView
				.setOnClickListener(new android.view.View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dismiss();
					}
				});
	}


}
