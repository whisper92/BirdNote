package com.bird.note.test;

import com.bird.note.R;
import com.bird.note.utils.BitmapUtil;

import android.R.integer;
import android.content.Context;

public class TestDataNote {
	public Context context;
	public TestDataNote(Context context){
		this.context=context;
	}
	public int _id;
	
	public int level;
	
	public String title="bird_note";
	
	public byte[] content=BitmapUtil.drawableToBytes(context, R.drawable.ic_launcher);
	
	public byte[] thumbnail=BitmapUtil.drawableToBytes(context, R.drawable.icon_text);

}
