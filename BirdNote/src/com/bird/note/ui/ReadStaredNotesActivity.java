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
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.model.ShowNoteAdapter.OnConfirmActionListener;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class ReadStaredNotesActivity extends Activity {

	private ShowNoteAdapter mNoteAdapter = null;
	private DbHelper mDbHelper = null;
	private GridView mGridView = null;
	private List<BirdNote> mBirdNotes = null;
	private ActionBar mActionBar = null;
	private TextView mSearchNothing = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_stared_notes_main);
		mActionBar = getActionBar();
		mDbHelper = new DbHelper(this);

		mGridView = (GridView) findViewById(R.id.id_show_gv);
		mSearchNothing = (TextView) findViewById(R.id.id_search_nothing);
		mBirdNotes = mDbHelper.queryStaredShowNotes();
		mActionBar.setTitle(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		reQuery();
	}

	public OnConfirmActionListener onConfirmRmStarListener = new OnConfirmActionListener() {

		@Override
		public void confirmDo(String[] noteids, int type) {
			mDbHelper.putStarToNoteById(noteids, 0);
			reQuery();
		}
	};

	public void reQuery() {
		mBirdNotes = mDbHelper.queryStaredShowNotes();
		mNoteAdapter = new ShowNoteAdapter(ReadStaredNotesActivity.this, 1, mBirdNotes, mGridView);
		showNothint(mBirdNotes.size());
		mGridView.setAdapter(mNoteAdapter);
		mNoteAdapter.setOnConfirmDeleteListener(onConfirmRmStarListener);
		mNoteAdapter.notifyDataSetChanged();
		mActionBar.setTitle(String.format(getString(R.string.stared_note_count), mBirdNotes.size()));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void showNothint(int count) {
		if (count == 0) {
			mSearchNothing.setVisibility(View.VISIBLE);
		} else {
			mSearchNothing.setVisibility(View.GONE);
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
