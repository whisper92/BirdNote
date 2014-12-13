package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bird.note.R;
import com.bird.note.customer.BirdPopMenu;
import com.bird.note.customer.BirdWaitDialog;
import com.bird.note.dao.DbHelper;
import com.bird.note.dao.NotesTable;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.BirdPopMenuItem;
import com.bird.note.model.ShowNoteAdapter;
import com.bird.note.utils.NoteApplication;
import com.bird.note.utils.PreferenceUtil;

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
	private BirdWaitDialog mWaitDialogUpdate = null;
	private BirdWaitDialog mWaitDialogDelete = null;
	private LinearLayout mLinearLayout = null;
	private TextView mTitleNoteCount;
	private int mCurrentSort= 0;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		
		mWaitDialogUpdate  = new BirdWaitDialog(this, android.R.style.Theme_Holo_Light_Dialog);
		mWaitDialogDelete = new BirdWaitDialog(this, android.R.style.Theme_Holo_Light_Dialog);
		mDbHelper=new DbHelper(this);
		mLinearLayout = (LinearLayout) findViewById(R.id.id_show_note_root);
		mShowTitle=(RelativeLayout)findViewById(R.id.id_show_title_rl);
		mShowDeleteTitle = (RelativeLayout) findViewById(R.id.id_show_title_delete_menu_rl);
		mConfirmDelete = (Button) findViewById(R.id.id_show_title_delete_confirm);
		mCancelDelete = (Button) findViewById(R.id.id_show_title_delete_cancle);
		mSelectAll = (Button) findViewById(R.id.id_show_title_delete_select_all);
        mTitleNoteCount =(TextView) findViewById(R.id.id_show_title_count);
		mGridView = (GridView) findViewById(R.id.id_show_gv);
		mCurrentSort = PreferenceUtil.getSortBy();
		mBirdNotes=queryByCurrentSort(mCurrentSort);
		
		mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
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

	
   /**
    * 根据当前的排序方式查询数据
 * @return 
    */
	public List<BirdNote> queryByCurrentSort(int sort){
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
			// TODO Auto-generated method stub
		
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
			case -2:
				/*Confirm*/
				PreferenceUtil.setSortBy(mCurrentSort);
				Log.e("wxp","mCurrentSort:"+mCurrentSort);
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
	switch ( item.getItemId()) {
	case R.id.id_show_menu_mutil_delete:
		if ((mNoteAdapter.getDeleteState()==false)&&mNoteAdapter!=null && mBirdNotes!=null && mBirdNotes.size()>0) {
			mNoteAdapter.setDeleteState(true);
			startShowMenuTitle();		
		}
		break;

	case R.id.id_show_menu_sort:
		AlertDialog sortDialog = PopMenuManager.createSortChooseAlertDialog(ShowNotesActivity.this, R.string.show_menu_sort, sortListener);
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
			int flag =0;
			for (int i = 0; i < mNoteAdapter.getSelectedNote().length; i++) {
				if (!mNoteAdapter.getSelectedNote()[i].equals(String.valueOf(-1))) {
					flag++;
				}		
			}
			
			if (mNoteAdapter!=null && flag > 0) {
				showHandler.sendEmptyMessage(BirdMessage.DELETE_RUNNABLE_START);
				showHandler.postDelayed(DeleteNotesRunnable,300);
			} else {
				mNoteAdapter.setDeleteState(false);
			}
			
			startShowNoamralTitle();
			break;
		case R.id.id_show_title_delete_cancle:
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
		
	}
	
	public Runnable DeleteNotesRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.deleteNoteByIds(mNoteAdapter.getSelectedNote());
			mBirdNotes.clear();
			mBirdNotes = queryByCurrentSort(mCurrentSort);
			mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mBirdNotes ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
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
	
	public void startShowMenuTitle(){
		Animator animatorSetOut=AnimatorInflater.loadAnimator(this, R.anim.normal_flip_out);
		Animator animatorSetIn=AnimatorInflater.loadAnimator(this, R.anim.menu_flip_in);
		animatorSetOut.setTarget(mShowTitle);
		animatorSetIn.setTarget(mShowDeleteTitle);
		animatorSetOut.start();
		animatorSetIn.start();
		
		animatorSetOut.addListener(new Animator.AnimatorListener() {		
			@Override
			public void onAnimationStart(Animator animation) {}	
			@Override
			public void onAnimationRepeat(Animator animation) {}	
			@Override
			public void onAnimationEnd(Animator animation) {
				mShowTitle.setVisibility(View.GONE);				
				mShowDeleteTitle.setVisibility(View.VISIBLE);		
			}	
			@Override
			public void onAnimationCancel(Animator animation) {}
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
			public void onAnimationStart(Animator animation) {}
			@Override
			public void onAnimationRepeat(Animator animation) {}	
			@Override
			public void onAnimationEnd(Animator animation) {
				mShowTitle.setVisibility(View.VISIBLE);				
				mShowDeleteTitle.setVisibility(View.GONE);	
			}	
			@Override
			public void onAnimationCancel(Animator animation) {}
		});
	}
	
	public void closeMenu(PopupWindow popupWindow){
		if (popupWindow!=null&&popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	public Runnable queryRunnable = new Runnable() {
		
		@Override
		public void run() {
			mBirdNotes=queryByCurrentSort(mCurrentSort);
			mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mBirdNotes,mGridView); 
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
			mDbHelper.deleteNoteById(mNoteAdapter.getSingleNoteId()+"");
		    mBirdNotes.remove(mChoosePosition);
		    mNoteAdapter.notifyDataSetChanged();
		    mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
			showHandler.sendEmptyMessage(BirdMessage.DELETE_OVER);	
		}
	};
	
	public Runnable changeMarkColorRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.updateLevelById(mNoteAdapter.getSingleNoteId()+"",mNoteAdapter.chooseLevel);
			mBirdNotes.get(mChoosePosition).level = mNoteAdapter.chooseLevel;
			mNoteAdapter.notifyDataSetChanged();
			showHandler.sendEmptyMessage(BirdMessage.UPDATETITLE_RUNNABLE_OVER);	
		}
	};
	
	public Runnable updateTitleRunnable = new Runnable() {	
		@Override
		public void run() {
			mDbHelper.updateTitleById(mNoteAdapter.getSingleNoteId()+"",mNoteAdapter.mNewTitleString);
			mBirdNotes.get(mChoosePosition).title = mNoteAdapter.mNewTitleString;
			mNoteAdapter.notifyDataSetChanged();
			showHandler.sendEmptyMessage(BirdMessage.UPDATETITLE_RUNNABLE_OVER);	
		}
	};
	
	
	public Handler showHandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what==BirdMessage.QUERY_RUNNABLE_OVER) {
				 mNoteAdapter.notifyDataSetChanged();
			    if (mGridView!=null) {
			    	mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
			    	mGridView.setAdapter(mNoteAdapter);
				}   
			}
			if (msg.what==BirdMessage.DELETE_OVER) {
				mWaitDialogDelete.dismiss();
			}
			if (msg.what==BirdMessage.DELETE_RUNNABLE_START) {
				mWaitDialogDelete.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialogDelete.show();
			}
			if (msg.what==BirdMessage.DELETE_SINGLE_NOTE_RUNNABLE_START) {
				mChoosePosition = (Integer)msg.obj;
				mWaitDialogDelete.setWaitContent(getString(R.string.deleteing_note));
				mWaitDialogDelete.show();
				post(deleteSingleNoteRunnable);
			}
			
			if (msg.what==BirdMessage.CHANGEMARKCOLOR_RUNNABLE_START) {
				mChoosePosition = (Integer)msg.obj;
				mWaitDialogUpdate.setWaitContent(getString(R.string.alert_sort));
				mWaitDialogUpdate.show();
				post(changeMarkColorRunnable);
			}
			
			if (msg.what==BirdMessage.UPDATETITLE_RUNNABLE_START) { 
				mChoosePosition = (Integer)msg.obj;
				mWaitDialogUpdate.setWaitContent(getString(R.string.saveing_note));
				mWaitDialogUpdate.show();
			    post(updateTitleRunnable);
			}
			if (msg.what==BirdMessage.UPDATETITLE_RUNNABLE_OVER) { 
				mWaitDialogUpdate.dismiss();
			}	
			if (msg.what==BirdMessage.SORT_START) { 
				mWaitDialogUpdate.setWaitContent(getString(R.string.alert_sort));
				mWaitDialogUpdate.show();
			}	
			
			if (msg.what==BirdMessage.SORT_OVER) { 
				mWaitDialogUpdate.dismiss();
				mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mBirdNotes ,mGridView); 				    
				mNoteAdapter.notifyDataSetChanged();
			    if (mGridView!=null) {
			    	mGridView.setAdapter(mNoteAdapter);
				}  
			}	

			
		};
	};
	

}
