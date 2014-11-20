package com.bird.note.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bird.note.R;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.dao.DbHelper;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.utils.NoteApplication;

/**
 * 首页
 * @author wangxianpeng
 * 
 */
public class ShowNotesActivity extends Activity implements OnClickListener{

	private RelativeLayout mShowTitle;
	private RelativeLayout mShowDeleteTitle;
	private Button mConfirmDelete;
	private Button mCancelDelete;
	private Button mSelectAll;
	
	private ImageView addPen;
	private ImageView addText;
 
	private ShowNoteAdapter mNoteAdapter=null;
	private DbHelper mDbHelper=null;
	private GridView mGridView=null;
	private List<BirdNote> mBirdNotes=null;
	private BirdWaitDialog mWaitDialog = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		mWaitDialog  =new BirdWaitDialog(this, R.style.birdalertdialog);
		mDbHelper=new DbHelper(this);
		mShowTitle=(RelativeLayout)findViewById(R.id.id_show_title_rl);
		mShowDeleteTitle = (RelativeLayout) findViewById(R.id.id_show_title_delete_menu_rl);
		mConfirmDelete = (Button) findViewById(R.id.id_show_title_delete_confirm);
		mCancelDelete = (Button) findViewById(R.id.id_show_title_delete_cancle);
		mSelectAll = (Button) findViewById(R.id.id_show_title_delete_select_all);

		mGridView = (GridView) findViewById(R.id.id_show_gv);
		mBirdNotes=mDbHelper.queryShowNotes();
	    mNoteAdapter = new ShowNoteAdapter(this,mDbHelper.queryShowNotes() ,mGridView);
	    mGridView.setAdapter(mNoteAdapter);
		mNoteAdapter.notifyDataSetChanged();

		addPen = (ImageView) findViewById(R.id.id_show_title_new_pen);
		addPen.setOnClickListener(this);
		addText = (ImageView) findViewById(R.id.id_show_title_new_text);
		addText.setOnClickListener(this);        
		
		mConfirmDelete.setOnClickListener(this);
		mCancelDelete.setOnClickListener(this);
		mSelectAll.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		intent.setClass(ShowNotesActivity.this, EditNoteActivity.class);
		intent.putExtra(BirdMessage.START_TYPE_KEY, BirdMessage.START_TYPE_CREATE_VALUE);
		NoteApplication noteApplication=(NoteApplication)getApplication();
		noteApplication.setCurrentNoteEidtType(BirdMessage.NOTE_EDIT_TYPE_CREATE);
		noteApplication.setEditedQuadrants(new int[]{0,0,0,0});
		switch (v.getId()) {
		case R.id.id_show_title_new_pen:
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_pen);
			startActivity(intent);
			break;

		case R.id.id_show_title_new_text:
			intent.putExtra(BirdMessage.START_MODE_KEY, R.id.id_edit_title_text);
			startActivity(intent);
			break;
		case R.id.id_show_title_delete_confirm:
			showHandler.sendEmptyMessage(BirdMessage.DELETE_RUNNABLE_START);
			showHandler.postDelayed(DeleteNotesRunnable,300);
			mShowTitle.setVisibility(View.VISIBLE);
			mShowDeleteTitle.setVisibility(View.GONE);
			break;
		case R.id.id_show_title_delete_cancle:
			mShowTitle.setVisibility(View.VISIBLE);
			mShowDeleteTitle.setVisibility(View.GONE);	
			mNoteAdapter.cancelDelete();
			mNoteAdapter.setDeleteState(false);
			break;
		case R.id.id_show_title_delete_select_all:
			mNoteAdapter.selectAll();
			break;
		default:
			break;
		}	
		
		//finish();
	}
	
	public Runnable DeleteNotesRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.deleteNoteByIds(mNoteAdapter.getSelectedNote());
			mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mDbHelper.queryShowNotes() ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
		    	mGridView.setAdapter(mNoteAdapter);
			}  
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);		
		}
	};
	
	@Override
	protected void onRestart() {
		mBirdNotes=mDbHelper.queryShowNotes();
		mNoteAdapter= new ShowNoteAdapter(this,mBirdNotes,mGridView); 
	    mNoteAdapter.notifyDataSetChanged();
	    if (mGridView!=null) {
	    	mGridView.setAdapter(mNoteAdapter);
		}    
		super.onRestart();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.show_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_show_menu_mutil_delete:
             if (mNoteAdapter!=null && mBirdNotes!=null && mBirdNotes.size()>0) {
				mNoteAdapter.setDeleteState(true);
				mShowTitle.setVisibility(View.GONE);
				mShowDeleteTitle.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.id_show_menu_search:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Handler showHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==BirdMessage.DELETE_OVER) {
				mWaitDialog.dismiss();
			}
			if (msg.what==BirdMessage.DELETE_RUNNABLE_START) {
				mWaitDialog.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialog.show();
			}
		};
	};
	
}
