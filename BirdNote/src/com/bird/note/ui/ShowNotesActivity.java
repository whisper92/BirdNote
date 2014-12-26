package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.model.ShowNoteAdapter.OnConfirmActionListener;
import com.bird.note.utils.BitmapUtil;
import com.bird.note.utils.NoteApplication;
import com.bird.note.utils.PreferenceUtil;

/**
 * @author wangxianpeng
 * @since 19/12/14
 *
 */
public class ShowNotesActivity extends Activity implements OnClickListener {

	private ImageView addPen;
	private ImageView addText;

	private ShowNoteAdapter mNoteAdapter = null;
	private DbHelper mDbHelper = null;
	private GridView mGridView = null;
	private List<BirdNote> mBirdNotes = null;
	private BirdWaitDialog mWaitDialogUpdate = null;
	private BirdWaitDialog mWaitDialogDelete = null;
	private TextView mTitleNoteCount;
	private int mCurrentSort = 0;
	private NoteApplication mNoteApplication = null;
	private ActionBar mActionBar = null;
	private ActionMode mActionMode = null;
	private LinearLayout mSearchView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.show_notes_main);
		mActionBar = getActionBar();

		mNoteApplication = (NoteApplication) getApplication();
		mWaitDialogUpdate = new BirdWaitDialog(this,android.R.style.Theme_Holo_Light_Dialog);
		mWaitDialogDelete = new BirdWaitDialog(this,android.R.style.Theme_Holo_Light_Dialog);
		mDbHelper = new DbHelper(this);
		mCurrentSort = PreferenceUtil.getSortBy();
		mBirdNotes = queryByCurrentSort(mCurrentSort);
		initActionBar();
		
	}

	public void initActionBar() {
		View headView = getLayoutInflater().inflate(R.layout.show_notes_header,null);
		mActionBar.setCustomView(headView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setTitle(String.format(getString(R.string.show_note_count),mBirdNotes.size()));
		mNoteApplication.setNotescount(mBirdNotes.size());

		mTitleNoteCount = (TextView) headView.findViewById(R.id.id_show_title_count);
		mGridView = (GridView) findViewById(R.id.id_show_gv);
		mSearchView = (LinearLayout) findViewById(R.id.id_show_search);

		mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
		mNoteAdapter = new ShowNoteAdapter(this, 0, mBirdNotes, mGridView);
		mGridView.setAdapter(mNoteAdapter);
		mNoteAdapter.notifyDataSetChanged();

		addPen = (ImageView) headView.findViewById(R.id.id_show_title_new_pen);
		addPen.setOnClickListener(this);
		addText = (ImageView) headView.findViewById(R.id.id_show_title_new_text);
		addText.setOnClickListener(this);
		mSearchView.setOnClickListener(this);

		mNoteAdapter.setOnConfirmDeleteListener(mConfirmDeleteListener);

	}

	String[] mNotesId = null;
	public OnConfirmActionListener mConfirmDeleteListener = new OnConfirmActionListener() {

		@Override
		public void confirmDo(String[] noteids, int type) {
			mNotesId = noteids;
			if (type == 0) {
				showHandler.post(DeleteNotesRunnable);
			} else {
				showHandler.post(StarNotesRunnable);
			}

		}
	};

	public Runnable StarNotesRunnable = new Runnable() {
		@Override
		public void run() {
			if (mNoteAdapter.mActionMode != null) {
				mNoteAdapter.mActionMode.finish();
			}
			mDbHelper.putStarToNoteById(mNotesId, 1);
			mBirdNotes.clear();
			mBirdNotes = queryByCurrentSort(mCurrentSort);
			mNoteApplication.setNotescount(mBirdNotes.size());
			mNoteAdapter = new ShowNoteAdapter(ShowNotesActivity.this, 0, mBirdNotes, mGridView);
			mNoteAdapter.notifyDataSetChanged();
			mNoteAdapter.setOnConfirmDeleteListener(mConfirmDeleteListener);
			if (mGridView != null) {
				mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
				mGridView.setAdapter(mNoteAdapter);
			}
			showHandler.sendEmptyMessage(BirdMessage.UPDATETITLE_RUNNABLE_OVER);
		}
	};

	/**
	 * 根据当前的排序方式查询数据
	 *
	 * @return
	 */
	public List<BirdNote> queryByCurrentSort(int sort) {
		List<BirdNote> sortNotes = new ArrayList<BirdNote>();
		switch (sort) {
		case 0:
			sortNotes = mDbHelper.sortShowNotesByCreateTime();
			break;

		case 1:
			sortNotes = mDbHelper.sortShowNotesByUpdateTime();
			break;

		case 2:
			sortNotes = mDbHelper.sortShowNotesByLevel();
			break;

		default:
			break;
		}
		return sortNotes;
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu) {
		getMenuInflater().inflate(R.menu.show_menu, menu);
		return true;
	};

	android.content.DialogInterface.OnClickListener sortListener = new android.content.DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case 0:
				mCurrentSort = 0;
				break;

			case 1:
				mCurrentSort = 1;
				break;

			case 2:
				mCurrentSort = 2;
				break;

			case -1:
				/* Confirm */
				PreferenceUtil.setSortBy(mCurrentSort);
				showHandler.sendEmptyMessage(BirdMessage.SORT_START);
				showHandler.post(sortRunnable);
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_show_menu_sort:
			AlertDialog sortDialog = PopMenuManager.createSortChooseAlertDialog(ShowNotesActivity.this,R.string.show_menu_sort, sortListener);
			sortDialog.show();
			break;

		case R.id.id_show_menu_all_star:
			Intent startintent = new Intent();
			startintent.setClass(ShowNotesActivity.this, ReadStaredNotesActivity.class);
			startintent.putExtra("flag", "star");
			startActivity(startintent);
			break;

		case R.id.id_show_menu_search:
			Intent searchintent = new Intent();
			searchintent.setClass(ShowNotesActivity.this, SearchNotesActivity.class);
			searchintent.putExtra("flag", "search");
			startActivity(searchintent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ShowNotesActivity.this, EditNoteActivity.class);
		intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		NoteApplication noteApplication = (NoteApplication) getApplication();
		noteApplication.setCurrentNoteEidtType(BirdMessage.NOTE_EDIT_TYPE_CREATE);
		noteApplication.setEditedQuadrants(new int[] { 0, 0, 0, 0 });

		switch (v.getId()) {
		case R.id.id_show_title_new_pen:
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_pen);
			mNoteApplication.setEditBackground(BitmapUtil.EDIT_BGS[0]);
			startActivity(intent);
			break;

		case R.id.id_show_title_new_text:
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_text);
			mNoteApplication.setEditBackground(BitmapUtil.EDIT_BGS[0]);
			startActivity(intent);
			break;

		case R.id.id_show_search:
			Intent searchintent = new Intent();
			searchintent.setClass(ShowNotesActivity.this,SearchNotesActivity.class);
			searchintent.putExtra("flag", "search");
			startActivity(searchintent);
			break;

		default:
			break;
		}

	}

	public Runnable DeleteNotesRunnable = new Runnable() {
		@Override
		public void run() {
			mDbHelper.deleteNoteByIds(mNotesId);
			mBirdNotes.clear();
			mBirdNotes = queryByCurrentSort(mCurrentSort);
			mNoteApplication.setNotescount(mBirdNotes.size());
			mNoteAdapter = new ShowNoteAdapter(ShowNotesActivity.this, 0,	mBirdNotes, mGridView);
			mNoteAdapter.notifyDataSetChanged();
			mNoteAdapter.setOnConfirmDeleteListener(mConfirmDeleteListener);
			if (mGridView != null) {
				mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
				mGridView.setAdapter(mNoteAdapter);
			}
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);
		}
	};

	@Override
	protected void onRestart() {
		super.onRestart();

		showHandler.post(queryRunnable);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mNoteAdapter.mActionMode != null) {
			mNoteAdapter.mActionMode.finish();
		}
	}

	public void closeMenu(PopupWindow popupWindow) {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public Runnable queryRunnable = new Runnable() {

		@Override
		public void run() {
			mBirdNotes = queryByCurrentSort(mCurrentSort);
			mNoteApplication.setNotescount(mBirdNotes.size());
			mNoteAdapter = new ShowNoteAdapter(ShowNotesActivity.this, 0, mBirdNotes, mGridView);
			mNoteAdapter.setOnConfirmDeleteListener(mConfirmDeleteListener);
			showHandler.sendEmptyMessage(BirdMessage.QUERY_RUNNABLE_OVER);
		}
	};

	public Runnable sortRunnable = new Runnable() {
		@Override
		public void run() {
			mBirdNotes = queryByCurrentSort(mCurrentSort);
			showHandler.sendEmptyMessage(BirdMessage.SORT_OVER);
		}
	};

	int mChoosePosition = 0;
	public Runnable deleteSingleNoteRunnable = new Runnable() {
		@Override
		public void run() {
			mDbHelper.deleteNoteById(mNoteAdapter.getSingleNoteId() + "");
			mBirdNotes.remove(mChoosePosition);
			mNoteAdapter.notifyDataSetChanged();
			mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);
		}
	};

	public Handler showHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == BirdMessage.QUERY_RUNNABLE_OVER) {
				mNoteAdapter.notifyDataSetChanged();
				if (mGridView != null) {
					mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
					mGridView.setAdapter(mNoteAdapter);
				}
			}
			if (msg.what == BirdMessage.DELETE_OVER) {
				mWaitDialogDelete.dismiss();
			}
			if (msg.what == BirdMessage.DELETE_RUNNABLE_START) {
				mWaitDialogDelete.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialogDelete.show();
			}
			if (msg.what == BirdMessage.DELETE_SINGLE_NOTE_RUNNABLE_START) {
				mChoosePosition = (Integer) msg.obj;
				mWaitDialogDelete.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialogDelete.show();
				post(deleteSingleNoteRunnable);
			}

			if (msg.what == BirdMessage.CHANGEMARKCOLOR_RUNNABLE_START) {
				mChoosePosition = (Integer) msg.obj;
				mWaitDialogUpdate.setWaitContent(getString(R.string.alert_sort));
				mWaitDialogUpdate.show();
			}

			if (msg.what == BirdMessage.UPDATETITLE_RUNNABLE_START) {
				mChoosePosition = (Integer) msg.obj;
				mWaitDialogUpdate.setWaitContent(getString(R.string.saveing_note));
				mWaitDialogUpdate.show();

			}
			if (msg.what == BirdMessage.UPDATETITLE_RUNNABLE_OVER) {
				mWaitDialogUpdate.dismiss();

			}
			if (msg.what == BirdMessage.SORT_START) {
				mWaitDialogUpdate.setWaitContent(getString(R.string.alert_sort));
				mWaitDialogUpdate.show();
			}

			if (msg.what == BirdMessage.SORT_OVER) {
				mWaitDialogUpdate.dismiss();
				mNoteAdapter = new ShowNoteAdapter(ShowNotesActivity.this, 0,	mBirdNotes, mGridView);
				mNoteAdapter.notifyDataSetChanged();
				mNoteAdapter.setOnConfirmDeleteListener(mConfirmDeleteListener);
				if (mGridView != null) {
					mGridView.setAdapter(mNoteAdapter);
				}
			}

		};
	};

	protected void onDestroy() {
		super.onDestroy();
	};

}
