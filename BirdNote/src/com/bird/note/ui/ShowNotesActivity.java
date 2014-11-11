package com.bird.note.ui;

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
import com.bird.note.model.BirdMessage;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.ui.EditNoteActivity;
import com.bird.note.utils.NoteApplication;

/**
 * 首页
 * @author wangxianpeng
 * 
 */
public class ShowNotesActivity extends Activity implements
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
		dbHelper=new DbHelper(this);
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
		intent.setClass(ShowNotesActivity.this, EditNoteActivity.class);
		if (v.getId() == R.id.id_show_title_new_pen) {
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_pen);
		}
		if (v.getId() == R.id.id_show_title_new_text) {
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_text);
		}
		intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		NoteApplication noteApplication=(NoteApplication)getApplication();
		noteApplication.setCurrentNoteEidtType(BirdMessage.NOTE_EDIT_TYPE_CREATE);
		noteApplication.setEditedQuadrants(new int[]{0,0,0,0});
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
		super.onStart();
	}
	
}