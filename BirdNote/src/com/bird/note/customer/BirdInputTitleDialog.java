package com.bird.note.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bird.note.R;

/**
 * 自定义输入标题对话框
 */
public class BirdInputTitleDialog extends Dialog {
	private android.view.View.OnClickListener listener;
	public EditText mEditText;
	private String mContentString = "";

	public BirdInputTitleDialog(Context context,
			android.view.View.OnClickListener listener) {
		super(context);
	}

	public BirdInputTitleDialog(Context context, int theme) {
		super(context, theme);
	}

	public void setInputContent(String content) {
		this.mContentString = content;
		if (mEditText != null) {
			mEditText.append(mContentString);
		}

	}

	public String getContent() {
		return mEditText.getText().toString();
	}

	public void setOnConfirmClickListener(
			android.view.View.OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.birdinputtitledialog);

		TextView mCancleTextView = (TextView) findViewById(R.id.id_alertdiaolg_cancel);
		TextView mConfirmTextView = (TextView) findViewById(R.id.id_alertdiaolg_confirm);
		mEditText = (EditText) findViewById(R.id.id_alertdiaolg_input_title);

		mConfirmTextView.setOnClickListener(listener);
		mCancleTextView
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dismiss();
					}
				});	
		setOnCancelListener(null);
	}

}
