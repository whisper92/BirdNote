package com.bird.note.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;

import com.bird.note.R;
import com.bird.note.dao.Db;
import com.bird.note.dao.DbHelper;
import com.bird.note.dao.NotesTable;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.ui.EditNoteActivity;

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
 
	ShowNoteAdapter noteAdapter=null;
	DbHelper dbHelper=null;
	GridView gridView=null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.show_notes);
		Db db = new Db(this);
		 dbHelper=new DbHelper(this);
        Cursor c=dbHelper.queryShowNoteCursor();
		//SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(this, R.layout.note_item, c, new String[]{NotesTable.LEVEL,NotesTable.TITLE,NotesTable.BG_ID}, new int[]{R.id.id_n});

		 gridView = (GridView) findViewById(R.id.id_show_gv);
	    noteAdapter = new ShowNoteAdapter(this,dbHelper.queryShowNotes() ,gridView);
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
		//finish();
	}
	
	@Override
	protected void onRestart() {
		noteAdapter= new ShowNoteAdapter(this,dbHelper.queryShowNotes() ,gridView); 
	    noteAdapter.notifyDataSetChanged();
	    if (gridView!=null) {
	    	gridView.setAdapter(noteAdapter);
		}    
		super.onRestart();
	}
	@Override
	protected void onStart() {
		Log.e("wxp","onStart");
		super.onStart();
	}
	
	public class ShowCursorAdapter extends SimpleCursorAdapter{

		public ShowCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return super.getView(position, convertView, parent);
		}
	
	}

	
}
