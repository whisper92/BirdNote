package com.bird.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
public class TestGridViewActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		ArrayList<Map<String, Object>> noteData=new ArrayList<Map<String,Object>>();
		for (int i = 0; i < 20; i++) {
			HashMap<String, Object> map=new HashMap<String, Object>();
			map.put("title", i+"-->");
			map.put("thumb", R.drawable.ic_launcher);
			noteData.add(map);
		}
		SimpleAdapter noteAdapter=new SimpleAdapter(this, noteData, R.layout.note_item, new String[]{"title","thumb"}, new int[]{R.id.id_note_item_title_tv,R.id.id_note_item_thumb_iv});
	   GridView gridView=(GridView)findViewById(R.id.id_show_gv);
	   gridView.setAdapter(noteAdapter);
	}
}
