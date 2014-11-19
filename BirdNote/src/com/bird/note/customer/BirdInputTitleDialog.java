package com.bird.note.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.utils.CommonUtils;

/**
 * 自定义输入标题对话框
 */
public class BirdInputTitleDialog extends Dialog {
	Context mContext;
	android.view.View.OnClickListener listener;

	EditText mEditText;
	public BirdInputTitleDialog(Context context,
			android.view.View.OnClickListener listener) {
		super(context);
		init(context);
	}

	public BirdInputTitleDialog(Context context, int theme) {
		super(context, theme);
		init(context);
	}

	public void init(Context context) {
		this.mContext = context;
	}

	public String getTitle(){
		return mEditText.getText().toString();
	}
	
	public void setOnConfirmClickListener(android.view.View.OnClickListener listener){
		this.listener = listener;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.birdinputtitledialog);
		TextView mCancleTextView = (TextView) findViewById(R.id.id_alertdiaolg_cancel);
		TextView mConfirmTextView = (TextView) findViewById(R.id.id_alertdiaolg_confirm);
		mEditText = (EditText) findViewById(R.id.id_alertdiaolg_input_title);
		mEditText.getEditableText().append(CommonUtils.getCurrentTime());

		mConfirmTextView.setOnClickListener(listener);
		mCancleTextView.setOnClickListener(new android.view.View.OnClickListener() {	
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

}
