package com.bird.note.customer;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.bird.note.R;

/**
 * @author wangxianpeng
 * @since 19/12/14
 */
public class BirdWaitDialog extends Dialog {
	private TextView mTextView;
	private String mWaitContent = "...";

	public BirdWaitDialog(Context context) {
		super(context);
	}

	public BirdWaitDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bird_wait_dialog);

		mTextView = (TextView) findViewById(R.id.id_wait_dialog_content);
		
		mTextView.setText(this.mWaitContent);
	}

	/**
	 * Set the title's text
	 *
	 * @param textString
	 *            the text of dialog title
	 */
	public void setWaitContent(String textString) {
		this.mWaitContent = textString;
	}

}
