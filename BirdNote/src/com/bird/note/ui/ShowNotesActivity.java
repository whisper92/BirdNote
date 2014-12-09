package com.bird.note.ui;

import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.bird.note.dao.NotesTable;
import com.bird.note.model.BirdMessage;
import com.bird.note.model.BirdNote;
import com.bird.note.model.BirdPopMenuItem;
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

	private LinearLayout mLinearLayout = null;
	private TextView mTitleNoteCount;
	private int mCurrentSort= 1;
	
	private List<BirdPopMenuItem> mBirdMenuItems;
	private BirdPopMenu mShowPopMenu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_notes);
		
		mWaitDialog  =new BirdWaitDialog(this, R.style.birdalertdialog);
		mDbHelper=new DbHelper(this);
		mLinearLayout = (LinearLayout) findViewById(R.id.id_show_note_root);
		mShowTitle=(RelativeLayout)findViewById(R.id.id_show_title_rl);
		mShowDeleteTitle = (RelativeLayout) findViewById(R.id.id_show_title_delete_menu_rl);
		mConfirmDelete = (Button) findViewById(R.id.id_show_title_delete_confirm);
		mCancelDelete = (Button) findViewById(R.id.id_show_title_delete_cancle);
		mSelectAll = (Button) findViewById(R.id.id_show_title_delete_select_all);
        mTitleNoteCount =(TextView) findViewById(R.id.id_show_title_count);
		mGridView = (GridView) findViewById(R.id.id_show_gv);
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
		
		mShowPopMenu = PopMenuManager.createShowMenu(this, showMenuListener);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU && event.getRepeatCount() == 0) {
			togglePopMenu();
			return true;
		}else if (keyCode == KeyEvent.KEYCODE_BACK){
			if (mShowPopMenu.isShowing()) {
				mShowPopMenu.dismiss();
				return true;
			}
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void togglePopMenu() {
		if  (!mShowPopMenu.isShowing()) {
			mShowPopMenu.showAtLocation(mLinearLayout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		} else {
			mShowPopMenu.dismiss();
		}
		
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
			mBirdNotes = mDbHelper.queryShowNotes();
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
		mBirdNotes=queryByCurrentSort(mCurrentSort);
		mNoteAdapter= new ShowNoteAdapter(this,mBirdNotes,mGridView); 
	    mNoteAdapter.notifyDataSetChanged();
	    if (mGridView!=null) {
	    	mTitleNoteCount.setText(String.format(getString(R.string.show_note_count), mBirdNotes.size()));
	    	mGridView.setAdapter(mNoteAdapter);
		}    
		super.onRestart();
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
	
	public OnClickListener sortByListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			    closeMenu(mPopMenuSort);		
			    mCurrentSort = v.getId();
			    showHandler.sendEmptyMessage(BirdMessage.SORT_START);
			    showHandler.post(sortRunnable);
				
		}
	};
	
	BirdPopMenu mPopMenuSort;

	public OnClickListener showMenuListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case 0:
	             if ((mNoteAdapter.getDeleteState()==false)&&mNoteAdapter!=null && mBirdNotes!=null && mBirdNotes.size()>0) {
					mNoteAdapter.setDeleteState(true);
					startShowMenuTitle();		
				}
				break;
			case 1:
				mPopMenuSort=PopMenuManager.createSortMenu(ShowNotesActivity.this, sortByListener);
				mPopMenuSort.showAtLocation(mLinearLayout, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);	
				break;
			case 2:
				Intent startintent = new Intent();
				startintent.setClass(ShowNotesActivity.this, ReadStaredNotesActivity.class);
				startintent.putExtra("flag", "star");
				startActivity(startintent);
				break;
			case 3:
				Intent searchintent = new Intent();
				searchintent.setClass(ShowNotesActivity.this, SearchNotesActivity.class);
				searchintent.putExtra("flag", "search");
				startActivity(searchintent);
				break;
			default:
				break;
			}
			
			closeMenu(mShowPopMenu);
		}
	};
	

	
	public void closeMenu(PopupWindow popupWindow){
		if (popupWindow!=null&&popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

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
     /*	 mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mDbHelper.queryShowNotes() ,mGridView); 
		    mNoteAdapter.notifyDataSetChanged();
		    if (mGridView!=null) {
		    	mGridView.setAdapter(mNoteAdapter);
			}  */
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
				mWaitDialog.setWaitContent(getString(R.string.alert_sort));
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
			if (msg.what==BirdMessage.SORT_START) { 
				mWaitDialog.setWaitContent(getString(R.string.alert_sort));
				mWaitDialog.show();
			}	
			
			if (msg.what==BirdMessage.SORT_OVER) { 
				mWaitDialog.dismiss();
				mNoteAdapter= new ShowNoteAdapter(ShowNotesActivity.this,mBirdNotes ,mGridView); 				    
				mNoteAdapter.notifyDataSetChanged();
			    if (mGridView!=null) {
			    	mGridView.setAdapter(mNoteAdapter);
				}  
			}	

			
		};
	};
	
}
