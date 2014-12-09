package com.bird.note.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.bird.note.R;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ReadStaredNoteAdapter;

/**
 * 首页
 * @author wangxianpeng
 * 
 */
public class SearchNotesActivity extends Activity{


 
	private ReadStaredNoteAdapter mNoteAdapter=null;
	private DbHelper mDbHelper=null;
	private GridView mGridView=null;
	private List<BirdNote> mBirdNotes=null;
    private EditText mSearchEditText;
	private ImageView mBackImageView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_notes);
		mBackImageView = (ImageView)findViewById(R.id.id_starnotes_title_back);
		mSearchEditText = (EditText) findViewById(R.id.search_edt);
		mDbHelper=new DbHelper(this);
		mGridView = (GridView) findViewById(R.id.id_show_gv);
		mBackImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		
		mSearchEditText.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode==KeyEvent.KEYCODE_ENTER){
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(
					SearchNotesActivity.this
					.getCurrentFocus()
					.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
					mBirdNotes = mDbHelper.searchNotesByTag(mSearchEditText.getText().toString());
				    mNoteAdapter = new ReadStaredNoteAdapter(SearchNotesActivity.this,mBirdNotes,mGridView);
				    mGridView.setAdapter(mNoteAdapter);
					mNoteAdapter.notifyDataSetChanged();
				}

				return false;
			}
		});
	}

	

	@Override
	protected void onRestart() {
		if (mSearchEditText!=null&&(!mSearchEditText.getText().toString().equals(""))&&(mSearchEditText.getText().toString()!=null)) {
			mBirdNotes = mDbHelper.searchNotesByTag(mSearchEditText.getText().toString());
		    mNoteAdapter = new ReadStaredNoteAdapter(SearchNotesActivity.this,mBirdNotes,mGridView);
			mNoteAdapter.notifyDataSetChanged();
		}
		
	    if (mGridView!=null) {
	    	mGridView.setAdapter(mNoteAdapter);
		}    
		super.onRestart();
	}
	
	
}
