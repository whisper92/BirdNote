package com.bird.note.ui;

import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ReadStaredNoteAdapter;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.model.ShowNoteAdapter.OnConfirmActionListener;

/**
 * 首页
 * 
 * @author wangxianpeng
 * 
 */
public class SearchNotesActivity extends Activity {

	private ShowNoteAdapter mNoteAdapter = null;
	private DbHelper mDbHelper = null;
	private GridView mGridView = null;
	private List<BirdNote> mBirdNotes = null;
	private EditText mSearchEditText;
	private ImageView mBackImageView;
	private ActionBar mActionBar = null;
	private TextView mSearchNothing = null;
	private Context mContext = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_notes_main);
		mActionBar = getActionBar();
		View headView = getLayoutInflater().inflate(
				R.layout.search_notes_header, null);
		mActionBar.setCustomView(headView, new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setDisplayShowCustomEnabled(true);

		mBackImageView = (ImageView) headView
				.findViewById(R.id.id_starnotes_title_back);
		mSearchNothing = (TextView) findViewById(R.id.id_search_nothing);
		mSearchEditText = (EditText) headView.findViewById(R.id.search_edt);
		mDbHelper = new DbHelper(this);
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
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
							.hideSoftInputFromWindow(SearchNotesActivity.this
									.getCurrentFocus().getWindowToken(),
									InputMethodManager.HIDE_NOT_ALWAYS);
					reQuery();
				}

				return false;
			}
		});
	}

	public void showNothint(int count) {
		if (count == 0) {
			mSearchNothing.setVisibility(View.VISIBLE);
		} else {
			mSearchNothing.setVisibility(View.GONE);
		}
	}

	public OnConfirmActionListener onConfirmDeleteListener = new OnConfirmActionListener() {

		@Override
		public void confirmDo(String[] noteids,int type) {
			mDbHelper.deleteNoteByIds(noteids);
			reQuery();
		}
	};

	public void reQuery() {
		if (mSearchEditText != null
				&& (!mSearchEditText.getText().toString().equals(""))
				&& (mSearchEditText.getText().toString() != null)) {
			mBirdNotes = mDbHelper.searchNotesByTag(mSearchEditText.getText()
					.toString());
			mNoteAdapter = new ShowNoteAdapter(SearchNotesActivity.this, 0,
					mBirdNotes, mGridView);
			showNothint(mBirdNotes.size());
			mGridView.setAdapter(mNoteAdapter);
			mNoteAdapter.setOnConfirmDeleteListener(onConfirmDeleteListener);
			mNoteAdapter.notifyDataSetChanged();
			mActionBar.setTitle(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
		}
		
	}

	@Override
	protected void onRestart() {
		if (mNoteAdapter.mActionMode != null) {
			mNoteAdapter.mActionMode.finish();
		}
		reQuery();

		if (mGridView != null) {
			mGridView.setAdapter(mNoteAdapter);
		}
		super.onRestart();
	}

}
