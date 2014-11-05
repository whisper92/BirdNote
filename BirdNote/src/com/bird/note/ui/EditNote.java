package com.bird.note.ui;

import com.bird.note.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;;

public class EditNote extends Activity {
	EditText mEditText;
	String editSum = " ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_note_main);
		mEditText = (EditText) findViewById(R.id.id_edit_main_et);
		//initEditText(mEditText);

	}

	public String getSpaces(){
		String sum=" ";
		for (int i = 0; i < 60; i++) {
			sum+= " ";
		}
		return sum;
	}
	/*public void initEditText(final EditText editText) {
		int lineCount = editText.getLineCount();
		String sum = "1";
		Editable editable = editText.getEditableText();
		for (int i = 0; i < 1500; i++) {
			if (i%50==0) {
				sum+="\n";
			}else {
				sum = sum + " z ";
			}

			
		}
		sum+="2";
		editText.setText(sum);
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		editText.setOnEditorActionListener(new OnEditorActionListener() {	
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				int lastindex=editText.getSelectionStart();
				editText.setSelection(lastindex+60);
				int index = editText.getSelectionStart();
				Editable editable = editText.getText();
				editable.insert(index, getSpaces());
				Log.e("wxp","action");
				return false;
			}
		});
	}*/
}
