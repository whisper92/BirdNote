package com.bird.note.test;

import android.app.Activity;
import android.os.Bundle;

import com.bird.note.R;

/**
 * 测试序列化 测试结果：OK
 * 
 * @author root
 * 
 */
public class TestParcelActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
	}
}
