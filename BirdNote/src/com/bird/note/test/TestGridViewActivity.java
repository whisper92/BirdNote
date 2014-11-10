package com.bird.note.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bird.note.R;
import com.bird.note.dao.Db;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.model.TextLine;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.utils.BitmapUtil;

import android.app.*;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
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
public class TestGridViewActivity extends Activity implements
		View.OnClickListener {
	ImageView addPen;
	ImageView addText;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		Db db = new Db(this);
		DbHelper dbHelper=new DbHelper(this);

		GridView gridView = (GridView) findViewById(R.id.id_show_gv);
		ShowNoteAdapter noteAdapter = new ShowNoteAdapter(this,dbHelper.queryShowNotes() ,gridView);
		gridView.setAdapter(noteAdapter);
		noteAdapter.notifyDataSetChanged();

		addPen = (ImageView) findViewById(R.id.id_show_title_new_pen);
		addPen.setOnClickListener(this);
		addText = (ImageView) findViewById(R.id.id_show_title_new_text);
		addText.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(TestGridViewActivity.this, EditNoteActivity.class);
		if (v.getId() == R.id.id_show_title_new_pen) {
			intent.putExtra("type", R.id.id_edit_title_pen);
		}
		if (v.getId() == R.id.id_show_title_new_text) {
			intent.putExtra("type", R.id.id_edit_title_text);
		}
		startActivity(intent);
	}

}
