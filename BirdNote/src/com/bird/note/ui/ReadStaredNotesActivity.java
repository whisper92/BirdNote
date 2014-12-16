package com.bird.note.ui;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ReadStaredNoteAdapter;

/**
 * 首页
 * @author wangxianpeng
 * 
 */
public class ReadStaredNotesActivity extends Activity{
	
	private ReadStaredNoteAdapter mNoteAdapter=null;
	private DbHelper mDbHelper=null;
	private GridView mGridView=null;
	private List<BirdNote> mBirdNotes=null;
	private ActionBar mActionBar = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_stared_notes_main);
		mActionBar = getActionBar();
		mDbHelper=new DbHelper(this);

		mGridView = (GridView) findViewById(R.id.id_show_gv);

		mBirdNotes = mDbHelper.queryStaredShowNotes();
		mActionBar.setTitle(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		mActionBar.setDisplayHomeAsUpEnabled(true);
	    mNoteAdapter = new ReadStaredNoteAdapter(this,mBirdNotes,mGridView);
	    mGridView.setAdapter(mNoteAdapter);
		mNoteAdapter.notifyDataSetChanged();

	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onRestart() {
		mBirdNotes=mDbHelper.queryStaredShowNotes();
		mNoteAdapter= new ReadStaredNoteAdapter(this,mBirdNotes,mGridView);
	    mNoteAdapter.notifyDataSetChanged();
	    if (mGridView!=null) {
			mActionBar.setTitle(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
	    	mGridView.setAdapter(mNoteAdapter);
		}    
		super.onRestart();
	}
	
	
}
