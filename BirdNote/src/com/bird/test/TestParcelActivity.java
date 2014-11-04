package com.bird.test;

import com.bird.model.BirdNote;
import com.bird.model.ChildNote;
import com.bird.model.TextLine;
import com.bird.note.R;

import android.app.*;
import android.os.*;
import android.util.Log;
import android.view.*;
import android.widget.*;

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
		 Bundle bundle = getIntent().getExtras();

		ChildNote note = (ChildNote)bundle.getParcelable("note");
		BirdNote birdNote=(BirdNote)bundle.getParcelable("birdnote");
		Log.e("wxp",note.noteID+" | "+note.quadrant+" | "+note.textLines.get(0).textContent);
		Log.e("wxp",birdNote.childNotes.get(0).quadrant+"");

		
	}
}
