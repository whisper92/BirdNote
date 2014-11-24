package com.bird.note.ui;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
	    mNoteAdapter = new ShowNoteAdapter(this,mBirdNotes,mGridView);
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
/*			mShowTitle.setVisibility(View.VISIBLE);
			mShowDeleteTitle.setVisibility(View.GONE);*/
			startShowNoamralTitle();
			break;
		case R.id.id_show_title_delete_cancle:
/*			mShowTitle.setVisibility(View.VISIBLE);
			mShowDeleteTitle.setVisibility(View.GONE);	*/
			mNoteAdapter.cancelDelete();
			mNoteAdapter.setDeleteState(false);
			startShowNoamralTitle();
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

	public void startShowMenuTitle(){
		Animator animatorSetOut=AnimatorInflater.loadAnimator(this, R.anim.normal_flip_out);
		Animator animatorSetIn=AnimatorInflater.loadAnimator(this, R.anim.menu_flip_in);
		animatorSetOut.setTarget(mShowTitle);
		animatorSetIn.setTarget(mShowDeleteTitle);
		animatorSetOut.start();
		animatorSetIn.start();
		
		animatorSetOut.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				mShowTitle.setVisibility(View.GONE);				
				mShowDeleteTitle.setVisibility(View.VISIBLE);
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
				
			}
		});

	}
	
	public void startShowNoamralTitle(){
		final Animator animatorSetIn=AnimatorInflater.loadAnimator(this, R.anim.normal_flip_in);
        Animator animatorSetOut=AnimatorInflater.loadAnimator(this, R.anim.menu_flip_out);
		animatorSetOut.setTarget(mShowDeleteTitle);
		animatorSetIn.setTarget(mShowTitle);
		animatorSetOut.start();
		animatorSetIn.start();
		
		animatorSetOut.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				mShowTitle.setVisibility(View.VISIBLE);				
				mShowDeleteTitle.setVisibility(View.GONE);
				
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
				
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.id_show_menu_mutil_delete:
             if ((mNoteAdapter.getDeleteState()==false)&&mNoteAdapter!=null && mBirdNotes!=null && mBirdNotes.size()>0) {
				mNoteAdapter.setDeleteState(true);
				startShowMenuTitle();		
			}
			break;
		case R.id.id_show_menu_search:
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	

	int mChoosePosition = 0;
	public Runnable deleteSingleNoteRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.deleteNoteById(mNoteAdapter.getSingleNoteId()+"");
     /*	 mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mDbHelper.queryShowNotes() ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
		    	mGridView.setAdapter(mNoteAdapter);
			}  */
		    mBirdNotes.remove(mChoosePosition);
		    mNoteAdapter.notifyDataSetChanged();
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);	
		}
	};
	
	public Runnable changeMarkColorRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.updateLevelById(mNoteAdapter.getSingleNoteId()+"",mNoteAdapter.chooseLevel);
			mBirdNotes.get(mChoosePosition).level = mNoteAdapter.chooseLevel;
			mNoteAdapter.notifyDataSetChanged();
/*			mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mDbHelper.queryShowNotes() ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
		    	mGridView.setAdapter(mNoteAdapter);
			}  	*/	
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);	
		}
	};
	
	public Runnable updateTitleRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.updateTitleById(mNoteAdapter.getSingleNoteId()+"",mNoteAdapter.mNewTitleString);
			mBirdNotes.get(mChoosePosition).title = mNoteAdapter.mNewTitleString;
			mNoteAdapter.notifyDataSetChanged();
/*			mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mDbHelper.queryShowNotes() ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
		    	mGridView.setAdapter(mNoteAdapter);
			}  */
			showHandler.sendEmptyMessage(BirdMessage.UPDATETITLE_RUNNABLE_OVER);	
		}
	};
	
	
	public Handler showHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==BirdMessage.DELETE_OVER) {
				mWaitDialog.dismiss();
			}
			if (msg.what==BirdMessage.DELETE_RUNNABLE_START) {
				mWaitDialog.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialog.show();
			}
			if (msg.what==BirdMessage.DELETE_SINGLE_NOTE_RUNNABLE_START) {
				mChoosePosition = (Integer)msg.obj;
				mWaitDialog.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialog.show();
				post(deleteSingleNoteRunnable);
			}
			
			if (msg.what==BirdMessage.CHANGEMARKCOLOR_RUNNABLE_START) {
				mChoosePosition = (Integer)msg.obj;
				mWaitDialog.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialog.show();
				post(changeMarkColorRunnable);
			}
			
			if (msg.what==BirdMessage.UPDATETITLE_RUNNABLE_START) { 
				mChoosePosition = (Integer)msg.obj;
		    	mWaitDialog.setWaitContent(getString(R.string.saveing_note));
			    mWaitDialog.show();
			    post(updateTitleRunnable);
			}
			if (msg.what==BirdMessage.UPDATETITLE_RUNNABLE_OVER) { 
				mWaitDialog.dismiss();
			}	
			
		};
	};
	
}
