package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.customer.BirdPopMenu;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.DBUG;
import com.bird.note.model.ReadStaredNoteAdapter;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.utils.NoteApplication;

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
				if(keyCode==KeyEvent.KEYCODE_ENTER){//修改回车键功能
					// 先隐藏键盘
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
		DBUG.e("restart...");
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
