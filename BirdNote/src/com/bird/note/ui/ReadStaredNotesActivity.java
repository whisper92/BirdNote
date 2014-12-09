package com.bird.note.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
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
	private TextView mTitleNoteCount;
	private ImageView mBackImageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_stared_notes);
		mBackImageView = (ImageView)findViewById(R.id.id_starnotes_title_back);

		mDbHelper=new DbHelper(this);

        mTitleNoteCount =(TextView) findViewById(R.id.id_show_title_count);
		mGridView = (GridView) findViewById(R.id.id_show_gv);

		mBirdNotes = mDbHelper.queryStaredShowNotes();
		mTitleNoteCount.setText(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
	    mNoteAdapter = new ReadStaredNoteAdapter(this,mBirdNotes,mGridView);
	    mGridView.setAdapter(mNoteAdapter);
		mNoteAdapter.notifyDataSetChanged();

		mBackImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
	}

	

	@Override
	protected void onRestart() {
		mBirdNotes=mDbHelper.queryStaredShowNotes();
		mNoteAdapter= new ReadStaredNoteAdapter(this,mBirdNotes,mGridView);
	    mNoteAdapter.notifyDataSetChanged();
	    if (mGridView!=null) {
	    	mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
	    	mGridView.setAdapter(mNoteAdapter);
		}    
		super.onRestart();
	}
	
	
}
